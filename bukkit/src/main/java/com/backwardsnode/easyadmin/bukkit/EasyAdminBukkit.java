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
import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import com.backwardsnode.easyadmin.api.data.Platform;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.data.ServiceSource;
import com.backwardsnode.easyadmin.bukkit.command.BukkitCommandRegister;
import com.backwardsnode.easyadmin.bukkit.event.BukkitListener;
import com.backwardsnode.easyadmin.bukkit.messaging.BukkitMessagingChannel;
import com.backwardsnode.easyadmin.bukkit.wrapper.OfflinePlayerWrapper;
import com.backwardsnode.easyadmin.bukkit.wrapper.OnlinePlayerWrapper;
import com.backwardsnode.easyadmin.core.BukkitPluginMode;
import com.backwardsnode.easyadmin.core.EasyAdminService;
import com.backwardsnode.easyadmin.core.boot.Registration;
import com.backwardsnode.easyadmin.core.command.CommandManager;
import com.backwardsnode.easyadmin.core.exception.ServiceInitializationException;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.UUID;

public class EasyAdminBukkit extends JavaPlugin implements EasyAdminPlugin {

    private EasyAdminService instance;
    private CommandManager commandManager;
    private BukkitListener listener;
    private BukkitMessagingChannel bukkitChannel;

    private BukkitPluginMode mode;

    @Override
    public void onLoad() {
        try {
            instance = new EasyAdminService(this);
        } catch (ServiceInitializationException e) {
            throw new RuntimeException(e);
        }
        bukkitChannel = new BukkitMessagingChannel(this);
        commandManager = new CommandManager(this, new BukkitCommandRegister(this));
        listener = new BukkitListener(this);
    }

    @Override
    public void onEnable() {
        Registration.get().register(instance);

        getServer().getPluginManager().registerEvents(listener, this);

        Messenger messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, EasyAdmin.CHANNEL);
        messenger.registerIncomingPluginChannel(this, EasyAdmin.CHANNEL, bukkitChannel);

        commandManager.registerAllCommands();
    }

    @Override
    public void onDisable() {
        commandManager.unregisterAllCommands();

        Messenger messenger = getServer().getMessenger();
        messenger.unregisterOutgoingPluginChannel(this);
        messenger.unregisterIncomingPluginChannel(this);

        instance.close();
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
    public @NotNull Path getDataDirectory() {
        return getDataFolder().toPath();
    }

    @Override
    public @NotNull RecordBuilder getRecordBuilderFor(@NotNull ServiceSource provider) {
        return null;
    }

    public BukkitPluginMode getPluginMode() {
        return mode;
    }

    @Override
    public @Nullable OfflinePlayer getOfflinePlayer(@NotNull String username) {
        return OfflinePlayerWrapper.byUsername(username);
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull UUID uuid) {
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
