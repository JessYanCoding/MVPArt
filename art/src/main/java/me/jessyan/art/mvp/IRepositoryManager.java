package me.jessyan.art.mvp;

import android.content.Context;

/**
 * ================================================
 * Created by JessYan on 17/03/2017 11:15
 * Contact with jess.yan.effort@gmail.com
 * Follow me on https://github.com/JessYanCoding
 * ================================================
 */

public interface IRepositoryManager {
    /**
     * 根据传入的 Class 创建对应的仓库
     *
     * @param repository
     * @param <T>
     * @return
     */
    <T extends IModel> T createRepository(Class<T> repository);

    /**
     * 根据传入的 Class 创建对应的 Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */
    <T> T createRetrofitService(Class<T> service);

    /**
     * 根据传入的 Class 创建对应的 RxCache service
     *
     * @param cache
     * @param <T>
     * @return
     */
    <T> T createCacheService(Class<T> cache);

    /**
     * 清理所有缓存
     */
    void clearAllCache();

    Context getContext();

}
