package me.jessyan.art.di.module;

import android.app.Application;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import me.jessyan.art.mvp.IRepositoryManager;
import me.jessyan.art.mvp.RepositoryManager;

/**
 * Created by jess on 8/4/16.
 */
@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }

    @Singleton
    @Provides
    public Gson provideGson() {
        return new Gson();
    }

    @Singleton
    @Provides
    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    @Singleton
    @Provides
    public Map<String, Object> provideExtras(){
        return new ArrayMap<>();
    }

}
