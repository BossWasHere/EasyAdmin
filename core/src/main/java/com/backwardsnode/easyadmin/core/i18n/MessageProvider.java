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

import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.config.LocaleConfiguration;
import com.backwardsnode.easyadmin.api.data.ActionScope;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.core.i18n.time.CustomTimeUnit;
import com.backwardsnode.easyadmin.core.i18n.time.TimeRangeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import static com.backwardsnode.easyadmin.core.i18n.CommonMessages.ADMINISTRATIVE;
import static com.backwardsnode.easyadmin.core.i18n.CommonMessages.EASYADMIN;

public class MessageProvider {

    public static final String DEFAULT_LANGUAGE = "en_US";
    private static final String FALLBACK_DATE_FORMAT = "yyyy-MM-dd HH:mm:ssZ";

    private final Logger logger = LoggerFactory.getLogger(MessageProvider.class);

    private final EasyAdminPlugin plugin;
    private final String defaultLanguage;
    private final Map<String, LanguageGroup> loadedLanguages;

    private LanguageGroup defaultLanguageGroup;
    private SimpleDateFormat dateFormat;
    private TimeRangeFormat timeRangeFormat;

    public MessageProvider(EasyAdminPlugin plugin, boolean loadDefault) {
        this(plugin, false, DEFAULT_LANGUAGE);
    }

    public MessageProvider(EasyAdminPlugin plugin, boolean loadDefault, String defaultLanguage) {
        this.plugin = plugin;
        this.defaultLanguage = defaultLanguage;
        loadedLanguages = new HashMap<>();

        if (loadDefault) {
            defaultLanguageGroup = loadLanguage(defaultLanguage, false);
        }

        LocaleConfiguration localeConfig = plugin.getInstance().getConfigurationManager().getLocaleConfiguration();

        try {
            dateFormat = new SimpleDateFormat(localeConfig.getDateFormat());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid date format '" + localeConfig.getDateFormat() + "', using default format", e);
            dateFormat = new SimpleDateFormat(FALLBACK_DATE_FORMAT);
        }

        try {
            timeRangeFormat = new TimeRangeFormat(localeConfig.getTimeRangeFormat());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid time range format '" + localeConfig.getTimeRangeFormat() + "', using default format", e);
            timeRangeFormat = new TimeRangeFormat(CustomTimeUnit.DAY, CustomTimeUnit.HOUR, CustomTimeUnit.MINUTE);
        }
    }

    public void reloadLanguage(String language) {
        loadLanguage(language, true);
    }

    private LanguageGroup loadLanguage(String language, boolean forceReload) {
        LanguageGroup lg = loadedLanguages.get(language);
        if (lg != null && !forceReload) {
            return lg;
        }

        logger.debug("Loading language: " + language);

        Path langFilePath = plugin.getFileSystemProvider().getLanguageDirectory().resolve(language + ".yml");

        Yaml yaml = new Yaml();
        try (InputStream is = Files.newInputStream(langFilePath)) {
            Map<String, ?> languageMap = yaml.load(is);

            LanguageGroup newLanguage = new LanguageGroup(languageMap);
            loadedLanguages.put(language, newLanguage);
            return newLanguage;
        } catch (IOException e) {
            logger.warn("Failed to load language: " + language, e);
            return null;
        }
    }

    private LanguageGroup loadLanguageOrDefault(String language) {
        LanguageGroup lg = loadLanguage(language, false);
        if (lg == null) {
            if (defaultLanguageGroup == null) {
                defaultLanguageGroup = loadLanguage(defaultLanguage, false);

                if (defaultLanguageGroup == null && !DEFAULT_LANGUAGE.equals(language)) {
                    logger.warn("Failed to load default language, trying " + DEFAULT_LANGUAGE);
                    defaultLanguageGroup = loadLanguage(DEFAULT_LANGUAGE, false);

                    if (defaultLanguageGroup == null) {
                        throw new RuntimeException("Failed to load default and/or fallback languages");
                    }
                }
            }

            return defaultLanguageGroup;
        }
        return lg;
    }

//    public String toTimeRangeFromNowString(LocalDateTime date) {
//        return
//    }

    public void sendMessage(CommandExecutor executor, MessageKey message, Object... args) {
        executor.sendMessage(getMessage(message, executor.getLocale(), args));
    }

    public String getMessage(MessageKey key, String language, Object... args) {
        LanguageGroup lg = loadLanguageOrDefault(language);
        String msg = lg.getMessage(key);

        if (args.length > 0) {
            msg = MessageFormat.format(msg, args);
        }
        if (key.addPrefix()) {
            msg = lg.getMessage(EASYADMIN.PREFIX) + msg;
        }
        return plugin.translateAlternateColorCodes('&', msg);
    }

    public String getMessageDefault(MessageKey key, Object... args) {
        return getMessage(key, defaultLanguage, args);
    }

    private MessageKey getBanCommandMessage(ActionScope scope, boolean usageMsg) {
        return switch (scope) {
            case DEFAULT -> usageMsg ? ADMINISTRATIVE.BAN.BAN_USAGE : ADMINISTRATIVE.BAN.BAN_DESC;
            case GLOBAL -> usageMsg ? ADMINISTRATIVE.BAN.BAN_ALL_USAGE : ADMINISTRATIVE.BAN.BAN_ALL_DESC;
            case GLOBAL_IP -> usageMsg ? ADMINISTRATIVE.BAN.BAN_IP_ALL_USAGE : ADMINISTRATIVE.BAN.BAN_IP_ALL_DESC;
            case IP -> usageMsg ? ADMINISTRATIVE.BAN.BAN_IP_USAGE : ADMINISTRATIVE.BAN.BAN_IP_DESC;
            case TEMPORARY -> usageMsg ? ADMINISTRATIVE.BAN.TEMPBAN_USAGE : ADMINISTRATIVE.BAN.TEMPBAN_DESC;
            case TEMPORARY_GLOBAL ->
                    usageMsg ? ADMINISTRATIVE.BAN.TEMPBAN_ALL_USAGE : ADMINISTRATIVE.BAN.TEMPBAN_ALL_DESC;
            case TEMPORARY_GLOBAL_IP ->
                    usageMsg ? ADMINISTRATIVE.BAN.TEMPBAN_IP_ALL_USAGE : ADMINISTRATIVE.BAN.TEMPBAN_IP_ALL_DESC;
            case TEMPORARY_IP -> usageMsg ? ADMINISTRATIVE.BAN.TEMPBAN_IP_USAGE : ADMINISTRATIVE.BAN.TEMPBAN_IP_DESC;
        };
    }

    private MessageKey getCommentCommandMessage(boolean usageMsg) {
        return usageMsg ? ADMINISTRATIVE.COMMENT.USAGE : ADMINISTRATIVE.COMMENT.DESC;
    }

    private MessageKey getKickCommandMessage(ActionScope scope, boolean usageMsg) {
        if (scope.isGlobal()) {
            return usageMsg ? ADMINISTRATIVE.KICK.KICK_ALL_USAGE : ADMINISTRATIVE.KICK.KICK_ALL_DESC;
        } else {
            return usageMsg ? ADMINISTRATIVE.KICK.KICK_USAGE : ADMINISTRATIVE.KICK.KICK_DESC;
        }
    }

    private MessageKey getMuteCommandMessage(ActionScope scope, boolean usageMsg) {
        return switch (scope) {
            case DEFAULT -> usageMsg ? ADMINISTRATIVE.MUTE.MUTE_USAGE : ADMINISTRATIVE.MUTE.MUTE_DESC;
            case GLOBAL -> usageMsg ? ADMINISTRATIVE.MUTE.MUTE_ALL_USAGE : ADMINISTRATIVE.MUTE.MUTE_ALL_DESC;
            case GLOBAL_IP -> usageMsg ? ADMINISTRATIVE.MUTE.MUTE_IP_ALL_USAGE : ADMINISTRATIVE.MUTE.MUTE_IP_ALL_DESC;
            case IP -> usageMsg ? ADMINISTRATIVE.MUTE.MUTE_IP_USAGE : ADMINISTRATIVE.MUTE.MUTE_IP_DESC;
            case TEMPORARY -> usageMsg ? ADMINISTRATIVE.MUTE.TEMPMUTE_USAGE : ADMINISTRATIVE.MUTE.TEMPMUTE_DESC;
            case TEMPORARY_GLOBAL ->
                    usageMsg ? ADMINISTRATIVE.MUTE.TEMPMUTE_ALL_USAGE : ADMINISTRATIVE.MUTE.TEMPMUTE_ALL_DESC;
            case TEMPORARY_GLOBAL_IP ->
                    usageMsg ? ADMINISTRATIVE.MUTE.TEMPMUTE_IP_ALL_USAGE : ADMINISTRATIVE.MUTE.TEMPMUTE_IP_ALL_DESC;
            case TEMPORARY_IP -> usageMsg ? ADMINISTRATIVE.MUTE.TEMPMUTE_IP_USAGE : ADMINISTRATIVE.MUTE.TEMPMUTE_IP_DESC;
        };
    }

    private MessageKey getWarnCommandMessage(boolean usageMsg) {
        return usageMsg ? ADMINISTRATIVE.WARNING.USAGE : ADMINISTRATIVE.WARNING.DESC;
    }

    private MessageKey getLookupCommandMessage(boolean usageMsg) {
        return usageMsg ? ADMINISTRATIVE.LOOKUP.USAGE : ADMINISTRATIVE.LOOKUP.DESC;
    }

    private MessageKey getStaffLookupCommandMessage(boolean usageMsg) {
        return usageMsg ? ADMINISTRATIVE.STAFF_LOOKUP.USAGE : ADMINISTRATIVE.STAFF_LOOKUP.DESC;
    }

//    public String createDetailledStaffMessage(BanRecord record, boolean isExisting, boolean showIPs) {
//        int targetMsg = createFlagsInt(record.hasReason(), !record.hasContexts(), record.hasIpAddress(), record.isTemporary(), record.hasEnded(), isExisting);
//        MessageKey type = switch (targetMsg) {
//            case 0 -> ADMINISTRATIVE.BAN.BANNED;
//            case 1 -> ADMINISTRATIVE.BAN.BANNED_REASON;
//            case 2 -> ADMINISTRATIVE.BAN.BANNED_ALL;
//            case 3 -> ADMINISTRATIVE.BAN.BANNED_ALL_REASON;
//            case 4 -> ADMINISTRATIVE.BAN.BANNED_IP;
//            case 5 -> ADMINISTRATIVE.BAN.BANNED_IP_REASON;
//            case 6 -> ADMINISTRATIVE.BAN.BANNED_IP_ALL;
//            case 7 -> ADMINISTRATIVE.BAN.BANNED_IP_ALL_REASON;
//            case 8 -> ADMINISTRATIVE.BAN.TEMPBANNED;
//            case 9 -> ADMINISTRATIVE.BAN.TEMPBANNED_REASON;
//            case 10 -> ADMINISTRATIVE.BAN.TEMPBANNED_ALL;
//            case 11 -> ADMINISTRATIVE.BAN.TEMPBANNED_ALL_REASON;
//            case 12 -> ADMINISTRATIVE.BAN.TEMPBANNED_IP;
//            case 13 -> ADMINISTRATIVE.BAN.TEMPBANNED_IP_REASON;
//            case 14 -> ADMINISTRATIVE.BAN.TEMPBANNED_IP_ALL;
//            case 15 -> ADMINISTRATIVE.BAN.TEMPBANNED_IP_ALL_REASON;
//            case 16, 24 -> ADMINISTRATIVE.BAN.UNBANNED;
//            case 17, 25 -> ADMINISTRATIVE.BAN.UNBANNED_REASON;
//            case 18, 26 -> ADMINISTRATIVE.BAN.UNBANNED_ALL;
//            case 19, 27 -> ADMINISTRATIVE.BAN.UNBANNED_ALL_REASON;
//            case 20, 28 -> ADMINISTRATIVE.BAN.UNBANNED_IP;
//            case 21, 29 -> ADMINISTRATIVE.BAN.UNBANNED_IP_REASON;
//            case 22, 30 -> ADMINISTRATIVE.BAN.UNBANNED_IP_ALL;
//            case 23, 31 -> ADMINISTRATIVE.BAN.UNBANNED_IP_ALL_REASON;
//            case 32 -> ADMINISTRATIVE.BAN.ALREADY_BANNED;
//            case 33 -> ADMINISTRATIVE.BAN.ALREADY_BANNED_REASON;
//            case 34 -> ADMINISTRATIVE.BAN.ALREADY_BANNED_ALL;
//            case 35 -> ADMINISTRATIVE.BAN.ALREADY_BANNED_ALL_REASON;
//            case 36 -> ADMINISTRATIVE.BAN.ALREADY_BANNED_IP;
//            case 37 -> ADMINISTRATIVE.BAN.ALREADY_BANNED_IP_REASON;
//            case 38 -> ADMINISTRATIVE.BAN.ALREADY_BANNED_IP_ALL;
//            case 39 -> ADMINISTRATIVE.BAN.ALREADY_BANNED_IP_ALL_REASON;
//            case 40 -> ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED;
//            case 41 -> ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_REASON;
//            case 42 -> ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_ALL;
//            case 43 -> ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_ALL_REASON;
//            case 44 -> ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_IP;
//            case 45 -> ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_IP_REASON;
//            case 46 -> ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_IP_ALL;
//            case 47 -> ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_IP_ALL_REASON;
//            default -> null;
//        };
//
//        Object[] vars = new Object[6];
//        int targetVar = 0;
//        if (record.hasIpAddress() && showIPs) {
//            vars[targetVar++] = record.getIpAddress();
//        } else {
//            vars[targetVar++] = Objects.requireNonNull(plugin.getOfflinePlayer(record.getPlayer()), "Player with UUID " + record.getPlayer() + " not tracked.").getUsername();
//        }
//        if (record.hasContexts()) {
//            vars[targetVar++] = record.getContexts();
//        }
//        if (record.isTemporary() && !record.hasEnded()) {
//            vars[targetVar++] = toTimeRangeFromNowString(record.getTerminationDate());
//        }
//        if (isExisting) {
//            if (record.getStaff() == null) {
//                vars[targetVar++] = "[CONSOLE]";
//            } else {
//                vars[targetVar++] = Objects.requireNonNull(plugin.getOfflinePlayer(record.getStaff()), "Player with UUID " + record.getStaff() + " not tracked.").getUsername();
//            }
//            vars[targetVar++] = dateFormat.format(record.getDateAdded());
//        }
//        if (record.hasReason()) {
//            vars[targetVar] = record.getReason();
//        }
//        return getMessage(type, vars);
//
//    }
//
//    public String createDetailledStaffMessage(AdminRecord record, boolean isExisting, boolean showIPs) {
//        MessageKey type = null;
//        Object[] vars = new Object[6];
//        int targetMsg = createFlagsInt(record.hasReason(), !record.isContextual(), record.isIPRecord(), record.isTemporary(), record.isCancelled(), isExisting);
//        int targetVar = 0;
//
//        switch (record.getRecordType()) {
//            case BAN:
//                switch (targetMsg) {
//                    case 0:
//                        type = ADMINISTRATIVE.BAN.BANNED;
//                        break;
//                    case 1:
//                        type = ADMINISTRATIVE.BAN.BANNED_REASON;
//                        break;
//                    case 2:
//                        type = ADMINISTRATIVE.BAN.BANNED_ALL;
//                        break;
//                    case 3:
//                        type = ADMINISTRATIVE.BAN.BANNED_ALL_REASON;
//                        break;
//                    case 4:
//                        type = ADMINISTRATIVE.BAN.BANNED_IP;
//                        break;
//                    case 5:
//                        type = ADMINISTRATIVE.BAN.BANNED_IP_REASON;
//                        break;
//                    case 6:
//                        type = ADMINISTRATIVE.BAN.BANNED_IP_ALL;
//                        break;
//                    case 7:
//                        type = ADMINISTRATIVE.BAN.BANNED_IP_ALL_REASON;
//                        break;
//                    case 8:
//                        type = ADMINISTRATIVE.BAN.TEMPBANNED;
//                        break;
//                    case 9:
//                        type = ADMINISTRATIVE.BAN.TEMPBANNED_REASON;
//                        break;
//                    case 10:
//                        type = ADMINISTRATIVE.BAN.TEMPBANNED_ALL;
//                        break;
//                    case 11:
//                        type = ADMINISTRATIVE.BAN.TEMPBANNED_ALL_REASON;
//                        break;
//                    case 12:
//                        type = ADMINISTRATIVE.BAN.TEMPBANNED_IP;
//                        break;
//                    case 13:
//                        type = ADMINISTRATIVE.BAN.TEMPBANNED_IP_REASON;
//                        break;
//                    case 14:
//                        type = ADMINISTRATIVE.BAN.TEMPBANNED_IP_ALL;
//                        break;
//                    case 15:
//                        type = ADMINISTRATIVE.BAN.TEMPBANNED_IP_ALL_REASON;
//                        break;
//                    case 16:
//                    case 24:
//                        type = ADMINISTRATIVE.BAN.UNBANNED;
//                        break;
//                    case 17:
//                    case 25:
//                        type = ADMINISTRATIVE.BAN.UNBANNED_REASON;
//                        break;
//                    case 18:
//                    case 26:
//                        type = ADMINISTRATIVE.BAN.UNBANNED_ALL;
//                        break;
//                    case 19:
//                    case 27:
//                        type = ADMINISTRATIVE.BAN.UNBANNED_ALL_REASON;
//                        break;
//                    case 20:
//                    case 28:
//                        type = ADMINISTRATIVE.BAN.UNBANNED_IP;
//                        break;
//                    case 21:
//                    case 29:
//                        type = ADMINISTRATIVE.BAN.UNBANNED_IP_REASON;
//                        break;
//                    case 22:
//                    case 30:
//                        type = ADMINISTRATIVE.BAN.UNBANNED_IP_ALL;
//                        break;
//                    case 23:
//                    case 31:
//                        type = ADMINISTRATIVE.BAN.UNBANNED_IP_ALL_REASON;
//                        break;
//                    case 32:
//                        type = ADMINISTRATIVE.BAN.ALREADY_BANNED_ALL;
//                        break;
//                    case 33:
//                        type = ADMINISTRATIVE.BAN.ALREADY_BANNED_REASON;
//                        break;
//                    case 34:
//                        type = ADMINISTRATIVE.BAN.ALREADY_BANNED_ALL;
//                        break;
//                    case 35:
//                        type = ADMINISTRATIVE.BAN.ALREADY_BANNED_ALL_REASON;
//                        break;
//                    case 36:
//                        type = ADMINISTRATIVE.BAN.ALREADY_BANNED_IP;
//                        break;
//                    case 37:
//                        type = ADMINISTRATIVE.BAN.ALREADY_BANNED_IP_REASON;
//                        break;
//                    case 38:
//                        type = ADMINISTRATIVE.BAN.ALREADY_BANNED_IP_ALL;
//                        break;
//                    case 39:
//                        type = ADMINISTRATIVE.BAN.ALREADY_BANNED_IP_ALL_REASON;
//                        break;
//                    case 40:
//                        type = ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED;
//                        break;
//                    case 41:
//                        type = ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_REASON;
//                        break;
//                    case 42:
//                        type = ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_ALL;
//                        break;
//                    case 43:
//                        type = ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_ALL_REASON;
//                        break;
//                    case 44:
//                        type = ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_IP;
//                        break;
//                    case 45:
//                        type = ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_IP_REASON;
//                        break;
//                    case 46:
//                        type = ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_IP_ALL;
//                        break;
//                    case 47:
//                        type = ADMINISTRATIVE.BAN.ALREADY_TEMPBANNED_IP_ALL_REASON;
//                        break;
//                }
//                break;
//            case COMMENT:
//                type = ADMINISTRATIVE.COMMENT.COMMENTED;
//                break;
//            case KICK:
//                switch (targetMsg) {
//                    case 0:
//                        type = ADMINISTRATIVE.KICK.KICKED;
//                        break;
//                    case 1:
//                        type = ADMINISTRATIVE.KICK.KICKED_REASON;
//                        break;
//                    case 2:
//                        type = ADMINISTRATIVE.KICK.KICKED_ALL;
//                        break;
//                    case 3:
//                        type = ADMINISTRATIVE.KICK.KICKED_ALL_REASON;
//                        break;
//                }
//                break;
//            case MUTE:
//                switch (targetMsg) {
//                    case 0:
//                        type = ADMINISTRATIVE.MUTE.MUTED;
//                        break;
//                    case 1:
//                        type = ADMINISTRATIVE.MUTE.MUTED_REASON;
//                        break;
//                    case 2:
//                        type = ADMINISTRATIVE.MUTE.MUTED_ALL;
//                        break;
//                    case 3:
//                        type = ADMINISTRATIVE.MUTE.MUTED_ALL_REASON;
//                        break;
//                    case 4:
//                        type = ADMINISTRATIVE.MUTE.MUTED_IP;
//                        break;
//                    case 5:
//                        type = ADMINISTRATIVE.MUTE.MUTED_IP_REASON;
//                        break;
//                    case 6:
//                        type = ADMINISTRATIVE.MUTE.MUTED_IP_ALL;
//                        break;
//                    case 7:
//                        type = ADMINISTRATIVE.MUTE.MUTED_IP_ALL_REASON;
//                        break;
//                    case 8:
//                        type = ADMINISTRATIVE.MUTE.TEMPMUTED;
//                        break;
//                    case 9:
//                        type = ADMINISTRATIVE.MUTE.TEMPMUTED_REASON;
//                        break;
//                    case 10:
//                        type = ADMINISTRATIVE.MUTE.TEMPMUTED_ALL;
//                        break;
//                    case 11:
//                        type = ADMINISTRATIVE.MUTE.TEMPMUTED_ALL_REASON;
//                        break;
//                    case 12:
//                        type = ADMINISTRATIVE.MUTE.TEMPMUTED_IP;
//                        break;
//                    case 13:
//                        type = ADMINISTRATIVE.MUTE.TEMPMUTED_IP_REASON;
//                        break;
//                    case 14:
//                        type = ADMINISTRATIVE.MUTE.TEMPMUTED_IP_ALL;
//                        break;
//                    case 15:
//                        type = ADMINISTRATIVE.MUTE.TEMPMUTED_IP_ALL_REASON;
//                        break;
//                    case 16:
//                    case 24:
//                        type = ADMINISTRATIVE.MUTE.UNMUTED;
//                        break;
//                    case 17:
//                    case 25:
//                        type = ADMINISTRATIVE.MUTE.UNMUTED_REASON;
//                        break;
//                    case 18:
//                    case 26:
//                        type = ADMINISTRATIVE.MUTE.UNMUTED_ALL;
//                        break;
//                    case 19:
//                    case 27:
//                        type = ADMINISTRATIVE.MUTE.UNMUTED_ALL_REASON;
//                        break;
//                    case 20:
//                    case 28:
//                        type = ADMINISTRATIVE.MUTE.UNMUTED_IP;
//                        break;
//                    case 21:
//                    case 29:
//                        type = ADMINISTRATIVE.MUTE.UNMUTED_IP_REASON;
//                        break;
//                    case 22:
//                    case 30:
//                        type = ADMINISTRATIVE.MUTE.UNMUTED_IP_ALL;
//                        break;
//                    case 23:
//                    case 31:
//                        type = ADMINISTRATIVE.MUTE.UNMUTED_IP_ALL_REASON;
//                        break;
//                    case 32:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_MUTED_ALL;
//                        break;
//                    case 33:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_MUTED_REASON;
//                        break;
//                    case 34:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_MUTED_ALL;
//                        break;
//                    case 35:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_MUTED_ALL_REASON;
//                        break;
//                    case 36:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_MUTED_IP;
//                        break;
//                    case 37:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_MUTED_IP_REASON;
//                        break;
//                    case 38:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_MUTED_IP_ALL;
//                        break;
//                    case 39:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_MUTED_IP_ALL_REASON;
//                        break;
//                    case 40:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_TEMPMUTED;
//                        break;
//                    case 41:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_TEMPMUTED_REASON;
//                        break;
//                    case 42:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_TEMPMUTED_ALL;
//                        break;
//                    case 43:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_TEMPMUTED_ALL_REASON;
//                        break;
//                    case 44:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_TEMPMUTED_IP;
//                        break;
//                    case 45:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_TEMPMUTED_IP_REASON;
//                        break;
//                    case 46:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_TEMPMUTED_IP_ALL;
//                        break;
//                    case 47:
//                        type = ADMINISTRATIVE.MUTE.ALREADY_TEMPMUTED_IP_ALL_REASON;
//                        break;
//                }
//                break;
//            case WARN:
//                type = ADMINISTRATIVE.WARNING.WARNED;
//                break;
//        }
//        if (type == null) {
//            return null;
//        }
//
//        if (record.isIPRecord() && showIPs) {
//            vars[targetVar++] = record.getIPAddress();
//        } else {
//            vars[targetVar++] = PLUGIN.getRecordsCache().getPlayer(record.getPlayerUUID(), true).getName();
//        }
//        if (record.isContextual()) {
//            vars[targetVar++] = record.getContext();
//        }
//        if (record.isTemporary() && !record.isCancelled()) {
//            vars[targetVar++] = toTimeRangeFromNowString(record.getAutomaticEndDate());
//        }
//        if (isExisting) {
//            vars[targetVar++] = PLUGIN.getRecordsCache().getPlayer(record.getStaffUUID(), true).getName();
//            vars[targetVar++] = dateFormat.format(record.getIssueDate());
//        }
//        if (record.hasReason()) {
//            vars[targetVar++] = record.getReason();
//        }
//        return getMessage(type, vars);
//    }
//
//
//
//
//
//    /*
//     * AFFINITY:
//     * hasReason: 0 (no priority)
//     * isGlobal: 1 (doesn't apply to warn, comment)
//     * isTemporary: 2 (doesn't apply to kick, warn, comment)
//     * isIPBased: 2 (doesn't apply to kick, warn, comment)
//     * isDisconnecting: 3 (only applies to bans)
//     *
//     */
//    public String createDetailledPlayerMessage(AdminRecord record, boolean isDisconnecting) {
//        if (record instanceof SpecialAdminRecord specialRecord) {
//
//        } else {
//
//        }
//
//
//        PluginMessage type = null;
//        Object[] vars = new Object[5];
//        int targetMsg = createFlagsInt(record.hasReason(), !record.isContextual(), record.isIPRecord(), record.isTemporary(), isDisconnecting);
//        int targetVar = 0;
//        switch (record.getRecordType()) {
//            case BAN:
//                if (record.hasEnded()) {
//                    if (record.isContextual()) {
//                        type = PLAYER.BAN.UNBANNED;
//                    } else {
//                        type = PLAYER.BAN.UNBANNED_ALL;
//                    }
//                    break;
//                }
//                type = switch (targetMsg) {
//                    case 0 -> PLAYER.BAN.BAN_RECONNECTED;
//                    case 1 -> PLAYER.BAN.BAN_RECONNECTED_REASON;
//                    case 2, 18 -> PLAYER.BAN.BAN_ALL;
//                    case 3, 19 -> PLAYER.BAN.BAN_ALL_REASON;
//                    case 4 -> PLAYER.BAN.BAN_IP_RECONNECTED;
//                    case 5 -> PLAYER.BAN.BAN_IP_RECONNECTED_REASON;
//                    case 6, 22 -> PLAYER.BAN.BAN_IP_ALL;
//                    case 7, 23 -> PLAYER.BAN.BAN_IP_ALL_REASON;
//                    case 8 -> PLAYER.BAN.TEMPBAN_RECONNECTED;
//                    case 9 -> PLAYER.BAN.TEMPBAN_RECONNECTED_REASON;
//                    case 10, 26 -> PLAYER.BAN.TEMPBAN_ALL;
//                    case 11, 27 -> PLAYER.BAN.TEMPBAN_ALL_REASON;
//                    case 12 -> PLAYER.BAN.TEMPBAN_IP_RECONNECTED;
//                    case 13 -> PLAYER.BAN.TEMPBAN_IP_RECONNECTED_REASON;
//                    case 14, 30 -> PLAYER.BAN.TEMPBAN_IP_ALL;
//                    case 15, 31 -> PLAYER.BAN.TEMPBAN_IP_ALL_REASON;
//                    case 16 -> PLAYER.BAN.BAN_DISCONNECTED;
//                    case 17 -> PLAYER.BAN.BAN_DISCONNECTED_REASON;
//                    case 20 -> PLAYER.BAN.BAN_IP_DISCONNECTED;
//                    case 21 -> PLAYER.BAN.BAN_IP_DISCONNECTED_REASON;
//                    case 24 -> PLAYER.BAN.TEMPBAN_DISCONNECTED;
//                    case 25 -> PLAYER.BAN.TEMPBAN_DISCONNECTED_REASON;
//                    case 28 -> PLAYER.BAN.TEMPBAN_IP_DISCONNECTED;
//                    case 29 -> PLAYER.BAN.TEMPBAN_IP_DISCONNECTED_REASON;
//                    default -> type;
//                };
//                break;
//            case COMMENT:
//                return null;
//            case KICK:
//                switch (targetMsg) {
//                    case 0:
//                        type = PLAYER.KICK.KICK_RECONNECTED;
//                        break;
//                    case 1:
//                        type = PLAYER.KICK.KICK_RECONNECTED_REASON;
//                        break;
//                    case 2:
//                        type = PLAYER.KICK.KICK_ALL;
//                        break;
//                    case 3:
//                        type = PLAYER.KICK.KICK_ALL_REASON;
//                        break;
//                    case 16:
//                        type = PLAYER.KICK.KICK_DISCONNECTED;
//                        break;
//                    case 17:
//                        type = PLAYER.KICK.KICK_DISCONNECTED_REASON;
//                        break;
//                }
//                break;
//            case MUTE:
//                switch (targetMsg) {
//                    case 0:
//                        type = PLAYER.MUTE.MUTE;
//                        break;
//                    case 1:
//                        type = PLAYER.MUTE.MUTE_REASON;
//                        break;
//                    case 2:
//                        type = PLAYER.MUTE.MUTE_ALL;
//                        break;
//                    case 3:
//                        type = PLAYER.MUTE.MUTE_ALL_REASON;
//                        break;
//                    case 4:
//                        type = PLAYER.MUTE.MUTE_IP;
//                        break;
//                    case 5:
//                        type = PLAYER.MUTE.MUTE_IP_REASON;
//                        break;
//                    case 6:
//                        type = PLAYER.MUTE.MUTE_IP_ALL;
//                        break;
//                    case 7:
//                        type = PLAYER.MUTE.MUTE_IP_ALL_REASON;
//                        break;
//                    case 8:
//                        type = PLAYER.MUTE.TEMPMUTE;
//                        break;
//                    case 9:
//                        type = PLAYER.MUTE.TEMPMUTE_REASON;
//                        break;
//                    case 10:
//                        type = PLAYER.MUTE.TEMPMUTE_ALL;
//                        break;
//                    case 11:
//                        type = PLAYER.MUTE.TEMPMUTE_ALL_REASON;
//                        break;
//                    case 12:
//                        type = PLAYER.MUTE.TEMPMUTE_IP;
//                        break;
//                    case 13:
//                        type = PLAYER.MUTE.TEMPMUTE_IP_REASON;
//                        break;
//                    case 14:
//                        type = PLAYER.MUTE.TEMPMUTE_IP_ALL;
//                        break;
//                    case 15:
//                        type = PLAYER.MUTE.TEMPMUTE_IP_ALL_REASON;
//                        break;
//                }
//                break;
//            case WARN:
//                type = PLAYER.WARNING;
//                break;
//        }
//        if (type == null) {
//            return null;
//        }
//
//        vars[targetVar++] = PLUGIN.getRecordsCache().getPlayer(record.getStaffUUID(), true).getName();
//        if (record.isContextual()) {
//            vars[targetVar++] = record.getContext();
//        }
//        if (record.isTemporary() && !record.hasEnded()) {
//            vars[targetVar++] = toTimeRangeFromNowString(record.getAutomaticEndDate());
//        }
//        if (record.hasReason() && !record.hasEnded()) {
//            vars[targetVar++] = record.getReason();
//        }
//        return getMessage(type, vars);
//    }

    private static int createFlagsInt(boolean... flags) {
        int x = 0;
        int m = 1;
        for (boolean flag : flags) {
            if (flag) {
                x += m;
            }
            m *= 2;
        }
        return x;
    }

}
