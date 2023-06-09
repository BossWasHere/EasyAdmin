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

package com.backwardsnode.easyadmin.api.entity;

import com.backwardsnode.easyadmin.api.EasyAdminProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents a player who may be offline.
 */
public interface OfflinePlayer {

    /**
     * Gets the unique identifier for this player.
     * @return The player's UUID.
     */
    @NotNull UUID getUUID();

    /**
     * Gets the username for this player.
     * @return The player's username.
     */
    @NotNull String getUsername() throws IllegalStateException;

    /**
     * Gets the online player object for this player, if they are online.
     * @return An optional possibly containing an {@link OnlinePlayer} object.
     */
    @NotNull Optional<OnlinePlayer> getOnlinePlayer();

    /**
     * Determines if this target has ever been seen by the server.
     * @return true if the target has been seen, false otherwise.
     */
    default boolean isRegistered() {
        return EasyAdminProvider.get().getAdminManager().getPlayerRecord(getUUID()).isPresent();
    }

}
