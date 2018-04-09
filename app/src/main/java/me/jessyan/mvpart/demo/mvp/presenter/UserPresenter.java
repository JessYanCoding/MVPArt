/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.mvpart.demo.mvp.presenter;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;

import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.art.base.DefaultAdapter;
import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.mvp.BasePresenter;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.art.utils.PermissionUtil;
import me.jessyan.mvpart.demo.mvp.model.UserRepository;
import me.jessyan.mvpart.demo.mvp.model.entity.User;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;
import timber.log.Timber;

/**
 * ================================================
 * 展示 Presenter 的用法
 *
 * Created by JessYan on 09/04/2016 10:59
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class UserPresenter extends BasePresenter<UserRepository> {
    private RxErrorHandler mErrorHandler;
    private RxPermissions mRxPermissions;
    private List<User> mUsers = new ArrayList<>();
    private DefaultAdapter mAdapter;
    private int lastUserId = 1;
    private boolean isFirst = true;
    private int preEndIndex;


    public UserPresenter(AppComponent appComponent, DefaultAdapter adapter, RxPermissions rxPermissions) {
        super(appComponent.repositoryManager().createRepository(UserRepository.class));
        this.mAdapter = adapter;
        this.mUsers = adapter.getInfos();
        this.mErrorHandler = appComponent.rxErrorHandler();
        this.mRxPermissions = rxPermissions;
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 {@link SupportActivity} 和 {@link Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        Timber.d("onCreate");
    }

    public void requestUsers(final Message msg) {
        final boolean pullToRefresh = (boolean) msg.objs[0];
        IView view = msg.getTarget();
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                view.showMessage("Request permissions failure");
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                view.showMessage("Need to go to the settings");
            }
        }, mRxPermissions, mErrorHandler);


        if (pullToRefresh) lastUserId = 1;//下拉刷新默认只请求第一页

        //关于RxCache缓存库的使用请参考 http://www.jianshu.com/p/b58ef6b0624b

        boolean isEvictCache = pullToRefresh;//是否驱逐缓存,为ture即不使用缓存,每次下拉刷新即需要最新数据,则不使用缓存

        if (pullToRefresh && isFirst) {//默认在第一次下拉刷新时使用缓存
            isFirst = false;
            isEvictCache = false;
        }

        mModel.getUsers(lastUserId, isEvictCache)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    addDispose(disposable);//在订阅时必须调用这个方法,不然Activity退出时可能内存泄漏
                    if (pullToRefresh)
                        msg.getTarget().showLoading();//显示下拉刷新的进度条
                    else {
                        //显示上拉加载更多的进度条
                        msg.what = 0;
                        msg.handleMessageToTargetUnrecycle();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh) {
                        msg.getTarget().hideLoading();//隐藏下拉刷新的进度条
                        //因为hideLoading,为默认方法,直接可以调用所以不需要发送消息给handleMessage()来处理,
                        //HandleMessageToTarget()的原理就是发送消息并回收消息
                        //调用默认方法后不需要调用HandleMessageToTarget(),但是如果后面对view没有其他操作了请调用message.recycle()回收消息
                        msg.recycle();
                    } else {
                        //隐藏上拉加载更多的进度条
                        msg.what = 1;
                        msg.handleMessageToTarget();//方法最后必须调HandleMessageToTarget,将消息所有引用清空后回收进消息池
                    }
                })
                .subscribe(new ErrorHandleSubscriber<List<User>>(mErrorHandler) {
                    @Override
                    public void onNext(List<User> users) {
                        lastUserId = users.get(users.size() - 1).getId();//记录最后一个id,用于下一次请求

                        if (pullToRefresh) mUsers.clear();//如果是下拉刷新则清空列表

                        preEndIndex = mUsers.size();//更新之前列表总长度,用于确定加载更多的起始位置
                        mUsers.addAll(users);

                        if (pullToRefresh)
                            mAdapter.notifyDataSetChanged();
                        else
                            mAdapter.notifyItemRangeInserted(preEndIndex, users.size());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mAdapter = null;
        this.mUsers = null;
        this.mErrorHandler = null;
    }
}
