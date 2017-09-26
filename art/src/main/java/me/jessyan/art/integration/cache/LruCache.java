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

import android.support.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ================================================
 * LRU 即 Least Recently Used,最近最少使用,也就是说,当缓存满了,会优先淘汰那些最近最不常访问的数据
 * 此种缓存策略为框架默认提供,可自行实现其他缓存策略,如磁盘缓存,为框架或使用者提供缓存的功能
 * <p>
 * Created by JessYan on 25/09/2017 16:57
 * Contact with <mailto:jess.yan.effort@gmail.com>
 * Follow me on <https://github.com/JessYanCoding>
 * ================================================
 */
public class LruCache<K, V> implements Cache<K, V> {
    private final LinkedHashMap<K, V> cache = new LinkedHashMap<>(100, 0.75f, true);
    private final int initialMaxSize;
    private int maxSize;
    private int currentSize = 0;

    /**
     * Constructor for LruCache.
     *
     * @param size 这个缓存的最大 size,这个 size 所使用的单位必须和 {@link #getItemSize(Object)} 所使用的单位一致.
     */
    public LruCache(int size) {
        this.initialMaxSize = size;
        this.maxSize = size;
    }

    /**
     * 设置一个系数应用于当时构造函数中所传入的 size, 从而得到一个新的 {@link #maxSize}
     * 并会立即调用 {@link #evict} 开始清除满足条件的条目
     *
     * @param multiplier 系数
     */
    public synchronized void setSizeMultiplier(float multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("Multiplier must be >= 0");
        }
        maxSize = Math.round(initialMaxSize * multiplier);
        evict();
    }

    /**
     * 返回每个 item 所占用的 size,默认为1,这个 size 的单位必须和构造函数所传入的 size 一致
     * 子类可以重写这个方法以适应不同的单位,比如说 bytes
     *
     * @param item 每个 item 所占用的 size
     */
    protected int getItemSize(V item) {
        return 1;
    }

    /**
     * 当缓存中有被驱逐的条目时,会回调此方法,默认空实现,子类可以重写这个方法
     *
     * @param key  被驱逐条目的 key
     * @param item 被驱逐条目的 value
     */
    protected void onItemEvicted(K key, V item) {
        // optional override
    }

    /**
     * 返回当前缓存所能承受的最大 size
     */
    @Override
    public synchronized int getMaxSize() {
        return maxSize;
    }

    /**
     * 返回当前缓存已占用的总 size
     */
    @Override
    public synchronized int size() {
        return currentSize;
    }

    /**
     * 如果这个 key 在缓存中有对应的值,则返回 true
     *
     * @param key 用来映射的 key
     */
    @Override
    public synchronized boolean containsKey(K key) {
        return cache.containsKey(key);
    }

    /**
     * 返回这个 key 在缓存中对应的值,如果返回 null 说明这个 key 没有对应的值
     *
     * @param key 用来映射的 key
     */
    @Override
    @Nullable
    public synchronized V get(K key) {
        return cache.get(key);
    }

    /**
     * 将 key 和 value 以条目的形式加入缓存,如果这个 key 在缓存中已经有对应的 value ,则此 value 被新的 value
     * 替换并返回,如果为 null 说明是一个新条目
     * <p>
     * 如果 {@link #getItemSize} 返回的 size 大于或等于缓存所能承受的最大 size, 则不能向缓存中添加此条目
     * 此时会回调 {@link #onItemEvicted(Object, Object)} 通知此方法当前被驱逐的条目
     *
     * @param key  通过这个 key 添加条目
     * @param item 需要添加的值
     */
    @Override
    @Nullable
    public synchronized V put(K key, V item) {
        final int itemSize = getItemSize(item);
        if (itemSize >= maxSize) {
            onItemEvicted(key, item);
            return null;
        }

        final V result = cache.put(key, item);
        if (item != null) {
            currentSize += getItemSize(item);
        }
        if (result != null) {
            // TODO: should we call onItemEvicted here?
            currentSize -= getItemSize(result);
        }
        evict();

        return result;
    }

    /**
     * 移除缓存中这个 key 所对应的条目,并返回所移除条目的 value ,如果返回为 null 则有可能时因为 value 为 null
     * 或条目不存在
     *
     * @param key 使用这个 key 移除对应的条目
     */
    @Override
    @Nullable
    public synchronized V remove(K key) {
        final V value = cache.remove(key);
        if (value != null) {
            currentSize -= getItemSize(value);
        }
        return value;
    }

    /**
     * 清除缓存中所有的条目
     */
    @Override
    public void clear() {
        trimToSize(0);
    }

    /**
     * 当指定的 size 小于当前缓存已占用的总 size 时,会开始清除缓存中最近最少使用的条目
     *
     * @param size
     */
    protected synchronized void trimToSize(int size) {
        Map.Entry<K, V> last;
        while (currentSize > size) {
            last = cache.entrySet().iterator().next();
            final V toRemove = last.getValue();
            currentSize -= getItemSize(toRemove);
            final K key = last.getKey();
            cache.remove(key);
            onItemEvicted(key, toRemove);
        }
    }

    /**
     * 当缓存中已占用的总 size 大于所能承受的最大 size ,会使用  {@link #trimToSize(int)} 开始清除满足条件的条目
     */
    private void evict() {
        trimToSize(maxSize);
    }
}

