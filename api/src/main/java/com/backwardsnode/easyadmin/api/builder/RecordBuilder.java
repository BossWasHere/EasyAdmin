/*
 * MIT License
 *
 * Copyright (c) 2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

package com.backwardsnode.easyadmin.api.builder;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Builder for creating and activating new records.
 * <p>The primary API builder can be accessed through the main instance of {@link EasyAdmin}</p>
 */
public interface RecordBuilder {

    /**
     * Instantiates a new ban builder targeting the given player.
     * @param playerUUID the UUID of the player to ban.
     * @return a new {@link BanBuilder}.
     */
    @NotNull BanBuilder banPlayer(@NotNull UUID playerUUID);

    /**
     * Instantiates a new ban builder targeting the given network address.
     * @param address the address to ban.
     * @return a new {@link BanBuilder}.
     */
    @NotNull BanBuilder banAddress(@NotNull String address);

    /**
     * Instantiates a new ban builder targeting the given player and specified network address.
     * @param playerUUID the UUID of the player to ban.
     * @param address the address to ban.
     * @return a new {@link BanBuilder}.
     */
    @NotNull BanBuilder banPlayerAndAddress(@NotNull UUID playerUUID, @NotNull String address);

    /**
     * Instantiates a new mute builder targeting the given player.
     * @param playerUUID the UUID of the player to mute.
     * @return a new {@link MuteBuilder}.
     */
    @NotNull MuteBuilder mutePlayer(@NotNull UUID playerUUID);

    /**
     * Instantiates a new mute builder targeting the given network address.
     * @param address the address to mute.
     * @return a new {@link MuteBuilder}.
     */
    @NotNull MuteBuilder muteAddress(@NotNull String address);

    /**
     * Instantiates a new mute builder targeting the given player and specified network address.
     * @param playerUUID the UUID of the player to mute.
     * @param address the address to mute.
     * @return a new {@link MuteBuilder}.
     */
    @NotNull MuteBuilder mutePlayerAndAddress(@NotNull UUID playerUUID, @NotNull String address);

    /**
     * Instantiates a new kick builder targeting the given player.
     * @param playerUUID the UUID of the player to kick.
     * @return a new {@link KickBuilder}.
     */
    @NotNull KickBuilder kickPlayer(@NotNull UUID playerUUID);

    /**
     * Instantiates a new comment builder targeting the given player.
     * @param playerUUID the UUID of the player to comment or warn.
     * @param comment the comment to add to the player's record.
     * @return a new {@link CommentBuilder}.
     */
    @NotNull CommentBuilder commentOnPlayer(@NotNull UUID playerUUID, @NotNull String comment);
}


