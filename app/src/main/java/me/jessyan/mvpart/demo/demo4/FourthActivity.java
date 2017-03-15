package me.jessyan.mvpart.demo.demo4;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.art.base.BaseActivity;
import me.jessyan.art.mvp.IView;
import me.jessyan.art.mvp.Message;
import me.jessyan.mvpart.demo.MainPresenter;
import me.jessyan.mvpart.demo.R;

/**
 * Created by jess on 25/02/2017 20:22
 * Contact with jess.yan.effort@gmail.com
 * 这里为了展示一个页面可以持有多个Presenter对象,做不同的逻辑处理,相比于传统MVP,可以随便重用Presenter
 * 而不用担心,实现多余的view接口,也可以减少大量Presenter类
 */

public class FourthActivity extends BaseActivity<MainPresenter> implements IView {

    private SecondPresenter mSecondPresenter;

    @BindView(R.id.activity_main)
    RelativeLayout mRoot;


    @Override
    protected int initView() {
        return R.layout.activity_fourth;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected MainPresenter getPresenter() {
        mSecondPresenter = new SecondPresenter();
        return new MainPresenter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSecondPresenter != null)
            mSecondPresenter.onDestroy();
        mSecondPresenter = null;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what) {
            // 两个presenter都使用了"2"这个what字段,所以使用presenter字段来区分
            // 但记得每次调用presenter方法前将此presenter的类名赋值给message的presenter字段
            case 2:
                if (message.isFromThisPresenter(MainPresenter.class)) {
                    mRoot.setBackgroundResource(R.color.colorAccent);
                    // 在一个请求链中重用多个不同presenter的方法来完成所有请求,灵活重用presenter使MVP更强大
                    mSecondPresenter.request3(Message.obtain(this, SecondPresenter.class));

                } else if (message.isFromThisPresenter(SecondPresenter.class)) {
                    showMessage("MVPArt");
                }
                break;
        }
    }

    @OnClick({R.id.btn_request})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_request:
                // 使用多个presenter时,可能都使用过同一个what值,存在冲突的情况
                // 所以message提供一个presenter字段,避免这个冲突的情况
                // 这个方法也就是将presenter的类名赋值给message.presenter
                mPresenter.request2(Message.obtain(this, MainPresenter.class));
                break;
        }
    }
}
