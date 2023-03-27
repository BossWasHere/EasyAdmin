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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public class PlayerRecordImpl implements PlayerRecord, MutableRecordProvider<MutablePlayerRecordImpl> {

    protected transient boolean _loaded;

    protected UUID uuid;
    protected String username;
    protected LocalDateTime firstJoin;
    protected LocalDateTime lastJoin;
    protected LocalDateTime lastLeave;
    protected long playtime;
    protected int totalJoins;
    protected String lastServer;
    protected String lastAddress;

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

    protected PlayerRecordImpl(PlayerRecordImpl source) {
        this(source._loaded, source.uuid, source.username, source.firstJoin, source.lastJoin, source.lastLeave, source.playtime, source.totalJoins, source.lastServer, source.lastAddress);
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
    public @NotNull MutablePlayerRecordImpl asMutable() {
        return new MutablePlayerRecordImpl(this);
    }
}
