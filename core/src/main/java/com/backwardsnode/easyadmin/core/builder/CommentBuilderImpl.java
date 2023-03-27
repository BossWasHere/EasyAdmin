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

import com.backwardsnode.easyadmin.api.builder.CommentBuilder;
import com.backwardsnode.easyadmin.api.commit.CommitException;
import com.backwardsnode.easyadmin.api.commit.Committer;
import com.backwardsnode.easyadmin.api.record.CommentRecord;
import com.backwardsnode.easyadmin.api.commit.CommitResult;
import com.backwardsnode.easyadmin.core.record.CommentRecordImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class CommentBuilderImpl implements CommentBuilder {

    private final Committer committer;
    private final UUID playerUUID;
    private final String comment;
    private UUID staffUUID;
    private LocalDateTime commentDate;
    private boolean isWarning;

    public CommentBuilderImpl(final Committer committer, final UUID playerUUID, final String comment) {
        this.committer = committer;
        this.playerUUID = Objects.requireNonNull(playerUUID);
        this.comment = Objects.requireNonNull(comment);
        this.commentDate = LocalDateTime.now();
    }

    @Override
    public @NotNull CommentBuilder byStaff(@Nullable UUID staffUUID) {
        this.staffUUID = staffUUID;
        return this;
    }

    @Override
    public @NotNull CommentBuilder withCommentDate(@NotNull LocalDateTime commentDate) {
        this.commentDate = Objects.requireNonNull(commentDate);
        return this;
    }

    @Override
    public @NotNull CommentBuilder setWarning(boolean isWarning) {
        this.isWarning = isWarning;
        return this;
    }

    @Override
    public @NotNull CommentRecordImpl build() {
        return new CommentRecordImpl(playerUUID, staffUUID, commentDate, isWarning, comment);
    }

    @Override
    public @NotNull CommitResult<CommentRecord> buildAndCommit() throws CommitException {
        return committer.commit(build());
    }
}
