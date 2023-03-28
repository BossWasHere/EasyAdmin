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

package com.backwardsnode.easyadmin.core.component;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.admin.AdminManager;
import com.backwardsnode.easyadmin.api.commit.CommitException;
import com.backwardsnode.easyadmin.api.contextual.ContextTester;
import com.backwardsnode.easyadmin.api.contextual.Contextual;
import com.backwardsnode.easyadmin.api.data.LookupOptions;
import com.backwardsnode.easyadmin.api.data.PunishmentStatus;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.record.*;
import com.backwardsnode.easyadmin.api.record.mutable.MutableBanRecord;
import com.backwardsnode.easyadmin.api.record.mutable.MutableMuteRecord;
import com.backwardsnode.easyadmin.core.EasyAdminService;
import com.backwardsnode.easyadmin.core.cache.CacheGroupType;
import com.backwardsnode.easyadmin.core.cache.CacheLoader;
import com.backwardsnode.easyadmin.core.cache.RecordCache;
import com.backwardsnode.easyadmin.core.database.DatabaseController;
import com.backwardsnode.easyadmin.core.record.MutableRecordProvider;
import com.backwardsnode.easyadmin.core.record.PlayerRecordImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class AdminManagerImpl implements AdminManager {

    private final EasyAdminService service;
    private final DatabaseController databaseController;

    public AdminManagerImpl(EasyAdminService service) {
        this.service = service;
        this.databaseController = service.getDatabaseController();
    }

    @Override
    public @NotNull Optional<BanRecord> getActiveBanRecord(@NotNull UUID playerUUID, @Nullable String inContext, boolean includeGlobalBans) {
        Collection<BanRecord> records = service.getRecordCache().request(CacheGroupType.ACTIVE_BAN_BY_UUID, playerUUID);
        return firstMatchContext(records, inContext, includeGlobalBans);
    }

    @Override
    public @NotNull Optional<BanRecord> getActiveBanRecord(@NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalBans) {
        Collection<BanRecord> records = service.getRecordCache().request(CacheGroupType.ACTIVE_BAN_BY_ADDR, ipAddress);
        return firstMatchContext(records, inContext, includeGlobalBans);
    }

    @Override
    public @NotNull Optional<BanRecord> getActiveBanRecord(@NotNull UUID playerUUID, @NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalBans) {
        Optional<BanRecord> record = getActiveBanRecord(playerUUID, inContext, includeGlobalBans);
        if (record.isPresent()) {
            return record;
        }
        return getActiveBanRecord(ipAddress, inContext, includeGlobalBans);
    }

    @Override
    public @NotNull Optional<BanRecord> getActiveBanRecord(@NotNull OfflinePlayer player, @NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalBans) {
        return getActiveBanRecord(player.getUUID(), ipAddress, inContext, includeGlobalBans);
    }

    @Override
    public @NotNull Optional<MuteRecord> getActiveMuteRecord(@NotNull UUID playerUUID, @Nullable String inContext, boolean includeGlobalMutes) {
        Collection<MuteRecord> records = service.getRecordCache().request(CacheGroupType.ACTIVE_MUTE_BY_UUID, playerUUID);
        return firstMatchContext(records, inContext, includeGlobalMutes);
    }

    @Override
    public @NotNull Optional<MuteRecord> getActiveMuteRecord(@NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalMutes) {
        Collection<MuteRecord> records = service.getRecordCache().request(CacheGroupType.ACTIVE_MUTE_BY_ADDR, ipAddress);
        return firstMatchContext(records, inContext, includeGlobalMutes);
    }

    @Override
    public @NotNull Optional<MuteRecord> getActiveMuteRecord(@NotNull UUID playerUUID, @NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalMutes) {
        Optional<MuteRecord> record = getActiveMuteRecord(playerUUID, inContext, includeGlobalMutes);
        if (record.isPresent()) {
            return record;
        }
        return getActiveMuteRecord(ipAddress, inContext, includeGlobalMutes);
    }

    @Override
    public @NotNull Optional<MuteRecord> getActiveMuteRecord(@NotNull OnlinePlayer player, @Nullable String inContext, boolean includeGlobalMutes) {
        return getActiveMuteRecord(player.getUUID(), player.getSerializedIPAddress(), inContext, includeGlobalMutes);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecords(@NotNull UUID playerUUID) {
        return service.getRecordCache().request(CacheGroupType.BAN_BY_UUID, playerUUID);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecords(@NotNull UUID playerUUID, @Nullable LookupOptions options) {
        if (options == null) {
            return getBanRecords(playerUUID);
        }
        // non-cached
        return databaseController.getPlayerBans(playerUUID, options);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecords(@NotNull String ipAddress) {
        return service.getRecordCache().request(CacheGroupType.BAN_BY_ADDR, ipAddress);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecords(@NotNull String ipAddress, @Nullable LookupOptions options) {
        if (options == null) {
            return getBanRecords(ipAddress);
        }
        // non-cached
        return databaseController.getIPBans(ipAddress, options);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecords(@NotNull UUID playerUUID, @NotNull String ipAddress) {
        return getBanRecords(playerUUID, ipAddress, null);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecords(@NotNull UUID playerUUID, @NotNull String ipAddress, @Nullable LookupOptions options) {
        // TODO maybe cache both uuid+ip groups?
        // non-cached
        return databaseController.getPlayerBansOrIPBans(playerUUID, ipAddress, options);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecords(@NotNull OnlinePlayer player) {
        return getBanRecords(player.getUUID(), player.getSerializedIPAddress());
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecords(@NotNull OnlinePlayer player, @Nullable LookupOptions options) {
        return getBanRecords(player.getUUID(), player.getSerializedIPAddress(), options);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull UUID playerUUID, @NotNull PunishmentStatus status) {
        return getBanRecordsByStatus(playerUUID, status, null);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull UUID playerUUID, @NotNull PunishmentStatus status, @Nullable LookupOptions options) {
        // non-cached
        return databaseController.getPlayerBansByStatus(playerUUID, status, options);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull String ipAddress, @NotNull PunishmentStatus status) {
        // non-cached
        return getBanRecordsByStatus(ipAddress, status, null);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull String ipAddress, @NotNull PunishmentStatus status, @Nullable LookupOptions options) {
        // non-cached
        return databaseController.getIPBansByStatus(ipAddress, status, options);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull UUID playerUUID, @NotNull String ipAddress, @NotNull PunishmentStatus status) {
        return getBanRecordsByStatus(playerUUID, ipAddress, status, null);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull UUID playerUUID, @NotNull String ipAddress, @NotNull PunishmentStatus status, @Nullable LookupOptions options) {
        // non-cached
        return databaseController.getPlayerBansOrIPBansByStatus(playerUUID, ipAddress, status, options);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull OnlinePlayer player, @NotNull PunishmentStatus status) {
        return getBanRecordsByStatus(player.getUUID(), player.getSerializedIPAddress(), status);
    }

    @Override
    public @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull OnlinePlayer player, @NotNull PunishmentStatus status, @Nullable LookupOptions options) {
        return getBanRecordsByStatus(player.getUUID(), player.getSerializedIPAddress(), status, options);
    }

    @Override
    public @NotNull Collection<CommentRecord> getCommentRecords(@NotNull UUID playerUUID) {
        return service.getRecordCache().request(CacheGroupType.COMMENT, playerUUID);
    }

    @Override
    public @NotNull Collection<CommentRecord> getCommentRecords(@NotNull UUID playerUUID, @Nullable LookupOptions options) {
        if (options == null) {
            return getCommentRecords(playerUUID);
        }
        // non-cached
        return databaseController.getPlayerComments(playerUUID, options);
    }

    @Override
    public @NotNull Collection<CommentRecord> getCommentRecords(@NotNull OnlinePlayer player) {
        return getCommentRecords(player.getUUID());
    }

    @Override
    public @NotNull Collection<CommentRecord> getCommentRecords(@NotNull OnlinePlayer player, @Nullable LookupOptions options) {
        if (options == null) {
            return getCommentRecords(player);
        }
        // non-cached
        return databaseController.getPlayerComments(player.getUUID(), options);
    }

    @Override
    public @NotNull Collection<CommentRecord> getCommentRecordsByType(@NotNull UUID playerUUID, boolean warning) {
        return service.getRecordCache().request(CacheGroupType.COMMENT, playerUUID).stream()
                .filter(record -> record.isWarning() == warning).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @NotNull Collection<CommentRecord> getCommentRecordsByType(@NotNull UUID playerUUID, boolean warning, @Nullable LookupOptions options) {
        if (options == null) {
            return getCommentRecordsByType(playerUUID, warning);
        }
        // non-cached
        return databaseController.getPlayerCommentsByType(playerUUID, warning, options);
    }

    @Override
    public @NotNull Collection<CommentRecord> getCommentRecordsByType(@NotNull OnlinePlayer player, boolean warning) {
        return getCommentRecordsByType(player.getUUID(), warning);
    }

    @Override
    public @NotNull Collection<CommentRecord> getCommentRecordsByType(@NotNull OnlinePlayer player, boolean warning, @Nullable LookupOptions options) {
        return getCommentRecordsByType(player.getUUID(), warning, options);
    }

    @Override
    public @NotNull Collection<KickRecord> getKickRecords(@NotNull UUID playerUUID) {
        return service.getRecordCache().request(CacheGroupType.KICK, playerUUID);
    }

    @Override
    public @NotNull Collection<KickRecord> getKickRecords(@NotNull UUID playerUUID, @Nullable LookupOptions options) {
        if (options == null) {
            return getKickRecords(playerUUID);
        }
        // non-cached
        return databaseController.getPlayerKicks(playerUUID, options);
    }

    @Override
    public @NotNull Collection<KickRecord> getKickRecords(@NotNull OnlinePlayer player) {
        return getKickRecords(player.getUUID());
    }

    @Override
    public @NotNull Collection<KickRecord> getKickRecords(@NotNull OnlinePlayer player, @Nullable LookupOptions options) {
        return getKickRecords(player.getUUID(), options);
    }

    @Override
    public @NotNull Collection<KickRecord> getKickRecordsByType(@NotNull UUID playerUUID, boolean global) {
        return getKickRecords(playerUUID).stream().filter(x -> x.isGlobal() == global).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @NotNull Collection<KickRecord> getKickRecordsByType(@NotNull UUID playerUUID, boolean global, @Nullable LookupOptions options) {
        if (options == null) {
            return getKickRecordsByType(playerUUID, global);
        }
        // non-cached
        return databaseController.getPlayerKicks(playerUUID, options).stream().filter(x -> x.isGlobal() == global).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public @NotNull Collection<KickRecord> getKickRecordsByType(@NotNull OnlinePlayer player, boolean global) {
        return getKickRecordsByType(player.getUUID(), global);
    }

    @Override
    public @NotNull Collection<KickRecord> getKickRecordsByType(@NotNull OnlinePlayer player, boolean global, @Nullable LookupOptions options) {
        return getKickRecordsByType(player.getUUID(), global, options);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecords(@NotNull UUID playerUUID) {
        return service.getRecordCache().request(CacheGroupType.MUTE_BY_UUID, playerUUID);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecords(@NotNull UUID playerUUID, @Nullable LookupOptions options) {
        if (options == null) {
            return getMuteRecords(playerUUID);
        }
        // non-cached
        return databaseController.getPlayerMutes(playerUUID, options);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecords(@NotNull String ipAddress) {
        return service.getRecordCache().request(CacheGroupType.MUTE_BY_ADDR, ipAddress);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecords(@NotNull String ipAddress, @Nullable LookupOptions options) {
        if (options == null) {
            return getMuteRecords(ipAddress);
        }
        // non-cached
        return databaseController.getIPMutes(ipAddress, options);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecords(@NotNull UUID playerUUID, @NotNull String ipAddress) {
        return getMuteRecords(playerUUID, ipAddress, null);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecords(@NotNull UUID playerUUID, @NotNull String ipAddress, @Nullable LookupOptions options) {
        // TODO maybe cache both uuid+ip groups?
        // non-cached
        return databaseController.getPlayerMutesOrIPMutes(playerUUID, ipAddress, options);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecords(@NotNull OnlinePlayer player) {
        return getMuteRecords(player.getUUID(), player.getSerializedIPAddress());
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecords(@NotNull OnlinePlayer player, @Nullable LookupOptions options) {
        return getMuteRecords(player.getUUID(), player.getSerializedIPAddress(), options);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull UUID playerUUID, @NotNull PunishmentStatus status) {
        return getMuteRecordsByStatus(playerUUID, status, null);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull UUID playerUUID, @NotNull PunishmentStatus status, @Nullable LookupOptions options) {
        return databaseController.getPlayerMutesByStatus(playerUUID, status, options);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull String ipAddress, @NotNull PunishmentStatus status) {
        return getMuteRecordsByStatus(ipAddress, status, null);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull String ipAddress, @NotNull PunishmentStatus status, @Nullable LookupOptions options) {
        return databaseController.getIPMutesByStatus(ipAddress, status, options);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull UUID playerUUID, @NotNull String ipAddress, @NotNull PunishmentStatus status) {
        return getMuteRecordsByStatus(playerUUID, ipAddress, status, null);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull UUID playerUUID, @NotNull String ipAddress, @NotNull PunishmentStatus status, @Nullable LookupOptions options) {
        return databaseController.getPlayerMutesOrIPMutesByStatus(playerUUID, ipAddress, status, options);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull OnlinePlayer player, @NotNull PunishmentStatus status) {
        return getMuteRecordsByStatus(player.getUUID(), player.getSerializedIPAddress(), status);
    }

    @Override
    public @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull OnlinePlayer player, @NotNull PunishmentStatus status, @Nullable LookupOptions options) {
        return getMuteRecordsByStatus(player.getUUID(), player.getSerializedIPAddress(), status, options);
    }

    @Override
    public @NotNull Optional<PlayerRecord> getPlayerRecord(@NotNull UUID playerUUID) {
        return Optional.ofNullable(service.getRecordCache().requestSingleton(CacheGroupType.PLAYER, playerUUID));
    }

    @Override
    public @NotNull Optional<PlayerRecord> getPlayerRecordByName(@NotNull String username) {
        // TODO lookup cache/database or resolve UUID first?
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<PlayerRecord> getPlayerRecord(@NotNull OnlinePlayer player) {
        return getPlayerRecord(player.getUUID());
    }

    @Override
    public @NotNull PlayerRecord getOrInitPlayerRecord(@NotNull UUID playerUUID, @NotNull String currentUsername) {
        Optional<PlayerRecord> record = getPlayerRecord(playerUUID);
        if (record.isPresent()) {
            return record.get();
        }
        PlayerRecord newRecord = new PlayerRecordImpl(playerUUID, currentUsername, LocalDateTime.now(), null, null);
        service.getRecordCache().insert(CacheGroupType.PLAYER, playerUUID, CacheLoader.singleton(newRecord));

        return newRecord;
    }

    @Override
    public @NotNull PlayerRecord getOrInitPlayerRecord(@NotNull OnlinePlayer player) {
        return getOrInitPlayerRecord(player.getUUID(), player.getUsername());
    }

    @Override
    public int unbanPlayerEverywhere(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unbanReason) {
        LocalDateTime now = LocalDateTime.now();
        int count = 0;
        for (BanRecord record : getBanRecordsByStatus(playerUUID, PunishmentStatus.ACTIVE)) {
            if (record.isTemporary() && now.isAfter(record.getTerminationDate())) {
                // TODO where are we updating the status here!?!?!?!
                continue;
            }
            if (record instanceof MutableRecordProvider<?> mrp) {
                MutableBanRecord mbr = (MutableBanRecord) mrp.asMutable();
                mbr.unbanNow(staffUUID, unbanReason);

                try {
                    service.getCommitter().commit(mbr);
                    count++;
                } catch (CommitException e) {
                    service.getLogger().error("Failed to unban player everywhere (record ID: " + mbr.getId() + ")", e);
                }
            }
        }
        return count;
    }

    @Override
    public int unbanPlayerInContexts(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unbanReason, @NotNull String... contexts) {
        LocalDateTime now = LocalDateTime.now();
        ContextTester contextTester = service.getContextTester();
        int count = 0;
        for (BanRecord record : getBanRecordsByStatus(playerUUID, PunishmentStatus.ACTIVE)) {
            if (record.isTemporary() && now.isAfter(record.getTerminationDate())) {
                // TODO where are we updating the status here!?!?!?!
                continue;
            }
            if (record instanceof MutableRecordProvider<?> mrp) {
                for (String context : contexts) {
                    if (!contextTester.testContext(record, context)) {
                        continue;
                    }

                    MutableBanRecord mbr = (MutableBanRecord) mrp.asMutable();
                    mbr.unbanNow(staffUUID, unbanReason);

                    try {
                        service.getCommitter().commit(mbr);
                        count++;
                    } catch (CommitException e) {
                        service.getLogger().error("Failed to unban player (context: " + context
                                + " | record ID: " + mbr.getId() + ")", e);
                    }
                    break;
                }
            }
        }
        return count;
    }

    @Override
    public boolean unbanPlayerInGlobalContextOnly(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unbanReason) {
        Optional<BanRecord> record = getActiveBanRecord(playerUUID, null, true);
        if (record.isPresent() && record.get() instanceof MutableRecordProvider<?> mrp) {
            MutableBanRecord mbr = (MutableBanRecord) mrp.asMutable();
            mbr.unbanNow(staffUUID, unbanReason);

            try {
                service.getCommitter().commit(mbr);
                return true;
            } catch (CommitException e) {
                service.getLogger().error("Failed to unban player (record ID: " + mbr.getId() + ")", e);
            }
        }
        return false;
    }

    @Override
    public int unmutePlayerEverywhere(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unmuteReason) {
        LocalDateTime now = LocalDateTime.now();
        int count = 0;
        for (MuteRecord record : getMuteRecordsByStatus(playerUUID, PunishmentStatus.ACTIVE)) {
            if (record.isTemporary() && now.isAfter(record.getTerminationDate())) {
                // TODO where are we updating the status here!?!?!?!
                continue;
            }
            if (record instanceof MutableRecordProvider<?> mrp) {
                MutableMuteRecord mmr = (MutableMuteRecord) mrp.asMutable();
                mmr.unmuteNow(staffUUID, unmuteReason);

                try {
                    service.getCommitter().commit(mmr);
                    count++;
                } catch (CommitException e) {
                    service.getLogger().error("Failed to unmute player everywhere (record ID: " + mmr.getId() + ")", e);
                }
            }
        }
        return count;
    }

    @Override
    public int unmutePlayerInContexts(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unmuteReason, @NotNull String... contexts) {
        LocalDateTime now = LocalDateTime.now();
        ContextTester contextTester = service.getContextTester();
        int count = 0;
        for (MuteRecord record : getMuteRecordsByStatus(playerUUID, PunishmentStatus.ACTIVE)) {
            if (record.isTemporary() && now.isAfter(record.getTerminationDate())) {
                // TODO where are we updating the status here!?!?!?!
                continue;
            }
            if (record instanceof MutableRecordProvider<?> mrp) {
                for (String context : contexts) {
                    if (!contextTester.testContext(record, context)) {
                        continue;
                    }

                    MutableMuteRecord mmr = (MutableMuteRecord) mrp.asMutable();
                    mmr.unmuteNow(staffUUID, unmuteReason);

                    try {
                        service.getCommitter().commit(mmr);
                        count++;
                    } catch (CommitException e) {
                        service.getLogger().error("Failed to unmute player (context: " + context
                                + " | record ID: " + mmr.getId() + ")", e);
                    }
                    break;
                }
            }
        }
        return count;
    }

    @Override
    public boolean unmutePlayerInGlobalContextOnly(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unmuteReason) {
        Optional<MuteRecord> record = getActiveMuteRecord(playerUUID, null, true);
        if (record.isPresent() && record.get() instanceof MutableRecordProvider<?> mrp) {
            MutableMuteRecord mmr = (MutableMuteRecord) mrp.asMutable();
            mmr.unmuteNow(staffUUID, unmuteReason);

            try {
                service.getCommitter().commit(mmr);
                return true;
            } catch (CommitException e) {
                service.getLogger().error("Failed to unmute player (record ID: " + mmr.getId() + ")", e);
            }
        }
        return false;
    }

    @NotNull
    private <T extends Contextual> Optional<T> firstMatchContext(Collection<T> records, @Nullable String inContext, boolean includeGlobals) {
        for (T record : records) {
            if (!record.hasContext() && (includeGlobals || inContext == null)) {
                return Optional.of(record);
            }
            if (service.getContextTester().testContext(record, inContext)) {
                return Optional.of(record);
            }
        }
        return Optional.empty();
    }
}
