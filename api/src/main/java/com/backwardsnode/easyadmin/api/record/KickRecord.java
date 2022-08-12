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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a kick entry for a specific player.
 * <p>A new record can be created by calling the public constructor.</p>
 */
public final class KickRecord implements LiveRecord<Integer>, ReasonedAdminRecord {

    private transient boolean _loaded;

    private int id;
    private UUID player;
    private UUID staff;
    private LocalDateTime kickDate;
    private boolean isGlobal;
    private String reason;

    KickRecord(boolean loaded, int id, UUID player, UUID staff, LocalDateTime kickDate, boolean isGlobal, String reason) {
        this._loaded = loaded;
        this.id = id;
        this.player = player;
        this.staff = staff;
        this.kickDate = kickDate;
        this.isGlobal = isGlobal;
        this.reason = reason;
    }

    /**
     * Creates a new kick record.
     * @param player the player impacted by this kick.
     * @param staff the staff member who issued this kick, or null if issued by the console.
     * @param kickDate the date and time the kick was issued.
     * @param isGlobal whether this kick is global or not.
     * @param reason the reason for this kick.
     */
    public KickRecord(UUID player, UUID staff, LocalDateTime kickDate, boolean isGlobal, String reason) {
        this(false, -1, player, staff, kickDate, isGlobal, reason);
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
    public @Nullable UUID getStaff() {
        return staff;
    }
    @Override
    public @NotNull LocalDateTime getDateAdded() {
        return kickDate;
    }

    /**
     * Gets whether this kick is global or not.
     * <p>A global kick disconnects the player from the network. A simple kick moves the player into a previous/fallback server when used with a server proxy.</p>
     * @return true if this kick is global, false otherwise.
     */
    public boolean isGlobal() {
        return isGlobal;
    }
    @Override
    public @Nullable String getReason() {
        return reason;
    }

}
