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
import com.backwardsnode.easyadmin.api.record.BanRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public class BanRecordImpl implements BanRecord, MutableRecordProvider<MutableBanRecordImpl> {

    protected transient boolean _loaded;

    protected int id;
    protected PunishmentStatus status;
    protected UUID player;
    protected UUID staff;
    protected UUID unbanStaff;
    protected LocalDateTime banDate;
    protected LocalDateTime unbanDate;
    protected String ipAddress;
    protected String context;
    protected String reason;
    protected String unbanReason;

    BanRecordImpl(boolean loaded, int id, PunishmentStatus status, UUID player, UUID staff, UUID unbanStaff, LocalDateTime banDate, LocalDateTime unbanDate, String ipAddress, String contexts, String reason, String unbanReason) {
        this._loaded = loaded;
        this.id = id;
        this.status = status;
        this.player = player;
        this.staff = staff;
        this.unbanStaff = unbanStaff;
        this.banDate = banDate;
        this.unbanDate = unbanDate;
        this.ipAddress = ipAddress;
        this.context = contexts;
        this.reason = reason;
        this.unbanReason = unbanReason;
    }

    protected BanRecordImpl(BanRecordImpl source) {
        this(source._loaded, source.id, source.status, source.player, source.staff, source.unbanStaff, source.banDate, source.unbanDate, source.ipAddress, source.context, source.reason, source.unbanReason);
    }

    public BanRecordImpl(@NotNull UUID player, @Nullable UUID staff, @NotNull LocalDateTime banDate, @Nullable LocalDateTime unbanDate, @Nullable String ipAddress, @Nullable String contexts, @Nullable String reason) {
        this(false, -1, PunishmentStatus.ACTIVE, player, staff, null, banDate, unbanDate, ipAddress, contexts, reason, null);
    }

    @Override
    public boolean isLoaded() {
        return _loaded;
    }

    @Override
    public @NotNull Integer getId() {
        return id;
    }
    @Override
    public @NotNull PunishmentStatus getStatus() {
        return status;
    }

    @Override
    public @NotNull UUID getPlayer() {
        return player;
    }

    @Override
    public @Nullable UUID getAuthor() {
        return staff;
    }

    @Override
    public @Nullable UUID getTerminatingStaff() {
        return unbanStaff;
    }

    @Override
    public @NotNull LocalDateTime getDateAdded() {
        return banDate;
    }

    @Override
    public @Nullable LocalDateTime getTerminationDate() {
        return unbanDate;
    }

    @Override
    public @Nullable String getIpAddress() {
        return ipAddress;
    }

    @Override
    public @Nullable String getContext() {
        return context;
    }

    @Override
    public @Nullable String getReason() {
        return reason;
    }

    @Override
    public @Nullable String getTerminationReason() {
        return unbanReason;
    }

    @Override
    public @NotNull MutableBanRecordImpl asMutable() {
        return new MutableBanRecordImpl(this);
    }
}
