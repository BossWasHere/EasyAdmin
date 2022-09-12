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
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.core.command.Command;
import com.backwardsnode.easyadmin.core.command.CommandData;
import com.backwardsnode.easyadmin.core.command.ExecutionStatus;
import com.backwardsnode.easyadmin.core.command.args.ArgumentResult;
import com.backwardsnode.easyadmin.core.command.args.ArgumentSelector;
import com.backwardsnode.easyadmin.core.commands.data.KickData;

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
    public String getCommand() {
        return COMMAND;
    }

    @Override
    public String getShorthandCommand() {
        return SHORTHAND_COMMAND;
    }

    @Override
    public String[] getAliases() {
        return allowGlobal && allowLocal ? new String[] { COMMAND_GLOBAL } : new String[0];
    }

    @Override
    public String[] getSubcommandAliases() {
        return allowGlobal && allowLocal ? new String[] { SHORTHAND_COMMAND_GLOBAL } : new String[0];
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
    public ExecutionStatus execute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, KickData state) {
        UUID staffUUID = executor instanceof OfflinePlayer player ? player.getUUID() : null;
        AdminManager manager = instance.getInstance().getAdminManager();

        manager.kickPlayer(state.getPlayer().getUUID(), staffUUID, state.getReason(), state.isGlobal());

        return ExecutionStatus.SUCCESS;
    }

}
