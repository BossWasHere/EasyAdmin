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
import com.backwardsnode.easyadmin.api.config.CommandConfiguration;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.core.command.Command;
import com.backwardsnode.easyadmin.core.command.CommandData;
import com.backwardsnode.easyadmin.core.command.ExecutionStatus;
import com.backwardsnode.easyadmin.core.command.args.ArgumentResult;
import com.backwardsnode.easyadmin.core.command.args.ArgumentSelector;
import com.backwardsnode.easyadmin.core.commands.data.CommentData;

import java.util.UUID;

public class Comment implements Command<CommentData> {

    private static final String COMMAND_COMMENT = "comment";
    private static final String SHORTHAND_COMMAND_COMMENT = "c";
    private static final String COMMAND_WARNING = "warning";
    private static final String SHORTHAND_COMMAND_WARNING = "w";

    private final boolean registerComment, registerWarning;

    public Comment(boolean registerComment, boolean registerWarning) {
        this.registerComment = registerComment;
        this.registerWarning = registerWarning;
    }

    @Override
    public String getCommand() {
        return registerComment ? COMMAND_COMMENT : COMMAND_WARNING;
    }

    @Override
    public String getShorthandCommand() {
        return registerComment ? SHORTHAND_COMMAND_COMMENT : SHORTHAND_COMMAND_WARNING;
    }

    @Override
    public String[] getAliases() {
        return registerComment && registerWarning ? new String[] { COMMAND_WARNING } : new String[0];
    }

    @Override
    public String[] getSubcommandAliases() {
        return registerComment && registerWarning ? new String[] { SHORTHAND_COMMAND_WARNING } : new String[0];
    }

    @Override
    public CommentData loadState(EasyAdminPlugin instance, CommandExecutor executor, CommandData data) {
        String cmd = data.command();
        return new CommentData(cmd.equals(COMMAND_WARNING) || cmd.equals(SHORTHAND_COMMAND_WARNING));
    }

    @Override
    public boolean processArgs(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, CommentData state) {
        ArgumentSelector selector = new ArgumentSelector(instance, data.args());

        ArgumentResult<OfflinePlayer> player = selector.readPlayerName();
        if (!player.isValid()) {
            return false;
        }
        state.setPlayer(player.value());
        state.setComment(selector.readRemainingString());

        return !state.getComment().isEmpty();
    }

    @Override
    public ExecutionStatus execute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, CommentData state) {
        UUID staffUUID = executor instanceof OfflinePlayer player ? player.getUUID() : null;
        AdminManager manager = instance.getInstance().getAdminManager();
        CommandConfiguration commandConfiguration = instance.getInstance().getConfigurationManager().getCommandConfiguration();

        if (state.isWarning()) {
            manager.addPlayerWarning(state.getPlayer().getUUID(), staffUUID, state.getComment(), commandConfiguration.notifyPlayerOnWarning());
        } else {
            manager.addPlayerComment(state.getPlayer().getUUID(), staffUUID, state.getComment(), commandConfiguration.notifyPlayerOnComment());
        }

        return ExecutionStatus.SUCCESS;
    }

}
