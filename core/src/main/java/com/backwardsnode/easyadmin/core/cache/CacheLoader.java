/*
 * MIT License
 *
 * Copyright (c) 2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

import java.util.Collection;

public final class CacheLoader<T> {

    private final boolean isCollection;
    private final T payloadSingleton;
    private final Collection<T> payloadCollection;

    private CacheLoader(final T payloadSingleton, final Collection<T> payloadCollection, final boolean isCollection) {
        this.payloadSingleton = payloadSingleton;
        this.payloadCollection = payloadCollection;
        this.isCollection = isCollection;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public T getPayloadSingleton() {
        return payloadSingleton;
    }

    public Collection<T> getPayloadCollection() {
        return payloadCollection;
    }

    public static <T> CacheLoader<T> singleton(T payload) {
        return new CacheLoader<>(payload, null, false);
    }

    public static <T> CacheLoader<T> collection(Collection<T> payload) {
        return new CacheLoader<>(null, payload, true);
    }

}
