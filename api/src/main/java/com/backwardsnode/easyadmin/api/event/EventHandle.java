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

package com.backwardsnode.easyadmin.api.event;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Represents a handle to an event listener.
 * @param <T> the event class.
 */
public interface EventHandle<T extends EasyAdminEvent> extends AutoCloseable {

    /**
     * Gets the handler responsible for handling the event.
     * @return the event consumer.
     */
    @NotNull Consumer<? super T> getHandler();

    /**
     * Removes this event handler from the event bus.
     */
    @Override
    void close();

    /**
     * Determines if this handle should be called even if the event was cancelled by another handler.
     * @return true if the handle should be called even if it was cancelled.
     */
    boolean ignoreCancelled();

}
