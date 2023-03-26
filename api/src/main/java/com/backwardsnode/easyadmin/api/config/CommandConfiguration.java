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

package com.backwardsnode.easyadmin.api.config;

import com.backwardsnode.easyadmin.api.data.ActionScope;

/**
 * Holds configuration entries for managing the functionality of in-game commands
 */
public interface CommandConfiguration {

    /**
     * Determines whether the ban command is enabled.
     * @return true if enabled.
     */
    boolean isBanCommandEnabled();

    /**
     * Determines whether the comment command is enabled.
     * @return true if enabled.
     */
    boolean isCommentCommandEnabled();

    /**
     * Determines whether the kick command is enabled.
     * @return true if enabled.
     */
    boolean isKickCommandEnabled();

    /**
     * Determines whether the mute command is enabled.
     * @return true if enabled.
     */
    boolean isMuteCommandEnabled();

    /**
     * Determines whether the warning command is enabled.
     * @return true if enabled.
     */
    boolean isWarningCommandEnabled();

    /**
     * Determines whether the lookup command is enabled.
     * @return true if enabled.
     */
    boolean isLookupCommandEnabled();

    /**
     * Gets the scopes which the ban command is enabled for.
     * @return the enabled {@link ActionScope}s for "ban".
     * @apiNote consumers should check {@link #isBanCommandEnabled()} first.
     */
    ActionScope getEnabledBanScopes();

    /**
     * Gets the scopes which the mute command is enabled for.
     * @return the enabled {@link ActionScope}s for "mute".
     * @apiNote consumers should check {@link #isMuteCommandEnabled()} first.
     */
    ActionScope getEnabledMuteScopes();

    /**
     * Determines whether global kicks should be allowed.
     * @return true if global kicks are allowed.
     * @apiNote consumers should check {@link #isKickCommandEnabled()} first.
     */
    boolean allowGlobalKicks();

    /**
     * Determines whether local kicks should be allowed.
     * @return true if local kicks are allowed.
     * @apiNote consumers should check {@link #isKickCommandEnabled()} first.
     */
    boolean allowLocalKicks();

    /**
     * Determines whether players should be notified when they are commented on.
     * @return true if players should be notified.
     */
    boolean notifyPlayerOnComment();

    /**
     * Determines whether players should be notified when they are warned.
     * @return true if players should be notified.
     */
    boolean notifyPlayerOnWarning();

    /**
     * Determines whether a reason must be provided when banning a player.
     * @return true if a reason is required.
     */
    boolean banRequiresReason();

    /**
     * Determines whether a reason must be provided when kicking a player.
     * @return true if a reason is required.
     */
    boolean kickRequiresReason();

    /**
     * Determines whether a reason must be provided when muting a player.
     * @return true if a reason is required.
     */
    boolean muteRequiresReason();
}
