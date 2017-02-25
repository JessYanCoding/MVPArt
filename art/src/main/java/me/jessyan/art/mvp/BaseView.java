package me.jessyan.art.mvp;

import android.content.Intent;

/**
 * Created by jess on 16/4/22.
 */
public interface BaseView {

    /**
     * 显示加载
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 显示信息
     */
    void showMessage(String message);

    /**
     * 跳转activity
     */
    void launchActivity(Intent intent);

    /**
     * 处理消息,这里面和handler的原理一样,通过swith(what),做不同的操作
     * @param message
     */
    void handleMessage(Message message);

}
