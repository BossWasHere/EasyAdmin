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
import net.md_5.bungee.api.CommandSender;

public class BungeeCommand<S> extends net.md_5.bungee.api.plugin.Command {

    private final EasyAdminBungee plugin;
    private final Command<S> source;

    public BungeeCommand(EasyAdminBungee plugin, Command<S> source) {
        super(source.getCommand(), null, source.getAliases());
        this.plugin = plugin;
        this.source = source;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        //TODO label not passed??
        CommandExecutor wrapper = new CommandExecutorWrapper(sender);
        CommandData data = new CommandData(null, args);
        S state = source.loadState(plugin, wrapper, data);

        if (source.processArgs(plugin, wrapper, data, state)) {
            ExecutionStatus status = source.preExecute(plugin, wrapper, data, state);
            if (status == ExecutionStatus.SUCCESS) {
                source.execute(plugin, wrapper, data, state);
            } else {
                // TODO: pre execution cancelled
            }
        } else {
            // TODO: bad args
        }
    }
}
