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

package com.backwardsnode.easyadmin.bukkit.wrapper;

import com.backwardsnode.easyadmin.api.EasyAdminProvider;
import com.backwardsnode.easyadmin.api.data.AdminAction;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class OfflinePlayerWrapper implements OfflinePlayer {

    private final org.bukkit.OfflinePlayer player;

    public OfflinePlayerWrapper(@NotNull org.bukkit.OfflinePlayer player) {
        this.player = player;
    }

    @Override
    public @NotNull UUID getUUID() {
        return player.getUniqueId();
    }

    @Override
    public @NotNull String getUsername() throws IllegalStateException {
        String username = player.getName();
        if (username == null) {
            username = EasyAdminProvider.get().getExternalDataSource().getUsernameForUUID(getUUID());
            if (username == null) {
                throw new IllegalStateException("Could not resolve username for player " + getUUID());
            }
        }

        return username;
    }

    @Override
    public @NotNull Optional<OnlinePlayer> getOnlinePlayer() {
        if (this instanceof OnlinePlayer) {
            return Optional.of((OnlinePlayer) this);
        }

        Player bukkitPlayer = player.getPlayer();
        return bukkitPlayer == null ? Optional.empty() : Optional.of(new OnlinePlayerWrapper(bukkitPlayer));
    }

    public static OfflinePlayerWrapper byUUID(@NotNull UUID uuid) {
        return new OfflinePlayerWrapper(Bukkit.getOfflinePlayer(uuid));
    }

    @SuppressWarnings("deprecation")
    public static OfflinePlayerWrapper byUsername(@NotNull String username) {
        return new OfflinePlayerWrapper(Bukkit.getOfflinePlayer(username));
    }
}
