/*
 * MIT License
 *
 * Copyright (c) 2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

package com.backwardsnode.easyadmin.velocity;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import com.backwardsnode.easyadmin.api.data.Platform;
import com.backwardsnode.easyadmin.api.data.ServiceSource;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.core.EasyAdminService;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.UUID;

@Plugin(id = "easyadmin", name = "EasyAdmin", version = BuildInfo.VERSION,
        description = "Velocity adapter for network administration plugin", authors = { "BackwardsNode" })
public class EasyAdminVelocity implements EasyAdminPlugin {

    private final ProxyServer server;
    private final Logger logger;
    private final Path dataDirectory;

    private EasyAdminService service;

    @Inject
    public EasyAdminVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.server = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            service = new EasyAdminService(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public @NotNull String translateAlternateColorCodes(char altColorChar, @NotNull String text) {
        return null;
    }

    @Override
    public @NotNull EasyAdmin getInstance() {
        return service;
    }

    @Override
    public @NotNull RecordBuilder getRecordBuilderFor(@NotNull ServiceSource provider) {
        return null;
    }

    @Override
    public @NotNull Platform getPlatform() {
        return Platform.VELOCITY;
    }

    @Override
    public @NotNull Path getDataDirectory() {
        return dataDirectory;
    }

    @Override
    public @Nullable OfflinePlayer getOfflinePlayer(@NotNull String username) {
        return null;
    }

    @Override
    public @NotNull OfflinePlayer getOfflinePlayer(@NotNull UUID uuid) {
        return null;
    }

    @Override
    public @Nullable OnlinePlayer getOnlinePlayer(@NotNull String username) {
        return null;
    }

    @Override
    public @Nullable OnlinePlayer getOnlinePlayer(@NotNull UUID uuid) {
        return null;
    }
}
