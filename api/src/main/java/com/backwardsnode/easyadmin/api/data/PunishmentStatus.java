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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the status of a punishment.
 */
public enum PunishmentStatus {
    /**
     * Signifies that a given punishment is still active and should be enforced.
     */
    ACTIVE("Active"),
    /**
     * Signifies that a given punishment has expired and should be ignored.
     */
    EXPIRED("Expired"),
    /**
     * Signifies that a given punishment has been manually terminated and should be ignored.
     */
    ENDED("Ended");

    private final String dbValue;

    PunishmentStatus(String dbValue) {
        this.dbValue = dbValue;
    }

    /**
     * Gets the database-friendly name of the punishment status.
     * @return the name of the punishment status.
     */
    @Override
    public String toString() {
        return dbValue;
    }

    /**
     * Gets the punishment status from the database-friendly name.
     * @param name the name of the punishment status.
     * @return the punishment status, or null if not found.
     */
    public static @Nullable PunishmentStatus fromString(@NotNull String name) {
        for (PunishmentStatus status : values()) {
            if (status.dbValue.equals(name)) {
                return status;
            }
        }
        return null;
    }
}
