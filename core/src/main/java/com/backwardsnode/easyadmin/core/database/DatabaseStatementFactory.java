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
import java.util.UUID;

public interface DatabaseStatementFactory {

    String getSchemaInitScriptName();

    PreparedStatement getCreatePlayerRecordStatement(Connection connection, PlayerRecord record) throws SQLException;

    PreparedStatement getCreateBanStatement(Connection connection, BanRecord record) throws SQLException;

    PreparedStatement getCreateCommentStatement(Connection connection, CommentRecord record) throws SQLException;

    PreparedStatement getCreateKickStatement(Connection connection, KickRecord record) throws SQLException;

    PreparedStatement getCreateMuteStatement(Connection connection, MuteRecord record) throws SQLException;

    PreparedStatement getCountPlayerBansStatement(Connection connection, UUID player) throws SQLException;

    PreparedStatement getCountPlayerCommentsStatement(Connection connection, UUID player) throws SQLException;

    PreparedStatement getCountPlayerKicksStatement(Connection connection, UUID player) throws SQLException;

    PreparedStatement getCountPlayerMutesStatement(Connection connection, UUID player) throws SQLException;

    PreparedStatement getCountStaffBansStatement(Connection connection, UUID staff) throws SQLException;

    PreparedStatement getCountStaffCommentsStatement(Connection connection, UUID staff) throws SQLException;

    PreparedStatement getCountStaffKicksStatement(Connection connection, UUID staff) throws SQLException;

    PreparedStatement getCountStaffMutesStatement(Connection connection, UUID staff) throws SQLException;

    PreparedStatement getRetrievePlayerRecordStatement(Connection connection, UUID player) throws SQLException;

    PreparedStatement getRetrievePlayerByUsernameStatement(Connection connection, String username) throws SQLException;

    PreparedStatement getRetrievePlayerBansStatement(Connection connection, UUID player, LookupOptions options) throws SQLException;

    PreparedStatement getRetrievePlayerCommentsStatement(Connection connection, UUID player, LookupOptions options) throws SQLException;

    PreparedStatement getRetrievePlayerKicksStatement(Connection connection, UUID player, LookupOptions options) throws SQLException;

    PreparedStatement getRetrievePlayerMutesStatement(Connection connection, UUID player, LookupOptions options) throws SQLException;

    PreparedStatement getRetrieveStaffBansStatement(Connection connection, UUID staff, LookupOptions options) throws SQLException;

    PreparedStatement getRetrieveStaffCommentsStatement(Connection connection, UUID staff, LookupOptions options) throws SQLException;

    PreparedStatement getRetrieveStaffKicksStatement(Connection connection, UUID staff, LookupOptions options) throws SQLException;

    PreparedStatement getRetrieveStaffMutesStatement(Connection connection, UUID staff, LookupOptions options) throws SQLException;

    PreparedStatement getRetrievePlayerBansByStatusStatement(Connection connection, UUID player, PunishmentStatus status, LookupOptions options) throws SQLException;

    PreparedStatement getRetrievePlayerMutesByStatusStatement(Connection connection, UUID player, PunishmentStatus status, LookupOptions options) throws SQLException;

    PreparedStatement getRetrievePlayerCommentsByTypeStatement(Connection connection, UUID player, boolean isWarning, LookupOptions options) throws SQLException;

    PreparedStatement getRetrieveIPBansStatement(Connection connection, String ipAddress, LookupOptions options) throws SQLException;

    PreparedStatement getRetrieveIPMutesStatement(Connection connection, String ipAddress, LookupOptions options) throws SQLException;

    PreparedStatement getRetrieveIPBansByStatusStatement(Connection connection, String ipAddress, PunishmentStatus status, LookupOptions options) throws SQLException;

    PreparedStatement getRetrieveIPMutesByStatusStatement(Connection connection, String ipAddress, PunishmentStatus status, LookupOptions options) throws SQLException;

    PreparedStatement getRetrievePlayerBansOrIPBansStatement(Connection connection, UUID player, String ipAddress, LookupOptions options) throws SQLException;

    PreparedStatement getRetrievePlayerMutesOrIPMutesStatement(Connection connection, UUID player, String ipAddress, LookupOptions options) throws SQLException;

    PreparedStatement getRetrievePlayerBansOrIPBansByStatusStatement(Connection connection, UUID player, String ipAddress, PunishmentStatus status, LookupOptions options) throws SQLException;

    PreparedStatement getRetrievePlayerMutesOrIPMutesByStatusStatement(Connection connection, UUID player, String ipAddress, PunishmentStatus status, LookupOptions options) throws SQLException;

    PreparedStatement getUpdatePlayerRecordStatement(Connection connection, PlayerRecordModifier recordModification) throws SQLException;

    PreparedStatement getUpdatePlayerBanStatement(Connection connection, BanRecordModifier recordModification) throws SQLException;

    PreparedStatement getUpdatePlayerMuteStatement(Connection connection, MuteRecordModifier recordModification) throws SQLException;

}
