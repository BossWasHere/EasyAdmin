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

package com.backwardsnode.easyadmin.api;

import com.backwardsnode.easyadmin.api.data.Platform;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a plugin platform-agnostic EasyAdmin plugin.
 */
public interface EasyAdminPlugin {

    /**
     * Called when the server loads the plugin.
     */
    void onLoad();

    /**
     * Called when the server enables the plugin.
     */
    void onEnable();

    /**
     * Called when the server disables the plugin.
     */
    void onDisable();

    /**
     * Gets the {@link Platform} that this plugin is running on.
     * @return {@link Platform#BUKKIT} if running on Bukkit or {@link Platform#BUNGEE} if running on Bungeecord.
     */
    @NotNull Platform getPlatform();

}
