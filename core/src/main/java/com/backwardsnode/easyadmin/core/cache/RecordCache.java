/*
 * MIT License
 *
 * Copyright (c) 2022 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.backwardsnode.easyadmin.core.cache;

import com.backwardsnode.easyadmin.core.database.DatabaseController;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RecordCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecordCache.class);
    private final DatabaseController controller;
    private final LoadingCache<CacheKey<?, ?>, CacheLoader<?>> cache;

    public RecordCache(DatabaseController controller) {
        this.controller = controller;

        cache = Caffeine.newBuilder()
                .maximumSize(10000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build(this::load);
    }

    private CacheLoader<?> load(CacheKey<?, ?> key) {
        return key.retrieve(controller);
    }

    public <R, T> void insert(CacheGroupType<R, T> type, R referrer, CacheLoader<T> value) {
        cache.put(new CacheKey<>(type, referrer), value);
    }

    public <R, T> void invalidate(CacheGroupType<R, T> type, R referrer) {
        cache.invalidate(new CacheKey<>(type, referrer));
    }

    public <R, T> void invalidateWithRelatives(CacheGroupType<R, T> type, R referrer) {
        invalidate(type, referrer);
        type.getRelatives().forEach(relative -> invalidate(relative, referrer));
    }

    @SuppressWarnings("unchecked")
    public <R, T> Collection<T> request(CacheGroupType<R, T> type, R referrer) {
        return (Collection<T>) cache.get(new CacheKey<>(type, referrer)).getPayloadCollection();
    }

    @SuppressWarnings("unchecked")
    public <R, T> T requestSingleton(CacheGroupType<R, T> type, R referrer) {
        return (T) cache.get(new CacheKey<>(type, referrer)).getPayloadSingleton();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <R, T> void infuseIfCollectionPresent(CacheGroupType<R, T> type, R referrer, T value) {
        final Object o = cache.getIfPresent(new CacheKey<>(type, referrer));
        if (o == null) {
            return;
        }
        try {
            if (o instanceof Collection) {
                ((Collection) o).add(value);
            }
            for (CacheGroupType<?, ?> relative : type.getRelatives()) {
                final Object relativeCollection = cache.getIfPresent(new CacheKey<>((CacheGroupType<? super Object, ?>) relative, referrer));
                if (relativeCollection instanceof Collection) {
                    ((Collection) relativeCollection).add(value);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error infusing record cache", e);
        }
    }

    private record CacheKey<R, T>(CacheGroupType<R, T> type, R referrer) {

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CacheKey<?, ?> cacheKey = (CacheKey<?, ?>) o;
            return type == cacheKey.type && Objects.equals(referrer, cacheKey.referrer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, referrer);
        }

        private CacheLoader<T> retrieve(final DatabaseController db) {
            return type.retrieve(db, referrer);
        }
    }
}
