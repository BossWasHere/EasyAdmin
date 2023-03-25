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
import com.backwardsnode.easyadmin.api.data.ActionScope;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.core.command.CommandData;
import com.backwardsnode.easyadmin.core.command.ExecutionStatus;
import com.backwardsnode.easyadmin.core.command.ScopedCommand;
import com.backwardsnode.easyadmin.core.commands.data.TemporalScopedData;
import com.backwardsnode.easyadmin.core.i18n.CommonMessages;
import com.backwardsnode.easyadmin.api.internal.MessageKey;

public class Unmute extends ScopedCommand<TemporalScopedData> {

    private static final String COMMAND = "unmute";
    private static final String SHORTHAND_COMMAND = "um";

    public Unmute(ActionScope enabledScopes) {
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
        //TODO unmute command

        return true;
    }

    @Override
    public MessageKey getDescription(CommandExecutor executor, CommandData data, TemporalScopedData state) {
        return switch (state.getScope()) {
            case DEFAULT, TEMPORARY -> CommonMessages.ADMINISTRATIVE.MUTE.UNMUTE_DESC;
            case GLOBAL, TEMPORARY_GLOBAL -> CommonMessages.ADMINISTRATIVE.MUTE.UNMUTE_ALL_DESC;
            case GLOBAL_IP, TEMPORARY_GLOBAL_IP -> CommonMessages.ADMINISTRATIVE.MUTE.UNMUTE_IP_ALL_DESC;
            case IP, TEMPORARY_IP -> CommonMessages.ADMINISTRATIVE.MUTE.UNMUTE_IP_DESC;
        };
    }

    @Override
    public MessageKey getUsageMessage(CommandExecutor executor, CommandData data, TemporalScopedData state) {
        return switch (state.getScope()) {
            case DEFAULT, TEMPORARY -> CommonMessages.ADMINISTRATIVE.MUTE.UNMUTE_USAGE;
            case GLOBAL, TEMPORARY_GLOBAL -> CommonMessages.ADMINISTRATIVE.MUTE.UNMUTE_ALL_USAGE;
            case GLOBAL_IP, TEMPORARY_GLOBAL_IP -> CommonMessages.ADMINISTRATIVE.MUTE.UNMUTE_IP_ALL_USAGE;
            case IP, TEMPORARY_IP -> CommonMessages.ADMINISTRATIVE.MUTE.UNMUTE_IP_USAGE;
        };
    }

    @Override
    public ExecutionStatus execute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, TemporalScopedData scope) {
        //TODO unmute command

        return ExecutionStatus.SUCCESS;
    }

}
