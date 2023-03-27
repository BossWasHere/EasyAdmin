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

import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.data.ActionScope;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.backwardsnode.easyadmin.api.data.ActionScope.*;

public abstract class ScopedCommand<S extends ScopedCommand.ScopedData> implements Command<S> {

    private final ActionScope enabledScopes;
    private final Map<String, ActionScope> commands;
    private final Map<String, String[]> nameAndAliases;

    public ScopedCommand(ActionScope enabledScopes) {
        this.enabledScopes = enabledScopes;
        this.commands = new HashMap<>();
        Map<String, String[]> nameAndAliases = new HashMap<>();

        final String root = getRootCommandPart();
        final String shorthand = getShorthandCommandPart();

        if (enabledScopes.isTemporary()) {
            commands.put("temp" + root, TEMPORARY);
            commands.put("t" + shorthand, TEMPORARY);

            nameAndAliases.put("temp" + root, new String[] { "t" + shorthand });

            if (enabledScopes.isGlobal()) {
                commands.put("gtemp" + root, TEMPORARY_GLOBAL);
                commands.put("gt" + shorthand, TEMPORARY_GLOBAL);

                nameAndAliases.put("gtemp" + root, new String[] { "gt" + shorthand });

                if (enabledScopes.isIP()) {
                    commands.put("gtemp" + root + "ip", TEMPORARY_GLOBAL_IP);
                    commands.put("gt" + shorthand + "i", TEMPORARY_GLOBAL_IP);

                    nameAndAliases.put("gtemp" + root + "ip", new String[] { "gt" + shorthand + "i" });
                }
            }
            if (enabledScopes.isIP()) {
                commands.put("temp" + root + "ip", TEMPORARY_IP);
                commands.put("gt" + shorthand + "i", TEMPORARY_GLOBAL_IP);

                nameAndAliases.put("temp" + root + "ip", new String[] { "t" + shorthand + "i" });
            }
        }
        if (enabledScopes.isGlobal()) {
            commands.put("g" + root, GLOBAL);
            commands.put("g" + shorthand, GLOBAL);

            nameAndAliases.put("g" + root, new String[] { "g" + shorthand });

            if (enabledScopes.isIP()) {
                commands.put("g" + root + "ip", GLOBAL_IP);
                commands.put("g" + shorthand + "i", GLOBAL_IP);

                nameAndAliases.put("g" + root + "ip", new String[] { "g" + shorthand + "i" });
            }
        }
        if (enabledScopes.isIP()) {
            commands.put(root + "ip", IP);
            commands.put(shorthand + "i", IP);

            nameAndAliases.put(root + "ip", new String[] { shorthand + "i" });
        }

        commands.put(root, DEFAULT);
        commands.put(shorthand, DEFAULT);

        nameAndAliases.put(root, new String[] { shorthand });

        this.nameAndAliases = Collections.unmodifiableMap(nameAndAliases);
    }


    public abstract String getRootCommandPart();


    public abstract String getShorthandCommandPart();

    @Override
    public CommandRegistration getRegistration() {
        return new CommandRegistration(getRootCommandPart(), nameAndAliases);
    }

    @Override
    public boolean requiresBasePermission() {
        return false;
    }

    public ActionScope getEnabledScopes() {
        return enabledScopes;
    }

    protected abstract S createDefaultState();

    @Override
    public final S loadState(EasyAdminPlugin instance, CommandExecutor executor, CommandData data) {
        S state = createDefaultState();
        ActionScope scope = commands.get(data.command());
        if (scope == null) {
            state.setStatus(ExecutionStatus.ERROR);
        } else {
            state.setScope(scope);
            if (checkPermission(executor, scope)) {
                state.setStatus(ExecutionStatus.SUCCESS);
            } else {
                state.setStatus(ExecutionStatus.NO_PERMISSION);
            }
        }
        return state;
    }

    @Override
    public final ExecutionStatus preExecute(EasyAdminPlugin instance, CommandExecutor executor, CommandData data, S state) {
        return state.getStatus();
    }

    protected boolean checkPermission(CommandExecutor executor, ActionScope scope) {
        return executor.hasPermission(getBasePermission() + "." + scope.getPermissionSuffix());
    }

    public static class ScopedData {

        private ExecutionStatus status;
        private ActionScope scope;

        protected ScopedData(ExecutionStatus status) {
            this(status, DEFAULT);
        }

        protected ScopedData(ExecutionStatus status, ActionScope scope) {
            this.status = status;
            this.scope = scope;
        }

        protected ExecutionStatus getStatus() {
            return status;
        }

        protected void setStatus(ExecutionStatus status) {
            this.status = status;
        }

        public ActionScope getScope() {
            return scope;
        }

        public void setScope(ActionScope scope) {
            this.scope = scope;
        }
    }

}
