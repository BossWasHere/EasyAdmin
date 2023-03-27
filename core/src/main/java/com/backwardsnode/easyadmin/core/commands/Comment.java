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
import com.backwardsnode.easyadmin.api.builder.CommentBuilder;
import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.internal.InternalServiceProviderType;
import com.backwardsnode.easyadmin.api.internal.MessageKey;
import com.backwardsnode.easyadmin.api.record.CommentRecord;
import com.backwardsnode.easyadmin.api.commit.CommitResult;
import com.backwardsnode.easyadmin.core.command.Command;
import com.backwardsnode.easyadmin.core.command.CommandData;
import com.backwardsnode.easyadmin.core.command.CommandRegistration;
import com.backwardsnode.easyadmin.core.command.ExecutionStatus;
import com.backwardsnode.easyadmin.core.command.args.ArgumentResult;
import com.backwardsnode.easyadmin.core.command.args.ArgumentSelector;
import com.backwardsnode.easyadmin.core.commands.data.CommentData;
import com.backwardsnode.easyadmin.core.i18n.CommonMessages;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
    public CommandRegistration getRegistration() {
        Map<String, String[]> entries = new HashMap<>(2);
        if (registerComment) {
            entries.put(COMMAND_COMMENT, new String[] { SHORTHAND_COMMAND_COMMENT });
        }
        if (registerWarning) {
            entries.put(COMMAND_WARNING, new String[] { SHORTHAND_COMMAND_WARNING });
        }
        return new CommandRegistration(COMMAND_COMMENT, Collections.unmodifiableMap(entries));
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
    public MessageKey getDescription(CommandExecutor executor, CommandData data, CommentData state) {
        if (state.isWarning()) {
            return CommonMessages.ADMINISTRATIVE.WARNING.DESC;
        }
        return CommonMessages.ADMINISTRATIVE.COMMENT.DESC;
    }

    @Override
    public MessageKey getUsageMessage(CommandExecutor executor, CommandData data, CommentData state) {
        if (state.isWarning()) {
            return CommonMessages.ADMINISTRATIVE.WARNING.USAGE;
        }
        return CommonMessages.ADMINISTRATIVE.COMMENT.USAGE;
    }

    @Override
    public ExecutionStatus execute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, CommentData state) {
        UUID staffUUID = executor instanceof OfflinePlayer player ? player.getUUID() : null;
        RecordBuilder builder = instance.getRecordBuilderFor(InternalServiceProviderType.COMMAND);
        CommentBuilder commentBuilder =
                builder.commentOnPlayer(state.getPlayer().getUUID(), state.getComment())
                        .byStaff(staffUUID)
                        .setWarning(state.isWarning());

        try {
            CommitResult<CommentRecord> result = commentBuilder.buildAndCommit();
            switch (result.status()) {
                case COMMITTED:
                    if (state.isWarning()) {
                        executor.sendMessage(CommonMessages.ADMINISTRATIVE.WARNING.WARNED, state.getPlayer().getUsername(), state.getComment());
                    } else {
                        executor.sendMessage(CommonMessages.ADMINISTRATIVE.COMMENT.COMMENTED, state.getPlayer().getUsername(), state.getComment());
                    }
                    break;
                case CANCELLED:
                    if (state.isWarning()) {
                        executor.sendMessage(CommonMessages.ADMINISTRATIVE.CANCELLED.WARNING);
                    } else {
                        executor.sendMessage(CommonMessages.ADMINISTRATIVE.CANCELLED.COMMENT);
                    }
                    break;
                case CANCELLED_DUPLICATE:
                    // should never happen
                    break;
                case CANCELLED_IMMUNE:
                    executor.sendMessage(CommonMessages.ADMINISTRATIVE.PLAYER_IMMUNE);
                    break;
                case IMPOSSIBLE:
                    // TODO send playerNonExistent message in preExecute?
                    executor.sendMessage(CommonMessages.ADMINISTRATIVE.PLAYER_OFFLINE);
                    break;
                case WITHHELD:
                    if (state.isWarning()) {
                        executor.sendMessage(CommonMessages.ADMINISTRATIVE.WITHHELD.WARNING);
                    } else {
                        executor.sendMessage(CommonMessages.ADMINISTRATIVE.WITHHELD.COMMENT);
                    }
                    break;
            }
        } catch (Exception e) {
            executor.sendMessage(CommonMessages.ADMINISTRATIVE.ERROR, e.getClass().getName());
            e.printStackTrace();
            return ExecutionStatus.ERROR;
        }

        return ExecutionStatus.SUCCESS;
    }

}
