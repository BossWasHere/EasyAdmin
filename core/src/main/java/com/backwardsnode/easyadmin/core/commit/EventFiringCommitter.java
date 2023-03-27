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

package com.backwardsnode.easyadmin.core.commit;

import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.event.admin.AdminEventSource;
import com.backwardsnode.easyadmin.api.record.*;
import com.backwardsnode.easyadmin.core.EasyAdminService;
import com.backwardsnode.easyadmin.core.event.admin.BanEventImpl;
import com.backwardsnode.easyadmin.core.event.admin.CommentEventImpl;
import com.backwardsnode.easyadmin.core.event.admin.KickEventImpl;
import com.backwardsnode.easyadmin.core.event.admin.MuteEventImpl;
import com.backwardsnode.easyadmin.core.record.BanRecordImpl;
import com.backwardsnode.easyadmin.core.record.CommentRecordImpl;
import com.backwardsnode.easyadmin.core.record.KickRecordImpl;
import com.backwardsnode.easyadmin.core.record.MuteRecordImpl;

import java.util.EnumSet;

public class EventFiringCommitter implements Committer {

    private final EasyAdminService service;
    private final AdminEventSource source;
    private final EnumSet<CommitterMode> modes;

    public EventFiringCommitter(EasyAdminService service, AdminEventSource source, EnumSet<CommitterMode> modes) {
        this.service = service;
        this.source = source;
        this.modes = modes;
    }

    @Override
    public CommitResult<BanRecord> commit(BanRecordImpl record, CommandExecutor executor) {
        if (modes.contains(CommitterMode.DUMMY)) {
            return new CommitResult<>(record, CommitStatus.COMMITTED);
        }

        if (!modes.contains(CommitterMode.SKIP_EVENTS)) {
            BanEventImpl event = new BanEventImpl(service, source, record, executor,
                    modes.contains(CommitterMode.EVENT_ALLOW_CANCELLATIONS),
                    modes.contains(CommitterMode.EVENT_ALLOW_MODIFICATIONS));

            service.getEventBus().call(event);
            record = event.getBanRecord();

            if (event.isCancelled()) {
                return new CommitResult<>(record, CommitStatus.CANCELLED);
            }
        }

        // TODO commit it

        return new CommitResult<>(record, CommitStatus.COMMITTED);
    }

    @Override
    public CommitResult<MuteRecord> commit(MuteRecordImpl record, CommandExecutor executor) {
        if (modes.contains(CommitterMode.DUMMY)) {
            return new CommitResult<>(record, CommitStatus.COMMITTED);
        }

        if (!modes.contains(CommitterMode.SKIP_EVENTS)) {
            MuteEventImpl event = new MuteEventImpl(service, source, record, executor,
                    modes.contains(CommitterMode.EVENT_ALLOW_CANCELLATIONS),
                    modes.contains(CommitterMode.EVENT_ALLOW_MODIFICATIONS));

            service.getEventBus().call(event);
            record = event.getMuteRecord();

            if (event.isCancelled()) {
                return new CommitResult<>(record, CommitStatus.CANCELLED);
            }
        }

        // TODO commit it

        return new CommitResult<>(record, CommitStatus.COMMITTED);
    }

    @Override
    public CommitResult<CommentRecord> commit(CommentRecordImpl record, CommandExecutor executor) {
        if (modes.contains(CommitterMode.DUMMY)) {
            return new CommitResult<>(record, CommitStatus.COMMITTED);
        }

        if (!modes.contains(CommitterMode.SKIP_EVENTS)) {
            CommentEventImpl event = new CommentEventImpl(service, source, record, executor,
                    modes.contains(CommitterMode.EVENT_ALLOW_CANCELLATIONS),
                    modes.contains(CommitterMode.EVENT_ALLOW_MODIFICATIONS));

            service.getEventBus().call(event);
            record = event.getCommentRecord();

            if (event.isCancelled()) {
                return new CommitResult<>(record, CommitStatus.CANCELLED);
            }
        }

        // TODO commit it

        return new CommitResult<>(record, CommitStatus.COMMITTED);
    }

    @Override
    public CommitResult<KickRecord> commit(KickRecordImpl record, CommandExecutor executor) {
        if (modes.contains(CommitterMode.DUMMY)) {
            return new CommitResult<>(record, CommitStatus.COMMITTED);
        }

        if (!modes.contains(CommitterMode.SKIP_EVENTS)) {
            KickEventImpl event = new KickEventImpl(service, source, record, executor,
                    modes.contains(CommitterMode.EVENT_ALLOW_CANCELLATIONS),
                    modes.contains(CommitterMode.EVENT_ALLOW_MODIFICATIONS));

            service.getEventBus().call(event);
            record = event.getKickRecord();

            if (event.isCancelled()) {
                return new CommitResult<>(record, CommitStatus.CANCELLED);
            }
        }

        // TODO commit it

        return new CommitResult<>(record, CommitStatus.COMMITTED);
    }
}
