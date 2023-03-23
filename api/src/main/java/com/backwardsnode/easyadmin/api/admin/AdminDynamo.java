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

import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Records and responds to repeated player violations.
 */
public interface AdminDynamo {

    /**
     * Records a chat message sent by a muted player that was blocked from being sent.
     * @param player The player who sent the message.
     * @param messageContent The content of the message.
     * @param context The server/world context the message was sent in.
     */
    void recordAttemptedChatMessage(@NotNull OnlinePlayer player, @NotNull String messageContent, @Nullable String context);

    /**
     * Records a chat message sent by a player that was blocked from being sent by the chat filter.
     * @param player The player who sent the message.
     * @param messageContent The content of the message.
     * @param context The server/world context the message was sent in.
     */
    void recordBlockedChatMessage(@NotNull OnlinePlayer player, @NotNull String messageContent, @Nullable String context);

    /**
     * Records an attempted join by a player who was banned from the server.
     * @param player The player who attempted to join.
     * @param context The server/world context the player attempted to join.
     */
    void recordAttemptedJoin(@NotNull OfflinePlayer player, @Nullable String context);

}
