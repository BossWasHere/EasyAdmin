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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Provides access to the {@link EasyAdmin} API.
 * This may only be used after the plugin responsible has been initialized.
 */
public final class EasyAdminProvider {

    private static EasyAdmin easyAdmin;

    private EasyAdminProvider() {}

    /**
     * Gets access to the {@link EasyAdmin} API.
     * @return the current {@link EasyAdmin} API instance
     * @throws  IllegalStateException if the plugin has not loaded the API yet
     */
    public static @NotNull EasyAdmin get() {
        if (EasyAdminProvider.easyAdmin == null) {
            throw new IllegalStateException("EasyAdmin has not been initialized yet!");
        }
        return EasyAdminProvider.easyAdmin;
    }

    @ApiStatus.Internal
    static void register(@NotNull EasyAdmin easyAdmin) {
        if (EasyAdminProvider.easyAdmin != null) {
            throw new IllegalStateException("An instance has already been registered");
        }
        EasyAdminProvider.easyAdmin = easyAdmin;
    }

    @ApiStatus.Internal
    static void unregister() {
        EasyAdminProvider.easyAdmin = null;
    }

}
