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
import com.backwardsnode.easyadmin.api.builder.BanBuilder;
import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import com.backwardsnode.easyadmin.api.commit.CommitResult;
import com.backwardsnode.easyadmin.api.data.ActionScope;
import com.backwardsnode.easyadmin.api.data.ServiceSource;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.entity.OfflinePlayer;
import com.backwardsnode.easyadmin.api.entity.OnlinePlayer;
import com.backwardsnode.easyadmin.api.internal.MessageKey;
import com.backwardsnode.easyadmin.api.record.BanRecord;
import com.backwardsnode.easyadmin.core.command.CommandData;
import com.backwardsnode.easyadmin.core.command.ExecutionStatus;
import com.backwardsnode.easyadmin.core.command.ScopedCommand;
import com.backwardsnode.easyadmin.core.command.args.ArgumentResult;
import com.backwardsnode.easyadmin.core.command.args.ArgumentSelector;
import com.backwardsnode.easyadmin.core.commands.data.TemporalScopedData;
import com.backwardsnode.easyadmin.core.i18n.CommonMessages;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.backwardsnode.easyadmin.core.i18n.CommonMessages.ADMINISTRATIVE.BAN.*;

public class Ban extends ScopedCommand<TemporalScopedData> {

    private static final String COMMAND = "ban";
    private static final String SHORTHAND_COMMAND = "b";

    public Ban(ActionScope enabledScopes) {
        super(enabledScopes);
    }

    @Override
    public String getRootCommandPart() {
        return COMMAND;
    }

    @Override
    public String getShorthandCommandPart() {
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
            case DEFAULT -> BAN_DESC;
            case GLOBAL -> BAN_ALL_DESC;
            case GLOBAL_IP -> BAN_IP_ALL_DESC;
            case IP -> BAN_IP_DESC;
            case TEMPORARY -> TEMPBAN_DESC;
            case TEMPORARY_GLOBAL -> TEMPBAN_ALL_DESC;
            case TEMPORARY_GLOBAL_IP -> TEMPBAN_IP_ALL_DESC;
            case TEMPORARY_IP -> TEMPBAN_IP_DESC;
        };
    }

    @Override
    public MessageKey getUsageMessage(CommandExecutor executor, CommandData data, TemporalScopedData state) {
        return switch (state.getScope()) {
            case DEFAULT -> BAN_USAGE;
            case GLOBAL -> BAN_ALL_USAGE;
            case GLOBAL_IP -> BAN_IP_ALL_USAGE;
            case IP -> BAN_IP_USAGE;
            case TEMPORARY -> TEMPBAN_USAGE;
            case TEMPORARY_GLOBAL -> TEMPBAN_ALL_USAGE;
            case TEMPORARY_GLOBAL_IP -> TEMPBAN_IP_ALL_USAGE;
            case TEMPORARY_IP -> TEMPBAN_IP_USAGE;
        };
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public ExecutionStatus execute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, TemporalScopedData state) {
        UUID staffUUID = executor instanceof OfflinePlayer player ? player.getUUID() : null;
        RecordBuilder builder = instance.getRecordBuilderFor(ServiceSource.COMMAND);
        BanBuilder banBuilder;

        // TODO support IP only bans?
        Optional<OnlinePlayer> onlinePlayerOptional = state.getPlayer().getOnlinePlayer();

        // TODO resolve offline player IPs or just error to command sender?
        if (state.getScope().isIP() && onlinePlayerOptional.isPresent()) {
            OnlinePlayer onlinePlayer = onlinePlayerOptional.get();
            banBuilder = builder.banPlayerAndAddress(onlinePlayer.getUUID(), onlinePlayer.getSerializedIPAddress());
        } else {
            banBuilder = builder.banPlayer(state.getPlayer().getUUID());
        }

        banBuilder.byStaff(staffUUID);

        if (state.getScope().isTemporary()) {
           banBuilder.withUnbanAfter(state.getDuration());
        }

        if (state.hasContext()) {
            banBuilder.withContext(state.getContext());
        }

        if (state.hasReason()) {
            banBuilder.withBanReason(state.getReason());
        }

        try {
            CommitResult<BanRecord> result = banBuilder.buildAndCommit();
            String username = state.getPlayer().getUsername();
            switch (result.status()) {
                case COMMITTED -> {
                    String context = state.getContext();
                    Duration duration = state.getDuration();
                    String reason = state.getReason();
                    switch (state.getScope()) {
                        case DEFAULT -> {
                            MessageKey key = state.hasReason() ? BANNED_REASON : BANNED;
                            executor.sendKeyedMessage(key, username, context, reason);
                        }
                        case TEMPORARY -> {
                            MessageKey key = state.hasReason() ? TEMPBANNED_REASON : TEMPBANNED;
                            executor.sendKeyedMessage(key, username, context, duration, reason);
                        }
                        case GLOBAL -> {
                            MessageKey key = state.hasReason() ? BANNED_ALL_REASON : BANNED_ALL;
                            executor.sendKeyedMessage(key, username, reason);
                        }
                        case IP -> {
                            MessageKey key = state.hasReason() ? BANNED_IP_REASON : BANNED_IP;
                            executor.sendKeyedMessage(key, username, context, reason);
                        }
                        case TEMPORARY_GLOBAL -> {
                            MessageKey key = state.hasReason() ? TEMPBANNED_ALL_REASON : TEMPBANNED_ALL;
                            executor.sendKeyedMessage(key, username, duration, reason);
                        }
                        case TEMPORARY_IP -> {
                            MessageKey key = state.hasReason() ? TEMPBANNED_IP_REASON : TEMPBANNED_IP;
                            executor.sendKeyedMessage(key, username, context, duration, reason);
                        }
                        case GLOBAL_IP -> {
                            MessageKey key = state.hasReason() ? BANNED_IP_ALL_REASON : BANNED_IP_ALL;
                            executor.sendKeyedMessage(key, username, reason);
                        }
                        case TEMPORARY_GLOBAL_IP -> {
                            MessageKey key = state.hasReason() ? TEMPBANNED_IP_ALL_REASON : TEMPBANNED_IP_ALL;
                            executor.sendKeyedMessage(key, username, duration, reason);
                        }
                    }
                }
                case CANCELLED_DUPLICATE -> {
                    BanRecord existing = result.existing();
                    Object staffUsername = CommonMessages.EASYADMIN.CONSOLE;
                    if (existing.getAuthor() == null) {
                        OfflinePlayer staffPlayer = instance.getOfflinePlayer(existing.getAuthor());
                        if (staffPlayer != null) {
                            staffUsername = staffPlayer.getUsername();
                        }
                    }
                    String context = existing.getContext();
                    Duration duration = existing.getDuration();
                    LocalDateTime dateAdded = existing.getDateAdded();
                    String reason = existing.getReason();


                    switch (existing.getScope()) {
                        case DEFAULT -> {
                            MessageKey key = existing.hasReason() ? ALREADY_BANNED_REASON : ALREADY_BANNED;
                            executor.sendKeyedMessage(key, username, context, staffUsername, dateAdded, reason);
                        }
                        case TEMPORARY -> {
                            MessageKey key = existing.hasReason() ? ALREADY_TEMPBANNED_REASON : ALREADY_TEMPBANNED;
                            executor.sendKeyedMessage(key, username, context, duration, staffUsername, dateAdded, reason);
                        }
                        case GLOBAL -> {
                            MessageKey key = existing.hasReason() ? ALREADY_BANNED_ALL_REASON : ALREADY_BANNED_ALL;
                            executor.sendKeyedMessage(key, username, staffUsername, dateAdded, staffUsername, dateAdded, reason);
                        }
                        case IP -> {
                            MessageKey key = existing.hasReason() ? ALREADY_BANNED_IP_REASON : ALREADY_BANNED_IP;
                            executor.sendKeyedMessage(key, username, context, staffUsername, dateAdded, reason);
                        }
                        case TEMPORARY_GLOBAL -> {
                            MessageKey key = existing.hasReason() ? ALREADY_TEMPBANNED_ALL_REASON : ALREADY_TEMPBANNED_ALL;
                            executor.sendKeyedMessage(key, username, duration, staffUsername, dateAdded, reason);
                        }
                        case TEMPORARY_IP -> {
                            MessageKey key = existing.hasReason() ? ALREADY_TEMPBANNED_IP_REASON : ALREADY_TEMPBANNED_IP;
                            executor.sendKeyedMessage(key, username, context, duration, staffUsername, dateAdded, reason);
                        }
                        case GLOBAL_IP -> {
                            MessageKey key = existing.hasReason() ? ALREADY_BANNED_IP_ALL_REASON : ALREADY_BANNED_IP_ALL;
                            executor.sendKeyedMessage(key, username, staffUsername, dateAdded, reason);
                        }
                        case TEMPORARY_GLOBAL_IP -> {
                            MessageKey key = existing.hasReason() ? ALREADY_TEMPBANNED_IP_ALL_REASON : ALREADY_TEMPBANNED_IP_ALL;
                            executor.sendKeyedMessage(key, username, duration, staffUsername, dateAdded, reason);
                        }
                    }
                }
                case CANCELLED -> executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.CANCELLED.BAN);
                case CANCELLED_IMMUNE -> executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.PLAYER_IMMUNE);
                // TODO send playerNonExistent message in preExecute?
                case IMPOSSIBLE -> executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.PLAYER_OFFLINE);
                case WITHHELD -> executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.WITHHELD.BAN);
            }
        } catch (Exception e) {
            executor.sendKeyedMessage(CommonMessages.ADMINISTRATIVE.ERROR, e.getClass().getName());
            e.printStackTrace();
            return ExecutionStatus.ERROR;
        }

        return ExecutionStatus.SUCCESS;
    }

}
