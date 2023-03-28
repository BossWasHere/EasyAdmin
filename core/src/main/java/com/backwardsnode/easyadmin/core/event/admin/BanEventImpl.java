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

package com.backwardsnode.easyadmin.core.event.admin;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.data.PunishmentStatus;
import com.backwardsnode.easyadmin.api.data.ServiceSource;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.event.admin.BanEvent;
import com.backwardsnode.easyadmin.api.record.BanRecord;
import com.backwardsnode.easyadmin.api.record.mutable.MutableBanRecord;
import com.backwardsnode.easyadmin.core.event.base.CancellableEvent;
import com.backwardsnode.easyadmin.core.exception.ImmutableEventException;
import com.backwardsnode.easyadmin.core.record.BanRecordImpl;
import com.backwardsnode.easyadmin.core.record.MutableBanRecordImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public final class BanEventImpl extends CancellableEvent implements BanEvent {

    private final ServiceSource source;
    private final BanRecordImpl banRecord;
    private final CommandExecutor executor;
    private final boolean modifiable;

    private MutableBanRecordImpl mutableRecord;

    public BanEventImpl(EasyAdmin instance, ServiceSource source, BanRecordImpl banRecord,
                        CommandExecutor executor, boolean cancellable, boolean modifiable) {
        super(instance, BanEvent.class, cancellable);
        this.source = source;
        this.banRecord = banRecord;
        this.executor = executor;
        this.modifiable = modifiable;
    }

    @Override
    public @NotNull ServiceSource getSource() {
        return source;
    }

    @Override
    public @Nullable CommandExecutor getExecutor() {
        return executor;
    }

    @Override
    public boolean isModified() {
        return mutableRecord != null && mutableRecord.isModified();
    }

    @Override
    public boolean canModify() {
        return modifiable && !sealed;
    }

    @Override
    public BanRecord getOriginal() {
        return banRecord;
    }

    @Override
    public @NotNull BanRecordImpl getCurrent() {
        return isModified() ? mutableRecord.asImmutable() : banRecord;
    }

    @Override
    public @Nullable String getContext() {
        return getCurrent().getContext();
    }

    @Override
    public @NotNull UUID getPlayer() {
        return getCurrent().getPlayer();
    }

    @Override
    public @Nullable UUID getAuthor() {
        return getCurrent().getAuthor();
    }

    @Override
    public @NotNull LocalDateTime getDateAdded() {
        return getCurrent().getDateAdded();
    }

    @Override
    public @NotNull Integer getId() {
        return getCurrent().getId();
    }

    @Override
    public boolean isLoaded() {
        return getCurrent().isLoaded();
    }

    @Override
    public @Nullable String getReason() {
        return getCurrent().getReason();
    }

    @Override
    public @NotNull PunishmentStatus getStatus() {
        return getCurrent().getStatus();
    }

    @Override
    public @Nullable UUID getTerminatingStaff() {
        return getCurrent().getTerminatingStaff();
    }

    @Override
    public @Nullable LocalDateTime getTerminationDate() {
        return getCurrent().getTerminationDate();
    }

    @Override
    public @Nullable String getIpAddress() {
        return getCurrent().getIpAddress();
    }

    @Override
    public @Nullable String getTerminationReason() {
        return getCurrent().getTerminationReason();
    }

    @Override
    public void setAuthor(@Nullable UUID author) {
        getMutable().setAuthor(author);
    }

    @Override
    public void setIpAddress(@Nullable String ipAddress) {
        getMutable().setIpAddress(ipAddress);
    }

    @Override
    public void setReason(@Nullable String reason) {
        getMutable().setReason(reason);
    }

    @Override
    public void setContext(@Nullable String context) {
        getMutable().setContext(context);
    }

    @Override
    public void setAutoUnbanDate(@NotNull LocalDateTime unbanDate) {
        getMutable().setAutoUnbanDate(unbanDate);
    }

    @Override
    public void unbanNow(@Nullable UUID unbanStaff, @Nullable String unbanReason) {
        getMutable().unbanNow(unbanStaff, unbanReason);
    }

    private MutableBanRecord getMutable() {
        if (!modifiable || sealed) {
            throw new ImmutableEventException();
        }
        if (mutableRecord == null) {
            mutableRecord = banRecord.asMutable();
        }
        return mutableRecord;
    }
}
