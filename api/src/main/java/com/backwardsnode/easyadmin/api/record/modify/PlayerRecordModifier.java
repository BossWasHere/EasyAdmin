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

package com.backwardsnode.easyadmin.api.record.modify;

import com.backwardsnode.easyadmin.api.record.PlayerRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

/**
 * Modification wrapper for {@link PlayerRecord}.
 */
public interface PlayerRecordModifier extends RecordModifier<PlayerRecord> {

    /**
     * Checks if any stats which are updated when a player connects to the server have changed.
     * @return true if any stats have changed, otherwise false.
     */
    boolean hasPlayerJoinStatsChanged();

    /**
     * Checks if any stats which are updated when a player disconnects from the server have changed.
     * @return true if any stats have changed, otherwise false.
     */
    boolean hasPlayerLeaveStatsChanged();

    /**
     * Checks if any stats which are updated during play have changed.
     * @return true if any stats have changed, otherwise false.
     */
    boolean hasPlayerDynamicStatsChanged();

    /**
     * Updates the username of this player record.
     * @param username the username to set.
     */
    void setUsername(@NotNull String username);

    /**
     * Updates the last join time of this player record.
     * @param lastJoin the last join time to set.
     */
    void setLastJoined(@NotNull LocalDateTime lastJoin);

    /**
     * Updates the last leave time of this player record.
     * @param lastLeave the last leave time to set.
     */
    void setLastLeft(@Nullable LocalDateTime lastLeave);

    /**
     * Updates the playtime of this player record.
     * @param playtime the playtime to set.
     */
    void setPlaytime(long playtime);

    /**
     * Adds to the playtime of this player record.
     * @param timeToAdd the amount of playtime to add.
     */
    void addPlaytime(long timeToAdd);

    /**
     * Updates the total number of joins of this player record.
     * @param totalJoins the total joins to set.
     */
    void setTotalJoins(int totalJoins);

    /**
     * Increments the total number of joins of this player record by 1.
     */
    void incrementTotalJoins();

    /**
     * Updates name of the last server of this player record.
     * @param lastServer the last server this player was on, or null to clear.
     */
    void setLastServer(@Nullable String lastServer);

    /**
     * Updates the last IP address of this player record.
     * @param lastAddress the last IP address this player was on, or null to clear.
     */
    void setLastAddress(@Nullable String lastAddress);

}
