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

import com.backwardsnode.easyadmin.api.record.mutable.MutablePlayerRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;

public final class MutablePlayerRecordImpl extends PlayerRecordImpl implements MutablePlayerRecord {

    private final PlayerRecordImpl source;

    private boolean joinStatsChanged = false;
    private boolean leaveStatsChanged = false;
    private boolean dynamicStatsChanged = false;

    MutablePlayerRecordImpl(PlayerRecordImpl source) {
        super(source);
        this.source = source;
    }

    @Override
    public boolean hasPlayerJoinStatsChanged() {
        return joinStatsChanged;
    }

    @Override
    public boolean hasPlayerLeaveStatsChanged() {
        return leaveStatsChanged;
    }

    @Override
    public boolean hasPlayerDynamicStatsChanged() {
        return dynamicStatsChanged;
    }

    @Override
    public void setUsername(@NotNull String username) {
        if (!Objects.requireNonNull(username).equals(this.username)) {
            this.username = username;
            joinStatsChanged = true;
        }
    }

    @Override
    public void setLastJoined(@NotNull LocalDateTime lastJoin) {
        if (!Objects.requireNonNull(lastJoin).equals(this.lastJoin)) {
            this.lastJoin = lastJoin;
            joinStatsChanged = true;
        }
    }

    @Override
    public void setLastLeft(@Nullable LocalDateTime lastLeave) {
        if (!Objects.equals(lastLeave, this.lastLeave)) {
            this.lastLeave = lastLeave;
            leaveStatsChanged = true;
        }
    }

    @Override
    public void setPlaytime(long playtime) {
        if (playtime != this.playtime) {
            this.playtime = playtime;
            leaveStatsChanged = true;
        }
    }

    @Override
    public void addPlaytime(long timeToAdd) {
        if (timeToAdd != 0) {
            this.playtime += timeToAdd;
            leaveStatsChanged = true;
        }
    }

    @Override
    public void setTotalJoins(int totalJoins) {
        if (totalJoins != this.totalJoins) {
            this.totalJoins = totalJoins;
            joinStatsChanged = true;
        }
    }

    @Override
    public void incrementTotalJoins() {
        this.totalJoins++;
        joinStatsChanged = true;
    }

    @Override
    public void setLastServer(@Nullable String lastServer) {
        if (!Objects.equals(lastServer, this.lastServer)) {
            this.lastServer = lastServer;
            dynamicStatsChanged = true;
        }
    }

    @Override
    public void setLastAddress(@Nullable String lastAddress) {
        if (!Objects.equals(lastAddress, this.lastAddress)) {
            this.lastAddress = lastAddress;
            joinStatsChanged = true;
        }
    }

    @Override
    public boolean isModified() {
        return joinStatsChanged || leaveStatsChanged || dynamicStatsChanged;
    }

    @Override
    public PlayerRecordImpl getOriginal() {
        return source;
    }

    @Override
    public PlayerRecordImpl asImmutable() {
        return isModified() ? new PlayerRecordImpl(this) : source;
    }
}
