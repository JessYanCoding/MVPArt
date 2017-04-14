package me.jessyan.art.integration;

import android.content.Context;

import me.jessyan.art.di.module.GlobeConfigModule;

/**
 * 此接口可以给框架配置一些参数,需要实现类实现后,并在AndroidManifest中声明该实现类
 * Created by jess on 12/04/2017 11:37
 * Contact with jess.yan.effort@gmail.com
 */

public interface ConfigModule {

    /**
     * 使用{@link GlobeConfigModule.Builder}给框架配置一些配置参数
     * @param context
     * @param builder
     */
    void applyOptions(Context context, GlobeConfigModule.Builder builder);
}
