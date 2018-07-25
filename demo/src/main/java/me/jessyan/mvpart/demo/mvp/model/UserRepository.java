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
package me.jessyan.mvpart.demo.mvp.model;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;
import me.jessyan.art.mvp.IModel;
import me.jessyan.art.mvp.IRepositoryManager;
import me.jessyan.mvpart.demo.mvp.model.api.cache.CommonCache;
import me.jessyan.mvpart.demo.mvp.model.api.service.UserService;
import me.jessyan.mvpart.demo.mvp.model.entity.User;

/**
 * ================================================
 * 必须实现 IModel
 * 可以根据不同的业务逻辑划分多个 Repository 类,多个业务逻辑相近的页面可以使用同一个 Repository 类
 * 无需每个页面都创建一个独立的 Repository
 * 通过 {@link IRepositoryManager#createRepository(Class)} 获得的 Repository 实例,为单例对象
 * <p>
 * Created by JessYan on 9/4/16 10:56
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public class UserRepository implements IModel {

    private IRepositoryManager mManager;
    public static final int USERS_PER_PAGE = 10;

    /**
     * 必须含有一个接收IRepositoryManager接口的构造函数,否则会报错
     *
     * @param manager
     */
    public UserRepository(IRepositoryManager manager) {
        this.mManager = manager;
    }


    public Observable<List<User>> getUsers(int lastIdQueried, boolean update) {
        //使用rxcache缓存,上拉刷新则不读取缓存,加载更多读取缓存
        return Observable.just(mManager
                .createRetrofitService(UserService.class)
                .getUsers(lastIdQueried, USERS_PER_PAGE))
                .flatMap(new Function<Observable<List<User>>, ObservableSource<List<User>>>() {
                    @Override
                    public ObservableSource<List<User>> apply(@NonNull Observable<List<User>> listObservable) throws Exception {
                        return mManager.createCacheService(CommonCache.class)
                                .getUsers(listObservable
                                        , new DynamicKey(lastIdQueried)
                                        , new EvictDynamicKey(update))
                                .map(listReply -> listReply.getData());
                    }
                });
    }

    @Override
    public void onDestroy() {

    }
}
