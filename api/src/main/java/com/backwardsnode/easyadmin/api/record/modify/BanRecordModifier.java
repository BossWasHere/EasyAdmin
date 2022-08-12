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

import com.backwardsnode.easyadmin.api.record.BanRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Modification wrapper for {@link BanRecord}.
 */
public interface BanRecordModifier extends RecordModifier<BanRecord> {

    /**
     * Sets the date at which the ban will automatically expire.
     * <p>Setting a date in the <b>past</b> will immediately expire this record.</p>
     * <p>Setting a date in the <b>future</b> will reinstate this record, even if it had previously ended.</p>
     * @param unbanDate the date and time at which the record will expire.
     */
    void setAutoUnbanDate(@NotNull LocalDateTime unbanDate);

    /**
     * Reinstates this ban record, clearing any previous expiration date and cancellation details.
     */
    void reinstateBan();

    /**
     * Immediately ends this ban record at this current time instant, overwriting unban details even if this record has already ended.
     * @param unbanStaff the UUID of the staff member who cancelled this record, or null for console.
     * @param unbanReason the reason for cancelling this record.
     */
    void unbanNow(@Nullable UUID unbanStaff, @Nullable String unbanReason);

}
