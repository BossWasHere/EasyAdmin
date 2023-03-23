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

package com.backwardsnode.easyadmin.bungee.messaging;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.bungee.EasyAdminBungee;
import com.backwardsnode.easyadmin.core.messaging.PluginMessage;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class BungeeMessagingChannel implements Listener {

    private final EasyAdminBungee plugin;

    public BungeeMessagingChannel(EasyAdminBungee plugin) {
        this.plugin = plugin;
    }

    public void broadcastPluginMessage(PluginMessage message) {
        for (ServerInfo server : plugin.getProxy().getServers().values()) {
            server.sendData(EasyAdmin.CHANNEL, message.create(), false);
        }
    }

    public void sendPluginMessage(ServerInfo target, PluginMessage message) {
        target.sendData(EasyAdmin.CHANNEL, message.create(), false);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPluginMessageEvent(PluginMessageEvent e) {
        if (!e.getTag().equals(EasyAdmin.CHANNEL)) {
            return;
        }

        e.setCancelled(true);

        //TODO handle incoming data
    }
}
