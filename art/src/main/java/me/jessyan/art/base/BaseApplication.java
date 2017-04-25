package me.jessyan.art.base;

import android.app.Application;
import android.content.Context;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.art.di.component.AppComponent;
import me.jessyan.art.di.component.DaggerAppComponent;
import me.jessyan.art.di.module.AppModule;
import me.jessyan.art.di.module.ClientModule;
import me.jessyan.art.di.module.GlobalConfigModule;
import me.jessyan.art.di.module.ImageModule;
import me.jessyan.art.integration.ActivityLifecycle;
import me.jessyan.art.integration.ConfigModule;
import me.jessyan.art.integration.ManifestParser;

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
                .globalConfigModule(getGlobalConfigModule(this))//全局配置
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
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mApplication = null;
    }


    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类,和Glide的配置方式相似
     *
     * @return
     */
    private GlobalConfigModule getGlobalConfigModule(Application context) {
        List<ConfigModule> modules = new ManifestParser(context).parse();

        GlobalConfigModule.Builder builder = GlobalConfigModule
                .builder();

        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }

        return builder.build();
    }


    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
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
