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

package com.backwardsnode.easyadmin.core.database;

import com.backwardsnode.easyadmin.api.data.LookupOptions;
import com.backwardsnode.easyadmin.api.data.PunishmentStatus;
import com.backwardsnode.easyadmin.api.record.*;
import com.backwardsnode.easyadmin.api.record.modify.BanRecordModifier;
import com.backwardsnode.easyadmin.api.record.modify.MuteRecordModifier;
import com.backwardsnode.easyadmin.api.record.modify.PlayerRecordModifier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

import static com.backwardsnode.easyadmin.api.internal.Tables.*;

public abstract class AbstractStatementFactory implements DatabaseStatementFactory {

    private static final int DEFAULT_SELECT_LIMIT_MAX = 100;
    protected final LookupOptions DEFAULT_OPTIONS;

    protected AbstractStatementFactory() {
        this(new LookupOptions(DEFAULT_SELECT_LIMIT_MAX));
    }

    protected AbstractStatementFactory(LookupOptions options) {
        DEFAULT_OPTIONS = options;
    }

    protected PreparedStatement initCreatePlayerRecordSql(Connection connection) throws SQLException {
        return connection.prepareStatement("INSERT INTO players (uuid,username,lastIP,firstJoin,lastJoin,lastServer) VALUES (?,?,?,?,?,?);");
    }

    protected PreparedStatement initCreateBanSql(Connection connection, BanRecord record) throws SQLException {
        if (record.getTerminationDate() == null) {
            if (record.getIpAddress() == null) {
                return connection.prepareStatement("INSERT INTO bans (status,playerUuid,staffUuid,banDate,contexts,reason) VALUES (?,?,?,?,?,?);");
            } else {
                return connection.prepareStatement("INSERT INTO bans (status,playerUuid,staffUuid,playerIP,banDate,contexts,reason) VALUES (?,?,?,?,?,?,?);");
            }
        } else {
            if (record.getIpAddress() == null) {
                return connection.prepareStatement("INSERT INTO bans (status,playerUuid,staffUuid,banDate,unbanDate,contexts,reason) VALUES (?,?,?,?,?,?,?);");
            } else {
                return connection.prepareStatement("INSERT INTO bans (status,playerUuid,staffUuid,playerIP,banDate,unbanDate,contexts,reason) VALUES (?,?,?,?,?,?,?,?);");
            }
        }
    }

    protected PreparedStatement initCreateCommentSql(Connection connection) throws SQLException {
        return connection.prepareStatement("INSERT INTO comments (playerUuid,staffUuid,dateAdded,isWarning,comment) VALUES (?,?,?,?,?);");
    }

    protected PreparedStatement initCreateKickSql(Connection connection) throws SQLException {
        return connection.prepareStatement("INSERT INTO kicks (playerUuid,staffUuid,kickDate,isGlobal,reason) VALUES (?,?,?,?,?);");
    }

    protected PreparedStatement initCreateMuteSql(Connection connection, MuteRecord record) throws SQLException {
        if (record.getTerminationDate() == null) {
            if (record.getIpAddress() == null) {
                return connection.prepareStatement("INSERT INTO mutes (status,playerUuid,staffUuid,muteDate,contexts,reason) VALUES (?,?,?,?,?,?);");
            } else {
                return connection.prepareStatement("INSERT INTO mutes (status,playerUuid,staffUuid,playerIP,muteDate,contexts,reason) VALUES (?,?,?,?,?,?,?);");
            }
        } else {
            if (record.getIpAddress() == null) {
                return connection.prepareStatement("INSERT INTO mutes (status,playerUuid,staffUuid,muteDate,unmuteDate,contexts,reason) VALUES (?,?,?,?,?,?,?);");
            } else {
                return connection.prepareStatement("INSERT INTO mutes (status,playerUuid,staffUuid,playerIP,muteDate,unmuteDate,contexts,reason) VALUES (?,?,?,?,?,?,?,?);");
            }
        }
    }

    protected PreparedStatement initCountPlayerBansSql(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT COUNT(*) AS count FROM bans WHERE playerUuid = ?;");
    }

    protected PreparedStatement initCountPlayerCommentsSql(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT COUNT(*) AS count FROM comments WHERE playerUuid = ?;");
    }

    protected PreparedStatement initCountPlayerKicksSql(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT COUNT(*) AS count FROM kicks WHERE playerUuid = ?;");
    }

    protected PreparedStatement initCountPlayerMutesSql(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT COUNT(*) AS count FROM mutes WHERE playerUuid = ?;");
    }

    protected PreparedStatement initCountStaffBansSql(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT COUNT(*) AS count FROM bans WHERE staffUuid = ?;");
    }

    protected PreparedStatement initCountStaffCommentsSql(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT COUNT(*) AS count FROM comments WHERE staffUuid = ?;");
    }

    protected PreparedStatement initCountStaffKicksSql(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT COUNT(*) AS count FROM kicks WHERE staffUuid = ?;");
    }

    protected PreparedStatement initCountStaffMutesSql(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT COUNT(*) AS count FROM mutes WHERE staffUuid = ?;");
    }

    protected PreparedStatement initRetrievePlayerRecordSql(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM players WHERE uuid = ?;");
    }

    protected PreparedStatement initRetrievePlayerByUsernameSql(Connection connection) throws SQLException {
        return connection.prepareStatement("SELECT * FROM players WHERE username = ?;");
    }

    protected PreparedStatement initRetrievePlayerBansSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM bans WHERE playerUuid = ?" + addDateFilter(BANS.BAN_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrievePlayerCommentsSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM comments WHERE playerUuid = ?" + addDateFilter(COMMENTS.DATE_ADDED, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrievePlayerKicksSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM kicks WHERE playerUuid = ?" + addDateFilter(KICKS.KICK_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrievePlayerMutesSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM mutes WHERE playerUuid = ?" + addDateFilter(MUTES.MUTE_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrieveStaffBansSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM bans WHERE staffUuid = ?" + addDateFilter(BANS.BAN_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrieveStaffCommentsSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM comments WHERE staffUuid = ?" + addDateFilter(COMMENTS.DATE_ADDED, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrieveStaffKicksSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM kicks WHERE staffUuid = ?" + addDateFilter(KICKS.KICK_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrieveStaffMutesSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM mutes WHERE staffUuid = ?" + addDateFilter(MUTES.MUTE_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrievePlayerBansByStatusSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM bans WHERE playerUuid = ? AND status = ?" + addDateFilter(BANS.BAN_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrievePlayerMutesByStatusSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM mutes WHERE playerUuid = ? AND status = ?" + addDateFilter(MUTES.MUTE_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrievePlayerCommentsByTypeSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM comments WHERE playerUuid = ? AND isWarning = ?" + addDateFilter(COMMENTS.DATE_ADDED, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrieveIPBansSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM bans WHERE playerIP = ?" + addDateFilter(BANS.BAN_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrieveIPMutesSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM mutes WHERE playerIP = ?" + addDateFilter(MUTES.MUTE_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrieveIPBansByStatusSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM bans WHERE playerIP = ? AND status = ?" + addDateFilter(BANS.BAN_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrieveIPMutesByStatusSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM mutes WHERE playerIP = ? AND status = ?" + addDateFilter(MUTES.MUTE_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrievePlayerBansOrIPBansSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM bans WHERE (playerUuid = ? OR playerIP = ?)" + addDateFilter(BANS.BAN_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrievePlayerMutesOrIPMutesSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM mutes WHERE (playerUuid = ? OR playerIP = ?)" + addDateFilter(MUTES.MUTE_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrievePlayerBansOrIPBansByStatusSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM bans WHERE (playerUuid = ? OR playerIP = ?) AND status = ?" + addDateFilter(BANS.BAN_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initRetrievePlayerMutesOrIPMutesByStatusSql(Connection connection, LookupOptions options) throws SQLException {
        return connection.prepareStatement("SELECT * FROM mutes WHERE (playerUuid = ? OR playerIP = ?) AND status = ?" + addDateFilter(MUTES.MUTE_DATE, options) + " ORDER BY id DESC LIMIT ? OFFSET ?;");
    }

    protected PreparedStatement initUpdatePlayerJoiningRecordSql(Connection connection) throws SQLException {
        return connection.prepareStatement("UPDATE players SET username = ?, lastJoin = ?, lastIP = ?, totalJoins = ? WHERE uuid = ?;");
    }

    protected PreparedStatement initUpdatePlayerLeavingRecordSql(Connection connection) throws SQLException {
        return connection.prepareStatement("UPDATE players SET lastLeave = ?, lastServer = ?, playtime = ? WHERE uuid = ?;");
    }

    protected PreparedStatement initUpdatePlayerDynamicRecordSql(Connection connection) throws SQLException {
        return connection.prepareStatement("UPDATE players SET lastServer = ? WHERE uuid = ?;");
    }

    protected PreparedStatement initUpdatePlayerFullRecordSql(Connection connection) throws SQLException {
        return connection.prepareStatement("UPDATE players SET username = ?, lastJoin = ?, lastLeave = ?, lastIP = ?, lastServer = ?, playtime = ?, totalJoins = ? WHERE uuid = ?;");
    }

    protected PreparedStatement initUpdatePlayerBanSql(Connection connection) throws SQLException {
        return connection.prepareStatement("UPDATE bans SET status = ?, unbanStaffUuid = ?, unbanDate = ?, unbanReason = ? WHERE id = ?;");
    }

    protected PreparedStatement initUpdatePlayerMuteSql(Connection connection) throws SQLException {
        return connection.prepareStatement("UPDATE mutes SET status = ?, unmuteStaffUuid = ?, unmuteDate = ?, unmuteReason = ? WHERE id = ?;");
    }

    @Override
    public PreparedStatement getCreatePlayerRecordStatement(Connection connection, PlayerRecord record) throws SQLException {
        PreparedStatement statement = initCreatePlayerRecordSql(connection);

        statement.setString(1, notNull(record.getId()));
        statement.setString(2, notNull(record.getUsername()));
        statement.setString(3, record.getLastAddress());
        statement.setTimestamp(4, Timestamp.valueOf(record.getFirstJoin()));
        statement.setTimestamp(5, Timestamp.valueOf(record.getLastJoin()));
        statement.setString(6, record.getLastServer());

        return statement;
    }

    @Override
    public PreparedStatement getCreateBanStatement(Connection connection, BanRecord record) throws SQLException {
        PreparedStatement statement = initCreateBanSql(connection, record);

        applyCreateStatusAdminRecordStatement(statement, record);
        return statement;
    }

    @Override
    public PreparedStatement getCreateCommentStatement(Connection connection, CommentRecord record) throws SQLException {
        PreparedStatement statement = initCreateCommentSql(connection);

        statement.setString(1, notNull(record.getPlayer()));
        statement.setString(2, maybeNull(record.getStaff()));
        statement.setTimestamp(3, Timestamp.valueOf(record.getDateAdded()));
        statement.setBoolean(4, record.isWarning());
        statement.setString(5, notNull(record.getComment()));

        return statement;
    }

    @Override
    public PreparedStatement getCreateKickStatement(Connection connection, KickRecord record) throws SQLException {
        PreparedStatement statement = initCreateKickSql(connection);

        statement.setString(1, notNull(record.getPlayer()));
        statement.setString(2, maybeNull(record.getStaff()));
        statement.setTimestamp(3, Timestamp.valueOf(record.getDateAdded()));
        statement.setBoolean(4, record.isGlobal());
        statement.setString(5, record.getReason());

        return statement;
    }

    @Override
    public PreparedStatement getCreateMuteStatement(Connection connection, MuteRecord record) throws SQLException {
        PreparedStatement statement = initCreateMuteSql(connection, record);

        applyCreateStatusAdminRecordStatement(statement, record);
        return statement;
    }

    protected void applyCreateStatusAdminRecordStatement(PreparedStatement statement, SpecialAdminRecord record) throws SQLException {
        int index = 1;
        statement.setString(index++, notNull(record.getStatus()));
        statement.setString(index++, notNull(record.getPlayer()));
        statement.setString(index++, maybeNull(record.getStaff()));
        if (record.getIpAddress() != null) {
            statement.setString(index++, record.getIpAddress());
        }
        statement.setTimestamp(index++, Timestamp.valueOf(record.getDateAdded()));
        if (record.getTerminationDate() != null) {
            statement.setTimestamp(index++, Timestamp.valueOf(record.getTerminationDate()));
        }
        statement.setString(index++, record.getContexts());
        statement.setString(index, record.getReason());
    }

    @Override
    public PreparedStatement getCountPlayerBansStatement(Connection connection, UUID player) throws SQLException {
        PreparedStatement statement = initCountPlayerBansSql(connection);

        statement.setString(1, notNull(player));
        return statement;
    }

    @Override
    public PreparedStatement getCountPlayerCommentsStatement(Connection connection, UUID player) throws SQLException {
        PreparedStatement statement = initCountPlayerCommentsSql(connection);

        statement.setString(1, notNull(player));
        return statement;
    }

    @Override
    public PreparedStatement getCountPlayerKicksStatement(Connection connection, UUID player) throws SQLException {
        PreparedStatement statement = initCountPlayerKicksSql(connection);

        statement.setString(1, notNull(player));
        return statement;
    }

    @Override
    public PreparedStatement getCountPlayerMutesStatement(Connection connection, UUID player) throws SQLException {
        PreparedStatement statement = initCountPlayerMutesSql(connection);

        statement.setString(1, notNull(player));
        return statement;
    }

    @Override
    public PreparedStatement getCountStaffBansStatement(Connection connection, UUID staff) throws SQLException {
        PreparedStatement statement = initCountStaffBansSql(connection);

        statement.setString(1, notNull(staff));
        return statement;
    }

    @Override
    public PreparedStatement getCountStaffCommentsStatement(Connection connection, UUID staff) throws SQLException {
        PreparedStatement statement = initCountStaffCommentsSql(connection);

        statement.setString(1, notNull(staff));
        return statement;
    }

    @Override
    public PreparedStatement getCountStaffKicksStatement(Connection connection, UUID staff) throws SQLException {
        PreparedStatement statement = initCountStaffKicksSql(connection);

        statement.setString(1, notNull(staff));
        return statement;
    }

    @Override
    public PreparedStatement getCountStaffMutesStatement(Connection connection, UUID staff) throws SQLException {
        PreparedStatement statement = initCountStaffMutesSql(connection);

        statement.setString(1, notNull(staff));
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerRecordStatement(Connection connection, UUID player) throws SQLException {
        PreparedStatement statement = initRetrievePlayerRecordSql(connection);

        statement.setString(1, notNull(player));
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerByUsernameStatement(Connection connection, String username) throws SQLException {
        PreparedStatement statement = initRetrievePlayerByUsernameSql(connection);

        statement.setString(1, notNull(username));
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerBansStatement(Connection connection, UUID player, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerBansSql(connection, options);

        statement.setString(1, notNull(player));
        applyOptions(statement, optionsOrDefault(options), 2);
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerCommentsStatement(Connection connection, UUID player, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerCommentsSql(connection, options);

        statement.setString(1, notNull(player));
        applyOptions(statement, optionsOrDefault(options), 2);
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerKicksStatement(Connection connection, UUID player, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerKicksSql(connection, options);

        statement.setString(1, notNull(player));
        applyOptions(statement, optionsOrDefault(options), 2);
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerMutesStatement(Connection connection, UUID player, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerMutesSql(connection, options);

        statement.setString(1, notNull(player));
        applyOptions(statement, optionsOrDefault(options), 2);
        return statement;
    }

    @Override
    public PreparedStatement getRetrieveStaffBansStatement(Connection connection, UUID staff, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrieveStaffBansSql(connection, options);

        statement.setString(1, notNull(staff));
        applyOptions(statement, optionsOrDefault(options), 2);
        return statement;
    }

    @Override
    public PreparedStatement getRetrieveStaffCommentsStatement(Connection connection, UUID staff, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrieveStaffCommentsSql(connection, options);

        statement.setString(1, notNull(staff));
        applyOptions(statement, optionsOrDefault(options), 2);
        return statement;
    }

    @Override
    public PreparedStatement getRetrieveStaffKicksStatement(Connection connection, UUID staff, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrieveStaffKicksSql(connection, options);

        statement.setString(1, notNull(staff));
        applyOptions(statement, optionsOrDefault(options), 2);
        return statement;
    }

    @Override
    public PreparedStatement getRetrieveStaffMutesStatement(Connection connection, UUID staff, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrieveStaffMutesSql(connection, options);

        statement.setString(1, notNull(staff));
        applyOptions(statement, optionsOrDefault(options), 2);
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerBansByStatusStatement(Connection connection, UUID player, PunishmentStatus status, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerBansByStatusSql(connection, options);

        statement.setString(1, notNull(player));
        statement.setString(2, notNull(status));
        applyOptions(statement, optionsOrDefault(options), 3);
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerMutesByStatusStatement(Connection connection, UUID player, PunishmentStatus status, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerMutesByStatusSql(connection, options);

        statement.setString(1, notNull(player));
        statement.setString(2, notNull(status));
        applyOptions(statement, optionsOrDefault(options), 3);
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerCommentsByTypeStatement(Connection connection, UUID player, boolean isWarning, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerCommentsByTypeSql(connection, options);

        statement.setString(1, notNull(player));
        statement.setBoolean(2, isWarning);
        applyOptions(statement, optionsOrDefault(options), 3);
        return statement;
    }

    @Override
    public PreparedStatement getRetrieveIPBansStatement(Connection connection, String ipAddress, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrieveIPBansSql(connection, options);

        statement.setString(1, notNull(ipAddress));
        applyOptions(statement, options, 2);
        return statement;
    }

    @Override
    public PreparedStatement getRetrieveIPMutesStatement(Connection connection, String ipAddress, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrieveIPMutesSql(connection, options);

        statement.setString(1, notNull(ipAddress));
        applyOptions(statement, options, 2);
        return statement;
    }

    @Override
    public PreparedStatement getRetrieveIPBansByStatusStatement(Connection connection, String ipAddress, PunishmentStatus status, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrieveIPBansByStatusSql(connection, options);

        statement.setString(1, notNull(ipAddress));
        statement.setString(2, notNull(status));
        applyOptions(statement, options, 3);
        return statement;
    }

    @Override
    public PreparedStatement getRetrieveIPMutesByStatusStatement(Connection connection, String ipAddress, PunishmentStatus status, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrieveIPMutesByStatusSql(connection, options);

        statement.setString(1, notNull(ipAddress));
        statement.setString(2, notNull(status));
        applyOptions(statement, options, 3);
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerBansOrIPBansStatement(Connection connection, UUID player, String ipAddress, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerBansOrIPBansSql(connection, options);

        statement.setString(1, notNull(player));
        statement.setString(2, notNull(ipAddress));
        applyOptions(statement, options, 3);
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerMutesOrIPMutesStatement(Connection connection, UUID player, String ipAddress, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerMutesOrIPMutesSql(connection, options);

        statement.setString(1, notNull(player));
        statement.setString(2, notNull(ipAddress));
        applyOptions(statement, options, 3);
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerBansOrIPBansByStatusStatement(Connection connection, UUID player, String ipAddress, PunishmentStatus status, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerBansOrIPBansByStatusSql(connection, options);

        statement.setString(1, notNull(player));
        statement.setString(2, notNull(ipAddress));
        statement.setString(3, notNull(status));
        applyOptions(statement, options, 4);
        return statement;
    }

    @Override
    public PreparedStatement getRetrievePlayerMutesOrIPMutesByStatusStatement(Connection connection, UUID player, String ipAddress, PunishmentStatus status, LookupOptions options) throws SQLException {
        PreparedStatement statement = initRetrievePlayerMutesOrIPMutesByStatusSql(connection, options);

        statement.setString(1, notNull(player));
        statement.setString(2, notNull(ipAddress));
        statement.setString(3, notNull(status));
        applyOptions(statement, options, 4);
        return statement;
    }

    @Override
    public PreparedStatement getUpdatePlayerRecordStatement(Connection connection, PlayerRecordModifier recordModification) throws SQLException {
        PlayerRecord record = recordModification.getUpdatedRecord();
        if (recordModification.hasChanged()) {
            byte count = 0;
            if (recordModification.hasPlayerJoinStatsChanged()) count++;
            if (recordModification.hasPlayerLeaveStatsChanged()) count++;
            if (recordModification.hasPlayerDynamicStatsChanged()) count++;

            PreparedStatement statement;
            if (count > 1) {
                statement = initUpdatePlayerFullRecordSql(connection);
                statement.setString(1, notNull(record.getUsername()));
                statement.setTimestamp(2, Timestamp.valueOf(record.getLastJoin()));
                statement.setTimestamp(3, Timestamp.valueOf(record.getLastLeave()));
                statement.setString(4, record.getLastAddress());
                statement.setString(5, record.getLastServer());
                statement.setLong(6, record.getPlaytime());
                statement.setInt(7, record.getTotalJoins());
                statement.setString(8, notNull(record.getId()));

            } else if (recordModification.hasPlayerJoinStatsChanged()) {
                statement = initUpdatePlayerJoiningRecordSql(connection);
                statement.setString(1, notNull(record.getUsername()));
                statement.setTimestamp(2, Timestamp.valueOf(record.getLastJoin()));
                statement.setString(3, record.getLastAddress());
                statement.setInt(4, record.getTotalJoins());
                statement.setString(5, notNull(record.getId()));

            } else if (recordModification.hasPlayerLeaveStatsChanged()) {
                statement = initUpdatePlayerLeavingRecordSql(connection);
                statement.setTimestamp(1, Timestamp.valueOf(record.getLastLeave()));
                statement.setString(2, record.getLastServer());
                statement.setLong(3, record.getPlaytime());
                statement.setString(4, notNull(record.getId()));

            } else {
                statement = initUpdatePlayerDynamicRecordSql(connection);
                statement.setString(1, record.getLastServer());
                statement.setString(2, notNull(record.getId()));
            }
            return statement;
        }

        return getCreatePlayerRecordStatement(connection, record);
    }

    @Override
    public PreparedStatement getUpdatePlayerBanStatement(Connection connection, BanRecordModifier recordModification) throws SQLException {
        BanRecord record = recordModification.getUpdatedRecord();
        if (recordModification.hasChanged()) {
            PreparedStatement statement = initUpdatePlayerBanSql(connection);

            statement.setString(1, notNull(record.getStatus()));
            statement.setString(2, maybeNull(record.getTerminatingStaff()));
            statement.setTimestamp(3, record.getTerminationDate() == null ? null : Timestamp.valueOf(record.getTerminationDate()));
            statement.setString(4, maybeNull(record.getTerminationReason()));
            statement.setString(5, notNull(record.getId()));
            return statement;
        }

        return getCreateBanStatement(connection, record);
    }

    @Override
    public PreparedStatement getUpdatePlayerMuteStatement(Connection connection, MuteRecordModifier recordModification) throws SQLException {
        MuteRecord record = recordModification.getUpdatedRecord();
        if (recordModification.hasChanged()) {
            PreparedStatement statement = initUpdatePlayerMuteSql(connection);

            statement.setString(1, notNull(record.getStatus()));
            statement.setString(2, maybeNull(record.getTerminatingStaff()));
            statement.setTimestamp(3, record.getTerminationDate() == null ? null : Timestamp.valueOf(record.getTerminationDate()));
            statement.setString(4, maybeNull(record.getTerminationReason()));
            statement.setString(5, notNull(record.getId()));
            return statement;
        }

        return getCreateMuteStatement(connection, record);
    }

    protected String addDateFilter(String dateColumn, LookupOptions options) {
        if (options.hasDateBefore()) {
            if (options.hasDateAfter()) {
                return " AND " + dateColumn + " BETWEEN ? AND ?";
            }
            return " AND " + dateColumn + " < ?";
        }
        if (options.hasDateAfter()) {
            return " AND " + dateColumn + " > ?";
        }
        return "";
    }

    protected void applyOptions(PreparedStatement statement, LookupOptions options, int index) throws SQLException {
        if (options.hasDateAfter()) {
            statement.setTimestamp(index++, Timestamp.valueOf(options.getDateAfter()));
        }
        if (options.hasDateBefore()) {
            statement.setTimestamp(index++, Timestamp.valueOf(options.getDateBefore()));
        }

        statement.setInt(index++, options.getLimit());
        statement.setInt(index, options.getOffset());
    }

    protected LookupOptions optionsOrDefault(LookupOptions options) {
        return options == null ? DEFAULT_OPTIONS : options;
    }

    protected String notNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        return object.toString();
    }

    protected String maybeNull(Object object) {
        return object == null ? null : object.toString();
    }
}
