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

package com.backwardsnode.easyadmin.core.record;

import com.backwardsnode.easyadmin.api.record.mutable.MutableCommentRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public final class MutableCommentRecordImpl extends CommentRecordImpl implements MutableCommentRecord, ImmutableRecordProvider<CommentRecordImpl> {

    private final CommentRecordImpl source;

    private boolean isModified = false;

    MutableCommentRecordImpl(CommentRecordImpl source) {
        super(source);
        this.source = source;
    }

    @Override
    public boolean canSwitchWarningMode() {
        return !isLoaded();
    }

    @Override
    public void setComment(@NotNull String comment) {
        if (!Objects.equals(this.comment, comment)) {
            this.comment = Objects.requireNonNull(comment);
            isModified = true;
        }
    }

    @Override
    public void setWarning(boolean warning) throws IllegalStateException {
        if (!canSwitchWarningMode()) {
            throw new IllegalStateException("Cannot switch comment type of a loaded record");
        }
        if (this.isWarning != warning) {
            this.isWarning = warning;
            isModified = true;
        }
    }

    @Override
    public void setAuthor(@Nullable UUID author) {
        if (!Objects.equals(this.staff, author)) {
            this.staff = author;
            isModified = true;
        }
    }

    @Override
    public boolean isModified() {
        return isModified;
    }

    @Override
    public CommentRecordImpl getOriginal() {
        return source;
    }

    @Override
    public CommentRecordImpl asImmutable() {
        return isModified ? new CommentRecordImpl(this) : source;
    }
}
