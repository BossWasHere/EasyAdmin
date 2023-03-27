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

package com.backwardsnode.easyadmin.bungee;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import com.backwardsnode.easyadmin.api.data.Platform;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.internal.InternalServiceProviderType;
import com.backwardsnode.easyadmin.bungee.command.BungeeCommandRegister;
import com.backwardsnode.easyadmin.bungee.event.BungeeListener;
import com.backwardsnode.easyadmin.bungee.interop.ExtraServerInfoManager;
import com.backwardsnode.easyadmin.bungee.messaging.BungeeMessagingChannel;
import com.backwardsnode.easyadmin.bungee.wrapper.OfflinePlayerWrapper;
import com.backwardsnode.easyadmin.bungee.wrapper.OnlinePlayerWrapper;
import com.backwardsnode.easyadmin.core.EasyAdminService;
import com.backwardsnode.easyadmin.core.boot.Registration;
import com.backwardsnode.easyadmin.core.command.CommandManager;
import com.backwardsnode.easyadmin.core.exception.ServiceInitializationException;
import com.backwardsnode.easyadmin.core.i18n.MessageProvider;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Level;

public class EasyAdminBungee extends Plugin implements EasyAdminPlugin {

    private EasyAdminService instance;
    private CommandManager commandManager;
    private BungeeListener listener;
    private BungeeMessagingChannel bungeeChannel;
    private ExtraServerInfoManager extraServerInfoManager;

    @Override
    public void onLoad() {
        getLogger().info("--- Running EasyAdmin " + getDescription().getVersion() + " ---");
    }

    @Override
    public void onEnable() {
        try {
            instance = new EasyAdminService(this);
        } catch (ServiceInitializationException e) {
            getLogger().log(Level.SEVERE, "Failed to initialize EasyAdmin", e);
            return;
        }

        getLogger().info("EasyAdminService started");
        bungeeChannel = new BungeeMessagingChannel(this);
        commandManager = new CommandManager(this, new BungeeCommandRegister(this));
        extraServerInfoManager = new ExtraServerInfoManager();
        listener = new BungeeListener(this);

        Registration.get().register(instance);

        getProxy().registerChannel(EasyAdmin.CHANNEL);

        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerListener(this, listener);
        pluginManager.registerListener(this, bungeeChannel);

        commandManager.registerAllCommands();

        getLogger().info("Finished loading EasyAdmin Bungee");

        instance.getAdminManager().getOrInitPlayerRecord(UUID.randomUUID(), "Bob");
    }

    @Override
    public void onDisable() {
        if (instance == null) {
            return;
        }
        getLogger().info("Shutting down EasyAdmin Bungee");

        commandManager.unregisterAllCommands();

        getProxy().getPluginManager().unregisterListeners(this);

        getProxy().unregisterChannel(EasyAdmin.CHANNEL);

        getLogger().info("Stopping services...");
        instance.close();
    }

    @Override
    public @NotNull String translateAlternateColorCodes(char altColorChar, @NotNull String text) {
        return ChatColor.translateAlternateColorCodes(altColorChar, text);
    }

    @Override
    public @NotNull EasyAdmin getInstance() {
        return instance;
    }

    @Override
    public @NotNull Platform getPlatform() {
        return Platform.BUNGEE;
    }

    @Override
    public @NotNull Path getDataDirectory() {
        return getDataFolder().toPath();
    }

    @Override
    public @NotNull RecordBuilder getRecordBuilderFor(@NotNull InternalServiceProviderType provider) {
        return null;
    }

    public ExtraServerInfoManager getExtraServerInfoManager() {
        return extraServerInfoManager;
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
    public @Nullable OnlinePlayer getOnlinePlayer(@NotNull UUID playerUUID) {
        return OnlinePlayerWrapper.byUUID(playerUUID);
    }
}
