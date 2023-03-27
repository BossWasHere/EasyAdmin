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

import com.backwardsnode.easyadmin.api.builder.KickBuilder;
import com.backwardsnode.easyadmin.api.record.CommitResult;
import com.backwardsnode.easyadmin.api.record.CommitStatus;
import com.backwardsnode.easyadmin.api.record.KickRecord;
import com.backwardsnode.easyadmin.core.commit.Committer;
import com.backwardsnode.easyadmin.core.record.KickRecordImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class KickBuilderImpl implements KickBuilder {

    private final Committer committer;
    private final UUID playerUUID;
    private UUID staffUUID;
    private LocalDateTime kickDate;
    private boolean isGlobal;
    private String kickReason;
    private boolean shouldPerform;

    public KickBuilderImpl(final Committer committer, final UUID playerUUID) {
        this.committer = committer;
        this.playerUUID = Objects.requireNonNull(playerUUID);
        this.kickDate = LocalDateTime.now();
    }

    @Override
    public @NotNull KickBuilder byStaff(@Nullable UUID staffUUID) {
        this.staffUUID = staffUUID;
        return this;
    }

    @Override
    public @NotNull KickBuilder withKickDate(@NotNull LocalDateTime kickDate) {
        this.kickDate = Objects.requireNonNull(kickDate);
        return this;
    }

    @Override
    public @NotNull KickBuilder setGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
        return this;
    }

    @Override
    public @NotNull KickBuilder withKickReason(@Nullable String kickReason) {
        this.kickReason = kickReason;
        return this;
    }

    @Override
    public @NotNull KickBuilder actuallyPerform(boolean shouldPerform) {
        this.shouldPerform = shouldPerform;
        return this;
    }

    @Override
    public @NotNull KickRecordImpl build() {
        return new KickRecordImpl(playerUUID, staffUUID, kickDate, isGlobal, kickReason);
    }

    @Override
    public @NotNull CommitResult<KickRecord> buildAndCommit() {
        return committer.commit(build(), null);
    }
}
