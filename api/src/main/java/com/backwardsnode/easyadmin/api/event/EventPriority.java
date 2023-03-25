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

/**
 * The priority of a handler attached to an event.
 */
public enum EventPriority {

    /**
     * The lowest priority, called first.
     */
    LOWEST(0),
    /**
     * The second-lowest priority.
     */
    LOW(1),
    /**
     * The default priority.
     */
    NORMAL(2),
    /**
     * The second-highest priority.
     */
    HIGH(3),
    /**
     * The highest priority, called last.
     */
    HIGHEST(4);

    private final int ordinal;

    EventPriority(int ordinal) {
        this.ordinal = ordinal;
    }

    /**
     * Gets the ordinal value of this priority.
     * @return the ordinal value.
     */
    public int getOrdinal() {
        return ordinal;
    }

}
