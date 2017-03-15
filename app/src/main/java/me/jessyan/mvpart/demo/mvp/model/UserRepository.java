package me.jessyan.mvpart.demo.mvp.model;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.rx_cache.DynamicKey;
import io.rx_cache.EvictDynamicKey;
import io.rx_cache.Reply;
import me.jessyan.art.mvp.BaseRepository;
import me.jessyan.mvpart.demo.mvp.model.api.cache.CacheManager;
import me.jessyan.mvpart.demo.mvp.model.api.service.ServiceManager;
import me.jessyan.mvpart.demo.mvp.model.entity.User;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by jess on 9/4/16 10:56
 * Contact with jess.yan.effort@gmail.com
 */
@Singleton
public class UserRepository extends BaseRepository<ServiceManager, CacheManager> {
    public static final int USERS_PER_PAGE = 10;

    @Inject
    public UserRepository(ServiceManager serviceManager, CacheManager cacheManager) {
        super(serviceManager, cacheManager);
    }


    public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
        Observable<List<User>> users = mServiceManager.getUserService()
                .getUsers(lastIdQueried, USERS_PER_PAGE);
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return mCacheManager.getCommonCache()
                .getUsers(users
                        , new DynamicKey(lastIdQueried)
                        , new EvictDynamicKey(update))
                .flatMap(new Func1<Reply<List<User>>, Observable<List<User>>>() {
                    @Override
                    public Observable<List<User>> call(Reply<List<User>> listReply) {
                        return Observable.just(listReply.getData());
                    }
                });
    }

}
