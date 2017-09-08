/*
  * Copyright 2017 JessYan
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *      http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package me.jessyan.art.mvp;

import android.app.Application;
import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 * ================================================
 * 用来管理所有业务逻辑的仓库,网络请求层,以及数据缓存层,以后可以添加数据库请求层
 * 所有仓库不直接持有却通过 RepositoryManager 拿到需要的请求层做数据处理的好处是
 * 仓库可以直接和对应的请求层解耦,比如网路请求层,需要从 Retrofit 替换为其他网络请求库,这时仓库就不会受到影响
 * <p>
 * Created by JessYan on 16/03/2017 14:25
 * Contact with jess.yan.effort@gmail.com
 * Follow me on https://github.com/JessYanCoding
 * ================================================
 */

@Singleton
public class RepositoryManager implements IRepositoryManager {
    private Lazy<Retrofit> mRetrofit;
    private Lazy<RxCache> mRxCache;
    private Application mApplication;
    private final Map<String, IModel> mRepositoryCache = new HashMap<>();
    private final Map<String, Object> mRetrofitServiceCache = new HashMap<>();
    private final Map<String, Object> mCacheServiceCache = new HashMap<>();

    @Inject
    public RepositoryManager(Lazy<Retrofit> retrofit, Lazy<RxCache> rxCache, Application application) {
        this.mRetrofit = retrofit;
        this.mRxCache = rxCache;
        this.mApplication = application;
    }

    /**
     * 根据传入的 Class 创建对应的仓库
     *
     * @param repository
     * @param <T>
     * @return
     */
    @Override
    public <T extends IModel> T createRepository(Class<T> repository) {
        T repositoryInstance;
        synchronized (mRepositoryCache) {
            repositoryInstance = (T) mRepositoryCache.get(repository.getName());
            if (repositoryInstance == null) {
                Constructor<? extends IModel> constructor = findConstructorForClass(repository);
                try {
                    repositoryInstance = (T) constructor.newInstance(this);
                } catch (InstantiationException e) {
                    throw new RuntimeException("Unable to invoke " + constructor, e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Unable to invoke " + constructor, e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("create repository error", e);
                }
                mRepositoryCache.put(repository.getName(), repositoryInstance);
            }
        }
        return repositoryInstance;
    }


    /**
     * 根据传入的 Class 创建对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */
    @Override
    public <T> T createRetrofitService(Class<T> service) {
        T retrofitService;
        synchronized (mRetrofitServiceCache) {
            retrofitService = (T) mRetrofitServiceCache.get(service.getName());
            if (retrofitService == null) {
                retrofitService = mRetrofit.get().create(service);
                mRetrofitServiceCache.put(service.getName(), retrofitService);
            }
        }
        return retrofitService;
    }


    /**
     * 根据传入的 Class 创建对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return
     */
    @Override
    public <T> T createCacheService(Class<T> cache) {
        T cacheService;
        synchronized (mCacheServiceCache) {
            cacheService = (T) mCacheServiceCache.get(cache.getName());
            if (cacheService == null) {
                cacheService = mRxCache.get().using(cache);
                mCacheServiceCache.put(cache.getName(), cacheService);
            }
        }
        return cacheService;
    }

    /**
     * 清理所有缓存
     */
    @Override
    public void clearAllCache() {
        mRxCache.get().evictAll();
    }

    @Override
    public Context getContext() {
        return mApplication;
    }


    private static Constructor<? extends IModel> findConstructorForClass(Class<?> cls) {
        Constructor<? extends IModel> bindingCtor;

        String clsName = cls.getName();

        try {
            //noinspection unchecked
            bindingCtor = (Constructor<? extends IModel>) cls.getConstructor(IRepositoryManager.class);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find constructor for " + clsName, e);
        }

        return bindingCtor;
    }


}
