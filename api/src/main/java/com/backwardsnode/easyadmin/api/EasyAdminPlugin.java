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
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.internal.FileSystemProvider;
import com.backwardsnode.easyadmin.api.internal.InternalServiceProviderType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Represents a plugin platform-agnostic EasyAdmin plugin.
 */
public interface EasyAdminPlugin {

    /**
     * Transforms the input text into a color/style formatted string.
     * @param altColorChar the character to use as the alternate color code character.
     * @param text the raw text.
     * @return the formatted text.
     */
    @NotNull String translateAlternateColorCodes(char altColorChar, @NotNull String text);

    /**
     * Gets the active API instance for EasyAdmin.
     * @return the API instance.
     */
    @NotNull EasyAdmin getInstance();

    /**
     * Gets the RecordBuilder for the specified provider.
     * @param provider the {@link InternalServiceProviderType} to get the builder for.
     * @return the requested {@link RecordBuilder}.
     */
    @ApiStatus.Internal
    @NotNull RecordBuilder getRecordBuilderFor(@NotNull InternalServiceProviderType provider);

    /**
     * Gets the {@link Platform} that this plugin is running on.
     * @return {@link Platform#BUKKIT} if running on Bukkit or {@link Platform#BUNGEE} if running on Bungeecord.
     */
    @NotNull Platform getPlatform();

    /**
     * Gets the file system provider for this plugin instance.
     * @return the {@link FileSystemProvider} for this plugin instance.
     */
    @ApiStatus.Internal
    @NotNull FileSystemProvider getFileSystemProvider();

    /**
     * Gets details about a player by their username.
     * @param username the username of the player.
     * @return {@link OfflinePlayer} if the player is recognized by the server, otherwise null.
     */
    @Nullable OfflinePlayer getOfflinePlayer(@NotNull String username);

    /**
     * Gets details about a player by their UUID.
     * @param uuid the UUID of the player.
     * @return {@link OfflinePlayer} if the player is recognized by the server, otherwise null.
     */
    @Nullable OfflinePlayer getOfflinePlayer(@NotNull UUID uuid);

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
