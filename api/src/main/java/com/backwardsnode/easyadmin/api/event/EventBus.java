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

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Manages handlers and dispatching events for this EasyAdmin instance.
 */
public interface EventBus {

    /**
     * Adds an event handler for the specified event class.
     * @param eventClass The class of the event to handle.
     * @param handler The handler to add.
     * @param <T> The type of the event that is being handled.
     */
    <T extends EasyAdminEvent> void addHandle(@NotNull Class<T> eventClass, @NotNull Consumer<? super T> handler);

    /**
     * Adds an event handler for the specified event class.
     * @param eventClass The class of the event to handle.
     * @param handler The handler to add.
     * @param priority The priority of the handler.
     * @param <T> The type of the event that is being handled.
     */
    <T extends EasyAdminEvent> void addHandle(@NotNull Class<T> eventClass, @NotNull Consumer<? super T> handler, @NotNull EventPriority priority);

    /**
     * Adds an event handler for the specified event class.
     * @param eventClass The class of the event to handle.
     * @param handler The handler to add.
     * @param priority The priority of the handler.
     * @param ignoreCancelled Whether this handler should be called even if the event is cancelled.
     * @param <T> The type of the event that is being handled.
     */
    <T extends EasyAdminEvent> void addHandle(@NotNull Class<T> eventClass, @NotNull Consumer<? super T> handler, @NotNull EventPriority priority, boolean ignoreCancelled);

    /**
     * Gets all handlers for the specified event class.
     * @param eventClass The class of the event to get handlers for.
     * @return A collection of all handlers for the specified event class.
     * @param <T> The type of the event that is being handled.
     */
    @NotNull <T extends EasyAdminEvent> Collection<? extends EventHandle<T>> getHandles(@NotNull Class<T> eventClass);

}
