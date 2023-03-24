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

import com.backwardsnode.easyadmin.api.record.PlayerRecord;
import com.backwardsnode.easyadmin.api.record.modify.PlayerRecordModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public final class PlayerRecordImpl implements PlayerRecord {

    private transient boolean _loaded;

    private UUID uuid;
    private String username;
    private LocalDateTime firstJoin;
    private LocalDateTime lastJoin;
    private LocalDateTime lastLeave;
    private long playtime;
    private int totalJoins;
    private String lastServer;
    private String lastAddress;

    PlayerRecordImpl(boolean loaded, UUID uuid, String username, LocalDateTime firstJoin, LocalDateTime lastJoin, LocalDateTime lastLeave, long playtime, int totalJoins, String lastServer, String lastAddress) {
        this._loaded = loaded;
        this.uuid = uuid;
        this.username = username;
        this.firstJoin = firstJoin;
        this.lastJoin = lastJoin;
        this.lastLeave = lastLeave;
        this.playtime = playtime;
        this.totalJoins = totalJoins;
        this.lastServer = lastServer;
        this.lastAddress = lastAddress;
    }

    public PlayerRecordImpl(@NotNull UUID uuid, @NotNull String username, @NotNull LocalDateTime firstJoin, @Nullable String lastServer, @Nullable String lastAddress) {
        this(false, uuid, username, firstJoin, firstJoin, null, 0, 0, lastServer, lastAddress);
    }

    public PlayerRecordImpl(@NotNull UUID uuid, @NotNull String username, @NotNull LocalDateTime firstJoin, int totalJoins, @Nullable String lastServer, @Nullable String lastAddress) {
        this(false, uuid, username, firstJoin, firstJoin, null, 0, totalJoins, lastServer, lastAddress);
    }

    @Override
    public boolean isLoaded() {
        return _loaded;
    }

    @Override
    public @NotNull UUID getId() {
        return uuid;
    }

    public @NotNull String getUsername() {
        return username;
    }

    public @NotNull LocalDateTime getFirstJoin() {
        return firstJoin;
    }

    public @NotNull LocalDateTime getLastJoin() {
        return lastJoin;
    }

    public @Nullable LocalDateTime getLastLeave() {
        return lastLeave;
    }

    public long getPlaytime() {
        return playtime;
    }

    public int getTotalJoins() {
        return totalJoins;
    }

    public @Nullable String getLastServer() {
        return lastServer;
    }

    public @Nullable String getLastAddress() {
        return lastAddress;
    }

    @Override
    public @NotNull PlayerRecordModifier getModifiableRecord() {
        return new PlayerRecordModification(this);
    }

    @Override
    public @NotNull PlayerRecordImpl copy() {
        return new PlayerRecordImpl(this._loaded, this.uuid, this.username, this.firstJoin, this.lastJoin, this.lastLeave, this.playtime, this.totalJoins, this.lastServer, this.lastAddress);
    }

    private static final class PlayerRecordModification implements PlayerRecordModifier {

        private final PlayerRecordImpl record;

        private boolean playerJoiningStatsChanged = false;
        private boolean playerLeavingStatsChanged = false;
        private boolean playerDynamicStatsChanged = false;

        PlayerRecordModification(PlayerRecordImpl record) {
            this.record = record.copy();
        }

        @Override
        public @NotNull PlayerRecordImpl getUpdatedRecord() {
            return record;
        }

        @Override
        public boolean hasChanged() {
            return playerJoiningStatsChanged || playerLeavingStatsChanged || playerDynamicStatsChanged;
        }

        @Override
        public void push() {

        }

        public boolean hasPlayerJoinStatsChanged() {
            return playerJoiningStatsChanged;
        }

        public boolean hasPlayerLeaveStatsChanged() {
            return playerLeavingStatsChanged;
        }

        public boolean hasPlayerDynamicStatsChanged() {
            return playerDynamicStatsChanged;
        }

        public void setUsername(@NotNull String username) {
            record.username = username;
            playerJoiningStatsChanged = record._loaded;
        }

        public void setLastJoined(@NotNull LocalDateTime lastJoin) {
            record.lastJoin = lastJoin;
            playerJoiningStatsChanged = record._loaded;
        }

        public void setLastLeft(@Nullable LocalDateTime lastLeave) {
            record.lastLeave = lastLeave;
            playerLeavingStatsChanged = record._loaded;
        }

        public void setPlaytime(long playtime) {
            record.playtime = playtime;
            playerLeavingStatsChanged = record._loaded;
        }

        public void addPlaytime(long timeToAdd) {
            record.playtime += timeToAdd;
            playerLeavingStatsChanged = record._loaded;
        }

        public void setTotalJoins(int totalJoins) {
            record.totalJoins = totalJoins;
            playerJoiningStatsChanged = record._loaded;
        }

        public void incrementTotalJoins() {
            record.totalJoins++;
            playerJoiningStatsChanged = record._loaded;
        }

        public void setLastServer(@Nullable String lastServer) {
            record.lastServer = lastServer;
            playerDynamicStatsChanged = record._loaded;
        }

        public void setLastAddress(@Nullable String lastAddress) {
            record.lastAddress = lastAddress;
            playerJoiningStatsChanged = record._loaded;
        }
    }
}
