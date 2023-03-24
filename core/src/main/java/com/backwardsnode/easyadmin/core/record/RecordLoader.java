/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

package com.backwardsnode.easyadmin.core.record;

import com.backwardsnode.easyadmin.api.data.PunishmentStatus;
import com.backwardsnode.easyadmin.api.record.*;
import org.jetbrains.annotations.ApiStatus;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import static com.backwardsnode.easyadmin.api.internal.Tables.*;

/**
 * Class for loading records from an open result set. This is internal API and should not be used by plugins.
 */
@ApiStatus.Internal
public final class RecordLoader {

    /**
     * Loads a {@link PlayerRecord}.
     * @param result the result set to load from.
     * @return the loaded player record.
     * @throws SQLException if an error occured while loading the record.
     */
    public static PlayerRecord loadPlayerRecord(ResultSet result) throws SQLException {
        return new PlayerRecordImpl(true,
                UUID.fromString(result.getString(PLAYERS.UUID)),
                result.getString(PLAYERS.USERNAME),
                result.getTimestamp(PLAYERS.FIRST_JOIN).toLocalDateTime(),
                result.getTimestamp(PLAYERS.LAST_JOIN).toLocalDateTime(),
                result.getTimestamp(PLAYERS.LAST_LEAVE).toLocalDateTime(),
                result.getLong(PLAYERS.PLAYTIME),
                result.getInt(PLAYERS.TOTAL_JOINS),
                result.getString(PLAYERS.LAST_SERVER),
                result.getString(PLAYERS.LAST_IP));
    }

    /**
     * Loads a {@link BanRecord}.
     * @param result the result set to load from.
     * @return the loaded ban record.
     * @throws SQLException if an error occured while loading the record.
     */
    public static BanRecord loadBanRecord(ResultSet result) throws SQLException {
        return new BanRecordImpl(true,
                result.getInt(BANS.ID),
                PunishmentStatus.fromString(result.getString(BANS.STATUS)),
                UUID.fromString(result.getString(BANS.PLAYER_UUID)),
                nullableUUID(result.getString(BANS.STAFF_UUID)),
                nullableUUID(result.getString(BANS.UNBAN_STAFF_UUID)),
                result.getTimestamp(BANS.BAN_DATE).toLocalDateTime(),
                result.getTimestamp(BANS.UNBAN_DATE).toLocalDateTime(),
                result.getString(BANS.PLAYER_IP),
                result.getString(BANS.CONTEXTS),
                result.getString(BANS.REASON),
                result.getString(BANS.UNBAN_REASON));

    }

    /**
     * Loads a {@link CommentRecord}.
     * @param result the result set to load from.
     * @return the loaded comment record.
     * @throws SQLException if an error occured while loading the record.
     */
    public static CommentRecord loadCommentRecord(ResultSet result) throws SQLException {
        return new CommentRecordImpl(true,
                result.getInt(COMMENTS.ID),
                UUID.fromString(result.getString(COMMENTS.PLAYER_UUID)),
                nullableUUID(result.getString(COMMENTS.STAFF_UUID)),
                result.getTimestamp(COMMENTS.DATE_ADDED).toLocalDateTime(),
                result.getBoolean(COMMENTS.IS_WARNING),
                result.getString(COMMENTS.COMMENT));
    }

    /**
     * Loads a {@link KickRecord}.
     * @param result the result set to load from.
     * @return the loaded kick record.
     * @throws SQLException if an error occured while loading the record.
     */
    public static KickRecord loadKickRecord(ResultSet result) throws SQLException {
        return new KickRecordImpl(true,
                result.getInt(KICKS.ID),
                UUID.fromString(result.getString(KICKS.PLAYER_UUID)),
                nullableUUID(result.getString(KICKS.STAFF_UUID)),
                result.getTimestamp(KICKS.KICK_DATE).toLocalDateTime(),
                result.getBoolean(KICKS.IS_GLOBAL),
                result.getString(KICKS.REASON));
    }

    /**
     * Loads a {@link MuteRecord}.
     * @param result the result set to load from.
     * @return the loaded mute record.
     * @throws SQLException if an error occured while loading the record.
     */
    public static MuteRecord loadMuteRecord(ResultSet result) throws SQLException {
        return new MuteRecordImpl(true,
                result.getInt(MUTES.ID),
                PunishmentStatus.fromString(result.getString(MUTES.STATUS)),
                UUID.fromString(result.getString(MUTES.PLAYER_UUID)),
                nullableUUID(result.getString(MUTES.STAFF_UUID)),
                nullableUUID(result.getString(MUTES.UNMUTE_STAFF_UUID)),
                result.getTimestamp(MUTES.MUTE_DATE).toLocalDateTime(),
                result.getTimestamp(MUTES.UNMUTE_DATE).toLocalDateTime(),
                result.getString(MUTES.PLAYER_IP),
                result.getString(MUTES.CONTEXTS),
                result.getString(MUTES.REASON),
                result.getString(MUTES.UNMUTE_REASON));
    }

    /**
     * Gets the integer value from the "count" column. Used in count queries.
     * @param result the result set to load from.
     * @return the count value.
     * @throws SQLException if an error occured while reading the result.
     */
    public static int getCountColumn(ResultSet result) throws SQLException {
        return result.getInt("count");
    }

    private static UUID nullableUUID(String uuidString) {
        return uuidString == null ? null : UUID.fromString(uuidString);
    }
}
