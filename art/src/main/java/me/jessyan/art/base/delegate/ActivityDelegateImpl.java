package me.jessyan.art.base.delegate;

import android.app.Activity;
import android.os.Bundle;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jessyan.art.mvp.IPresenter;

/**
 * Created by jess on 26/04/2017 20:23
 * Contact with jess.yan.effort@gmail.com
 */

public class ActivityDelegateImpl implements ActivityDelegate {
    private Activity mActivity;
    private IActivity iActivity;
    private Unbinder mUnbinder;
    private IPresenter iPresenter;


    public ActivityDelegateImpl(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }


    public void onCreate(Bundle savedInstanceState) {
        this.iPresenter = iActivity.obtainPresenter();
        iActivity.setPresenter(iPresenter);
        if (iActivity.useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().register(mActivity);//注册到事件主线
        mActivity.setContentView(iActivity.initView());
        //绑定到butterknife
        mUnbinder = ButterKnife.bind(mActivity);
        iActivity.initData();
    }

    public void onStart() {

    }

    public void onResume() {

    }

    public void onPause() {

    }

    public void onStop() {

    }

    public void onSaveInstanceState(Bundle outState) {

    }


    public void onDestroy() {
        if (mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        if (iActivity.useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(mActivity);
        if (iPresenter != null) iPresenter.onDestroy(); //释放资源
        this.mUnbinder = null;
        this.iActivity = null;
        this.mActivity = null;
        this.iPresenter = null;
    }
}
