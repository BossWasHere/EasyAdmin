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
import com.backwardsnode.easyadmin.api.event.admin.AdminEventSource;
import com.backwardsnode.easyadmin.api.event.admin.KickEvent;
import com.backwardsnode.easyadmin.api.record.KickRecord;
import com.backwardsnode.easyadmin.api.record.mutable.MutableKickRecord;
import com.backwardsnode.easyadmin.core.event.base.CancellableEvent;
import com.backwardsnode.easyadmin.core.exception.ImmutableEventException;
import com.backwardsnode.easyadmin.core.record.KickRecordImpl;
import com.backwardsnode.easyadmin.core.record.MutableKickRecordImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class KickEventImpl extends CancellableEvent implements KickEvent {

    private final AdminEventSource source;
    private final KickRecordImpl kickRecord;
    private final CommandExecutor executor;
    private final boolean modifiable;

    private MutableKickRecordImpl mutableRecord;

    public KickEventImpl(EasyAdmin instance, AdminEventSource source, KickRecordImpl kickRecord,
                         CommandExecutor executor, boolean cancellable, boolean modifiable) {
        super(instance, KickEvent.class, cancellable);
        this.source = source;
        this.kickRecord = kickRecord;
        this.executor = executor;
        this.modifiable = modifiable;
    }

    @Override
    public @NotNull AdminEventSource getSource() {
        return source;
    }

    @Override
    public @NotNull KickRecordImpl getKickRecord() {
        return isModified() ? mutableRecord.asImmutable() : kickRecord;
    }

    @Override
    public @Nullable CommandExecutor getExecutor() {
        return executor;
    }

    @Override
    public boolean isModified() {
        return mutableRecord != null && mutableRecord.isModified();
    }

    @Override
    public boolean canModify() {
        return modifiable;
    }

    @Override
    public KickRecord getOriginal() {
        return kickRecord;
    }

    @Override
    public MutableKickRecord getSharedMutable() {
        if (!modifiable) {
            throw new ImmutableEventException();
        }
        if (mutableRecord == null) {
            mutableRecord = kickRecord.asMutable();
        }
        return mutableRecord;
    }

}
