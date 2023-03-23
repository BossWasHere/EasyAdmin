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

import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class OnlinePlayerWrapper extends OfflinePlayerWrapper implements OnlinePlayer {

    private final ProxiedPlayer player;

    public OnlinePlayerWrapper(@NotNull ProxiedPlayer player) {
        super(player.getUniqueId(), player.getName());
        this.player = player;
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessage(@NotNull String msg) {
        player.sendMessage(TextComponent.fromLegacyText(msg));
    }

    @Override
    public @Nullable String getLocale() {
        return player.getLocale().toString();
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull String getSerializedIPAddress() {
        // TODO: unix socket support??
        return player.getAddress().getAddress().getHostAddress();
    }

    @Override
    public @NotNull String getServerName() {
        return player.getServer().getInfo().getName();
    }

    public static OnlinePlayerWrapper byUUID(@NotNull UUID uuid) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        return player == null ? null : new OnlinePlayerWrapper(player);
    }

    public static OnlinePlayerWrapper byUsername(@NotNull String username) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(username);
        return player == null ? null : new OnlinePlayerWrapper(player);
    }
}
