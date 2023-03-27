/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

package com.backwardsnode.easyadmin.api.record.base;

import com.backwardsnode.easyadmin.api.data.ActionScope;
import com.backwardsnode.easyadmin.api.data.PunishmentStatus;
import com.backwardsnode.easyadmin.api.record.BanRecord;
import com.backwardsnode.easyadmin.api.record.MuteRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents an administrative action that has a status, along with other properties.
 * @see BanRecord
 * @see MuteRecord
 */
public interface ScopedRecord {

    /**
     * Gets the current {@link PunishmentStatus} of this record.
     * @return the status of this record.
     */
    @NotNull PunishmentStatus getStatus();

    /**
     * Gets the {@link UUID} of the player who ended this record.
     * @return the UUID of the staff member, or null if the console ended it.
     */
    @Nullable UUID getTerminatingStaff();

    /**
     * Gets the {@link LocalDateTime} at which this record will/did end.
     * @return the date at which this record ends/ended.
     */
    @Nullable LocalDateTime getTerminationDate();

    /**
     * Gets the IP address of this record, if applicable.
     * @return the IP address of this record, or null if it is not applicable.
     */
    @Nullable String getIpAddress();

    /**
     * Gets the reason this record was ended, or null if it hasn't ended or expired.
     * @return the reason if applicable, or null.
     */
    @Nullable String getTerminationReason();

    /**
     * Gets the total duration of this record.
     * @return the duration of this record, or null if it is non-temporary.
     */
    @Nullable Duration getDuration();

    /**
     * Gets the scope of this record.
     * @return the {@link ActionScope} of this record.
     */
    @NotNull ActionScope getScope();

    /**
     * Determines if this record has an IP address associated with it.
     * @return true if this record has an IP address, false otherwise.
     */
    default boolean hasIpAddress() {
        return getIpAddress() != null;
    }

    /**
     * Determines if this record is temporary.
     * @return true if this record is temporary, false otherwise.
     */
    default boolean isTemporary() {
        return getStatus() == PunishmentStatus.EXPIRED || getTerminationDate() != null;
    }

    /**
     * Determines if this record has ended.
     * @return true if this record has ended, false otherwise.
     */
    default boolean hasEnded() {
        return getStatus() == PunishmentStatus.ENDED;
    }

}
