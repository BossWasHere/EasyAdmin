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

package com.backwardsnode.easyadmin.api.builder;

import com.backwardsnode.easyadmin.api.builder.ext.CommitableBuilder;
import com.backwardsnode.easyadmin.api.builder.ext.StaffBuilder;
import com.backwardsnode.easyadmin.api.record.CommentRecord;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

/**
 * A builder for creating comment and warning records.
 * @see RecordBuilder
 */
public interface CommentBuilder extends StaffBuilder<CommentBuilder>, CommitableBuilder<CommentRecord> {

    /**
     * Sets the creation date for the comment record. Most applications will not need to use this method,
     * as the comment date will default to the current system time.
     * @param commentDate the comment date.
     * @return this builder, for chaining.
     */
    @NotNull CommentBuilder withCommentDate(@NotNull LocalDateTime commentDate);

    /**
     * Sets whether the comment is a warning or not. By default, the builder will create a comment.
     * @param isWarning whether the comment is a warning or not.
     * @return this builder, for chaining.
     */
    @NotNull CommentBuilder setWarning(boolean isWarning);

}
