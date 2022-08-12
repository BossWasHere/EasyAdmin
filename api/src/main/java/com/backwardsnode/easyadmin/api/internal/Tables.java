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

package com.backwardsnode.easyadmin.api.internal;

/**
 * Holds the column names for tables in the database.
 */
public final class Tables {

    /**
     * Holds the column names for the 'players' table.
     */
    public interface PLAYERS {
        /**
         * Player UUID.
         */
        String UUID = "uuid";
        /**
         * Player username.
         */
        String USERNAME = "username";
        /**
         * Player's last IP address.
         */
        String LAST_IP = "lastIP";
        /**
         * Player's first join date.
         */
        String FIRST_JOIN = "firstJoin";
        /**
         * Player's last join date.
         */
        String LAST_JOIN = "lastJoin";
        /**
         * Player's last leave date.
         */
        String LAST_LEAVE = "lastLeave";
        /**
         * Player's last server.
         */
        String LAST_SERVER = "lastServer";
        /**
         * Player's total playtime.
         */
        String PLAYTIME = "playtime";
        /**
         * Player's total joins.
         */
        String TOTAL_JOINS = "totalJoins";
    }

    /**
     * Holds the column names for the 'bans' table.
     */
    public interface BANS {
        /**
         * Ban ID.
         */
        String ID = "id";
        /**
         * Ban status.
         */
        String STATUS = "status";
        /**
         * UUID of banned player.
         */
        String PLAYER_UUID = "playerUuid";
        /**
         * UUID of banning staff member.
         */
        String STAFF_UUID = "staffUuid";
        /**
         * UUID of unbanning staff member.
         */
        String UNBAN_STAFF_UUID = "unbanStaffUuid";
        /**
         * Ban IP address.
         */
        String PLAYER_IP = "playerIP";
        /**
         * Ban date.
         */
        String BAN_DATE = "banDate";
        /**
         * Unban date.
         */
        String UNBAN_DATE = "unbanDate";
        /**
         * Ban contexts.
         */
        String CONTEXTS = "contexts";
        /**
         * Ban reason.
         */
        String REASON = "reason";
        /**
         * Unban reason.
         */
        String UNBAN_REASON = "unbanReason";
    }

    /**
     * Holds the column names for the 'comments' table.
     */
    public interface COMMENTS {
        /**
         * Comment ID.
         */
        String ID = "id";
        /**
         * UUID of commented player.
         */
        String PLAYER_UUID = "playerUuid";
        /**
         * UUID of commenting staff member.
         */
        String STAFF_UUID = "staffUuid";
        /**
         * Comment date.
         */
        String DATE_ADDED = "dateAdded";
        /**
         * Comment warning type.
         */
        String IS_WARNING = "isWarning";
        /**
         * Comment message.
         */
        String COMMENT = "comment";
    }

    /**
     * Holds the column names for the 'kicks' table.
     */
    public interface KICKS {
        /**
         * Kick ID.
         */
        String ID = "id";
        /**
         * UUID of kicked player.
         */
        String PLAYER_UUID = "playerUuid";
        /**
         * UUID of kicking staff member.
         */
        String STAFF_UUID = "staffUuid";
        /**
         * Kick date.
         */
        String KICK_DATE = "kickDate";
        /**
         * Is global kick.
         */
        String IS_GLOBAL = "isGlobal";
        /**
         * Kick reason.
         */
        String REASON = "reason";
    }

    /**
     * Holds the column names for the 'mutes' table.
     */
    public interface MUTES {
        /**
         * Mute ID.
         */
        String ID = "id";
        /**
         * Mute status.
         */
        String STATUS = "status";
        /**
         * UUID of muted player.
         */
        String PLAYER_UUID = "playerUuid";
        /**
         * UUID of muting staff member.
         */
        String STAFF_UUID = "staffUuid";
        /**
         * UUID of unmuting staff member.
         */
        String UNMUTE_STAFF_UUID = "unmuteStaffUuid";
        /**
         * Mute IP address.
         */
        String PLAYER_IP = "playerIP";
        /**
         * Mute date.
         */
        String MUTE_DATE = "muteDate";
        /**
         * Unmute date.
         */
        String UNMUTE_DATE = "unmuteDate";
        /**
         * Mute contexts.
         */
        String CONTEXTS = "contexts";
        /**
         * Mute reason.
         */
        String REASON = "reason";
        /**
         * Unmute reason.
         */
        String UNMUTE_REASON = "unmuteReason";
    }
}
