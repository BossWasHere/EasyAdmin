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

package com.backwardsnode.easyadmin.core.commands;

import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.builder.KickBuilder;
import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import com.backwardsnode.easyadmin.api.commit.CommitResult;
import com.backwardsnode.easyadmin.api.data.ServiceSource;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.internal.MessageKey;
import com.backwardsnode.easyadmin.api.record.KickRecord;
import com.backwardsnode.easyadmin.core.command.Command;
import com.backwardsnode.easyadmin.core.command.CommandData;
import com.backwardsnode.easyadmin.core.command.CommandRegistration;
import com.backwardsnode.easyadmin.core.command.ExecutionStatus;
import com.backwardsnode.easyadmin.core.command.args.ArgumentResult;
import com.backwardsnode.easyadmin.core.command.args.ArgumentSelector;
import com.backwardsnode.easyadmin.core.commands.data.KickData;
import com.backwardsnode.easyadmin.core.i18n.CommonMessages;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Kick implements Command<KickData> {

    private static final String COMMAND = "kick";
    private static final String SHORTHAND_COMMAND = "k";
    private static final String COMMAND_GLOBAL = "gkick";
    private static final String SHORTHAND_COMMAND_GLOBAL = "gk";

    private final boolean allowGlobal, allowLocal;

    public Kick(boolean allowGlobal, boolean allowLocal) {
        this.allowGlobal = allowGlobal;
        this.allowLocal = allowLocal;
    }

    @Override
    public CommandRegistration getRegistration() {
        Map<String, String[]> entries = new HashMap<>(2);
        if (allowLocal) {
            entries.put(COMMAND, new String[] { SHORTHAND_COMMAND });
        }
        if (allowGlobal) {
            entries.put(COMMAND_GLOBAL, new String[] { SHORTHAND_COMMAND_GLOBAL });
        }
        return new CommandRegistration(COMMAND, Collections.unmodifiableMap(entries));
    }

    @Override
    public KickData loadState(EasyAdminPlugin instance, CommandExecutor executor, CommandData data) {
        String cmd = data.command();
        return new KickData(!allowLocal || cmd.equals(COMMAND_GLOBAL) || cmd.equals(SHORTHAND_COMMAND_GLOBAL));
    }

    @Override
    public boolean processArgs(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, KickData state) {
        ArgumentSelector selector = new ArgumentSelector(instance, data.args());

        ArgumentResult<OnlinePlayer> player = selector.readOnlinePlayerName();
        if (!player.isValid()) {
            return false;
        }
        state.setPlayer(player.value());
        state.setReason(selector.readRemainingString());

        return true;
    }

    @Override
    public MessageKey getDescription(CommandExecutor executor, CommandData data, KickData state) {
        if (state.isGlobal()) {
            return CommonMessages.ADMINISTRATIVE.KICK.KICK_ALL_DESC;
        }

        return CommonMessages.ADMINISTRATIVE.KICK.KICK_DESC;
    }

    @Override
    public MessageKey getUsageMessage(CommandExecutor executor, CommandData data, KickData state) {
        if (state.isGlobal()) {
            return CommonMessages.ADMINISTRATIVE.KICK.KICK_ALL_USAGE;
        }

        return CommonMessages.ADMINISTRATIVE.KICK.KICK_USAGE;
    }

    @Override
    public ExecutionStatus execute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, KickData state) {
        UUID staffUUID = executor instanceof OfflinePlayer player ? player.getUUID() : null;
        RecordBuilder builder = instance.getRecordBuilderFor(ServiceSource.COMMAND);
        KickBuilder kickBuilder =
                builder.kickPlayer(state.getPlayer().getUUID())
                        .byStaff(staffUUID)
                        .setGlobal(state.isGlobal());

        if (state.hasReason()) {
            kickBuilder.withKickReason(state.getReason());
        }

        try {
            CommitResult<KickRecord> result = kickBuilder.buildAndCommit();
            switch (result.status()) {
                case COMMITTED:
                    if (state.isGlobal()) {
                        executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.KICK.KICKED_ALL, state.getPlayer().getUsername(), state.getReason());
                    } else {
                        executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.KICK.KICKED, state.getPlayer().getUsername(), result.record().getServerName(), state.getReason());
                    }
                    break;
                case CANCELLED:
                    executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.CANCELLED.KICK);
                    break;
                case CANCELLED_DUPLICATE:
                    // should never happen
                    break;
                case CANCELLED_IMMUNE:
                    executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.PLAYER_IMMUNE);
                    break;
                case IMPOSSIBLE:
                    executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.PLAYER_OFFLINE);
                    break;
                case WITHHELD:
                    executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.WITHHELD.KICK);
                    break;
            }
        } catch (Exception e) {
            executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.ERROR, e.getClass().getName());
            e.printStackTrace();
            return ExecutionStatus.ERROR;
        }

        return ExecutionStatus.SUCCESS;
    }

}
