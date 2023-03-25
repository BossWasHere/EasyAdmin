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

package com.backwardsnode.easyadmin.api.record;

import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a kick entry for a specific player.
 * <p>A new record can be created through the {@link RecordBuilder}</p>
 */
public interface KickRecord extends LiveRecord<Integer>, ReasonedAdminRecord {

    /**
     * Gets whether this kick is global or not.
     * <p>A global kick disconnects the player from the network. A simple kick moves the player into a previous/fallback server when used with a server proxy.</p>
     * @return true if this kick is global, false otherwise.
     */
    boolean isGlobal();

    /**
     * Gets the name of the server that this kick was performed on.
     * @return the server name.
     */
    @NotNull String getServerName();

}
