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

package com.backwardsnode.easyadmin.api.data;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * Class for configuring database lookups.
 */
public final class LookupOptions {

    private final int limit;
    private final int offset;
    private final boolean sortDescending;
    private final LocalDateTime dateBefore;
    private final LocalDateTime dateAfter;

    /**
     * Initializes a new {@link LookupOptions}.
     * @param limit the maximum number of results to return.
     */
    public LookupOptions(int limit) {
        this(limit, 0);
    }

    /**
     * Initializes a new {@link LookupOptions}.
     * @param limit the maximum number of results to return.
     * @param offset the number of results to skip.
     */
    public LookupOptions(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
        this.sortDescending = true;
        this.dateBefore = null;
        this.dateAfter = null;
    }

    /**
     * Initializes a new {@link LookupOptions}.
     * @param limit the maximum number of results to return.
     * @param offset the number of results to skip.
     * @param sortDescending whether to sort the results in descending order.
     * @param dateBefore the date before which to return results.
     * @param dateAfter the date after which to return results.
     */
    public LookupOptions(int limit, int offset, boolean sortDescending, @Nullable LocalDateTime dateBefore, @Nullable LocalDateTime dateAfter) {
        this.limit = limit;
        this.offset = offset;
        this.sortDescending = sortDescending;
        this.dateBefore = dateBefore;
        this.dateAfter = dateAfter;
    }

    /**
     * Gets the maximum number of results to return.
     * @return the limit.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Gets the number of results to skip.
     * @return the offset.
     */
    public int getOffset() {
        return offset;
    }

    /**
     * Gets the date before which to return results.
     * @return the {@link LocalDateTime} before which to return results, or null.
     */
    public @Nullable LocalDateTime getDateBefore() {
        return dateBefore;
    }

    /**
     * Gets the date after which to return results.
     * @return the {@link LocalDateTime} after which to return results, or null.
     */
    public @Nullable LocalDateTime getDateAfter() {
        return dateAfter;
    }

    /**
     * Gets whether to sort the results in descending order.
     * @return true if the results should be sorted in descending order, false otherwise.
     */
    public boolean getSortDescending() {
        return sortDescending;
    }
}
