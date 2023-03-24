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
import com.backwardsnode.easyadmin.api.record.MuteRecord;
import com.backwardsnode.easyadmin.api.record.modify.MuteRecordModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public final class MuteRecordImpl implements MuteRecord {

    private transient boolean _loaded;

    private int id;
    private PunishmentStatus status;
    private UUID player;
    private UUID staff;
    private UUID unmuteStaff;
    private LocalDateTime muteDate;
    private LocalDateTime unmuteDate;
    private String ipAddress;
    private String contexts;
    private String reason;
    private String unmuteReason;

    MuteRecordImpl(boolean loaded, int id, PunishmentStatus status, UUID player, UUID staff, UUID unmuteStaff, LocalDateTime muteDate, LocalDateTime unmuteDate, String ipAddress, String contexts, String reason, String unmuteReason) {
        this._loaded = loaded;
        this.id = id;
        this.status = status;
        this.player = player;
        this.staff = staff;
        this.unmuteStaff = unmuteStaff;
        this.muteDate = muteDate;
        this.unmuteDate = unmuteDate;
        this.ipAddress = ipAddress;
        this.contexts = contexts;
        this.reason = reason;
        this.unmuteReason = unmuteReason;
    }

    public MuteRecordImpl(UUID player, UUID staff, LocalDateTime muteDate, LocalDateTime unmuteDate, String ipAddress, String contexts, String reason) {
        this(false, -1, PunishmentStatus.ACTIVE, player, staff, null, muteDate, unmuteDate, ipAddress, contexts, reason, null);
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
        return unmuteStaff;
    }

    @Override
    public @NotNull LocalDateTime getDateAdded() {
        return muteDate;
    }

    @Override
    public @Nullable LocalDateTime getTerminationDate() {
        return unmuteDate;
    }

    @Override
    public @Nullable String getIpAddress() {
        return ipAddress;
    }

    @Override
    public @Nullable String getContexts() {
        return contexts;
    }

    @Override
    public @Nullable String getReason() {
        return reason;
    }

    @Override
    public @Nullable String getTerminationReason() {
        return unmuteReason;
    }

    @Override
    public @NotNull MuteRecordModifier getModifiableRecord() {
        return new MuteRecordModification(this);
    }

    @Override
    public @NotNull MuteRecordImpl copy() {
        return new MuteRecordImpl(this._loaded, this.id, this.status, this.player, this.staff, this.unmuteStaff, this.muteDate, this.unmuteDate, this.ipAddress, this.contexts, this.reason, this.unmuteReason);
    }

    private static final class MuteRecordModification implements MuteRecordModifier {

        private final MuteRecordImpl record;

        private boolean changed = false;

        MuteRecordModification(MuteRecordImpl record) {
            this.record = record.copy();
        }

        @Override
        public @NotNull MuteRecordImpl getUpdatedRecord() {
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
        public void setAutoUnmuteDate(@NotNull LocalDateTime unmuteDate) {
            record.status = unmuteDate.isBefore(LocalDateTime.now()) ? PunishmentStatus.EXPIRED : PunishmentStatus.ACTIVE;
            record.unmuteStaff = null;
            record.unmuteDate = unmuteDate;
            record.unmuteReason = null;
            changed = record._loaded;
        }

        @Override
        public void reinstateMute() {
            record.status = PunishmentStatus.ACTIVE;
            record.unmuteStaff = null;
            record.unmuteDate = null;
            record.unmuteReason = null;
            changed = record._loaded;
        }

        @Override
        public void unmuteNow(@Nullable UUID unmuteStaff, @Nullable String unmuteReason) {
            record.status = PunishmentStatus.ENDED;
            record.unmuteStaff = unmuteStaff;
            record.unmuteDate = LocalDateTime.now();
            record.unmuteReason = unmuteReason;
            changed = record._loaded;
        }
    }
}
