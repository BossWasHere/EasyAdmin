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

package com.backwardsnode.easyadmin.bungee.wrapper;

import com.backwardsnode.easyadmin.api.EasyAdminProvider;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class OfflinePlayerWrapper implements OfflinePlayer {

    private final UUID playerUUID;
    private final String username;

    public OfflinePlayerWrapper(@NotNull UUID playerUUID, @NotNull String username) {
        this.playerUUID = playerUUID;
        this.username = username;
    }

    @Override
    public @NotNull UUID getUUID() {
        return playerUUID;
    }

    @Override
    public @NotNull String getUsername() {
        return username;
    }

    @Override
    public @NotNull Optional<OnlinePlayer> getOnlinePlayer() {
        if (this instanceof OnlinePlayer) {
            return Optional.of((OnlinePlayer) this);
        }
        return Optional.ofNullable(OnlinePlayerWrapper.byUUID(playerUUID));
    }

    public static OfflinePlayerWrapper byUUID(@NotNull UUID uuid) {
        OnlinePlayerWrapper onlinePlayer = OnlinePlayerWrapper.byUUID(uuid);
        if (onlinePlayer != null) {
            return onlinePlayer;
        }
        String username = EasyAdminProvider.get().getExternalDataSource().getUsernameForUUID(uuid);
        return username == null ? null : new OfflinePlayerWrapper(uuid, username);
    }

    public static OfflinePlayerWrapper byUsername(@NotNull String username) {
        OnlinePlayerWrapper onlinePlayer = OnlinePlayerWrapper.byUsername(username);
        if (onlinePlayer != null) {
            return onlinePlayer;
        }
        UUID uuid = EasyAdminProvider.get().getExternalDataSource().getUUIDForUsername(username);
        return uuid == null ? null : new OfflinePlayerWrapper(uuid, username);
    }
}
