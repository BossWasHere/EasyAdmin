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
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.core.command.Command;
import com.backwardsnode.easyadmin.core.command.CommandData;
import com.backwardsnode.easyadmin.core.command.ExecutionStatus;
import com.backwardsnode.easyadmin.core.command.args.ArgumentResult;
import com.backwardsnode.easyadmin.core.command.args.ArgumentSelector;
import com.backwardsnode.easyadmin.core.commands.data.LookupData;

public class Lookup implements Command<LookupData> {

    private static final String COMMAND = "lookup";
    private static final String SHORTHAND_COMMAND = "l";

    @Override
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public String getShorthandCommand() {
        return SHORTHAND_COMMAND;
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String[] getSubcommandAliases() {
        return new String[0];
    }

    @Override
    public LookupData loadState(EasyAdminPlugin instance, CommandExecutor executor, CommandData data) {
        return new LookupData();
    }

    @Override
    public boolean processArgs(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, LookupData state) {
        ArgumentSelector selector = new ArgumentSelector(instance, data.args());

        ArgumentResult<OfflinePlayer> player = selector.readPlayerName();
        if (player.isValid()) {
            state.setPlayer(player.value());
            return true;
        }
        ArgumentResult<String> address = selector.undo().readNetworkAddress();
        if (address.isValid()) {
            state.setIpAddress(address.value());
            return true;
        }

        return false;
    }

    @Override
    public ExecutionStatus execute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, LookupData state) {
        //TODO implementation

        return ExecutionStatus.SUCCESS;
    }

}
