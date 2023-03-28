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

package com.backwardsnode.easyadmin.bungee.command;

import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.bungee.EasyAdminBungee;
import com.backwardsnode.easyadmin.bungee.wrapper.CommandExecutorWrapper;
import com.backwardsnode.easyadmin.core.command.Command;
import com.backwardsnode.easyadmin.core.command.CommandData;
import com.backwardsnode.easyadmin.core.command.ExecutionStatus;
import com.backwardsnode.easyadmin.core.i18n.CommonMessages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;

import java.util.logging.Level;

public class BungeeCommand<S> extends net.md_5.bungee.api.plugin.Command {

    private final EasyAdminBungee plugin;
    private final Command<S> source;
    private final String label;

    public BungeeCommand(EasyAdminBungee plugin, Command<S> source, String sharedName, String[] aliases) {
        super(sharedName, null, aliases);
        this.plugin = plugin;
        this.source = source;
        this.label = sharedName;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        CommandExecutor wrapper = new CommandExecutorWrapper(sender);
        CommandData data = new CommandData(label, args);

        ProxyServer.getInstance().getLogger().info("Executing command: " + label + " " + String.join(" ", args));

        try {
            S state = source.loadState(plugin, wrapper, data);

            if (source.processArgs(plugin, wrapper, data, state)) {
                ExecutionStatus status = source.preExecute(plugin, wrapper, data, state);
                if (status == ExecutionStatus.SUCCESS) {
                    status = source.execute(plugin, wrapper, data, state);
                }

                switch (status) {
                    case INVALID_ARGUMENTS -> wrapper.sendKeyedMessage(source.getUsageMessage(wrapper, data, state));
                    case NO_PERMISSION -> wrapper.sendKeyedMessage(CommonMessages.EASYADMIN.NO_PERMISSION);
                    case ERROR ->
                            ProxyServer.getInstance().getLogger().severe(
                                    "An error occurred whilst executing the following command: "
                                            + label + " " + String.join(" ", args));
                    default -> {}
                }
            } else {
                wrapper.sendKeyedMessage(source.getUsageMessage(wrapper, data, state));
            }
        } catch (Exception e) {
            ProxyServer.getInstance().getLogger().log(Level.SEVERE,
                    "An error occurred whilst executing the following command: "
                            + label + " " + String.join(" ", args), e);
        }

    }
}
