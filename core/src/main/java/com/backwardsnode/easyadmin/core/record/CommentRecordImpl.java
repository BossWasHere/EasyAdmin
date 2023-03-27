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

import com.backwardsnode.easyadmin.api.record.CommentRecord;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

public class CommentRecordImpl implements CommentRecord, MutableRecordProvider<MutableCommentRecordImpl> {

    protected transient boolean _loaded;

    protected int id;
    protected UUID player;
    protected UUID staff;
    protected LocalDateTime commentDate;
    protected boolean isWarning;
    protected String comment;

    CommentRecordImpl(boolean loaded, int id, UUID player, UUID staff, LocalDateTime commentDate, boolean isWarning, String comment) {
        this._loaded = loaded;
        this.id = id;
        this.player = player;
        this.staff = staff;
        this.commentDate = commentDate;
        this.isWarning = isWarning;
        this.comment = comment;
    }

    protected CommentRecordImpl(CommentRecordImpl source) {
        this(source._loaded, source.id, source.player, source.staff, source.commentDate, source.isWarning, source.comment);
    }

    public CommentRecordImpl(@NotNull UUID player, @Nullable UUID staff, @NotNull LocalDateTime commentDate, boolean isWarning, @NotNull String comment) {
        this(false, -1, player, staff, commentDate, isWarning, comment);
    }

    @Override
    public boolean isLoaded() {
        return _loaded;
    }

    @Override
    public @NotNull Integer getId() {
        return id;
    }

    @Override
    public @NotNull UUID getPlayer() {
        return player;
    }

    @Override
    public @Nullable UUID getAuthor() {
        return staff;
    }

    @Override
    public @NotNull LocalDateTime getDateAdded() {
        return commentDate;
    }

    public boolean isWarning() {
        return isWarning;
    }

    public @NotNull String getComment() {
        return comment;
    }

    @Override
    public @NotNull MutableCommentRecordImpl asMutable() {
        return new MutableCommentRecordImpl(this);
    }

}
