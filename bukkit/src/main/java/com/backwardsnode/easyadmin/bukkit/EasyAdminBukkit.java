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

package com.backwardsnode.easyadmin.bukkit;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.data.Platform;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.bukkit.command.BukkitCommandRegister;
import com.backwardsnode.easyadmin.bukkit.wrapper.OnlinePlayerWrapper;
import com.backwardsnode.easyadmin.bukkit.wrapper.OfflinePlayerWrapper;
import com.backwardsnode.easyadmin.core.command.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class EasyAdminBukkit extends JavaPlugin implements EasyAdminPlugin {

    private final EasyAdmin instance;
    private final CommandManager commandManager;

    public EasyAdminBukkit() {
        //TODO implementation
        instance = null;
        commandManager = new CommandManager(this, new BukkitCommandRegister(this));
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        commandManager.registerAllCommands();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public @NotNull EasyAdmin getInstance() {
        return instance;
    }

    @Override
    public @NotNull Platform getPlatform() {
        return Platform.BUKKIT;
    }

    @Override
    public @Nullable OfflinePlayer getOfflinePlayer(String username) {
        return OfflinePlayerWrapper.byUsername(username);
    }

    @Override
    public @Nullable OfflinePlayer getOfflinePlayer(UUID uuid) {
        return OfflinePlayerWrapper.byUUID(uuid);
    }

    @Override
    public @Nullable OnlinePlayer getOnlinePlayer(@NotNull String username) {
        return OnlinePlayerWrapper.byUsername(username);
    }

    @Override
    public @Nullable OnlinePlayer getOnlinePlayer(@NotNull UUID uuid) {
        return OnlinePlayerWrapper.byUUID(uuid);
    }

}
