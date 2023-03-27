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

package com.backwardsnode.easyadmin.core.record;

import com.backwardsnode.easyadmin.api.data.PunishmentStatus;
import com.backwardsnode.easyadmin.api.record.mutable.MutableMuteRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class MutableMuteRecordImpl extends MuteRecordImpl implements MutableMuteRecord, ImmutableRecordProvider<MuteRecordImpl> {

    private final MuteRecordImpl source;

    private boolean isModified = false;

    MutableMuteRecordImpl(MuteRecordImpl source) {
        super(source);
        this.source = source;
    }

    @Override
    public void setAuthor(@Nullable UUID author) {
        if (!Objects.equals(this.staff, author)) {
            this.staff = author;
            isModified = true;
        }
    }

    @Override
    public void setIpAddress(@Nullable String ipAddress) {
        if (!Objects.equals(this.ipAddress, ipAddress)) {
            this.ipAddress = ipAddress;
            isModified = true;
        }
    }

    @Override
    public void setReason(@Nullable String reason) {
        if (!Objects.equals(this.reason, reason)) {
            this.reason = reason;
            isModified = true;
        }
    }

    @Override
    public void setContext(@Nullable String context) {
        if (!Objects.equals(this.context, context)) {
            this.context = context;
            isModified = true;
        }
    }

    @Override
    public void setAutoUnmuteDate(@NotNull LocalDateTime unmuteDate) {
        if (!Objects.requireNonNull(unmuteDate).equals(this.unmuteDate)) {
            status = unmuteDate.isBefore(LocalDateTime.now()) ? PunishmentStatus.EXPIRED : PunishmentStatus.ACTIVE;
            this.unmuteDate = unmuteDate;
            if (status == PunishmentStatus.ACTIVE) {
                unmuteStaff = null;
                unmuteReason = null;
            }
            isModified = true;
        }
    }

    @Override
    public void unmuteNow(@Nullable UUID unmuteStaff, @Nullable String unmuteReason) {
        status = PunishmentStatus.ENDED;
        unmuteDate = LocalDateTime.now();
        this.unmuteStaff = unmuteStaff;
        this.unmuteReason = unmuteReason;
        isModified = true;
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public MuteRecordImpl getOriginal() {
        return source;
    }

    @Override
    public MuteRecordImpl asImmutable() {
        return isModified ? new MuteRecordImpl(this) : source;
    }
}
