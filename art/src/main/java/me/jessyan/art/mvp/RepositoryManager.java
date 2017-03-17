package me.jessyan.art.mvp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.rx_cache.internal.RxCache;
import retrofit2.Retrofit;

/**
 * Created by jess on 16/03/2017 14:25
 * Contact with jess.yan.effort@gmail.com
 */

@Singleton
public class RepositoryManager {
    private Retrofit mRetrofit;
    private RxCache mRxCache;
    private final Map<String, IModel> mRepositoryCache = new LinkedHashMap<>();
    private final Map<String, Object> mRetrofitServiceCache = new LinkedHashMap<>();
    private final Map<String, Object> mCacheServiceCache = new LinkedHashMap<>();

    @Inject
    public RepositoryManager(Retrofit retrofit, RxCache rxCache) {
        this.mRetrofit = retrofit;
        this.mRxCache = rxCache;
    }

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
                    throw new RuntimeException("create repository error",e);
                }
                mRepositoryCache.put(repository.getName(), repositoryInstance);
            }
        }
        return repositoryInstance;
    }


    public <T> T CreateRetrofitService(Class<T> service) {
        T retrofitService;
        synchronized (mRetrofitServiceCache) {
            retrofitService = (T) mRetrofitServiceCache.get(service.getName());
            if (retrofitService == null) {
                retrofitService = mRetrofit.create(service);
                mRetrofitServiceCache.put(service.getName(), retrofitService);
            }
        }
        return retrofitService;
    }

    public <T> T CreateCacheService(Class<T> cache) {
        T cacheService;
        synchronized (mCacheServiceCache) {
            cacheService = (T) mCacheServiceCache.get(cache.getName());
            if (cacheService == null) {
                cacheService = mRxCache.using(cache);
                mCacheServiceCache.put(cache.getName(), cacheService);
            }
        }
        return cacheService;
    }


    private static Constructor<? extends IModel> findConstructorForClass(Class<?> cls) {
        Constructor<? extends IModel> bindingCtor = null;

        String clsName = cls.getName();

        try {
            //noinspection unchecked
            bindingCtor = (Constructor<? extends IModel>) cls.getConstructor(RepositoryManager.class);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find constructor for " + clsName, e);
        }

        return bindingCtor;
    }


}
