/*
 * MIT License
 *
 * Copyright (c) 2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

package com.backwardsnode.easyadmin.core.builder;

import com.backwardsnode.easyadmin.api.builder.BuilderException;
import com.backwardsnode.easyadmin.api.builder.MuteBuilder;
import com.backwardsnode.easyadmin.api.record.CommitResult;
import com.backwardsnode.easyadmin.api.record.CommitStatus;
import com.backwardsnode.easyadmin.api.record.MuteRecord;
import com.backwardsnode.easyadmin.core.record.MuteRecordImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class MuteBuilderImpl implements MuteBuilder {

    private final UUID playerUUID;
    private final String address;
    private UUID staffUUID;
    private LocalDateTime muteDate;
    private LocalDateTime unmuteDate;
    private String muteReason;
    private String context;

    public MuteBuilderImpl(UUID playerUUID, String address) {
        if (playerUUID == null && address == null) {
            throw new NullPointerException("Player UUID and address cannot both be null");
        }
        this.playerUUID = playerUUID;
        this.address = address;
        this.muteDate = LocalDateTime.now();
    }

    @Override
    public @NotNull MuteBuilder byStaff(@Nullable UUID staffUUID) {
        this.staffUUID = staffUUID;
        return this;
    }

    @Override
    public @NotNull MuteBuilder withMuteDate(@NotNull LocalDateTime muteDate) {
        this.muteDate = Objects.requireNonNull(muteDate);
        return this;
    }

    @Override
    public @NotNull MuteBuilder withUnmuteOn(@Nullable LocalDateTime unmuteDate) {
        this.unmuteDate = unmuteDate;
        return this;
    }

    @Override
    public @NotNull MuteBuilder withUnmuteAfter(@Nullable Duration unmuteDuration) {
        this.unmuteDate = unmuteDuration == null ? null : muteDate.plus(unmuteDuration);
        return this;
    }

    @Override
    public @NotNull MuteBuilder withMuteReason(@Nullable String muteReason) {
        this.muteReason = muteReason;
        return this;
    }

    @Override
    public @NotNull MuteBuilder withContext(@Nullable String context) {
        this.context = context;
        return this;
    }

    @Override
    public @NotNull MuteRecord build() throws BuilderException {
        if (unmuteDate != null && unmuteDate.isBefore(muteDate)) {
            throw new BuilderException("Unmute date cannot be before mute date");
        }
        return new MuteRecordImpl(playerUUID, staffUUID, muteDate, unmuteDate, address, muteReason, context);
    }

    @Override
    public @NotNull CommitResult<MuteRecord> buildAndCommit() throws BuilderException {
        MuteRecord record = build();

        // TODO commit it

        return new CommitResult<>(record, CommitStatus.COMMITTED);
    }

}
