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

package com.backwardsnode.easyadmin.core.event;

import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.event.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class CommonEventBus implements EventBus, AutoCloseable {

    private final Map<Class<? extends EasyAdminEvent>, HandlerGroup<? extends EasyAdminEvent>> handlers;

    public CommonEventBus() {
        handlers = new HashMap<>();
    }

    @Override
    public <T extends EasyAdminEvent> void addHandle(@NotNull Class<T> eventClass, @NotNull Consumer<? super T> handler) {
        addHandle(eventClass, handler, EventPriority.NORMAL);
    }

    @Override
    public <T extends EasyAdminEvent> void addHandle(@NotNull Class<T> eventClass, @NotNull Consumer<? super T> handler, @NotNull EventPriority priority) {
        addHandle(eventClass, handler, priority, false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EasyAdminEvent> void addHandle(@NotNull Class<T> eventClass, @NotNull Consumer<? super T> handler, @NotNull EventPriority priority, boolean ignoreCancelled) {
        Objects.requireNonNull(eventClass, "Event class cannot be null");
        Objects.requireNonNull(handler, "Handler cannot be null");
        Objects.requireNonNull(priority, "Priority cannot be null");

        if (!eventClass.isInterface() || !EasyAdminEvent.class.isAssignableFrom(eventClass)) {
            throw new IllegalArgumentException("Event class must be an interface that extends EasyAdminEvent");
        }

        CommonEventHandle<T> eventHandle = new CommonEventHandle<>(this, eventClass, handler, ignoreCancelled);

        HandlerGroup<T> handlerGroup = (HandlerGroup<T>) handlers.get(eventClass);
        if (handlerGroup == null) {
            handlerGroup = new HandlerGroup<>();
            handlers.put(eventClass, handlerGroup);
        }
        handlerGroup.addHandle(eventHandle, priority);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T extends EasyAdminEvent> Collection<? extends EventHandle<T>> getHandles(@NotNull Class<T> eventClass) {
        HandlerGroup<T> handlerGroup = (HandlerGroup<T>) handlers.get(eventClass);

        if (handlerGroup == null) {
            return Collections.emptyList();
        }
        return handlerGroup.getHandles();
    }

    @SuppressWarnings("unchecked")
    public <T extends EasyAdminEvent> void call(T event) {
        HandlerGroup<T> handlerGroup = (HandlerGroup<T>) handlers.get(event.getEventClass());

        if (handlerGroup != null) {
            if (event instanceof Cancellable cancellable) {
                for (EventHandle<T> handle : handlerGroup) {
                    if (handle.ignoreCancelled() || !cancellable.isCancelled()) {
                        handle.getHandler().accept(event);
                    }
                }
            } else {
                for (EventHandle<T> handle : handlerGroup) {
                    handle.getHandler().accept(event);
                }
            }
        }
    }

    @Override
    public void close() throws Exception {

    }
}
