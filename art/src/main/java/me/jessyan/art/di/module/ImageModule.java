package me.jessyan.art.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.jessyan.art.widget.imageloader.BaseImageLoaderStrategy;
import me.jessyan.art.widget.imageloader.glide.GlideImageLoaderStrategy;

/**
 * Created by jess on 8/5/16 16:10
 * contact with jess.yan.effort@gmail.com
 */
@Module
public class ImageModule {

    @Singleton
    @Provides
    public BaseImageLoaderStrategy provideImageLoaderStrategy(GlideImageLoaderStrategy glideImageLoaderStrategy) {
        return glideImageLoaderStrategy;
    }

}
