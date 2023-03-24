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
import com.backwardsnode.easyadmin.api.record.BanRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * A builder for creating ban records.
 * @see RecordBuilder
 */
public interface BanBuilder extends ContextualBuilder<BanBuilder>, StaffBuilder<BanBuilder>, CommitableBuilder<BanRecord> {

    /**
     * Sets the start date for the ban record. Most applications will not need to use this method,
     * as the ban date will default to the current system time.
     * @param banDate the ban date.
     * @return this builder, for chaining.
     */
    @NotNull BanBuilder withBanDate(@NotNull LocalDateTime banDate);

    /**
     * Sets the end date for the ban record. If this is not set, the ban record will be permanent.
     * <p>Alternatively, use {@link #withUnbanAfter(Duration)} to set the duration.</p>
     * @param unbanDate the date and time the ban record is to be deactivated.
     * @return this builder, for chaining.
     */
    @NotNull BanBuilder withUnbanOn(@Nullable LocalDateTime unbanDate);

    /**
     * Sets the end date for the ban record, relative to the date and time the ban record is to be created at.
     * <p>Alternatively, use {@link #withUnbanOn(LocalDateTime)} to set the date and time precisely.</p>
     * @param unbanDuration the duration until the ban record is to be deactivated.
     * @return this builder, for chaining.
     */
    @NotNull BanBuilder withUnbanAfter(@Nullable Duration unbanDuration);

    /**
     * Sets the reason for the ban record.
     * @param banReason the ban reason.
     * @return this builder, for chaining.
     */
    @NotNull BanBuilder withBanReason(@Nullable String banReason);

}
