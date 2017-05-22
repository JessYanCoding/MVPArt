package me.jessyan.art.base.delegate;

import android.os.Bundle;

import me.jessyan.art.mvp.IPresenter;

/**
 * Created by jess on 26/04/2017 21:42
 * Contact with jess.yan.effort@gmail.com
 */

public interface IActivity<P extends IPresenter> {
    boolean useEventBus();

    /**
     * 如果initView返回0,框架则不会调用{@link android.app.Activity#setContentView(int)}
     *
     * @return
     * @param savedInstanceState
     */
    int initView(Bundle savedInstanceState);

    void initData(Bundle savedInstanceState);

    P obtainPresenter();

    void setPresenter(P presenter);

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link me.jessyan.art.base.BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    boolean useFragment();
}
