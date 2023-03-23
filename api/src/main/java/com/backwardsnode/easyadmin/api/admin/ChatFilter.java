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

import com.backwardsnode.easyadmin.api.data.ChatFilterResult;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;

/**
 * Provides a way to filter chat messages from players
 */
public interface ChatFilter {

    /**
     * Determines if a message should be allowed to be sent into the chat.
     * @param player The player sending the message.
     * @param message The message being sent.
     * @return The {@link ChatFilterResult} with the recommended action for the message.
     */
    ChatFilterResult canSendChatMessage(OnlinePlayer player, String message);

    /**
     * Determines if a message should be allowed to be sent into the chat.
     * @param player The player sending the message.
     * @param target The player being sent the message.
     * @param message The message being sent.
     * @return The {@link ChatFilterResult} with the recommended action for the message.
     */
    ChatFilterResult canSendPrivateMessage(OnlinePlayer player, OnlinePlayer target, String message);

    /**
     * Censors a message using the current ruleset, if applicable.
     * @param player The player sending the message.
     * @param message The message being sent.
     * @return The censored message, or the original message if no censoring is required.
     */
    String censorMessage(OnlinePlayer player, String message);

}
