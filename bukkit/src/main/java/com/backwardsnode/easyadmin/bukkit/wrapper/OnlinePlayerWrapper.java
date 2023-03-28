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
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.internal.MessageKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OnlinePlayerWrapper extends OfflinePlayerWrapper implements OnlinePlayer {

    private final Player player;

    public OnlinePlayerWrapper(@NotNull Player player) {
        super(player);
        this.player = player;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessage(@NotNull String msg, boolean isColorCoded) {
        player.sendMessage(isColorCoded ? ChatColor.translateAlternateColorCodes('&', msg) : msg);
    }

    @Override
    public void sendKeyedMessage(@NotNull MessageKey key, @NotNull Object... args) {
        sendMessage(EasyAdminProvider.get().getMessageFactory().getMessage(key, getLocale(), args), true);
    }

    @Override
    public @Nullable String getLocale() {
        return player.getLocale();
    }

    @Override
    public @NotNull String getSerializedIPAddress() {
        return player.getAddress().getAddress().getHostAddress();
    }

    @Override
    public @NotNull String getServerName() {
        // TODO should this get name from proxy if available?
        return Bukkit.getServer().getName();
    }

    public static OnlinePlayerWrapper byUUID(@NotNull UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player == null ? null : new OnlinePlayerWrapper(player);
    }

    public static OnlinePlayerWrapper byUsername(@NotNull String username) {
        Player player = Bukkit.getPlayer(username);
        return player == null ? null : new OnlinePlayerWrapper(player);
    }
}
