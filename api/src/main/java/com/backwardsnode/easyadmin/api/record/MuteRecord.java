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

package com.backwardsnode.easyadmin.api.record;

import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import com.backwardsnode.easyadmin.api.contextual.Contextual;
import com.backwardsnode.easyadmin.api.data.ActionScope;
import com.backwardsnode.easyadmin.api.record.base.AdminRecord;
import com.backwardsnode.easyadmin.api.record.base.LiveRecord;
import com.backwardsnode.easyadmin.api.record.base.ReasonedRecord;
import com.backwardsnode.easyadmin.api.record.base.ScopedRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Represents a mute entry for a specific player (and sometimes IP address).
 * <p>A new record can be created through the {@link RecordBuilder}</p>
 */
public interface MuteRecord extends LiveRecord<Integer>, AdminRecord, ReasonedRecord, ScopedRecord, Contextual {

    default @Nullable Duration getDuration() {
        LocalDateTime terminationDate = getTerminationDate();
        return terminationDate == null ? null : Duration.between(getDateAdded(), terminationDate);
    }

    default @NotNull ActionScope getScope() {
        return ActionScope.fromFlags(isTemporary(), !hasContext(), hasIpAddress());
    }

}
