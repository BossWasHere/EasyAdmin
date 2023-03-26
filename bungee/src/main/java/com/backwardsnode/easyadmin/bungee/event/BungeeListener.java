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

package com.backwardsnode.easyadmin.bungee.event;

import com.backwardsnode.easyadmin.api.admin.ChatFilter;
import com.backwardsnode.easyadmin.api.data.ChatFilterResult;
import com.backwardsnode.easyadmin.api.data.RecommendationAction;
import com.backwardsnode.easyadmin.api.data.RecommendationReason;
import com.backwardsnode.easyadmin.api.record.BanRecord;
import com.backwardsnode.easyadmin.api.record.MuteRecord;
import com.backwardsnode.easyadmin.api.record.PlayerRecord;
import com.backwardsnode.easyadmin.api.record.modify.PlayerRecordModifier;
import com.backwardsnode.easyadmin.bungee.EasyAdminBungee;
import com.backwardsnode.easyadmin.bungee.wrapper.OfflinePlayerWrapper;
import com.backwardsnode.easyadmin.bungee.wrapper.OnlinePlayerWrapper;
import com.backwardsnode.easyadmin.core.BukkitPluginMode;
import com.backwardsnode.easyadmin.core.i18n.CommonMessages;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.time.LocalDateTime;
import java.util.Optional;

public class BungeeListener implements Listener {

    private final EasyAdminBungee plugin;

    public BungeeListener(EasyAdminBungee plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChatEvent(ChatEvent e) {
        if (!plugin.getInstance().getConfigurationManager().getFeatureConfiguration().isMutePlatformEnabled()) {
            return;
        }

        if (e.getSender() instanceof ProxiedPlayer pp) {
            if (plugin.getExtraServerInfoManager().getServerMode(pp.getServer().getInfo()) == BukkitPluginMode.PROXY_AWARE) {
                return;
            }
            OnlinePlayerWrapper player = new OnlinePlayerWrapper(pp);
            String message = e.getMessage();
            String context = player.getServerName();
            Optional<MuteRecord> muteRecord = plugin.getInstance().getAdminManager().getActiveMuteRecord(player, context, true);

            if (muteRecord.isPresent()) {
                e.setCancelled(true);
                player.sendMessage(CommonMessages.PLAYER.MUTE.MUTE_TITLE);

                plugin.getInstance().getAdminDynamo().recordAttemptedChatMessage(player, message, context);
            } else {
                ChatFilter chatFilter = plugin.getInstance().getChatFilter();
                ChatFilterResult filterResult = chatFilter.canSendChatMessage(player, message);
                switch (filterResult) {
                    case ALLOW -> {}
                    case DENY -> e.setCancelled(true);
                    case REQUIRE_CENSOR -> e.setMessage(chatFilter.censorMessage(player, message));
                    default -> {
                        RecommendationAction action = switch (filterResult) {
                            case OFFENSE_SHOULD_BAN -> RecommendationAction.BAN_PLAYER;
                            case OFFENSE_SHOULD_MUTE -> RecommendationAction.MUTE_PLAYER;
                            default -> RecommendationAction.WARN_PLAYER;
                        };
                        e.setCancelled(true);
                        plugin.getInstance().getAdminDynamo().recordBlockedChatMessage(player, message, context);
                        plugin.getInstance().getRecommendationEngine().recommendAction(player, action, RecommendationReason.CHAT_REPORT, message);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerDisconnectEvent(PlayerDisconnectEvent e) {
        Optional<PlayerRecord> record = plugin.getInstance().getAdminManager().getPlayerRecord(e.getPlayer().getUniqueId());
        if (record.isEmpty()) {
            plugin.getLogger().severe("Player record not found for player " + e.getPlayer().getName() + "[ + " + e.getPlayer().getUniqueId() + "]");
            return;
        }

        PlayerRecordModifier modifier = record.get().getModifiableRecord();
        modifier.setLastServer(e.getPlayer().getServer().getInfo().getName());
        modifier.setLastLeft(LocalDateTime.now());
        //TODO set playtime (need to cache this somehow)
        modifier.push();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onLoginEvent(LoginEvent e) {
        if (!plugin.getInstance().getConfigurationManager().getFeatureConfiguration().isBanPlatformEnabled()) {
            return;
        }

        PendingConnection conn = e.getConnection();
        OfflinePlayerWrapper player = new OfflinePlayerWrapper(conn.getUniqueId(), conn.getName());
        // TODO: unix socket support P2??
        Optional<BanRecord> record = plugin.getInstance().getAdminManager().getActiveBanRecord(player, conn.getAddress().getAddress().getHostAddress(), null, true);

        if (record.isPresent()) {
            // TODO fallback server if not global ban
            e.setCancelled(true);
            // TODO ban reason message styleize
            e.setCancelReason(TextComponent.fromLegacyText(plugin.getInstance().getMessageFactory().getMessageDefault(CommonMessages.PLAYER.BAN.BAN_TITLE)));

            plugin.getInstance().getAdminDynamo().recordAttemptedJoin(player, null);
        }
    }

    /*
     * Update player join stats
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPostLoginEvent(PostLoginEvent e) {
        OnlinePlayerWrapper player = new OnlinePlayerWrapper(e.getPlayer());
        PlayerRecordModifier modifier = plugin.getInstance().getAdminManager().getOrInitPlayerRecord(player).getModifiableRecord();
        modifier.setLastJoined(LocalDateTime.now());
        modifier.setLastAddress(player.getSerializedIPAddress());
        modifier.incrementTotalJoins();
        modifier.push();
    }

    /*
     * Check if player is allowed to join the server
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onServerConnectEvent(ServerConnectEvent e) {
        if (!plugin.getInstance().getConfigurationManager().getFeatureConfiguration().isBanPlatformEnabled()) {
            return;
        }

        OnlinePlayerWrapper player = new OnlinePlayerWrapper(e.getPlayer());
        Optional<BanRecord> record = plugin.getInstance().getAdminManager().getActiveBanRecord(player, player.getSerializedIPAddress(), e.getTarget().getName(), false);

        if (record.isPresent()) {
            // TODO fallback server
            e.setCancelled(true);
            // TODO ban reason message styleize
            e.getPlayer().disconnect(TextComponent.fromLegacyText(plugin.getInstance().getMessageFactory().getMessageDefault(CommonMessages.PLAYER.BAN.BAN_TITLE)));

            plugin.getInstance().getAdminDynamo().recordAttemptedJoin(player, e.getTarget().getName());
        }
    }

    /*
     * update last(current)Server for player
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onServerConnectedEvent(ServerConnectedEvent e) {
        PlayerRecordModifier modifier = plugin.getInstance().getAdminManager().getPlayerRecord(e.getPlayer().getUniqueId()).orElseThrow().getModifiableRecord();
        modifier.setLastServer(e.getServer().getInfo().getName());
        modifier.push();
    }

}
