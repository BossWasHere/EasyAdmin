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

import com.backwardsnode.easyadmin.api.builder.BanBuilder;
import com.backwardsnode.easyadmin.api.builder.BuilderException;
import com.backwardsnode.easyadmin.api.commit.CommitException;
import com.backwardsnode.easyadmin.api.commit.Committer;
import com.backwardsnode.easyadmin.api.record.BanRecord;
import com.backwardsnode.easyadmin.api.commit.CommitResult;
import com.backwardsnode.easyadmin.core.record.BanRecordImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class BanBuilderImpl implements BanBuilder {

    private final Committer committer;
    private final UUID playerUUID;
    private final String address;
    private UUID staffUUID;
    private LocalDateTime banDate;
    private LocalDateTime unbanDate;
    private String banReason;
    private String context;

    public BanBuilderImpl(final Committer committer, final UUID playerUUID, final String address) {
        if (playerUUID == null && address == null) {
            throw new NullPointerException("Player UUID and address cannot both be null");
        }
        this.committer = committer;
        this.playerUUID = playerUUID;
        this.address = address;
        this.banDate = LocalDateTime.now();
    }

    @Override
    public @NotNull BanBuilder byStaff(@Nullable UUID staffUUID) {
        this.staffUUID = staffUUID;
        return this;
    }

    @Override
    public @NotNull BanBuilder withBanDate(@NotNull LocalDateTime banDate) {
        this.banDate = Objects.requireNonNull(banDate);
        return this;
    }

    @Override
    public @NotNull BanBuilder withUnbanOn(@Nullable LocalDateTime unbanDate) {
        this.unbanDate = unbanDate;
        return this;
    }

    @Override
    public @NotNull BanBuilder withUnbanAfter(@Nullable Duration unbanDuration) {
        this.unbanDate = unbanDuration == null ? null : banDate.plus(unbanDuration);
        return this;
    }

    @Override
    public @NotNull BanBuilder withBanReason(@Nullable String banReason) {
        this.banReason = banReason;
        return this;
    }

    @Override
    public @NotNull BanBuilder withContext(@Nullable String context) {
        this.context = context;
        return this;
    }

    @Override
    public @NotNull BanRecordImpl build() throws BuilderException {
        if (unbanDate != null && unbanDate.isBefore(banDate)) {
            throw new BuilderException("Unban date cannot be before ban date");
        }
        return new BanRecordImpl(playerUUID, staffUUID, banDate, unbanDate, address, banReason, context);
    }

    @Override
    public @NotNull CommitResult<BanRecord> buildAndCommit() throws BuilderException, CommitException {
        return committer.commit(build());
    }

}
