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

import com.backwardsnode.easyadmin.api.commit.CommitException;
import com.backwardsnode.easyadmin.api.commit.CommitResult;
import com.backwardsnode.easyadmin.api.commit.CommitStatus;
import com.backwardsnode.easyadmin.api.commit.Committer;
import com.backwardsnode.easyadmin.api.data.AdminAction;
import com.backwardsnode.easyadmin.api.data.ServiceSource;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.record.BanRecord;
import com.backwardsnode.easyadmin.api.record.CommentRecord;
import com.backwardsnode.easyadmin.api.record.KickRecord;
import com.backwardsnode.easyadmin.api.record.MuteRecord;
import com.backwardsnode.easyadmin.api.record.base.LiveRecord;
import com.backwardsnode.easyadmin.core.EasyAdminService;
import com.backwardsnode.easyadmin.core.event.admin.BanEventImpl;
import com.backwardsnode.easyadmin.core.event.admin.CommentEventImpl;
import com.backwardsnode.easyadmin.core.event.admin.KickEventImpl;
import com.backwardsnode.easyadmin.core.event.admin.MuteEventImpl;
import com.backwardsnode.easyadmin.core.record.*;

import java.util.EnumSet;

public final class EventFiringCommitter implements Committer {

    private final EasyAdminService service;
    private final ServiceSource source;

    private final EnumSet<CommitterMode> modes;
    private final boolean allowCancellations;
    private final boolean allowModifications;

    public EventFiringCommitter(EasyAdminService service, ServiceSource source, EnumSet<CommitterMode> modes) {
        this.service = service;
        this.source = source;
        this.modes = modes;
        this.allowCancellations = modes.contains(CommitterMode.EVENT_ALLOW_CANCELLATIONS);
        this.allowModifications = modes.contains(CommitterMode.EVENT_ALLOW_MODIFICATIONS);
    }

    @Override
    public <T extends LiveRecord<?>> CommitResult<T> commit(T record) throws CommitException {
        return commitByExecutor(record, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends LiveRecord<?>> CommitResult<T> commitByExecutor(T record, CommandExecutor executor) throws CommitException {
        if (modes.contains(CommitterMode.DUMMY)) {
            return new CommitResult<>(record, CommitStatus.COMMITTED);
        }

        if (record instanceof PlayerRecordImpl playerRecord) {

            // TODO

        } else if (record instanceof BanRecordImpl banRecord) {

            return (CommitResult<T>) commit(banRecord, executor);

        } else if (record instanceof MuteRecordImpl muteRecord) {

            return (CommitResult<T>) commit(muteRecord, executor);

        } else if (record instanceof KickRecordImpl kickRecord) {

            return (CommitResult<T>) commit(kickRecord, executor);

        } else if (record instanceof CommentRecordImpl commentRecord) {

            return (CommitResult<T>) commit(commentRecord, executor);

        }

        throw new CommitException(record.getClass());
    }

    private CommitResult<BanRecord> commit(BanRecordImpl record, CommandExecutor executor) {
        OfflinePlayer player = service.getPlugin().getOfflinePlayer(record.getPlayer());
        if (!player.isRegistered() && !modes.contains(CommitterMode.ALLOW_TARGETING_UNREGISTERED)) {
            return new CommitResult<>(record, CommitStatus.CANCELLED_NOEXIST);
        }

        if (service.getPermissionsPlatform().isPlayerImmune(player, AdminAction.BAN)) {
            return new CommitResult<>(record, CommitStatus.CANCELLED_IMMUNE);
        }

        // TODO CHECK_EXISTING

        if (modes.contains(CommitterMode.CHECK_WITHHOLD)) {
            if (service.getWithholdAgent().withholdBan(record)) {
                return new CommitResult<>(record, CommitStatus.WITHHELD);
            }
        }

        if (!modes.contains(CommitterMode.SKIP_EVENTS)) {
            BanEventImpl event = new BanEventImpl(service, source, record, executor,
                    allowCancellations, allowModifications);

            service.getEventBus().call(event);

            if (event.isCancelled()) {
                return new CommitResult<>(record, CommitStatus.CANCELLED);
            }
            record = event.getCurrent();
        }

        service.getEnforcer().enforceBan(record);
        service.getDatabaseController().insertBan(record);

        return new CommitResult<>(record, CommitStatus.COMMITTED);
    }

    private CommitResult<MuteRecord> commit(MuteRecordImpl record, CommandExecutor executor) {
        OfflinePlayer player = service.getPlugin().getOfflinePlayer(record.getPlayer());
        if (!player.isRegistered() && !modes.contains(CommitterMode.ALLOW_TARGETING_UNREGISTERED)) {
            return new CommitResult<>(record, CommitStatus.CANCELLED_NOEXIST);
        }

        if (service.getPermissionsPlatform().isPlayerImmune(player, AdminAction.MUTE)) {
            return new CommitResult<>(record, CommitStatus.CANCELLED_IMMUNE);
        }

        // TODO CHECK_EXISTING

        if (modes.contains(CommitterMode.CHECK_WITHHOLD)) {
            if (service.getWithholdAgent().withholdMute(record)) {
                return new CommitResult<>(record, CommitStatus.WITHHELD);
            }
        }

        if (!modes.contains(CommitterMode.SKIP_EVENTS)) {
            MuteEventImpl event = new MuteEventImpl(service, source, record, executor,
                    allowCancellations, allowModifications);

            service.getEventBus().call(event);
            record = event.getCurrent();

            if (event.isCancelled()) {
                return new CommitResult<>(record, CommitStatus.CANCELLED);
            }
        }

        service.getEnforcer().enforceMute(record);
        service.getDatabaseController().insertMute(record);

        return new CommitResult<>(record, CommitStatus.COMMITTED);
    }

    private CommitResult<CommentRecord> commit(CommentRecordImpl record, CommandExecutor executor) {
        OfflinePlayer player = service.getPlugin().getOfflinePlayer(record.getPlayer());
        if (!player.isRegistered() && !modes.contains(CommitterMode.ALLOW_TARGETING_UNREGISTERED)) {
            return new CommitResult<>(record, CommitStatus.CANCELLED_NOEXIST);
        }

        boolean immuneToWarnings = service.getPermissionsPlatform().isPlayerImmune(player, AdminAction.WARN);
        boolean immuneToComments = service.getPermissionsPlatform().isPlayerImmune(player, AdminAction.COMMENT);

        if (record.isWarning() ? immuneToWarnings : immuneToComments) {
            return new CommitResult<>(record, CommitStatus.CANCELLED_IMMUNE);
        }

        // TODO CHECK_EXISTING

        if (modes.contains(CommitterMode.CHECK_WITHHOLD)) {
            if (service.getWithholdAgent().withholdComment(record)) {
                return new CommitResult<>(record, CommitStatus.WITHHELD);
            }
        }

        if (!modes.contains(CommitterMode.SKIP_EVENTS)) {
            CommentEventImpl event = new CommentEventImpl(service, source, record, executor,
                    allowCancellations, allowModifications, !immuneToWarnings && !immuneToComments);

            service.getEventBus().call(event);
            record = event.getCurrent();

            if (event.isCancelled()) {
                return new CommitResult<>(record, CommitStatus.CANCELLED);
            }
        }

        service.getEnforcer().enforceComment(record);
        service.getDatabaseController().insertComment(record);

        return new CommitResult<>(record, CommitStatus.COMMITTED);
    }

    private CommitResult<KickRecord> commit(KickRecordImpl record, CommandExecutor executor) {
        OfflinePlayer player = service.getPlugin().getOnlinePlayer(record.getPlayer());
        if (player == null) {
            return new CommitResult<>(record, CommitStatus.IMPOSSIBLE);
        }

        if (service.getPermissionsPlatform().isPlayerImmune(player, AdminAction.KICK)) {
            return new CommitResult<>(record, CommitStatus.CANCELLED_IMMUNE);
        }

        // TODO CHECK_EXISTING

        if (modes.contains(CommitterMode.CHECK_WITHHOLD)) {
            if (service.getWithholdAgent().withholdKick(record)) {
                return new CommitResult<>(record, CommitStatus.WITHHELD);
            }
        }

        if (!modes.contains(CommitterMode.SKIP_EVENTS)) {
            KickEventImpl event = new KickEventImpl(service, source, record, executor,
                    allowCancellations, allowModifications);

            service.getEventBus().call(event);
            record = event.getCurrent();

            if (event.isCancelled()) {
                return new CommitResult<>(record, CommitStatus.CANCELLED);
            }
        }

        service.getEnforcer().enforceKick(record);
        service.getDatabaseController().insertKick(record);

        return new CommitResult<>(record, CommitStatus.COMMITTED);
    }
}
