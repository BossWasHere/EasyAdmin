/*
 * MIT License
 *
 * Copyright (c) 2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

package com.backwardsnode.easyadmin.api.record.mutable;

import com.backwardsnode.easyadmin.api.record.MuteRecord;
import com.backwardsnode.easyadmin.api.record.base.MutableRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface MutableMuteRecord extends MuteRecord, MutableRecord<MuteRecord> {
    
    void setAuthor(@Nullable UUID author);
    
    void setIpAddress(@Nullable String ipAddress);
    
    void setReason(@Nullable String reason);
    
    void setContext(@Nullable String context);

    /**
     * Sets the date at which the mute will automatically expire.
     * <p>Setting a date in the <b>past</b> will immediately expire this record.</p>
     * <p>Setting a date in the <b>future</b> will reinstate this record, even if it had previously ended.</p>
     * @param unmuteDate the date and time at which the record will expire.
     */
    void setAutoUnmuteDate(@NotNull LocalDateTime unmuteDate);

    /**
     * Updates this mute record to end at this instant, overwriting unmute details even if this record has already ended.
     * @param unmuteStaff the UUID of the staff member who cancelled this record, or null for console.
     * @param unmuteReason the reason for cancelling this record.
     */
    void unmuteNow(@Nullable UUID unmuteStaff, @Nullable String unmuteReason);
    
}
