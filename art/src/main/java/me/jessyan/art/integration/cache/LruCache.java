/**
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
package me.jessyan.art.integration.cache;

import android.support.annotation.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ================================================
 * LRU 即 Least Recently Used,最近最少使用,也就是说,当缓存满了,会优先淘汰那些最近最不常访问的数据
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
     * @param size The maximum size of the cache, the units must match the units used in {@link
     *             #getItemSize(Object)}.
     */
    public LruCache(int size) {
        this.initialMaxSize = size;
        this.maxSize = size;
    }

    /**
     * Sets a size multiplier that will be applied to the size provided in the constructor to put the
     * new size of the cache. If the new size is less than the current size, entries will be evicted
     * until the current size is less than or equal to the new size.
     *
     * @param multiplier The multiplier to apply.
     */
    public synchronized void setSizeMultiplier(float multiplier) {
        if (multiplier < 0) {
            throw new IllegalArgumentException("Multiplier must be >= 0");
        }
        maxSize = Math.round(initialMaxSize * multiplier);
        evict();
    }

    /**
     * Returns the size of a given item, defaulting to one. The units must match those used in the
     * size passed in to the constructor. Subclasses can override this method to return sizes in
     * various units, usually bytes.
     *
     * @param item The item to get the size of.
     */
    protected int getItemSize(V item) {
        return 1;
    }

    /**
     * A callback called whenever an item is evicted from the cache. Subclasses can override.
     *
     * @param key  The key of the evicted item.
     * @param item The evicted item.
     */
    protected void onItemEvicted(K key, V item) {
        // optional override
    }

    /**
     * Returns the current maximum size of the cache in bytes.
     */
    @Override
    public synchronized int getMaxSize() {
        return maxSize;
    }

    /**
     * Returns the sum of the sizes of all items in the cache.
     */
    @Override
    public synchronized int getSize() {
        return currentSize;
    }

    /**
     * Returns true if there is a value for the given key in the cache.
     *
     * @param key The key to check.
     */
    @Override
    public synchronized boolean contains(K key) {
        return cache.containsKey(key);
    }

    /**
     * Returns the item in the cache for the given key or null if no such item exists.
     *
     * @param key The key to check.
     */
    @Override
    @Nullable
    public synchronized V get(K key) {
        return cache.get(key);
    }

    /**
     * Adds the given item to the cache with the given key and returns any previous entry for the
     * given key that may have already been in the cache.
     * <p>
     * <p> If the size of the item is larger than the total cache size, the item will not be added to
     * the cache and instead {@link #onItemEvicted(Object, Object)} will be called synchronously with
     * the given key and item. </p>
     *
     * @param key  The key to add the item at.
     * @param item The item to add.
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
     * Removes the item at the given key and returns the removed item if present, and null otherwise.
     *
     * @param key The key to remove the item at.
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
     * Clears all items in the cache.
     */
    @Override
    public void clear() {
        trimToSize(0);
    }

    /**
     * Removes the least recently used items from the cache until the current size is less than the
     * given size.
     *
     * @param size The size the cache should be less than.
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

    private void evict() {
        trimToSize(maxSize);
    }
}

