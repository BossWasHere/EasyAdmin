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

package com.backwardsnode.easyadmin.api.builder;

import com.backwardsnode.easyadmin.api.builder.ext.CommitableBuilder;
import com.backwardsnode.easyadmin.api.builder.ext.ContextualBuilder;
import com.backwardsnode.easyadmin.api.builder.ext.StaffBuilder;
import com.backwardsnode.easyadmin.api.record.MuteRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * A builder for creating mute records.
 * @see RecordBuilder
 */
public interface MuteBuilder extends ContextualBuilder<MuteBuilder>, StaffBuilder<MuteBuilder>, CommitableBuilder<MuteRecord> {

    /**
     * Sets the start date for the mute record. Most applications will not need to use this method,
     * as the mute date will default to the current system time.
     * @param muteDate the mute date.
     * @return this builder, for chaining.
     */
    @NotNull MuteBuilder withMuteDate(@NotNull LocalDateTime muteDate);

    /**
     * Sets the end date for the mute record. If this is not set, the mute record will be permanent.
     * <p>Alternatively, use {@link #withUnmuteAfter(Duration)} to set the duration.</p>
     * @param unmuteDate the date and time the mute record is to be deactivated.
     * @return this builder, for chaining.
     */
    @NotNull MuteBuilder withUnmuteOn(@Nullable LocalDateTime unmuteDate);

    /**
     * Sets the end date for the mute record, relative to the date and time the mute record is to be created at.
     * <p>Alternatively, use {@link #withUnmuteOn(LocalDateTime)} to set the date and time precisely.</p>
     * @param unmuteDuration the duration until the mute record is to be deactivated.
     * @return this builder, for chaining.
     */
    @NotNull MuteBuilder withUnmuteAfter(@Nullable Duration unmuteDuration);

    /**
     * Sets the reason for the mute record.
     * @param muteReason the mute reason.
     * @return this builder, for chaining.
     */
    @NotNull MuteBuilder withMuteReason(@Nullable String muteReason);

}
