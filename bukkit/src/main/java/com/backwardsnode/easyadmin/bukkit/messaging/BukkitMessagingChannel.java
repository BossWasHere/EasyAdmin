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

package com.backwardsnode.easyadmin.bukkit.messaging;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.bukkit.EasyAdminBukkit;
import com.backwardsnode.easyadmin.core.messaging.PluginMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BukkitMessagingChannel implements PluginMessageListener {

    private final EasyAdminBukkit plugin;
    private final MessageDequeueTask dequeueTask;
    private final ConcurrentLinkedQueue<byte[]> messageQueue;

    public BukkitMessagingChannel(EasyAdminBukkit plugin) {
        this.plugin = plugin;
        dequeueTask = new MessageDequeueTask();
        messageQueue = new ConcurrentLinkedQueue<>();
    }

    public boolean sendPluginMessage(PluginMessage message, boolean mustSend) {
        Iterator<? extends Player> players = plugin.getServer().getOnlinePlayers().iterator();
        if (players.hasNext()) {
            Player player = players.next();
            player.sendPluginMessage(plugin, EasyAdmin.CHANNEL, message.create());
        } else if (mustSend) {
            messageQueue.add(message.create());
            dequeueTask.schedule();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte[] message) {
        if (!channel.equals(EasyAdmin.CHANNEL)) {
            return;
        }


    }

    private final class MessageDequeueTask extends BukkitRunnable {

        private boolean isActive;

        @Override
        public void run() {
            Iterator<? extends Player> players = plugin.getServer().getOnlinePlayers().iterator();
            if (players.hasNext()) {
                Player player = players.next();
                while (!messageQueue.isEmpty() && player.isOnline()) {
                    player.sendPluginMessage(plugin, EasyAdmin.CHANNEL, messageQueue.remove());
                }
            }
            if (messageQueue.isEmpty()) {
                cancel();
                isActive = false;
            }
        }

        public void schedule() {
            if (!isActive) {
                isActive = true;
                runTaskTimer(plugin, 1, 200);
            }
        }
    }
}
