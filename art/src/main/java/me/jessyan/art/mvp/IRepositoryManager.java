package me.jessyan.art.mvp;

/**
 * Created by jess on 17/03/2017 11:15
 * Contact with jess.yan.effort@gmail.com
 */

public interface IRepositoryManager {
    /**
     * 根据传入的Class创建对应的仓库
     * @param repository
     * @param <T>
     * @return
     */
    <T extends IModel> T createRepository(Class<T> repository);

    /**
     * 根据传入的Class创建对应的Retrift service
     * @param service
     * @param <T>
     * @return
     */
    <T> T CreateRetrofitService(Class<T> service);

    /**
     * 根据传入的Class创建对应的RxCache service
     * @param cache
     * @param <T>
     * @return
     */
    <T> T CreateCacheService(Class<T> cache);
}
