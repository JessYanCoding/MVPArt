package me.jessyan.mvpart.demo.demo2;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import me.jessyan.art.base.BaseFragment;
import me.jessyan.art.mvp.BaseView;
import me.jessyan.art.mvp.Message;
import me.jessyan.mvpart.demo.MainPresenter;
import me.jessyan.mvpart.demo.R;

/**
 * Created by jess on 25/02/2017 18:14
 * Contact with jess.yan.effort@gmail.com
 * 这里为了展示可以在fragment中直接重用MainPresenter
 */

public class SecondFragment extends BaseFragment<MainPresenter> implements BaseView {
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    protected void initData() {
        mPresenter.request(Message.obtain(this,"world!"));
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
        switch (message.what) {
            case 0:
                mRootView.setBackgroundResource(message.arg1);
                break;
            case 1:
                Toast.makeText(getContext(),message.str,Toast.LENGTH_LONG).show();
                break;
        }
    }
}
