package common;

import android.app.Application;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import me.jessyan.art.base.AppManager;
import me.jessyan.art.di.module.AppModule;
import me.jessyan.art.di.module.ClientModule;
import me.jessyan.art.di.module.GlobeConfigModule;
import me.jessyan.art.di.module.ImageModule;
import me.jessyan.art.widget.imageloader.ImageLoader;
import me.jessyan.mvpart.demo.di.module.CacheModule;
import me.jessyan.mvpart.demo.di.module.ServiceModule;
import me.jessyan.mvpart.demo.mvp.model.UserRepository;
import me.jessyan.mvpart.demo.mvp.model.api.cache.CacheManager;
import me.jessyan.mvpart.demo.mvp.model.api.service.ServiceManager;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import okhttp3.OkHttpClient;

/**
 * Created by jess on 8/4/16.
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class, ServiceModule.class, ImageModule.class,
        CacheModule.class, GlobeConfigModule.class})
public interface AppComponent {
    Application Application();

    //服务管理器,retrofitApi
    ServiceManager serviceManager();

    //缓存管理器
    CacheManager cacheManager();

    //Rxjava错误处理管理类
    RxErrorHandler rxErrorHandler();


    OkHttpClient okHttpClient();

    //图片管理器,用于加载图片的管理类,默认使用glide,使用策略模式,可替换框架
    ImageLoader imageLoader();

    //gson
    Gson gson();

    //用于管理所有activity
    AppManager appManager();


    UserRepository userRepository();
}
