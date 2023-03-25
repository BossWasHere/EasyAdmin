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
import com.backwardsnode.easyadmin.api.record.modify.BanRecordModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public final class BanRecordImpl implements BanRecord {

    private transient boolean _loaded;

    private int id;
    private PunishmentStatus status;
    private UUID player;
    private UUID staff;
    private UUID unbanStaff;
    private LocalDateTime banDate;
    private LocalDateTime unbanDate;
    private String ipAddress;
    private String contexts;
    private String reason;
    private String unbanReason;

    public BanRecordImpl(boolean loaded, int id, PunishmentStatus status, UUID player, UUID staff, UUID unbanStaff, LocalDateTime banDate, LocalDateTime unbanDate, String ipAddress, String contexts, String reason, String unbanReason) {
        this._loaded = loaded;
        this.status = status;
        this.player = player;
        this.staff = staff;
        this.unbanStaff = unbanStaff;
        this.banDate = banDate;
        this.unbanDate = unbanDate;
        this.ipAddress = ipAddress;
        this.contexts = contexts;
        this.reason = reason;
        this.unbanReason = unbanReason;
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
    public @Nullable UUID getStaff() {
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
        return contexts;
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
    public @NotNull BanRecordModifier getModifiableRecord() {
        return new BanRecordModification(this);
    }

    @Override
    public @NotNull BanRecordImpl copy() {
        return new BanRecordImpl(this._loaded, this.id, this.status, this.player, this.staff, this.unbanStaff, this.banDate, this.unbanDate, this.ipAddress, this.contexts, this.reason, this.unbanReason);
    }

    private static final class BanRecordModification implements BanRecordModifier {

        private final BanRecordImpl record;

        private boolean changed = false;

        public BanRecordModification(BanRecordImpl record) {
            this.record = record.copy();
        }

        @Override
        public @NotNull BanRecordImpl getUpdatedRecord() {
            return record;
        }

        @Override
        public boolean hasChanged() {
            return changed;
        }

        @Override
        public void push() {

        }

        @Override
        public void setAutoUnbanDate(@NotNull LocalDateTime unbanDate) {
            record.status = unbanDate.isBefore(LocalDateTime.now()) ? PunishmentStatus.EXPIRED : PunishmentStatus.ACTIVE;
            record.unbanStaff = null;
            record.unbanDate = unbanDate;
            record.unbanReason = null;
            changed = record._loaded;
        }

        @Override
        public void reinstateBan() {
            record.status = PunishmentStatus.ACTIVE;
            record.unbanStaff = null;
            record.unbanDate = null;
            record.unbanReason = null;
            changed = record._loaded;
        }

        @Override
        public void unbanNow(@Nullable UUID unbanStaff, @Nullable String unbanReason) {
            record.status = PunishmentStatus.ENDED;
            record.unbanStaff = unbanStaff;
            record.unbanDate = LocalDateTime.now();
            record.unbanReason = unbanReason;
            changed = record._loaded;
        }
    }
}
