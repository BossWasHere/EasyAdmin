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

import com.backwardsnode.easyadmin.api.event.EasyAdminEvent;
import com.backwardsnode.easyadmin.api.event.EventHandle;
import com.backwardsnode.easyadmin.api.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CommonEventHandle<T extends EasyAdminEvent> implements EventHandle<T> {

    private final CommonEventBus eventBus;
    private final Class<T> eventClass;
    private final Consumer<? super T> handle;
    private final boolean ignoreCancelled;
    private final EventPriority priority;

    protected CommonEventHandle(CommonEventBus eventBus, Class<T> eventClass, Consumer<? super T> handle, boolean ignoreCancelled, EventPriority priority) {
        this.eventBus = eventBus;
        this.eventClass = eventClass;
        this.handle = handle;
        this.ignoreCancelled = ignoreCancelled;
        this.priority = priority;
    }

    @Override
    public @NotNull Consumer<? super T> getHandler() {
        return handle;
    }

    @Override
    public boolean ignoreCancelled() {
        return ignoreCancelled;
    }

    @Override
    public EventPriority getPriority() {
        return priority;
    }

    @Override
    public void close() {

    }

}
