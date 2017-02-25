package me.jessyan.mvpart.demo;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.art.base.BaseActivity;
import me.jessyan.art.mvp.BaseView;
import me.jessyan.art.mvp.Message;

public class MainActivity extends BaseActivity<MainPresenter> implements BaseView {

    @BindView(R.id.tv_main)
    TextView mTextView;
    @BindView(R.id.activity_main)
    RelativeLayout mRoot;


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
                mRoot.setBackgroundColor(getResources().getColor(message.arg1));
                break;
            case 1:
                mTextView.setText(message.str);
                break;
        }
    }

    @Override
    protected int initView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected MainPresenter getPresenter() {
        return new MainPresenter();
    }


     @OnClick({R.id.btn_change})
         public void onClick(View v) {
             switch (v.getId()) {
                 case R.id.btn_change:
                     mPresenter.compute(Message.obtain(this));
                     break;
             }
         }

}
