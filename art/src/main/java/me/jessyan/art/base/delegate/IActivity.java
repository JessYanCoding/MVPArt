package me.jessyan.art.base.delegate;

import me.jessyan.art.mvp.IPresenter;

/**
 * Created by jess on 26/04/2017 21:42
 * Contact with jess.yan.effort@gmail.com
 */

public interface IActivity<P extends IPresenter> {
    boolean useEventBus();

    int initView();

    void initData();

    P obtainPresenter();

    void setPresenter(P presenter);
}
