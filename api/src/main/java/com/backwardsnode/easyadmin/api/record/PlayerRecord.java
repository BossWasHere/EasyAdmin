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

import com.backwardsnode.easyadmin.api.record.base.LiveRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents and stores statistics for a player.
 */
public interface PlayerRecord extends LiveRecord<UUID> {

    /**
     * Gets the username associated with this record. This may not be up-to-date.
     * @return the player's username.
     */
    @NotNull String getUsername();

    /**
     * Gets the date and time at which the player first joined.
     * @return the date of the first join.
     */
    @NotNull LocalDateTime getFirstJoin();

    /**
     * Gets the date and time at which the player last joined.
     * @return the date of the last join.
     */
    @NotNull LocalDateTime getLastJoin();

    /**
     * Gets the date and time at which the player last left.
     * @return the date of the last leave.
     */
    @Nullable LocalDateTime getLastLeave();

    /**
     * Gets the total amount of time in milliseconds the player has been online for.
     * <p>By default, this value is only updated when a player disconnects (thus excluding the extra time from the current session).</p>
     * @return the total playtime in milliseconds.
     */
    long getPlaytime();

    /**
     * Gets the total number of times the player has joined the server.
     * @return the number of joins.
     */
    int getTotalJoins();

    /**
     * Gets the name of the last server this player was on. May return null if configuration is set to not store this information.
     * @return the last server, or null if not stored.
     */
    @Nullable String getLastServer();

    /**
     * Gets the last IP address this player was on. May return null if configuration is set to not store this information.
     * @return the last IP address, or null if not stored.
     */
    @Nullable String getLastAddress();

}
