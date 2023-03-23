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

import com.backwardsnode.easyadmin.api.record.modify.PlayerRecordModifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents and stores statistics for a player.
 * <p>A new record can be created by calling the public constructor.</p>
 */
public final class PlayerRecord implements LiveRecord<UUID>, ModifiableRecord<PlayerRecordModifier, PlayerRecord> {

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

    PlayerRecord(boolean loaded, UUID uuid, String username, LocalDateTime firstJoin, LocalDateTime lastJoin, LocalDateTime lastLeave, long playtime, int totalJoins, String lastServer, String lastAddress) {
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

    /**
     * Creates a new player record.
     * @param uuid the UUID of the player.
     * @param username the username of the player.
     * @param firstJoin the date and time the player first joined. This will also set the last join date.
     * @param lastServer the last server the player was on, or null if not stored.
     * @param lastAddress the last IP address the player was on, or null if not stored.
     */
    public PlayerRecord(@NotNull UUID uuid, @NotNull String username, @NotNull LocalDateTime firstJoin, @Nullable String lastServer, @Nullable String lastAddress) {
        this(false, uuid, username, firstJoin, firstJoin, null, 0, 0, lastServer, lastAddress);
    }

    /**
     * Creates a new player record.
     * @param uuid the UUID of the player.
     * @param username the username of the player.
     * @param firstJoin the date and time the player first joined. This will also set the last join date.
     * @param totalJoins the total number of times the player has joined.
     * @param lastServer the last server the player was on, or null if not stored.
     * @param lastAddress the last IP address the player was on, or null if not stored.
     */
    public PlayerRecord(@NotNull UUID uuid, @NotNull String username, @NotNull LocalDateTime firstJoin, int totalJoins, @Nullable String lastServer, @Nullable String lastAddress) {
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

    /**
     * Gets the username associated with this record. This may not be up-to-date.
     * @return the player's username.
     */
    public @NotNull String getUsername() {
        return username;
    }

    /**
     * Gets the date and time at which the player first joined.
     * @return the date of the first join.
     */
    public @NotNull LocalDateTime getFirstJoin() {
        return firstJoin;
    }

    /**
     * Gets the date and time at which the player last joined.
     * @return the date of the last join.
     */
    public @NotNull LocalDateTime getLastJoin() {
        return lastJoin;
    }

    /**
     * Gets the date and time at which the player last left.
     * @return the date of the last leave.
     */
    public @Nullable LocalDateTime getLastLeave() {
        return lastLeave;
    }

    /**
     * Gets the total amount of time in milliseconds the player has been online for.
     * <p>By default, this value is only updated when a player disconnects (thus excluding the extra time from the current session).</p>
     * @return the total playtime in milliseconds.
     */
    public long getPlaytime() {
        return playtime;
    }

    /**
     * Gets the total number of times the player has joined the server.
     * @return the number of joins.
     */
    public int getTotalJoins() {
        return totalJoins;
    }

    /**
     * Gets the name of the last server this player was on. May return null if configuration is set to not store this information.
     * @return the last server, or null if not stored.
     */
    public @Nullable String getLastServer() {
        return lastServer;
    }

    /**
     * Gets the last IP address this player was on. May return null if configuration is set to not store this information.
     * @return the last IP address, or null if not stored.
     */
    public @Nullable String getLastAddress() {
        return lastAddress;
    }

    @Override
    public @NotNull PlayerRecordModifier getModifiableRecord() {
        return new PlayerRecordModification(this);
    }

    @Override
    public @NotNull PlayerRecord copy() {
        return new PlayerRecord(this._loaded, this.uuid, this.username, this.firstJoin, this.lastJoin, this.lastLeave, this.playtime, this.totalJoins, this.lastServer, this.lastAddress);
    }

    private static final class PlayerRecordModification implements PlayerRecordModifier {

        private final PlayerRecord record;

        private boolean playerJoiningStatsChanged = false;
        private boolean playerLeavingStatsChanged = false;
        private boolean playerDynamicStatsChanged = false;

        PlayerRecordModification(PlayerRecord record) {
            this.record = record.copy();
        }

        @Override
        public @NotNull PlayerRecord getUpdatedRecord() {
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
