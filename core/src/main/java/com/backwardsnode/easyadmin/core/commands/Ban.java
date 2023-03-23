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
import com.backwardsnode.easyadmin.api.admin.AdminManager;
import com.backwardsnode.easyadmin.api.data.ActionScope;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.core.command.CommandData;
import com.backwardsnode.easyadmin.core.command.ExecutionStatus;
import com.backwardsnode.easyadmin.core.command.ScopedCommand;
import com.backwardsnode.easyadmin.core.command.args.ArgumentResult;
import com.backwardsnode.easyadmin.core.command.args.ArgumentSelector;
import com.backwardsnode.easyadmin.core.commands.data.TemporalScopedData;
import com.backwardsnode.easyadmin.core.i18n.CommonMessages;
import com.backwardsnode.easyadmin.core.i18n.MessageKey;

import java.time.Duration;
import java.util.UUID;

public class Ban extends ScopedCommand<TemporalScopedData> {

    private static final String COMMAND = "ban";
    private static final String SHORTHAND_COMMAND = "b";

    public Ban(ActionScope enabledScopes) {
        super(enabledScopes);
    }

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public String getShorthandCommand() {
        return SHORTHAND_COMMAND;
    }

    @Override
    public TemporalScopedData createDefaultState() {
        return new TemporalScopedData(ExecutionStatus.SUCCESS);
    }

    @Override
    public boolean processArgs(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, TemporalScopedData state) {
        ArgumentSelector selector = new ArgumentSelector(instance, data.args());

        ArgumentResult<OfflinePlayer> player = selector.readPlayerName();
        if (!player.isValid()) {
            return false;
        }
        state.setPlayer(player.value());

        if (state.getScope().isTemporary()) {
            ArgumentResult<Duration> duration = selector.readDuration();
            if (!duration.isValid()) {
                return false;
            }
            state.setDuration(duration.value());
        }
        ArgumentResult<String> context = selector.readSingleArgument();
        if (!context.isValid()) {
            return false;
        }
        state.setContext(context.value());
        state.setReason(selector.readRemainingString());

        return true;
    }

    @Override
    public MessageKey getDescription(CommandExecutor executor, CommandData data, TemporalScopedData state) {
        return switch (state.getScope()) {
            case DEFAULT -> CommonMessages.ADMINISTRATIVE.BAN.BAN_DESC;
            case GLOBAL -> CommonMessages.ADMINISTRATIVE.BAN.BAN_ALL_DESC;
            case GLOBAL_IP -> CommonMessages.ADMINISTRATIVE.BAN.BAN_IP_ALL_DESC;
            case IP -> CommonMessages.ADMINISTRATIVE.BAN.BAN_IP_DESC;
            case TEMPORARY -> CommonMessages.ADMINISTRATIVE.BAN.TEMPBAN_DESC;
            case TEMPORARY_GLOBAL -> CommonMessages.ADMINISTRATIVE.BAN.TEMPBAN_ALL_DESC;
            case TEMPORARY_GLOBAL_IP -> CommonMessages.ADMINISTRATIVE.BAN.TEMPBAN_IP_ALL_DESC;
            case TEMPORARY_IP -> CommonMessages.ADMINISTRATIVE.BAN.TEMPBAN_IP_DESC;
        };
    }

    @Override
    public MessageKey getUsageMessage(CommandExecutor executor, CommandData data, TemporalScopedData state) {
        return switch (state.getScope()) {
            case DEFAULT -> CommonMessages.ADMINISTRATIVE.BAN.BAN_USAGE;
            case GLOBAL -> CommonMessages.ADMINISTRATIVE.BAN.BAN_ALL_USAGE;
            case GLOBAL_IP -> CommonMessages.ADMINISTRATIVE.BAN.BAN_IP_ALL_USAGE;
            case IP -> CommonMessages.ADMINISTRATIVE.BAN.BAN_IP_USAGE;
            case TEMPORARY -> CommonMessages.ADMINISTRATIVE.BAN.TEMPBAN_USAGE;
            case TEMPORARY_GLOBAL -> CommonMessages.ADMINISTRATIVE.BAN.TEMPBAN_ALL_USAGE;
            case TEMPORARY_GLOBAL_IP -> CommonMessages.ADMINISTRATIVE.BAN.TEMPBAN_IP_ALL_USAGE;
            case TEMPORARY_IP -> CommonMessages.ADMINISTRATIVE.BAN.TEMPBAN_IP_USAGE;
        };
    }

    @Override
    public ExecutionStatus execute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, TemporalScopedData scope) {
        UUID staffUUID = executor instanceof OfflinePlayer player ? player.getUUID() : null;
        AdminManager adminManager = instance.getInstance().getAdminManager();

        if (scope.getScope().isTemporary()) {
            adminManager.banPlayerFor(scope.getPlayer().getUUID(), staffUUID, scope.getDuration(), scope.getContexts(), scope.getReason());
        } else {
            adminManager.banPlayer(scope.getPlayer().getUUID(), staffUUID, scope.getContexts(), scope.getReason());
        }

        return ExecutionStatus.SUCCESS;
    }

}
