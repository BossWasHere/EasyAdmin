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

import java.time.LocalDateTime;

public final class LookupOptions {

    private final int limit;
    private final int offset;
    private final LocalDateTime dateBefore;
    private final LocalDateTime dateAfter;
    private final boolean sortDescending;

    public LookupOptions(int limit) {
        this.limit = limit;
        this.offset = 0;
        this.dateBefore = null;
        this.dateAfter = null;
        this.sortDescending = true;
    }

    public LookupOptions(int limit, int offset, boolean sortDescending) {
        this.limit = limit;
        this.offset = offset;
        this.sortDescending = sortDescending;
        this.dateBefore = null;
        this.dateAfter = null;
    }

    public LookupOptions(int limit, int offset, boolean sortDescending, LocalDateTime dateBefore, LocalDateTime dateAfter) {
        this.limit = limit;
        this.offset = offset;
        this.sortDescending = sortDescending;
        this.dateBefore = dateBefore;
        this.dateAfter = dateAfter;
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public LocalDateTime getDateBefore() {
        return dateBefore;
    }

    public LocalDateTime getDateAfter() {
        return dateAfter;
    }

    public boolean getSortDescending() {
        return sortDescending;
    }
}
