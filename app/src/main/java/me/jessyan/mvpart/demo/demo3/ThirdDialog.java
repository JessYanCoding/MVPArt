package me.jessyan.mvpart.demo.demo3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.jessyan.art.mvp.BaseView;
import me.jessyan.art.mvp.Message;
import me.jessyan.mvpart.demo.MainPresenter;
import me.jessyan.mvpart.demo.R;

/**
 * Created by jess on 25/02/2017 19:18
 * Contact with jess.yan.effort@gmail.com
 * * 这里为了展示可以在dialog或其他任何view中直接重用MainPresenter
 */

public class ThirdDialog extends DialogFragment implements BaseView, View.OnClickListener {
    private MainPresenter mPresenter;
    private View mRootView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPresenter = new MainPresenter();
        mRootView = inflater.inflate(R.layout.dialog_third, container);
        return mRootView;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootView.findViewById(R.id.btn_confirm).setOnClickListener(this);
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
                mRootView.findViewById(R.id.btn_confirm).setBackgroundColor(getContext().getResources().getColor(message.arg1));
                break;
            case 1:
                ((Button)mRootView.findViewById(R.id.btn_confirm)).setText(message.str);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        mPresenter.request(Message.obtain(this, "chengdu!"));
    }
}
