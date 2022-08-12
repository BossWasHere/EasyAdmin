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

package com.backwardsnode.easyadmin.api.admin;

import com.backwardsnode.easyadmin.api.record.BanRecord;
import com.backwardsnode.easyadmin.api.record.MuteRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * The main API for requesting, updating and saving admin records.
 */
public interface AdminManager {

    /**
     * Gets the first active {@link BanRecord} for a given player UUID, if one exists.
     * @param playerUUID the UUID of the player.
     * @param inContext the context to check for a ban in, or null for only global bans.
     * @param includeGlobalBans whether this method can return a global ban record.
     * @return a ban record, or null if none exist.
     */
    @Nullable BanRecord getActiveBanRecord(@NotNull UUID playerUUID, @Nullable String inContext, boolean includeGlobalBans);

    /**
     * Gets the first active {@link BanRecord} for a given IP address, if one exists.
     * @param ipAddress the IP address to check for.
     * @param inContext the context to check for a ban in, or null for only global bans.
     * @param includeGlobalBans whether this method can return a global ban record.
     * @return a ban record, or null if none exist.
     */
    @Nullable BanRecord getActiveBanRecord(@NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalBans);

    /**
     * Gets the first active {@link BanRecord} for a given player UUID <b>OR</b> IP address, if one exists.
     * @param playerUUID the UUID of the player.
     * @param ipAddress the IP address to check for.
     * @param inContext the context to check for a ban in, or null for only global bans.
     * @param includeGlobalBans whether this method can return a global ban record.
     * @return a ban record, or null if none exist.
     */
    @Nullable BanRecord getActiveBanRecord(@NotNull UUID playerUUID, @NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalBans);

    /**
     * Gets the first active {@link MuteRecord} for a given player UUID, if one exists.
     * @param playerUUID the UUID of the player.
     * @param inContext the context to check for a mute in, or null for only global mutes.
     * @param includeGlobalMutes whether this method can return a global mute record.
     * @return a mute record, or null if none exist.
     */
    @Nullable MuteRecord getActiveMuteRecord(@NotNull UUID playerUUID, @Nullable String inContext, boolean includeGlobalMutes);

    /**
     * Gets the first active {@link MuteRecord} for a given IP address, if one exists.
     * @param ipAddress the IP address to check for.
     * @param inContext the context to check for a mute in, or null for only global mutes.
     * @param includeGlobalMutes whether this method can return a global mute record.
     * @return a mute record, or null if none exist.
     */
    @Nullable MuteRecord getActiveMuteRecord(@NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalMutes);

    /**
     * Gets the first active {@link MuteRecord} for a given player UUID <b>OR</b> IP address, if one exists.
     * @param playerUUID the UUID of the player.
     * @param ipAddress the IP address to check for.
     * @param inContext the context to check for a mute in, or null for only global mutes.
     * @param includeGlobalMutes whether this method can return a global mute record.
     * @return a mute record, or null if none exist.
     */
    @Nullable MuteRecord getActiveMuteRecord(@NotNull UUID playerUUID, @NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalMutes);

}
