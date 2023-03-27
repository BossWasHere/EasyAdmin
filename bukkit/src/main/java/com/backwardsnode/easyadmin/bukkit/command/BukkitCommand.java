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

package com.backwardsnode.easyadmin.bukkit.command;

import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.bukkit.EasyAdminBukkit;
import com.backwardsnode.easyadmin.bukkit.wrapper.CommandExecutorWrapper;
import com.backwardsnode.easyadmin.core.command.Command;
import com.backwardsnode.easyadmin.core.command.CommandData;
import com.backwardsnode.easyadmin.core.command.CommandRegistration;
import com.backwardsnode.easyadmin.core.command.ExecutionStatus;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BukkitCommand<S> extends org.bukkit.command.Command {

    private final EasyAdminBukkit plugin;
    private final Command<S> source;

    public BukkitCommand(EasyAdminBukkit plugin, Command<S> source, String mainName, List<String> aliases) {
        super(mainName, source.getIdentifierNode(), source.getIdentifierNode(), aliases);
        this.plugin = plugin;
        this.source = source;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        CommandExecutor wrapper = new CommandExecutorWrapper(sender);
        CommandData data = new CommandData(label, args);
        S state = source.loadState(plugin, wrapper, data);

        if (source.processArgs(plugin, wrapper, data, state)) {
            ExecutionStatus status = source.preExecute(plugin, wrapper, data, state);
            if (status == ExecutionStatus.SUCCESS) {
                return source.execute(plugin, wrapper, data, state) == ExecutionStatus.SUCCESS;
            } else {
                // TODO: pre execution cancelled
            }
        } else {
            // TODO: bad args
        }
        return false;
    }
}
