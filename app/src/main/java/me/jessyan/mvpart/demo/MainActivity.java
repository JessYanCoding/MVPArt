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
import me.jessyan.mvpart.demo.demo2.SecondActivity;
import me.jessyan.mvpart.demo.demo3.ThirdDialog;
import me.jessyan.mvpart.demo.demo4.FourthActivity;

/**
 * Model层需要自行封装,demo中没有网络请求,presenter中只是模拟了一个请求
 */
public class MainActivity extends BaseActivity<MainPresenter> implements BaseView {

    @BindView(R.id.tv_main)
    TextView mTextView;
    @BindView(R.id.activity_main)
    RelativeLayout mRoot;
    private ThirdDialog mDialog;

    @Override
    protected int initView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        mDialog = new ThirdDialog();
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
        startActivity(intent);
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


     @OnClick({R.id.btn_request,R.id.btn_second,R.id.btn_third,R.id.btn_fourth})
         public void onClick(View v) {
             switch (v.getId()) {
                 case R.id.btn_request:
                     mPresenter.request(Message.obtain(this,"jess!"));
                     break;
                 case R.id.btn_second:
                     startActivity(new Intent(getApplicationContext(),SecondActivity.class));
                     break;
                 case R.id.btn_third:
                     mDialog.show(getSupportFragmentManager(),"dialog");
                     break;
                 case R.id.btn_fourth:
                     startActivity(new Intent(getApplicationContext(),FourthActivity.class));
                     break;
             }
         }

}
