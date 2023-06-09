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

package com.backwardsnode.easyadmin.core.command;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.internal.MessageKey;

public interface Command<S> {

    CommandRegistration getRegistration();

    default String getIdentifierNode() {
        return EasyAdmin.NAMESPACE + "." + getRegistration().id();
    }

    default String getBasePermission() {
        return getIdentifierNode();
    }

    default boolean requiresBasePermission() {
        return true;
    }

    S loadState(EasyAdminPlugin instance, CommandExecutor executor, CommandData data);

    boolean processArgs(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, S state);

    MessageKey getDescription(CommandExecutor executor, CommandData data, S state);

    MessageKey getUsageMessage(CommandExecutor executor, CommandData data, S state);

    default ExecutionStatus preExecute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, S state) {
        return requiresBasePermission()
                ? (executor.hasPermission(getBasePermission()) ? ExecutionStatus.SUCCESS : ExecutionStatus.NO_PERMISSION)
                : ExecutionStatus.SUCCESS;
    }

    ExecutionStatus execute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, S state);

}
