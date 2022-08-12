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

import com.backwardsnode.easyadmin.api.contextual.ContextTester;
import org.jetbrains.annotations.NotNull;

/**
 * The API for EasyAdmin.
 *
 * <p>Plugins can use this API to perform administrative actions and listen for them.</p>
 *
 * <p>An instance of this API can be obtained from {@link EasyAdminProvider#get()}.</p>
 */
public interface EasyAdmin {

    /**
     * Gets the {@link ContextTester}, which is used to match contexts on a per-server/world basis.
     * @return the {@link ContextTester}
     */
    @NotNull ContextTester getContextTester();

    /**
     * Gets the {@link EasyAdminPlugin} that is responsible for this API.
     * @return the {@link EasyAdminPlugin}
     */
    @NotNull EasyAdminPlugin getPluginInstance();

}
