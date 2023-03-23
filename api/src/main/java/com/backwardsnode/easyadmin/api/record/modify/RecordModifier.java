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

package com.backwardsnode.easyadmin.api.record.modify;

import com.backwardsnode.easyadmin.api.record.LiveRecord;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a record modification wrapper that will eventually return a modified version of a record.
 * @param <T> the type of record this wrapper will return.
 */
public interface RecordModifier<T extends LiveRecord<?>> {

    /**
     * Gets the modified version of the record.
     * @return The modified record.
     */
    @NotNull T getUpdatedRecord();

    /**
     * Determines if any changes to this record were actually made.
     * @implNote If the record was not initially loaded from a data store, this method will always return false.
     * @return true if any changes were made, otherwise false.
     */
    boolean hasChanged();

    /**
     * Pushes changes to the record data store.
     */
    void push();

}
