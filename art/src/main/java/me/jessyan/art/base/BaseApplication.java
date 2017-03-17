package me.jessyan.art.base;

import android.app.Application;
import android.content.Context;

import javax.inject.Inject;

import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.di.component.DaggerAppComponent;
import me.jessyan.art.di.module.AppModule;
import me.jessyan.art.di.module.ClientModule;
import me.jessyan.art.di.module.GlobeConfigModule;
import me.jessyan.art.di.module.ImageModule;

import static me.jessyan.art.utils.Preconditions.checkNotNull;

/**
 * 本项目由
 * mvp
 * +dagger2
 * +retrofit
 * +rxjava
 * +androideventbus
 * +butterknife组成
 */
public abstract class BaseApplication extends Application {
    static private BaseApplication mApplication;
    private AppComponent mAppComponent;
    @Inject
    protected ActivityLifecycle mActivityLifecycle;
    protected final String TAG = this.getClass().getSimpleName();


    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))////提供application
                .clientModule(new ClientModule())//用于提供okhttp和retrofit的单例
                .imageModule(new ImageModule())//图片加载框架默认使用glide
                .globeConfigModule(checkNotNull(getGlobeConfigModule(), "lobeConfigModule is required"))//全局配置
                .build();
        mAppComponent.inject(this);

        registerActivityLifecycleCallbacks(mActivityLifecycle);
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mActivityLifecycle != null) {
            unregisterActivityLifecycleCallbacks(mActivityLifecycle);
        }
        if (mApplication != null)
            this.mApplication = null;
    }


    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     *
     * @return
     */
    protected abstract GlobeConfigModule getGlobeConfigModule();




    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例, 在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    public AppComponent getAppComponent() {
        return mAppComponent;
    }


    /**
     * 返回上下文
     *
     * @return
     */
    public static Context getContext() {
        return mApplication;
    }

}
