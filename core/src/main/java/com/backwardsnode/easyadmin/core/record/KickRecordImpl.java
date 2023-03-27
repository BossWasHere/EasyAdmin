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

import com.backwardsnode.easyadmin.api.record.KickRecord;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public class KickRecordImpl implements KickRecord, MutableRecordProvider<MutableKickRecordImpl> {

    protected transient boolean _loaded;

    protected int id;
    protected UUID player;
    protected UUID staff;
    protected LocalDateTime kickDate;
    protected boolean isGlobal;
    protected String reason;
    protected String serverName;

    KickRecordImpl(boolean loaded, int id, UUID player, UUID staff, LocalDateTime kickDate, boolean isGlobal, String serverName, String reason) {
        this._loaded = loaded;
        this.id = id;
        this.player = player;
        this.staff = staff;
        this.kickDate = kickDate;
        this.isGlobal = isGlobal;
        this.serverName = serverName;
        this.reason = reason;
    }

    protected KickRecordImpl(KickRecordImpl source) {
        this(source._loaded, source.id, source.player, source.staff, source.kickDate, source.isGlobal, source.serverName, source.reason);
    }

    public KickRecordImpl(UUID player, UUID staff, LocalDateTime kickDate, boolean isGlobal, String reason) {
        this(false, -1, player, staff, kickDate, isGlobal, null, reason);
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
    public @NotNull UUID getPlayer() {
        return player;
    }

    @Override
    public @Nullable UUID getAuthor() {
        return staff;
    }

    @Override
    public @NotNull LocalDateTime getDateAdded() {
        return kickDate;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    @Override
    public @NotNull String getServerName() {
        return serverName;
    }

    @ApiStatus.Internal
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    @Override
    public @Nullable String getReason() {
        return reason;
    }

    @Override
    public @NotNull MutableKickRecordImpl asMutable() {
        return new MutableKickRecordImpl(this);
    }
}
