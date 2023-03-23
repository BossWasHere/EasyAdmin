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

package com.backwardsnode.easyadmin.core.i18n;

public final class CommonMessages {

    public static final class ADMINISTRATIVE {

        public static final class BAN {

            public static final MessageKey BAN_DESC = new MessageKey("admin.ban.description", false);
            public static final MessageKey BAN_USAGE = new MessageKey("admin.ban.usage", false);
            public static final MessageKey BANNED = new MessageKey("admin.ban.default");
            public static final MessageKey BANNED_REASON = new MessageKey("admin.ban.reason");
            public static final MessageKey ALREADY_BANNED = new MessageKey("admin.ban.already");
            public static final MessageKey ALREADY_BANNED_REASON = new MessageKey("admin.ban.alreadyReason");

            public static final MessageKey BAN_ALL_DESC = new MessageKey("admin.banAll.description", false);
            public static final MessageKey BAN_ALL_USAGE = new MessageKey("admin.banAll.usage", false);
            public static final MessageKey BANNED_ALL = new MessageKey("admin.banAll.default");
            public static final MessageKey BANNED_ALL_REASON = new MessageKey("admin.banAll.reason");
            public static final MessageKey ALREADY_BANNED_ALL = new MessageKey("admin.banAll.already");
            public static final MessageKey ALREADY_BANNED_ALL_REASON = new MessageKey("admin.banAll.alreadyReason");

            public static final MessageKey BAN_IP_DESC = new MessageKey("admin.banIP.description", false);
            public static final MessageKey BAN_IP_USAGE = new MessageKey("admin.banIP.usage", false);
            public static final MessageKey BANNED_IP = new MessageKey("admin.banIP.default");
            public static final MessageKey BANNED_IP_REASON = new MessageKey("admin.banIP.reason");
            public static final MessageKey ALREADY_BANNED_IP = new MessageKey("admin.banIP.already");
            public static final MessageKey ALREADY_BANNED_IP_REASON = new MessageKey("admin.banIP.alreadyReason");

            public static final MessageKey BAN_IP_ALL_DESC = new MessageKey("admin.banIPAll.description", false);
            public static final MessageKey BAN_IP_ALL_USAGE = new MessageKey("admin.banIPAll.usage", false);
            public static final MessageKey BANNED_IP_ALL = new MessageKey("admin.banIPAll.default");
            public static final MessageKey BANNED_IP_ALL_REASON = new MessageKey("admin.banIPAll.reason");
            public static final MessageKey ALREADY_BANNED_IP_ALL = new MessageKey("admin.banIPAll.already");
            public static final MessageKey ALREADY_BANNED_IP_ALL_REASON = new MessageKey("admin.banIPAll.alreadyReason");

            public static final MessageKey TEMPBAN_DESC = new MessageKey("admin.tempBan.description", false);
            public static final MessageKey TEMPBAN_USAGE = new MessageKey("admin.tempBan.usage", false);
            public static final MessageKey TEMPBANNED = new MessageKey("admin.tempBan.default");
            public static final MessageKey TEMPBANNED_REASON = new MessageKey("admin.tempBan.reason");
            public static final MessageKey ALREADY_TEMPBANNED = new MessageKey("admin.tempBan.already");
            public static final MessageKey ALREADY_TEMPBANNED_REASON = new MessageKey("admin.tempBan.alreadyReason");

            public static final MessageKey TEMPBAN_ALL_DESC = new MessageKey("admin.tempBanAll.description", false);
            public static final MessageKey TEMPBAN_ALL_USAGE = new MessageKey("admin.tempBanAll.usage", false);
            public static final MessageKey TEMPBANNED_ALL = new MessageKey("admin.tempBanAll.default");
            public static final MessageKey TEMPBANNED_ALL_REASON = new MessageKey("admin.tempBanAll.reason");
            public static final MessageKey ALREADY_TEMPBANNED_ALL = new MessageKey("admin.tempBanAll.already");
            public static final MessageKey ALREADY_TEMPBANNED_ALL_REASON = new MessageKey("admin.tempBanAll.alreadyReason");

            public static final MessageKey TEMPBAN_IP_DESC = new MessageKey("admin.tempBanIP.description", false);
            public static final MessageKey TEMPBAN_IP_USAGE = new MessageKey("admin.tempBanIP.usage", false);
            public static final MessageKey TEMPBANNED_IP = new MessageKey("admin.tempBanIP.default");
            public static final MessageKey TEMPBANNED_IP_REASON = new MessageKey("admin.tempBanIP.reason");
            public static final MessageKey ALREADY_TEMPBANNED_IP = new MessageKey("admin.tempBanIP.already");
            public static final MessageKey ALREADY_TEMPBANNED_IP_REASON = new MessageKey("admin.tempBanIP.alreadyReason");

            public static final MessageKey TEMPBAN_IP_ALL_DESC = new MessageKey("admin.tempBanIPAll.description", false);
            public static final MessageKey TEMPBAN_IP_ALL_USAGE = new MessageKey("admin.tempBanIPAll.usage", false);
            public static final MessageKey TEMPBANNED_IP_ALL = new MessageKey("admin.tempBanIPAll.default");
            public static final MessageKey TEMPBANNED_IP_ALL_REASON = new MessageKey("admin.tempBanIPAll.reason");
            public static final MessageKey ALREADY_TEMPBANNED_IP_ALL = new MessageKey("admin.tempBanIPAll.already");
            public static final MessageKey ALREADY_TEMPBANNED_IP_ALL_REASON = new MessageKey("admin.tempBanIPAll.alreadyReason");

            public static final MessageKey UNBAN_DESC = new MessageKey("admin.unban.description", false);
            public static final MessageKey UNBAN_USAGE = new MessageKey("admin.unban.usage", false);
            public static final MessageKey UNBANNED = new MessageKey("admin.unban.default");
            public static final MessageKey UNBANNED_REASON = new MessageKey("admin.unban.reason");
            public static final MessageKey NOT_BANNED = new MessageKey("admin.unban.isNot");
            public static final MessageKey IS_BANNED_GLOBALLY = new MessageKey("admin.unban.isGlobally");

            public static final MessageKey UNBAN_ALL_DESC = new MessageKey("admin.unbanAll.description", false);
            public static final MessageKey UNBAN_ALL_USAGE = new MessageKey("admin.unbanAll.usage", false);
            public static final MessageKey UNBANNED_ALL = new MessageKey("admin.unbanAll.default");
            public static final MessageKey UNBANNED_ALL_REASON = new MessageKey("admin.unbanAll.reason");
            public static final MessageKey NOT_BANNED_ALL = new MessageKey("admin.unbanAll.isNot");

            public static final MessageKey UNBAN_IP_DESC = new MessageKey("admin.unbanIP.description", false);
            public static final MessageKey UNBAN_IP_USAGE = new MessageKey("admin.unbanIP.usage", false);
            public static final MessageKey UNBANNED_IP = new MessageKey("admin.unbanIP.default");
            public static final MessageKey UNBANNED_IP_REASON = new MessageKey("admin.unbanIP.reason");
            public static final MessageKey NOT_BANNED_IP = new MessageKey("admin.unbanIP.isNot");
            public static final MessageKey IP_IS_BANNED_GLOBALLY = new MessageKey("admin.unbanIP.isGlobally");

            public static final MessageKey UNBAN_IP_ALL_DESC = new MessageKey("admin.unbanIPAll.description", false);
            public static final MessageKey UNBAN_IP_ALL_USAGE = new MessageKey("admin.unbanIPAll.usage", false);
            public static final MessageKey UNBANNED_IP_ALL = new MessageKey("admin.unbanIPAll.default");
            public static final MessageKey UNBANNED_IP_ALL_REASON = new MessageKey("admin.unbanIPAll.reason");
            public static final MessageKey NOT_BANNED_IP_ALL = new MessageKey("admin.unbanIPAll.isNot");

        }

        public static final class KICK {

            public static final MessageKey KICK_DESC = new MessageKey("admin.kick.description", false);
            public static final MessageKey KICK_USAGE = new MessageKey("admin.kick.usage", false);
            public static final MessageKey KICKED = new MessageKey("admin.kick.default");
            public static final MessageKey KICKED_REASON = new MessageKey("admin.kick.reason");

            public static final MessageKey KICK_ALL_DESC = new MessageKey("admin.kickAll.description", false);
            public static final MessageKey KICK_ALL_USAGE = new MessageKey("admin.kickAll.usage", false);
            public static final MessageKey KICKED_ALL = new MessageKey("admin.kickAll.default");
            public static final MessageKey KICKED_ALL_REASON = new MessageKey("admin.kickAll.reason");

        }

        public static final class LOOKUP {

            public static final MessageKey DESC = new MessageKey("admin.lookup.description", false);
            public static final MessageKey USAGE = new MessageKey("admin.lookup.usage", false);
            public static final MessageKey TITLE = new MessageKey("admin.lookup.title");
            public static final MessageKey BANS = new MessageKey("admin.lookup.bans");
            public static final MessageKey KICKS = new MessageKey("admin.lookup.kicks");
            public static final MessageKey MUTES = new MessageKey("admin.lookup.mutes");
            public static final MessageKey COMMENTS = new MessageKey("admin.lookup.comments");
            public static final MessageKey WARNINGS = new MessageKey("admin.lookup.warnings");
            public static final MessageKey DESCRIPTION_FIELD = new MessageKey("admin.lookup.descriptionField");
            public static final MessageKey DESCRIPTION_FIELD_ALL = new MessageKey("admin.lookup.descriptionFieldAll");

        }

        public static final class MUTE {

            public static final MessageKey MUTE_DESC = new MessageKey("admin.mute.description", false);
            public static final MessageKey MUTE_USAGE = new MessageKey("admin.mute.usage", false);
            public static final MessageKey MUTED = new MessageKey("admin.mute.default");
            public static final MessageKey MUTED_REASON = new MessageKey("admin.mute.reason");
            public static final MessageKey ALREADY_MUTED = new MessageKey("admin.mute.already");
            public static final MessageKey ALREADY_MUTED_REASON = new MessageKey("admin.mute.alreadyReason");

            public static final MessageKey MUTE_ALL_DESC = new MessageKey("admin.muteAll.description", false);
            public static final MessageKey MUTE_ALL_USAGE = new MessageKey("admin.muteAll.usage", false);
            public static final MessageKey MUTED_ALL = new MessageKey("admin.muteAll.default");
            public static final MessageKey MUTED_ALL_REASON = new MessageKey("admin.muteAll.reason");
            public static final MessageKey ALREADY_MUTED_ALL = new MessageKey("admin.muteAll.already");
            public static final MessageKey ALREADY_MUTED_ALL_REASON = new MessageKey("admin.muteAll.alreadyReason");

            public static final MessageKey MUTE_IP_DESC = new MessageKey("admin.muteIP.description", false);
            public static final MessageKey MUTE_IP_USAGE = new MessageKey("admin.muteIP.usage", false);
            public static final MessageKey MUTED_IP = new MessageKey("admin.muteIP.default");
            public static final MessageKey MUTED_IP_REASON = new MessageKey("admin.muteIP.reason");
            public static final MessageKey ALREADY_MUTED_IP = new MessageKey("admin.muteIP.already");
            public static final MessageKey ALREADY_MUTED_IP_REASON = new MessageKey("admin.muteIP.alreadyReason");

            public static final MessageKey MUTE_IP_ALL_DESC = new MessageKey("admin.muteIPAll.description", false);
            public static final MessageKey MUTE_IP_ALL_USAGE = new MessageKey("admin.muteIPAll.usage", false);
            public static final MessageKey MUTED_IP_ALL = new MessageKey("admin.muteIPAll.default");
            public static final MessageKey MUTED_IP_ALL_REASON = new MessageKey("admin.muteIPAll.reason");
            public static final MessageKey ALREADY_MUTED_IP_ALL = new MessageKey("admin.muteIPAll.already");
            public static final MessageKey ALREADY_MUTED_IP_ALL_REASON = new MessageKey("admin.muteIPAll.alreadyReason");

            public static final MessageKey TEMPMUTE_DESC = new MessageKey("admin.tempMute.description", false);
            public static final MessageKey TEMPMUTE_USAGE = new MessageKey("admin.tempMute.usage", false);
            public static final MessageKey TEMPMUTED = new MessageKey("admin.tempMute.default");
            public static final MessageKey TEMPMUTED_REASON = new MessageKey("admin.tempMute.reason");
            public static final MessageKey ALREADY_TEMPMUTED = new MessageKey("admin.tempMute.already");
            public static final MessageKey ALREADY_TEMPMUTED_REASON = new MessageKey("admin.tempMute.alreadyReason");

            public static final MessageKey TEMPMUTE_ALL_DESC = new MessageKey("admin.tempMuteAll.description", false);
            public static final MessageKey TEMPMUTE_ALL_USAGE = new MessageKey("admin.tempMuteAll.usage", false);
            public static final MessageKey TEMPMUTED_ALL = new MessageKey("admin.tempMuteAll.default");
            public static final MessageKey TEMPMUTED_ALL_REASON = new MessageKey("admin.tempMuteAll.reason");
            public static final MessageKey ALREADY_TEMPMUTED_ALL = new MessageKey("admin.tempMuteAll.already");
            public static final MessageKey ALREADY_TEMPMUTED_ALL_REASON = new MessageKey("admin.tempMuteAll.alreadyReason");

            public static final MessageKey TEMPMUTE_IP_DESC = new MessageKey("admin.tempMuteIP.description", false);
            public static final MessageKey TEMPMUTE_IP_USAGE = new MessageKey("admin.tempMuteIP.usage", false);
            public static final MessageKey TEMPMUTED_IP = new MessageKey("admin.tempMuteIP.default");
            public static final MessageKey TEMPMUTED_IP_REASON = new MessageKey("admin.tempMuteIP.reason");
            public static final MessageKey ALREADY_TEMPMUTED_IP = new MessageKey("admin.tempMuteIP.already");
            public static final MessageKey ALREADY_TEMPMUTED_IP_REASON = new MessageKey("admin.tempMuteIP.alreadyReason");

            public static final MessageKey TEMPMUTE_IP_ALL_DESC = new MessageKey("admin.tempMuteIPAll.description", false);
            public static final MessageKey TEMPMUTE_IP_ALL_USAGE = new MessageKey("admin.tempMuteIPAll.usage", false);
            public static final MessageKey TEMPMUTED_IP_ALL = new MessageKey("admin.tempMuteIPAll.default");
            public static final MessageKey TEMPMUTED_IP_ALL_REASON = new MessageKey("admin.tempMuteIPAll.reason");
            public static final MessageKey ALREADY_TEMPMUTED_IP_ALL = new MessageKey("admin.tempMuteIPAll.already");
            public static final MessageKey ALREADY_TEMPMUTED_IP_ALL_REASON = new MessageKey("admin.tempMuteIPAll.alreadyReason");

            public static final MessageKey UNMUTE_DESC = new MessageKey("admin.unmute.description", false);
            public static final MessageKey UNMUTE_USAGE = new MessageKey("admin.unmute.usage", false);
            public static final MessageKey UNMUTED = new MessageKey("admin.unmute.default");
            public static final MessageKey UNMUTED_REASON = new MessageKey("admin.unmute.reason");
            public static final MessageKey NOT_MUTED = new MessageKey("admin.unmute.isNot");
            public static final MessageKey IS_MUTED_GLOBALLY = new MessageKey("admin.unmute.isGlobally");

            public static final MessageKey UNMUTE_ALL_DESC = new MessageKey("admin.unmuteAll.description", false);
            public static final MessageKey UNMUTE_ALL_USAGE = new MessageKey("admin.unmuteAll.usage", false);
            public static final MessageKey UNMUTED_ALL = new MessageKey("admin.unmuteAll.default");
            public static final MessageKey UNMUTED_ALL_REASON = new MessageKey("admin.unmuteAll.reason");
            public static final MessageKey NOT_MUTED_ALL = new MessageKey("admin.unmuteAll.isNot");

            public static final MessageKey UNMUTE_IP_DESC = new MessageKey("admin.unmuteIP.description", false);
            public static final MessageKey UNMUTE_IP_USAGE = new MessageKey("admin.unmuteIP.usage", false);
            public static final MessageKey UNMUTED_IP = new MessageKey("admin.unmuteIP.default");
            public static final MessageKey UNMUTED_IP_REASON = new MessageKey("admin.unmuteIP.reason");
            public static final MessageKey NOT_MUTED_IP = new MessageKey("admin.unmuteIP.isNot");
            public static final MessageKey IP_IS_MUTED_GLOBALLY = new MessageKey("admin.unmuteIP.isGlobally");

            public static final MessageKey UNMUTE_IP_ALL_DESC = new MessageKey("admin.unmuteIPAll.description", false);
            public static final MessageKey UNMUTE_IP_ALL_USAGE = new MessageKey("admin.unmuteIPAll.usage", false);
            public static final MessageKey UNMUTED_IP_ALL = new MessageKey("admin.unmuteIPAll.default");
            public static final MessageKey UNMUTED_IP_ALL_REASON = new MessageKey("admin.unmuteIPAll.reason");
            public static final MessageKey NOT_MUTED_IP_ALL = new MessageKey("admin.unmuteIPAll.isNot");

        }

        public static final class STAFF_LOOKUP {

            public static final MessageKey DESC = new MessageKey("admin.staffLookup.description", false);
            public static final MessageKey USAGE = new MessageKey("admin.staffLookup.usage", false);
            public static final MessageKey TITLE = new MessageKey("admin.staffLookup.title");
            public static final MessageKey ACTIONS = new MessageKey("admin.staffLookup.actions");
            public static final MessageKey BANS = new MessageKey("admin.staffLookup.bans");
            public static final MessageKey KICKS = new MessageKey("admin.staffLookup.kicks");
            public static final MessageKey MUTES = new MessageKey("admin.staffLookup.mutes");
            public static final MessageKey COMMENTS = new MessageKey("admin.staffLookup.comments");
            public static final MessageKey WARNINGS = new MessageKey("admin.staffLookup.warnings");

        }

        public static final class COMMENT {

            public static final MessageKey DESC = new MessageKey("admin.comment.description", false);
            public static final MessageKey USAGE = new MessageKey("admin.comment.usage", false);
            public static final MessageKey COMMENTED = new MessageKey("admin.comment.default");

        }

        public static final class WARNING {

            public static final MessageKey DESC = new MessageKey("admin.warn.description", false);
            public static final MessageKey USAGE = new MessageKey("admin.warn.usage", false);
            public static final MessageKey WARNED = new MessageKey("admin.warn.default");

        }

        public static final MessageKey MUST_SPECIFY_SERVER = new MessageKey("admin.mustSpecifyServer");
        public static final MessageKey PLAYER_OFFLINE = new MessageKey("admin.playerOffline");
        public static final MessageKey PLAYER_NONEXISTENT = new MessageKey("admin.playerNonExistent");
        public static final MessageKey PLAYER_IMMUNE = new MessageKey("admin.playerImmune");

    }

    public static final class CHAT {

        public static final MessageKey MESSAGE_WARNING = new MessageKey("chat.messageWarning");

    }

    public static final class DATABASE {

        public static final MessageKey GENERIC_ERROR = new MessageKey("database.genericError", false);
        public static final MessageKey FS_ERROR = new MessageKey("database.fsError", false);
        public static final MessageKey NO_PROTOCOL = new MessageKey("database.noProtocol", false);
        public static final MessageKey BAD_CREDENTIALS = new MessageKey("database.badCredentials", false);
        public static final MessageKey CREATED = new MessageKey("database.created", false);
        public static final MessageKey UNSUPPORTED_TYPE = new MessageKey("database.unsupportedType", false);

    }

    public static final class EASYADMIN {

        public static final MessageKey PREFIX = new MessageKey("easyadmin.prefix", false);
        public static final MessageKey DESCRIPTION = new MessageKey("easyadmin.description", false);
        public static final MessageKey UPDATE_FOUND = new MessageKey("easyadmin.updateFound");
        public static final MessageKey STAFF = new MessageKey("easyadmin.staff", false);
        public static final MessageKey HELP =  new MessageKey("easyadmin.updateFound");
        public static final MessageKey HELP_DESCRIPTION = new MessageKey("easyadmin.helpDescription", false);

    }

    public static final class PLAYER {

        public static final class BAN {

            public static final MessageKey BAN_TITLE = new MessageKey("player.banTitle", false);

            public static final MessageKey BAN_RECONNECTED = new MessageKey("player.ban.reconnected");
            public static final MessageKey BAN_RECONNECTED_REASON = new MessageKey("player.ban.reconnectedReason");
            public static final MessageKey BAN_DISCONNECTED = new MessageKey("player.ban.disconnected");
            public static final MessageKey BAN_DISCONNECTED_REASON = new MessageKey("player.ban.disconnectedReason");

            public static final MessageKey BAN_ALL = new MessageKey("player.banAll.default");
            public static final MessageKey BAN_ALL_REASON = new MessageKey("player.banAll.reason");

            public static final MessageKey BAN_IP_RECONNECTED = new MessageKey("player.banIP.reconnected");
            public static final MessageKey BAN_IP_RECONNECTED_REASON = new MessageKey("player.banIP.reconnectedReason");
            public static final MessageKey BAN_IP_DISCONNECTED = new MessageKey("player.banIP.disconnected");
            public static final MessageKey BAN_IP_DISCONNECTED_REASON = new MessageKey("player.banIP.disconnectedReason");

            public static final MessageKey BAN_IP_ALL = new MessageKey("player.banIPAll.default");
            public static final MessageKey BAN_IP_ALL_REASON = new MessageKey("player.banIPAll.reason");

            public static final MessageKey TEMPBAN_RECONNECTED = new MessageKey("player.tempBan.reconnected");
            public static final MessageKey TEMPBAN_RECONNECTED_REASON = new MessageKey("player.tempBan.reconnectedReason");
            public static final MessageKey TEMPBAN_DISCONNECTED = new MessageKey("player.tempBan.disconnected");
            public static final MessageKey TEMPBAN_DISCONNECTED_REASON = new MessageKey("player.tempBan.disconnectedReason");

            public static final MessageKey TEMPBAN_ALL = new MessageKey("player.tempBanAll.default");
            public static final MessageKey TEMPBAN_ALL_REASON = new MessageKey("player.tempBanAll.reason");

            public static final MessageKey TEMPBAN_IP_RECONNECTED = new MessageKey("player.tempBanIP.reconnected");
            public static final MessageKey TEMPBAN_IP_RECONNECTED_REASON = new MessageKey("player.tempBanIP.reconnectedReason");
            public static final MessageKey TEMPBAN_IP_DISCONNECTED = new MessageKey("player.tempBanIP.disconnected");
            public static final MessageKey TEMPBAN_IP_DISCONNECTED_REASON = new MessageKey("player.tempBanIP.disconnectedReason");

            public static final MessageKey TEMPBAN_IP_ALL = new MessageKey("player.tempBanIPAll.default");
            public static final MessageKey TEMPBAN_IP_ALL_REASON = new MessageKey("player.tempBanIPAll.reason");

            public static final MessageKey UNBANNED = new MessageKey("player.unbanned");
            public static final MessageKey UNBANNED_ALL = new MessageKey("player.unbannedAll");

        }

        public static final class KICK {

            public static final MessageKey KICK_RECONNECTED = new MessageKey("player.kick.reconnected");
            public static final MessageKey KICK_RECONNECTED_REASON = new MessageKey("player.kick.reconnectedReason");
            public static final MessageKey KICK_DISCONNECTED = new MessageKey("player.kick.disconnected");
            public static final MessageKey KICK_DISCONNECTED_REASON = new MessageKey("player.kick.disconnectedReason");

            public static final MessageKey KICK_ALL = new MessageKey("player.kickAll.default");
            public static final MessageKey KICK_ALL_REASON = new MessageKey("player.kickAll.reason");

        }

        public static final class MUTE {

            public static final MessageKey MUTE_TITLE = new MessageKey("player.muteTitle");

            public static final MessageKey MUTE = new MessageKey("player.mute.default");
            public static final MessageKey MUTE_REASON = new MessageKey("player.mute.reason");

            public static final MessageKey MUTE_ALL = new MessageKey("player.muteAll.default");
            public static final MessageKey MUTE_ALL_REASON = new MessageKey("player.muteAll.reason");

            public static final MessageKey MUTE_IP = new MessageKey("player.muteIP.default");
            public static final MessageKey MUTE_IP_REASON = new MessageKey("player.muteIP.reason");

            public static final MessageKey MUTE_IP_ALL = new MessageKey("player.muteIPAll.default");
            public static final MessageKey MUTE_IP_ALL_REASON = new MessageKey("player.muteIPAll.reason");

            public static final MessageKey TEMPMUTE = new MessageKey("player.tempMute.default");
            public static final MessageKey TEMPMUTE_REASON = new MessageKey("player.tempMute.reason");

            public static final MessageKey TEMPMUTE_ALL = new MessageKey("player.tempMuteAll.default");
            public static final MessageKey TEMPMUTE_ALL_REASON = new MessageKey("player.tempMuteAll.reason");

            public static final MessageKey TEMPMUTE_IP = new MessageKey("player.tempMuteIP.default");
            public static final MessageKey TEMPMUTE_IP_REASON = new MessageKey("player.tempMuteIP.reason");

            public static final MessageKey TEMPMUTE_IP_ALL = new MessageKey("player.tempMuteIPAll.default");
            public static final MessageKey TEMPMUTE_IP_ALL_REASON = new MessageKey("player.tempMuteIPAll.reason");

            public static final MessageKey UNMUTED = new MessageKey("player.unmuted");
            public static final MessageKey UNMUTED_ALL = new MessageKey("player.unmutedAll");

        }

        public static final MessageKey WARNING = new MessageKey("admin.playerWarn");

    }

    public static final class TIME {

        public static final MessageKey SECOND = new MessageKey("time.second", false);
        public static final MessageKey SECONDS = new MessageKey("time.seconds", false);
        public static final MessageKey MINUTE = new MessageKey("time.minute", false);
        public static final MessageKey MINUTES = new MessageKey("time.minutes", false);
        public static final MessageKey HOUR = new MessageKey("time.hour", false);
        public static final MessageKey HOURS = new MessageKey("time.hours", false);
        public static final MessageKey DAY = new MessageKey("time.day", false);
        public static final MessageKey DAYS = new MessageKey("time.days", false);
        public static final MessageKey WEEK = new MessageKey("time.week", false);
        public static final MessageKey WEEKS = new MessageKey("time.weeks", false);
        public static final MessageKey MONTH = new MessageKey("time.month", false);
        public static final MessageKey MONTHS = new MessageKey("time.months", false);
        public static final MessageKey YEAR = new MessageKey("time.year", false);
        public static final MessageKey YEARS = new MessageKey("time.years", false);

    }
}
