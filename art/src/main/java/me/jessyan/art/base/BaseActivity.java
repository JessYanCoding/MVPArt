package me.jessyan.art.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.zhy.autolayout.AutoFrameLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.jessyan.art.mvp.IPresenter;

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    private Unbinder mUnbinder;

    protected P mPresenter;

    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    public static final String IS_NOT_ADD_ACTIVITY_LIST = "is_add_activity_list";//是否加入到activity的list，管理


    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new AutoFrameLayout(context, attrs);
        }

        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new AutoLinearLayout(context, attrs);
        }

        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new AutoRelativeLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }




    @Nullable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().register(this);//注册到事件主线
        setContentView(initView());
        //绑定到butterknife
        mUnbinder = ButterKnife.bind(this);
        initData();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mPresenter == null){
            mPresenter = getPresenter();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) mPresenter.onDestroy();//释放资源
        if (mUnbinder != Unbinder.EMPTY) mUnbinder.unbind();
        if (useEventBus())//如果要使用eventbus请将此方法返回true
            EventBus.getDefault().unregister(this);
        this.mPresenter = null;
        this.mUnbinder = null;
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return true;
    }

    protected abstract int initView();

    protected abstract void initData();

    protected abstract P getPresenter();

}
