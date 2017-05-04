package me.jessyan.art.mvp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 *
 * 用来管理所有业务逻辑的仓库,网络请求层,以及数据缓存层,以后可以添加数据库请求层
 * 所有仓库不直接持有却通过RepositoryManager拿到需要的请求层做数据处理的好处是
 * 仓库可以直接和对应的请求层解耦,比如网路请求层,需要从Retrofit替换为其他网络请求库,这时就仓库就不会受到影响
 *
 * Created by jess on 16/03/2017 14:25
 * Contact with jess.yan.effort@gmail.com
 */

@Singleton
public class RepositoryManager implements IRepositoryManager {
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

    /**
     * 根据传入的Class创建对应的仓库
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
                    throw new RuntimeException("create repository error",e);
                }
                mRepositoryCache.put(repository.getName(), repositoryInstance);
            }
        }
        return repositoryInstance;
    }


    /**
     * 根据传入的Class创建对应的Retrift service
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
                retrofitService = mRetrofit.create(service);
                mRetrofitServiceCache.put(service.getName(), retrofitService);
            }
        }
        return retrofitService;
    }


    /**
     * 根据传入的Class创建对应的RxCache service
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
                cacheService = mRxCache.using(cache);
                mCacheServiceCache.put(cache.getName(), cacheService);
            }
        }
        return cacheService;
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
