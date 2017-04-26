package me.jessyan.mvpart.demo.mvp.presenter;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import me.jessyan.art.base.DefaultAdapter;
import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.PermissionUtil;
import me.jessyan.mvpart.demo.mvp.model.UserRepository;
import me.jessyan.mvpart.demo.mvp.model.entity.User;
import me.jessyan.mvpart.demo.mvp.ui.adapter.UserAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jess on 9/4/16 10:59
 * Contact with jess.yan.effort@gmail.com
 */
public class UserPresenter extends BasePresenter<UserRepository> {
    private RxErrorHandler mErrorHandler;
    private List<User> mUsers = new ArrayList<>();
    private DefaultAdapter mAdapter;
    private int lastUserId = 1;
    private boolean isFirst = true;
    private int preEndIndex;


    public UserPresenter(AppComponent appComponent) {
        super(appComponent.repositoryManager().createRepository(UserRepository.class));
        this.mErrorHandler = appComponent.rxErrorHandler();
    }

    public void requestUsers(final Message msg) {
        final boolean pullToRefresh = (boolean) msg.objs[0];
        if (mAdapter == null) {
            mAdapter = new UserAdapter(mUsers);
            msg.what = 0;
            msg.obj = mAdapter;
            msg.HandleMessageToTargetUnrecycle();
        }


        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(() -> {
            //request permission success, do something.
        }, (RxPermissions) msg.objs[1], msg.getTarget(), mErrorHandler);


        if (pullToRefresh) lastUserId = 1;//上拉刷新默认只请求第一页

        //关于RxCache缓存库的使用请参考 http://www.jianshu.com/p/b58ef6b0624b

        boolean isEvictCache = pullToRefresh;//是否驱逐缓存,为ture即不使用缓存,每次上拉刷新即需要最新数据,则不使用缓存

        if (pullToRefresh && isFirst) {//默认在第一次上拉刷新时使用缓存
            isFirst = false;
            isEvictCache = false;
        }

        addSubscrebe(mModel.getUsers(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(() -> {
                    if (pullToRefresh)
                        msg.getTarget().showLoading();//显示上拉刷新的进度条
                    else {
                        //显示下拉加载更多的进度条
                        msg.what = 1;
                        msg.HandleMessageToTargetUnrecycle();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> {
                    if (pullToRefresh) {
                        msg.getTarget().hideLoading();//隐藏上拉刷新的进度条
                        //因为hideLoading,为默认方法,直接可以调用所以不需要发送消息给handleMessage()来处理,
                        //HandleMessageToTarget()的原理就是发送消息并回收消息
                        //调用默认方法后不需要调用HandleMessageToTarget(),但是如果后面对view没有其他操作了请调用message.recycle()回收消息
                        msg.recycle();
                    } else {
                        //隐藏下拉加载更多的进度条
                        msg.what = 2;
                        msg.HandleMessageToTarget();//方法最后必须调HandleMessageToTarget,将消息所有引用清空后回收进消息池
                    }
                })
                .subscribe(new ErrorHandleSubscriber<List<User>>(mErrorHandler) {
                    @Override
                    public void onNext(List<User> users) {
                        lastUserId = users.get(users.size() - 1).getId();//记录最后一个id,用于下一次请求

                        if (pullToRefresh) mUsers.clear();//如果是上拉刷新则清空列表

                        preEndIndex = mUsers.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        mUsers.addAll(users);

                        if (pullToRefresh)
                            mAdapter.notifyDataSetChanged();
                        else
                            mAdapter.notifyItemRangeInserted(preEndIndex, users.size());
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.mUsers = null;
        this.mErrorHandler = null;
    }
}
