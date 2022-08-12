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

package com.backwardsnode.easyadmin.api.record;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a comment for a specific player
 * <p>A new record can be created by calling the public constructor.</p>
 */
public final class CommentRecord implements LiveRecord<Integer>, AdminRecord {

    private transient boolean _loaded;

    private int id;
    private UUID player;
    private UUID staff;
    private LocalDateTime commentDate;
    private boolean isWarning;
    private String comment;

    CommentRecord(boolean loaded, int id, UUID player, UUID staff, LocalDateTime commentDate, boolean isWarning, String comment) {
        this._loaded = loaded;
        this.id = id;
        this.player = player;
        this.staff = staff;
        this.commentDate = commentDate;
        this.isWarning = isWarning;
        this.comment = comment;
    }

    /**
     * Creates a new comment record.
     * @param player the player this comment is made about.
     * @param staff the staff member who made the comment, or null if issued by the console.
     * @param commentDate the date and time the comment was made.
     * @param isWarning true if this is a warning comment, false if it is a regular comment.
     * @param comment the comment itself.
     */
    public CommentRecord(@NotNull UUID player, @Nullable UUID staff, @NotNull LocalDateTime commentDate, boolean isWarning, @NotNull String comment) {
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
    public @Nullable UUID getStaff() {
        return staff;
    }

    @Override
    public @NotNull LocalDateTime getDateAdded() {
        return commentDate;
    }

    /**
     * Gets the type of comment this is.
     * @return true if this is a warning comment, false if it is a regular comment.
     */
    public boolean isWarning() {
        return isWarning;
    }

    /**
     * Gets the comment itself.
     * @return the comment.
     */
    public @NotNull String getComment() {
        return comment;
    }

}
