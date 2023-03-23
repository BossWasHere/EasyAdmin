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

package com.backwardsnode.easyadmin.api.data;

import com.backwardsnode.easyadmin.api.admin.RecommendationEngine;

/**
 * Represents the output of a chat filtering request.
 */
public enum ChatFilterResult {

    /**
     * The chat message should be allowed to pass through.
     */
    ALLOW,
    /**
     * The chat message should be blocked, but no further action should be taken.
     */
    DENY,
    /**
     * The chat message should be censored before being sent.
     */
    REQUIRE_CENSOR,
    /**
     * The chat message should be blocked and is recommended that the player be banned.
     */
    OFFENSE_SHOULD_WARN,
    /**
     * The chat message should be blocked and is recommended that the player be banned.
     * @see RecommendationEngine
     */
    OFFENSE_SHOULD_BAN,
    /**
     * The chat message should be blocked and is recommended that the player be muted.
     * @see RecommendationEngine
     */
    OFFENSE_SHOULD_MUTE,

}
