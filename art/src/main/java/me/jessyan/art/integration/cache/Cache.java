/**
 * Copyright 2017 JessYan
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.jessyan.art.integration.cache;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.jessyan.art.di.module.GlobalConfigModule;

/**
 * ================================================
 * 用户缓存框架中所必需的组件,开发者可通过 {@link GlobalConfigModule.Builder#cacheFactory(Factory)} 扩展框架的缓存策略
 *
 * @see GlobalConfigModule#provideCacheFactory()
 * Created by JessYan on 25/09/2017 16:36
 * Contact with <mailto:jess.yan.effort@gmail.com>
 * Follow me on <https://github.com/JessYanCoding>
 * ================================================
 */
public interface Cache<K, V> {
    interface Factory {

        int DEFAULT_CACHE_SIZE = 100;

        /**
         * Returns a new cache
         *
         * @param type 框架中需要用到缓存的模块类型 Id
         * @return
         */
        @NonNull
        Cache build(int type);
    }

    /**
     * 获得当前缓存的大小
     *
     * @return
     */
    int size();

    /**
     * 获取可缓存的最大容积
     *
     * @return
     */
    int getMaxSize();

    /**
     * 根据 key 取出对应的缓存
     *
     * @param key
     * @return
     */
    @Nullable
    V get(K key);

    /**
     * 放置缓存
     *
     * @param key
     * @param item
     * @return
     */
    @Nullable
    V put(K key, V item);

    /**
     * 根据 key 移除对应的缓存
     *
     * @param key
     * @return
     */
    @Nullable
    V remove(K key);

    /**
     * key 对应的缓存是否存在
     *
     * @param key
     * @return
     */
    boolean containsKey(K key);

    /**
     * 清除全部缓存
     */
    void clear();
}
