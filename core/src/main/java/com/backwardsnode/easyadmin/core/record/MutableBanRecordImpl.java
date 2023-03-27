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
import com.backwardsnode.easyadmin.api.record.mutable.MutableBanRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class MutableBanRecordImpl extends BanRecordImpl implements MutableBanRecord, ImmutableRecordProvider<BanRecordImpl> {

    private final BanRecordImpl source;

    private boolean isModified = false;

    MutableBanRecordImpl(BanRecordImpl source) {
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
    public void setAutoUnbanDate(@NotNull LocalDateTime unbanDate) {
        if (!Objects.requireNonNull(unbanDate).equals(this.unbanDate)) {
            status = unbanDate.isBefore(LocalDateTime.now()) ? PunishmentStatus.EXPIRED : PunishmentStatus.ACTIVE;
            this.unbanDate = unbanDate;
            if (status == PunishmentStatus.ACTIVE) {
                unbanStaff = null;
                unbanReason = null;
            }
            isModified = true;
        }
    }

    @Override
    public void unbanNow(@Nullable UUID unbanStaff, @Nullable String unbanReason) {
        status = PunishmentStatus.ENDED;
        unbanDate = LocalDateTime.now();
        this.unbanStaff = unbanStaff;
        this.unbanReason = unbanReason;
        isModified = true;
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public BanRecordImpl getOriginal() {
        return source;
    }

    @Override
    public BanRecordImpl asImmutable() {
        return isModified ? new BanRecordImpl(this) : source;
    }
}
