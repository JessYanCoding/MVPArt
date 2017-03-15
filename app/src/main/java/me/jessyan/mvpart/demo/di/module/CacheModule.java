package me.jessyan.mvpart.demo.di.module;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache.internal.RxCache;
import me.jessyan.mvpart.demo.mvp.model.api.cache.CommonCache;

/**
 * Created by zhiyicx on 2016/3/30.
 */
@Module
public class CacheModule {

    @Singleton
    @Provides
    CommonCache provideCommonService(RxCache rxCache) {
        return rxCache.using(CommonCache.class);
    }


}
