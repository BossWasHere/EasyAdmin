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
import com.backwardsnode.easyadmin.api.builder.ext.StaffBuilder;
import com.backwardsnode.easyadmin.api.record.KickRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * A builder for creating kick records.
 * @see RecordBuilder
 */
public interface KickBuilder extends StaffBuilder<KickBuilder>, CommitableBuilder<KickRecord> {

    /**
     * Sets the creation date for the kick record. Most applications will not need to use this method,
     * as the kick date will default to the current system time.
     * @param kickDate the kick date.
     * @return this builder, for chaining.
     */
    @NotNull KickBuilder withKickDate(@NotNull LocalDateTime kickDate);

    /**
     * Sets whether the kick is global or not. By default, the builder will create a local kick,
     * moving them to a different server instead of disconnecting the player when in proxy mode.
     * This has no effect in single server mode.
     * @param isGlobal whether the kick is global or not.
     * @return this builder, for chaining.
     */
    @NotNull KickBuilder setGlobal(boolean isGlobal);

    /**
     * Sets the reason for the kick record.
     * @param kickReason the kick reason.
     * @return this builder, for chaining.
     */
    @NotNull KickBuilder withKickReason(@Nullable String kickReason);

    /**
     * Sets whether the kick should actually be performed. You may want to set this to false if you wish to handle this
     * manually. By default, the kick will be performed.
     * @param shouldPerform whether the kick should actually be performed.
     * @return this builder, for chaining.
     */
    @NotNull KickBuilder actuallyPerform(boolean shouldPerform);

}
