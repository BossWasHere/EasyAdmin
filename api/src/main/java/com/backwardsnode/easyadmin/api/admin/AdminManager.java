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

package com.backwardsnode.easyadmin.api.admin;

import com.backwardsnode.easyadmin.api.data.LookupOptions;
import com.backwardsnode.easyadmin.api.data.PunishmentStatus;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.record.*;
import com.backwardsnode.easyadmin.api.record.annotations.Cached;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * The main API for requesting, updating and saving admin records.
 */
public interface AdminManager {

    /**
     * Gets the first active {@link BanRecord} for a given player UUID, if one exists.
     * @param playerUUID the UUID of the player to check.
     * @param inContext the context to check for a ban in, or null for only global bans.
     * @param includeGlobalBans whether this method can return a global ban record.
     * @return an optional containing the ban record if one exists, or an empty optional if not.
     */
    @Cached
    @NotNull Optional<BanRecord> getActiveBanRecord(@NotNull UUID playerUUID, @Nullable String inContext, boolean includeGlobalBans);

    /**
     * Gets the first active {@link BanRecord} for a given IP address, if one exists.
     * @param ipAddress the IP address to check.
     * @param inContext the context to check for a ban in, or null for only global bans.
     * @param includeGlobalBans whether this method can return a global ban record.
     * @return an optional containing the ban record if one exists, or an empty optional if not.
     */
    @Cached
    @NotNull Optional<BanRecord> getActiveBanRecord(@NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalBans);

    /**
     * Gets the first active {@link BanRecord} for a given player UUID <b>OR</b> IP address, if one exists.
     * @param playerUUID the UUID of the player to check.
     * @param ipAddress the IP address to check.
     * @param inContext the context to check for a ban in, or null for only global bans.
     * @param includeGlobalBans whether this method can return a global ban record.
     * @return a ban record, or {@code null} if none exist.
     */
    @Cached
    @NotNull Optional<BanRecord> getActiveBanRecord(@NotNull UUID playerUUID, @NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalBans);

    /**
     * Gets the first active {@link BanRecord} for a given player (UUID and IP address), if one exists.
     * @param player the offline player to check.
     * @param ipAddress the IP address to check.
     * @param inContext the context to check for a ban in, or null for only global bans.
     * @param includeGlobalBans whether this method can return a global ban record.
     * @return a ban record, or {@code null} if none exist.
     */
    @Cached
    @NotNull Optional<BanRecord> getActiveBanRecord(@NotNull OfflinePlayer player, @NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalBans);

    /**
     * Gets the first active {@link MuteRecord} for a given player UUID, if one exists.
     * @param playerUUID the UUID of the player to check.
     * @param inContext the context to check for a mute in, or null for only global mutes.
     * @param includeGlobalMutes whether this method can return a global mute record.
     * @return a mute record, or {@code null} if none exist.
     */
    @Cached
    @NotNull Optional<MuteRecord> getActiveMuteRecord(@NotNull UUID playerUUID, @Nullable String inContext, boolean includeGlobalMutes);

    /**
     * Gets the first active {@link MuteRecord} for a given IP address, if one exists.
     * @param ipAddress the IP address to check.
     * @param inContext the context to check for a mute in, or null for only global mutes.
     * @param includeGlobalMutes whether this method can return a global mute record.
     * @return a mute record, or {@code null} if none exist.
     */
    @Cached
    @NotNull Optional<MuteRecord> getActiveMuteRecord(@NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalMutes);

    /**
     * Gets the first active {@link MuteRecord} for a given player UUID <b>OR</b> IP address, if one exists.
     * @param playerUUID the UUID of the player to check.
     * @param ipAddress the IP address to check.
     * @param inContext the context to check for a mute in, or null for only global mutes.
     * @param includeGlobalMutes whether this method can return a global mute record.
     * @return a mute record, or {@code null} if none exist.
     */
    @Cached
    @NotNull Optional<MuteRecord> getActiveMuteRecord(@NotNull UUID playerUUID, @NotNull String ipAddress, @Nullable String inContext, boolean includeGlobalMutes);

    /**
     * Gets the first active {@link MuteRecord} for a given online player (UUID and IP address), if one exists.
     * @param player the online player to check.
     * @param inContext the context to check for a mute in, or null for only global mutes.
     * @param includeGlobalMutes whether this method can return a global mute record.
     * @return a mute record, or {@code null} if none exist.
     */
    @Cached
    @NotNull Optional<MuteRecord> getActiveMuteRecord(@NotNull OnlinePlayer player, @Nullable String inContext, boolean includeGlobalMutes);

    /**
     * Gets all {@link BanRecord}s for a given player UUID, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @return a collection of ban records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getBanRecords(UUID, LookupOptions)} to set a limit.
     */
    @Cached
    @NotNull Collection<BanRecord> getBanRecords(@NotNull UUID playerUUID);

    /**
     * Gets all {@link BanRecord}s for a given player UUID, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param options the options to use when retrieving records.
     * @return a collection of ban records, or an empty collection if none exist.
     */
    @Cached
    @NotNull Collection<BanRecord> getBanRecords(@NotNull UUID playerUUID, @Nullable LookupOptions options);

    /**
     * Gets all {@link BanRecord}s for a given IP address, if any exist.
     * @param ipAddress the IP address to check.
     * @return a collection of ban records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getBanRecords(String, LookupOptions)} to set a limit.
     */
    @Cached
    @NotNull Collection<BanRecord> getBanRecords(@NotNull String ipAddress);

    /**
     * Gets all {@link BanRecord}s for a given IP address, if any exist.
     * @param ipAddress the IP address to check.
     * @param options the options to use when retrieving records.
     * @return a collection of ban records, or an empty collection if none exist.
     */
    @Cached
    @NotNull Collection<BanRecord> getBanRecords(@NotNull String ipAddress, @Nullable LookupOptions options);

    /**
     * Gets all {@link BanRecord}s for a given player UUID <b>OR</b> IP address, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param ipAddress the IP address to check.
     * @return a collection of ban records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getBanRecords(UUID, String, LookupOptions)} to set a limit.
     */
    @Cached
    @NotNull Collection<BanRecord> getBanRecords(@NotNull UUID playerUUID, @NotNull String ipAddress);
    
    /**
     * Gets all {@link BanRecord}s for a given player UUID <b>OR</b> IP address, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param ipAddress the IP address to check.
     * @param options the options to use when retrieving records.
     * @return a collection of ban records, or an empty collection if none exist.
     */
    @NotNull Collection<BanRecord> getBanRecords(@NotNull UUID playerUUID, @NotNull String ipAddress, @Nullable LookupOptions options);

    /**
     * Gets all {@link BanRecord}s for a given online player (UUID and IP address), if any exist.
     * @param player the online player to check.
     * @return a collection of ban records, or an empty collection if none exist.
     */
    @NotNull Collection<BanRecord> getBanRecords(@NotNull OnlinePlayer player);

    /**
     * Gets all {@link BanRecord}s for a given online player (UUID and IP address), if any exist.
     * @param player the online player to check.
     * @param options the options to use when retrieving records.
     * @return a collection of ban records, or an empty collection if none exist.
     */
    @NotNull Collection<BanRecord> getBanRecords(@NotNull OnlinePlayer player, @Nullable LookupOptions options);

    /**
     * Gets all {@link BanRecord}s for a given player UUID, by status, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param status the status of the records to retrieve.
     * @return a collection of ban records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getBanRecords(UUID, LookupOptions)} to set a limit.
     */
    @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull UUID playerUUID, @NotNull PunishmentStatus status);

    /**
     * Gets all {@link BanRecord}s for a given player UUID, by status, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param status the status of the records to retrieve.
     * @param options the options to use when retrieving records.
     * @return a collection of ban records, or an empty collection if none exist.
     */
    @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull UUID playerUUID, @NotNull PunishmentStatus status, @Nullable LookupOptions options);

    /**
     * Gets all {@link BanRecord}s for a given IP address, by status, if any exist.
     * @param ipAddress the IP address to check.
     * @param status the status of the records to retrieve.
     * @return a collection of ban records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getBanRecords(String, LookupOptions)} to set a limit.
     */
    @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull String ipAddress, @NotNull PunishmentStatus status);

    /**
     * Gets all {@link BanRecord}s for a given IP address, by status, if any exist.
     * @param ipAddress the IP address to check.
     * @param status the status of the records to retrieve.
     * @param options the options to use when retrieving records.
     * @return a collection of ban records, or an empty collection if none exist.
     */
    @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull String ipAddress, @NotNull PunishmentStatus status, @Nullable LookupOptions options);

    /**
     * Gets all {@link BanRecord}s for a given player UUID <b>OR</b> IP address, by status, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param ipAddress the IP address to check.
     * @param status the status of the records to retrieve.
     * @return a collection of ban records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getBanRecords(UUID, String, LookupOptions)} to set a limit.
     */
    @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull UUID playerUUID, @NotNull String ipAddress, @NotNull PunishmentStatus status);

    /**
     * Gets all {@link BanRecord}s for a given player UUID <b>OR</b> IP address, by status, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param ipAddress the IP address to check.
     * @param status the status of the records to retrieve.
     * @param options the options to use when retrieving records.
     * @return a collection of ban records, or an empty collection if none exist.
     */
    @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull UUID playerUUID, @NotNull String ipAddress, @NotNull PunishmentStatus status, @Nullable LookupOptions options);

    /**
     * Gets all {@link BanRecord}s for a given online player (UUID and IP address), by status, if any exist.
     * @param player the online player to check.
     * @param status the status of the records to retrieve.
     * @return a collection of ban records, or an empty collection if none exist.
     */
    @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull OnlinePlayer player, @NotNull PunishmentStatus status);

    /**
     * Gets all {@link BanRecord}s for a given online player (UUID and IP address), by status, if any exist.
     * @param player the online player to check.
     * @param status the status of the records to retrieve.
     * @param options the options to use when retrieving records.
     * @return a collection of ban records, or an empty collection if none exist.
     */
    @NotNull Collection<BanRecord> getBanRecordsByStatus(@NotNull OnlinePlayer player, @NotNull PunishmentStatus status, @Nullable LookupOptions options);

    /**
     * Gets all {@link CommentRecord}s, comments and warnings, for a given player UUID, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @return a collection of comment and warning records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getCommentRecords(UUID, LookupOptions)} to set a limit.
     */
    @Cached
    @NotNull Collection<CommentRecord> getCommentRecords(@NotNull UUID playerUUID);

    /**
     * Gets all {@link CommentRecord}s, comments and warnings, for a given player UUID, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param options the options to use when retrieving records.
     * @return a collection of comment and warning records, or an empty collection if none exist.
     */
    @Cached
    @NotNull Collection<CommentRecord> getCommentRecords(@NotNull UUID playerUUID, @Nullable LookupOptions options);

    /**
     * Gets all {@link CommentRecord}s, comments and warnings, for a given online player, if any exist.
     * @param player the online player to check.
     * @return a collection of comment and warning records, or an empty collection if none exist.
     */
    @Cached
    @NotNull Collection<CommentRecord> getCommentRecords(@NotNull OnlinePlayer player);

    /**
     * Gets all {@link CommentRecord}s, comments and warnings, for a given online player, if any exist.
     * @param player the online player to check.
     * @param options the options to use when retrieving records.
     * @return a collection of comment and warning records, or an empty collection if none exist.
     */
    @Cached
    @NotNull Collection<CommentRecord> getCommentRecords(@NotNull OnlinePlayer player, @Nullable LookupOptions options);

    /**
     * Gets all {@link CommentRecord}s for a given player UUID, by type, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param warning true to retrieve warnings, false to retrieve comments.
     * @return a collection of comment or warning records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getCommentRecordsByType(UUID, boolean, LookupOptions)} to set a limit.
     */
    @Cached
    @NotNull Collection<CommentRecord> getCommentRecordsByType(@NotNull UUID playerUUID, boolean warning);

    /**
     * Gets all {@link CommentRecord}s for a given player UUID, by type, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param warning true to retrieve warnings, false to retrieve comments.
     * @param options the options to use when retrieving records.
     * @return a collection of comment or warning records, or an empty collection if none exist.
     */
    @Cached
    @NotNull Collection<CommentRecord> getCommentRecordsByType(@NotNull UUID playerUUID, boolean warning, @Nullable LookupOptions options);

    /**
     * Gets all {@link CommentRecord}s for a given online player, by type, if any exist.
     * @param player the online player to check.
     * @param warning true to retrieve warnings, false to retrieve comments.
     * @return a collection of comment or warning records, or an empty collection if none exist.
     */
    @Cached
    @NotNull Collection<CommentRecord> getCommentRecordsByType(@NotNull OnlinePlayer player, boolean warning);

    /**
     * Gets all {@link CommentRecord}s for a given online player, by type, if any exist.
     * @param player the online player to check.
     * @param warning true to retrieve warnings, false to retrieve comments.
     * @param options the options to use when retrieving records.
     * @return a collection of comment or warning records, or an empty collection if none exist.
     */
    @Cached
    @NotNull Collection<CommentRecord> getCommentRecordsByType(@NotNull OnlinePlayer player, boolean warning, @Nullable LookupOptions options);

    /**
     * Gets all {@link KickRecord}s for a given player UUID, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @return a collection of kick records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getKickRecords(UUID, LookupOptions)} to set a limit.
     */
    @NotNull Collection<KickRecord> getKickRecords(@NotNull UUID playerUUID);

    /**
     * Gets all {@link KickRecord}s for a given player UUID, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param options the options to use when retrieving records.
     * @return a collection of kick records, or an empty collection if none exist.
     */
    @NotNull Collection<KickRecord> getKickRecords(@NotNull UUID playerUUID, @Nullable LookupOptions options);

    /**
     * Gets all {@link KickRecord}s for a given online player, if any exist.
     * @param player the online player to check.
     * @return a collection of kick records, or an empty collection if none exist.
     */
    @NotNull Collection<KickRecord> getKickRecords(@NotNull OnlinePlayer player);

    /**
     * Gets all {@link KickRecord}s for a given online player, if any exist.
     * @param player the online player to check.
     * @param options the options to use when retrieving records.
     * @return a collection of kick records, or an empty collection if none exist.
     */
    @NotNull Collection<KickRecord> getKickRecords(@NotNull OnlinePlayer player, @Nullable LookupOptions options);

    /**
     * Gets all {@link KickRecord}s for a given player UUID, by the global flag, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param global true if the records returned should be global, false if not.
     * @return a collection of kick records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getKickRecordsByType(UUID, boolean, LookupOptions)} to set a limit.
     */
    @NotNull Collection<KickRecord> getKickRecordsByType(@NotNull UUID playerUUID, boolean global);

    /**
     * Gets all {@link KickRecord}s for a given player UUID, by the global flag, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param global true if the records returned should be global, false if not.
     * @param options the options to use when retrieving records.
     * @return a collection of kick records, or an empty collection if none exist.
     */
    @NotNull Collection<KickRecord> getKickRecordsByType(@NotNull UUID playerUUID, boolean global, @Nullable LookupOptions options);

    /**
     * Gets all {@link KickRecord}s for a given online player, by the global flag, if any exist.
     * @param player the online player to check.
     * @param global true if the records returned should be global, false if not.
     * @return a collection of kick records, or an empty collection if none exist.
     */
    @NotNull Collection<KickRecord> getKickRecordsByType(@NotNull OnlinePlayer player, boolean global);

    /**
     * Gets all {@link KickRecord}s for a given online player, by the global flag, if any exist.
     * @param player the online player to check.
     * @param global true if the records returned should be global, false if not.
     * @param options the options to use when retrieving records.
     * @return a collection of kick records, or an empty collection if none exist.
     */
    @NotNull Collection<KickRecord> getKickRecordsByType(@NotNull OnlinePlayer player, boolean global, @Nullable LookupOptions options);

    /**
     * Gets all {@link MuteRecord}s for a given player UUID, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @return a collection of mute records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getMuteRecords(UUID, LookupOptions)} to set a limit.
     */
    @NotNull Collection<MuteRecord> getMuteRecords(@NotNull UUID playerUUID);

    /**
     * Gets all {@link MuteRecord}s for a given player UUID, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param options the options to use when retrieving records.
     * @return a collection of mute records, or an empty collection if none exist.
     */
    @NotNull Collection<MuteRecord> getMuteRecords(@NotNull UUID playerUUID, @Nullable LookupOptions options);

    /**
     * Gets all {@link MuteRecord}s for a given IP address, if any exist.
     * @param ipAddress the IP address to check.
     * @return a collection of mute records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getMuteRecords(String, LookupOptions)} to set a limit.
     */
    @NotNull Collection<MuteRecord> getMuteRecords(@NotNull String ipAddress);

    /**
     * Gets all {@link MuteRecord}s for a given IP address, if any exist.
     * @param ipAddress the IP address to check.
     * @param options the options to use when retrieving records.
     * @return a collection of mute records, or an empty collection if none exist.
     */
    @NotNull Collection<MuteRecord> getMuteRecords(@NotNull String ipAddress, @Nullable LookupOptions options);

    /**
     * Gets all {@link MuteRecord}s for a given player UUID <b>OR</b> IP address, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param ipAddress the IP address to check.
     * @return a collection of mute records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getMuteRecords(UUID, String, LookupOptions)} to set a limit.
     */
    @NotNull Collection<MuteRecord> getMuteRecords(@NotNull UUID playerUUID, @NotNull String ipAddress);

    /**
     * Gets all {@link MuteRecord}s for a given player UUID <b>OR</b> IP address, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param ipAddress the IP address to check.
     * @param options the options to use when retrieving records.
     * @return a collection of mute records, or an empty collection if none exist.
     */
    @NotNull Collection<MuteRecord> getMuteRecords(@NotNull UUID playerUUID, @NotNull String ipAddress, @Nullable LookupOptions options);

    /**
     * Gets all {@link MuteRecord}s for a given online player, if any exist.
     * @param player the online player to check.
     * @return a collection of mute records, or an empty collection if none exist.
     */
    @NotNull Collection<MuteRecord> getMuteRecords(@NotNull OnlinePlayer player);

    /**
     * Gets all {@link MuteRecord}s for a given online player, if any exist.
     * @param player the online player to check.
     * @param options the options to use when retrieving records.
     * @return a collection of mute records, or an empty collection if none exist.
     */
    @NotNull Collection<MuteRecord> getMuteRecords(@NotNull OnlinePlayer player, @Nullable LookupOptions options);

    /**
     * Gets all {@link MuteRecord}s for a given player UUID, by status, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param status the status of the records to retrieve.
     * @return a collection of mute records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getMuteRecords(UUID, LookupOptions)} to set a limit.
     */
    @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull UUID playerUUID, @NotNull PunishmentStatus status);

    /**
     * Gets all {@link MuteRecord}s for a given player UUID, by status, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param status the status of the records to retrieve.
     * @param options the options to use when retrieving records.
     * @return a collection of mute records, or an empty collection if none exist.
     */
    @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull UUID playerUUID, @NotNull PunishmentStatus status, @Nullable LookupOptions options);

    /**
     * Gets all {@link MuteRecord}s for a given IP address, by status, if any exist.
     * @param ipAddress the IP address to check.
     * @param status the status of the records to retrieve.
     * @return a collection of mute records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getMuteRecords(String, LookupOptions)} to set a limit.
     */
    @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull String ipAddress, @NotNull PunishmentStatus status);

    /**
     * Gets all {@link MuteRecord}s for a given IP address, by status, if any exist.
     * @param ipAddress the IP address to check.
     * @param status the status of the records to retrieve.
     * @param options the options to use when retrieving records.
     * @return a collection of mute records, or an empty collection if none exist.
     */
    @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull String ipAddress, @NotNull PunishmentStatus status, @Nullable LookupOptions options);

    /**
     * Gets all {@link MuteRecord}s for a given player UUID <b>OR</b> IP address, by status, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param ipAddress the IP address to check.
     * @param status the status of the records to retrieve.
     * @return a collection of mute records, or an empty collection if none exist.
     * @implNote the number of records returned may be capped. Use {@link #getMuteRecords(UUID, String, LookupOptions)} to set a limit.
     */
    @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull UUID playerUUID, @NotNull String ipAddress, @NotNull PunishmentStatus status);

    /**
     * Gets all {@link MuteRecord}s for a given player UUID <b>OR</b> IP address, by status, if any exist.
     * @param playerUUID the UUID of the player to check.
     * @param ipAddress the IP address to check.
     * @param status the status of the records to retrieve.
     * @param options the options to use when retrieving records.
     * @return a collection of mute records, or an empty collection if none exist.
     */
    @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull UUID playerUUID, @NotNull String ipAddress, @NotNull PunishmentStatus status, @Nullable LookupOptions options);

    /**
     * Gets all {@link MuteRecord}s for a given online player, by status, if any exist.
     * @param player the online player to check.
     * @param status the status of the records to retrieve.
     * @return a collection of mute records, or an empty collection if none exist.
     */
    @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull OnlinePlayer player, @NotNull PunishmentStatus status);

    /**
     * Gets all {@link MuteRecord}s for a given online player, by status, if any exist.
     * @param player the online player to check.
     * @param status the status of the records to retrieve.
     * @param options the options to use when retrieving records.
     * @return a collection of mute records, or an empty collection if none exist.
     */
    @NotNull Collection<MuteRecord> getMuteRecordsByStatus(@NotNull OnlinePlayer player, @NotNull PunishmentStatus status, @Nullable LookupOptions options);

    /**
     * Gets the {@link PlayerRecord} associated with the given UUID, if it exists.
     * @param playerUUID the UUID of the player to load a record for.
     * @return an optional containing the player record, or an empty optional if none exists.
     */
    @NotNull Optional<PlayerRecord> getPlayerRecord(@NotNull UUID playerUUID);

    /**
     * Gets the {@link PlayerRecord} associated with the given username, if it exists.
     * @param username the username of the player to load a record for.
     * @return an optional containing the player record, or an empty optional if none exists.
     * @apiNote if multiple records are found, the API will try to match it to the player who currently has the username.
     */
    @NotNull Optional<PlayerRecord> getPlayerRecordByName(@NotNull String username);

    /**
     * Gets the {@link PlayerRecord} associated with the given online player, if it exists.
     * @param player the online player to load a record for.
     * @return an optional containing the player record, or an empty optional if none exists.
     */
    @NotNull Optional<PlayerRecord> getPlayerRecord(@NotNull OnlinePlayer player);

    /**
     * Gets the {@link PlayerRecord} associated with the given UUID, or creates a new one if it doesn't exist.
     * @param playerUUID the UUID of the player to load a record for.
     * @param currentUsername the current username of the player.
     * @return the player record, or a new one if none exists.
     * @apiNote passing a username that is different to the recorded username will not update this record.
     */
    @NotNull PlayerRecord getOrInitPlayerRecord(@NotNull UUID playerUUID, @NotNull String currentUsername);

    /**
     * Gets the {@link PlayerRecord} associated with the given online player, or creates a new one if it doesn't exist.
     * @param player the online player to load a record for.
     * @return the player record, or a new one if none exists.
     */
    @NotNull PlayerRecord getOrInitPlayerRecord(@NotNull OnlinePlayer player);

    /**
     * Ends all active bans targeting a specific player.
     * @param playerUUID the UUID of the player to unban.
     * @param staffUUID the UUID of the staff member who is unbanning the player.
     * @param unbanReason the (optional) reason for this action.
     * @return the number of bans that have been ended.
     */
    int unbanPlayerEverywhere(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unbanReason);

    /**
     * Ends all active bans targeting a specific player in specific contexts.
     * @param playerUUID the UUID of the player to unban.
     * @param staffUUID the UUID of the staff member who is unbanning the player.
     * @param unbanReason the (optional) reason for this action.
     * @param contexts the contexts to unban in.
     * @return the number of bans that have been ended.
     */
    int unbanPlayerInContexts(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unbanReason, @NotNull String... contexts);

    /**
     * Ends an active ban if one exists, which must be a global ban, targeting a specific player.
     * <p>Bans in specific contexts will remain standing, if any.</p>
     * @param playerUUID the UUID of the player to unban.
     * @param staffUUID the UUID of the staff member who is unbanning the player.
     * @param unbanReason the (optional) reason for this action.
     * @return true if a ban was ended, false if no ban was found.
     */
    boolean unbanPlayerInGlobalContextOnly(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unbanReason);

    /**
     * Ends all active mutes targeting a specific player.
     * @param playerUUID the UUID of the player to unmute.
     * @param staffUUID the UUID of the staff member who is unmuting the player.
     * @param unmuteReason the (optional) reason for this action.
     * @return the number of mutes that have been ended.
     */
    int unmutePlayerEverywhere(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unmuteReason);

    /**
     * Ends all active mutes targeting a specific player in specific contexts.
     * @param playerUUID the UUID of the player to unmute.
     * @param staffUUID the UUID of the staff member who is unmuting the player.
     * @param unmuteReason the (optional) reason for this action.
     * @param contexts the contexts to unmute in.
     * @return the number of mutes that have been ended.
     */
    int unmutePlayerInContexts(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unmuteReason, @NotNull String... contexts);

    /**
     * Ends an active mute if one exists, which must be a global mute, targeting a specific player.
     * <p>Mutes in specific contexts will remain standing, if any.</p>
     * @param playerUUID the UUID of the player to unmute.
     * @param staffUUID the UUID of the staff member who is unmuting the player.
     * @param unmuteReason the (optional) reason for this action.
     * @return true if a mute was ended, false if no mute was found.
     */
    boolean unmutePlayerInGlobalContextOnly(@NotNull UUID playerUUID, @Nullable UUID staffUUID, @Nullable String unmuteReason);

}
