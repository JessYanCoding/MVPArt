package me.jessyan.mvpart.demo.demo4;

import android.content.Intent;

import me.jessyan.art.base.BaseActivity;
import me.jessyan.art.mvp.BaseView;
import me.jessyan.art.mvp.Message;
import me.jessyan.mvpart.demo.MainPresenter;
import me.jessyan.mvpart.demo.R;

/**
 * Created by jess on 25/02/2017 20:22
 * Contact with jess.yan.effort@gmail.com
 * 这里为了展示一个页面可以持有多个Presenter对象,做不同的逻辑处理,相比于传统MVP,可以随便重用Presenter
 * 而不用担心,实现多余的view接口,也可以减少大量Presenter类
 */

public class FourthActivity extends BaseActivity<MainPresenter> implements BaseView{
    @Override
    protected int initView() {
        return R.layout.activity_fourth;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void launchActivity(Intent intent) {

    }

    @Override
    public void handleMessage(Message message) {
        switch (message.what){

        }
    }
}
