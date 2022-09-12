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

import com.backwardsnode.easyadmin.api.record.SpecialAdminRecord;
import org.jetbrains.annotations.NotNull;

/**
 * Represents the scope of an administrative action.
 * @see SpecialAdminRecord
 */
public enum ActionScope {

    /**
     * The default scope; no special action properties.
     */
    DEFAULT("base"),
    /**
     * Indicates that the action has an expiry date and time.
     */
    TEMPORARY("temp"),
    /**
     * Indicates that the action has a global effect across all contexts.
     */
    GLOBAL("global"),
    /**
     * Indicates that the action is bound to a network address, rather than to a specific player.
     */
    IP("ip"),
    /**
     * Combination of {@link #TEMPORARY} and {@link #GLOBAL}.
     */
    TEMPORARY_GLOBAL("globaltemp"),
    /**
     * Combination of {@link #TEMPORARY} and {@link #IP}.
     */
    TEMPORARY_IP("tempip"),
    /**
     * Combination of {@link #GLOBAL} and {@link #IP}.
     */
    GLOBAL_IP("globalip"),
    /**
     * Combination of {@link #TEMPORARY}, {@link #GLOBAL} and {@link #IP}.
     */
    TEMPORARY_GLOBAL_IP("globaltempip");

    /**
     * The number of scopes.
     */
    public static final int SIZE = values().length;

    private final String permissionSuffix;

    ActionScope(String permissionSuffix) {
        this.permissionSuffix = permissionSuffix;
    }

    /**
     * Determines if this action is based on the {@link #TEMPORARY} scope.
     * @return true if temporary-based, false otherwise.
     */
    public boolean isTemporary() {
        return switch (this) {
            case TEMPORARY, TEMPORARY_GLOBAL, TEMPORARY_GLOBAL_IP, TEMPORARY_IP -> true;
            default -> false;
        };
    }

    /**
     * Determines if this action is based on the {@link #GLOBAL} scope.
     * @return true if global-based, false otherwise.
     */
    public boolean isGlobal() {
        return switch (this) {
            case GLOBAL, TEMPORARY_GLOBAL, TEMPORARY_GLOBAL_IP, GLOBAL_IP -> true;
            default -> false;
        };
    }

    /**
     * Determines if this action is based on the {@link #IP} scope.
     * @return true if IP-based, false otherwise.
     */
    public boolean isIP() {
        return switch (this) {
            case IP, TEMPORARY_IP, TEMPORARY_GLOBAL_IP, GLOBAL_IP -> true;
            default -> false;
        };
    }

    /**
     * Gets the permission suffix for this action scope.
     * @return the permission suffix.
     */
    public @NotNull String getPermissionSuffix() {
        return permissionSuffix;
    }

    /**
     * Converts the given scope flags into the corresponding action scope.
     * @param temporary if the action should be {@link #TEMPORARY}-based.
     * @param global if the action should be {@link #GLOBAL}-based.
     * @param ip if the action should be {@link #IP}-based.
     * @return the corresponding {@link ActionScope}.
     */
    public static @NotNull ActionScope fromFlags(boolean temporary, boolean global, boolean ip) {
        if (temporary) {
            if (global) {
                if (ip) {
                    return ActionScope.TEMPORARY_GLOBAL_IP;
                } else {
                    return ActionScope.TEMPORARY_GLOBAL;
                }
            } else {
                if (ip) {
                    return ActionScope.TEMPORARY_IP;
                } else {
                    return ActionScope.TEMPORARY;
                }
            }
        } else {
            if (global) {
                if (ip) {
                    return ActionScope.GLOBAL_IP;
                } else {
                    return ActionScope.GLOBAL;
                }
            } else {
                if (ip) {
                    return ActionScope.IP;
                } else {
                    return ActionScope.DEFAULT;
                }
            }
        }
    }
}
