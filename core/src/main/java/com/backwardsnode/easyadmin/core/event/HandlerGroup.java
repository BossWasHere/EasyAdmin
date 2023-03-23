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
import com.backwardsnode.easyadmin.api.event.EventPriority;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class HandlerGroup<T extends EasyAdminEvent> implements Iterable<CommonEventHandle<T>> {

    private static final int PRIORITIES = EventPriority.values().length;

    private final List<HandleNode<T>> priorityNodes;

    public HandlerGroup() {
        priorityNodes = new ArrayList<>(PRIORITIES);
    }

    public void addHandle(CommonEventHandle<T> handle, EventPriority priority) {
        HandleNode<T> newNode = new HandleNode<>(handle);
        HandleNode<T> priorityCurrent = priorityNodes.get(priority.getOrdinal());

        if (priorityCurrent != null) {
            newNode.next = priorityCurrent;
        }
        priorityNodes.set(priority.getOrdinal(), newNode);
    }

    public List<CommonEventHandle<T>> getHandles() {
        List<CommonEventHandle<T>> handles = new LinkedList<>();
        for (HandleNode<T> node : priorityNodes) {
            while (node != null) {
                handles.add(node.handle);
                node = node.next;
            }
        }
        return handles;
    }

    public List<CommonEventHandle<T>> getHandles(EventPriority priority) {
        List<CommonEventHandle<T>> handles = new LinkedList<>();

        HandleNode<T> node = priorityNodes.get(priority.getOrdinal());
        while (node != null) {
            handles.add(node.handle);
            node = node.next;
        }

        return handles;
    }

    @NotNull
    @Override
    public Iterator<CommonEventHandle<T>> iterator() {
        return new Iterator<>() {

            private int currentGroup = 0;
            private boolean advancedGroup = false;
            private HandleNode<T> last;

            @Override
            public boolean hasNext() {
                if (last != null) {
                    if (last.next != null) {
                        return true;
                    }
                    if (!advancedGroup) {
                        currentGroup++;
                        advancedGroup = true;
                    }
                }

                HandleNode<T> nextNode = null;
                while (currentGroup < PRIORITIES && nextNode == null) {
                    nextNode = priorityNodes.get(currentGroup++);
                }
                currentGroup--;
                return nextNode != null;
            }

            @Override
            public CommonEventHandle<T> next() {
                advancedGroup = false;

                if (last != null) {
                    last = last.next;
                    if (last != null) {
                        return last.handle;
                    }
                }

                while (currentGroup < PRIORITIES && last == null) {
                    last = priorityNodes.get(currentGroup++);
                }
                currentGroup--;
                return last != null ? last.handle : null;
            }
        };
    }


    private static final class HandleNode<T extends EasyAdminEvent> {

        private final CommonEventHandle<T> handle;

        private HandleNode<T> next;

        private HandleNode(CommonEventHandle<T> handle) {
            this.handle = handle;
        }

    }

}
