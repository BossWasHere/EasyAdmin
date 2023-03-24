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

package com.backwardsnode.easyadmin.core.event.admin;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.event.admin.CommentEvent;
import com.backwardsnode.easyadmin.api.record.CommentRecord;
import com.backwardsnode.easyadmin.core.event.base.CancellableEvent;
import org.jetbrains.annotations.NotNull;

public class CommentEventImpl extends CancellableEvent implements CommentEvent {

    private final OfflinePlayer player;
    private final AdminEventSource source;
    private final CommentRecord commentRecord;
    private final CommandExecutor staff;

    public CommentEventImpl(EasyAdmin instance, OfflinePlayer player,
                            AdminEventSource source, CommentRecord commentRecord, CommandExecutor staff) {
        super(instance, CommentEventImpl.class);
        this.player = player;
        this.source = source;
        this.commentRecord = commentRecord;
        this.staff = staff;
    }

    @Override
    public @NotNull OfflinePlayer getPlayer() {
        return player;
    }

    @Override
    public @NotNull AdminEventSource getSource() {
        return source;
    }

    @Override
    public @NotNull CommentRecord getCommentRecord() {
        return commentRecord;
    }

    @Override
    public @NotNull CommandExecutor getStaff() {
        return staff;
    }
}
