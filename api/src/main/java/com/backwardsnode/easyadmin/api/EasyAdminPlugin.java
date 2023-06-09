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

package com.backwardsnode.easyadmin.api;

import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import com.backwardsnode.easyadmin.api.data.Platform;
import com.backwardsnode.easyadmin.api.data.ServiceSource;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.UUID;

/**
 * Represents a plugin platform-agnostic EasyAdmin plugin.
 */
public interface EasyAdminPlugin {

    /**
     * Gets the active API instance for EasyAdmin.
     * @return the API instance.
     */
    @NotNull EasyAdmin getInstance();

    /**
     * Gets the RecordBuilder for the specified provider.
     * @param provider the {@link ServiceSource} to get the builder for.
     * @return the requested {@link RecordBuilder}.
     */
    @ApiStatus.Internal
    @NotNull RecordBuilder getRecordBuilderFor(@NotNull ServiceSource provider);

    /**
     * Gets the {@link Platform} that this plugin is running on.
     * @return {@link Platform#BUKKIT} if running on Bukkit or {@link Platform#BUNGEE} if running on Bungeecord.
     */
    @NotNull Platform getPlatform();

    /**
     * Gets the data folder for this plugin.
     * @return the {@link Path} to this plugin's data folder.
     */
    @ApiStatus.Internal
    @NotNull Path getDataDirectory();

    /**
     * Gets details about a player by their username.
     * @param username the username of the player.
     * @return {@link OfflinePlayer} if the player's username can be resolved, otherwise null.
     */
    @Nullable OfflinePlayer getOfflinePlayer(@NotNull String username);

    /**
     * Gets details about a player by their UUID.
     * @param uuid the UUID of the player.
     * @return {@link OfflinePlayer} for the specified UUID.
     */
    @NotNull OfflinePlayer getOfflinePlayer(@NotNull UUID uuid);

    /**
     * Gets details about an online player by their username.
     * @param username the username of the player.
     * @return {@link OnlinePlayer} if the player is online, otherwise null.
     */
    @Nullable OnlinePlayer getOnlinePlayer(@NotNull String username);

    /**
     * Gets details about an online player by their UUID.
     * @param uuid the UUID of the player.
     * @return {@link OnlinePlayer} if the player is online, otherwise null.
     */
    @Nullable OnlinePlayer getOnlinePlayer(@NotNull UUID uuid);
}
