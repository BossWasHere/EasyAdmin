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

package com.backwardsnode.easyadmin.core.config;

import com.backwardsnode.easyadmin.api.config.CommandConfiguration;
import com.backwardsnode.easyadmin.api.data.ActionScope;
import com.backwardsnode.easyadmin.core.exception.ConfigurationException;

import java.util.List;
import java.util.Map;

public final class CommandConfig implements CommandConfiguration, ConfigChecker {

    private BanCommandConfig ban;
    private CommentCommandConfig comment;
    private KickCommandConfig kick;
    private MuteCommandConfig mute;
    private WarningCommandConfig warn;
    private LookupCommandConfig lookup;
    private LookupCommandConfig stafflookup;

    public static final class BanCommandConfig {
        boolean enabled;
        boolean temporary;
        boolean global;
        boolean ip;
        boolean notifyOnUnban;
        boolean requireReason;
    }

    public static final class CommentCommandConfig {
        boolean enabled;
        boolean notifyOnComment;
    }

    public static final class KickCommandConfig {
        boolean enabled;
        boolean global;
        boolean local;
        boolean requireReason;
    }

    public static final class MuteCommandConfig {
        boolean enabled;
        boolean temporary;
        boolean global;
        boolean ip;
        boolean notifyOnMute;
        boolean notifyOnUnmute;
        boolean requireReason;
        Map<String, ContextBound> blockedCommands;
        Map<String, ContextBound> allowedMessages;

    }

    public static final class WarningCommandConfig {
        boolean enabled;
        boolean notifyOnWarning;
    }

    public static final class LookupCommandConfig {
        boolean enabled;
    }

    public static final class ContextBound {
        boolean regex;
        boolean allow;
        boolean block;
        List<String> context;
    }

    @Override
    public void validate(String parentPath) throws ConfigurationException {
        if (ban == null) throw new ConfigurationException(parentPath, "ban");
        if (comment == null) throw new ConfigurationException(parentPath, "comment");
        if (kick == null) throw new ConfigurationException(parentPath, "kick");
        if (mute == null) throw new ConfigurationException(parentPath, "mute");
        if (warn == null) throw new ConfigurationException(parentPath, "warning");
        if (lookup == null) throw new ConfigurationException(parentPath, "lookup");
        if (stafflookup == null) throw new ConfigurationException(parentPath, "stafflookup");
    }

    @Override
    public boolean isBanCommandEnabled() {
        return ban.enabled;
    }

    @Override
    public boolean isCommentCommandEnabled() {
        return comment.enabled;
    }

    @Override
    public boolean isKickCommandEnabled() {
        return kick.enabled;
    }

    @Override
    public boolean isMuteCommandEnabled() {
        return mute.enabled;
    }

    @Override
    public boolean isWarningCommandEnabled() {
        return warn.enabled;
    }

    @Override
    public boolean isLookupCommandEnabled() {
        return lookup.enabled;
    }

    @Override
    public ActionScope getEnabledBanScopes() {
        return ActionScope.fromFlags(ban.temporary, ban.global, ban.ip);
    }

    @Override
    public ActionScope getEnabledMuteScopes() {
        return ActionScope.fromFlags(mute.temporary, mute.global, mute.ip);
    }

    @Override
    public boolean allowGlobalKicks() {
        return kick.global;
    }

    @Override
    public boolean allowLocalKicks() {
        return kick.local;
    }

    @Override
    public boolean notifyPlayerOnComment() {
        return comment.notifyOnComment;
    }

    @Override
    public boolean notifyPlayerOnWarning() {
        return warn.notifyOnWarning;
    }

    @Override
    public boolean banRequiresReason() {
        return ban.requireReason;
    }

    @Override
    public boolean kickRequiresReason() {
        return kick.requireReason;
    }

    @Override
    public boolean muteRequiresReason() {
        return mute.requireReason;
    }

}
