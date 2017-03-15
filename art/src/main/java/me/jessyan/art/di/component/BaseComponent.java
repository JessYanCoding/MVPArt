package me.jessyan.art.di.component;

import javax.inject.Singleton;

import dagger.Component;
import me.jessyan.art.base.BaseApplication;
import me.jessyan.art.di.module.AppModule;

/**
 * Created by jess on 14/12/2016 13:58
 * Contact with jess.yan.effort@gmail.com
 */
@Singleton
@Component(modules={AppModule.class})
public interface BaseComponent {
    void inject(BaseApplication application);
}
