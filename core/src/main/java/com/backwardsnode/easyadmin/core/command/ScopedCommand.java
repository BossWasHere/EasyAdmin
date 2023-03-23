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

import java.util.HashMap;
import java.util.Map;

import static com.backwardsnode.easyadmin.api.data.ActionScope.*;

public abstract class ScopedCommand<S extends ScopedCommand.ScopedData> implements Command<S> {

    private final ActionScope enabledScopes;
    private final Map<String, AliasType> commands;

    private final String[] aliases;
    private final String[] subcommandAliases;

    public ScopedCommand(ActionScope enabledScopes) {
        this.enabledScopes = enabledScopes;
        this.commands = new HashMap<>();

        final String command = getCommand();
        final String shorthand = getShorthandCommand();

        if (enabledScopes.isTemporary()) {
            commands.put("temp" + command, AliasType.main(TEMPORARY));
            commands.put("t" + shorthand, AliasType.sub(TEMPORARY));

            if (enabledScopes.isGlobal()) {
                commands.put("gtemp" + command, AliasType.main(TEMPORARY_GLOBAL));
                commands.put("gt" + shorthand, AliasType.sub(TEMPORARY_GLOBAL));

                if (enabledScopes.isIP()) {
                    commands.put("gtemp" + command + "ip", AliasType.main(TEMPORARY_GLOBAL_IP));
                    commands.put("gt" + shorthand + "i", AliasType.sub(TEMPORARY_GLOBAL_IP));
                }
            }
            if (enabledScopes.isIP()) {
                commands.put("temp" + command + "ip", AliasType.main(TEMPORARY_IP));
                commands.put("gt" + shorthand + "i", AliasType.sub(TEMPORARY_GLOBAL_IP));
            }
        }
        if (enabledScopes.isGlobal()) {
            commands.put("g" + command, AliasType.main(GLOBAL));
            commands.put("g" + shorthand, AliasType.sub(GLOBAL));

            if (enabledScopes.isIP()) {
                commands.put("g" + command + "ip", AliasType.main(GLOBAL_IP));
                commands.put("g" + shorthand + "i", AliasType.sub(GLOBAL_IP));
            }
        }
        if (enabledScopes.isIP()) {
            commands.put(shorthand + "i", AliasType.sub(IP));
        }

        aliases = commands.entrySet().stream().filter(c -> !c.getValue().subcommandOnly()).map(Map.Entry::getKey).toArray(String[]::new);
        subcommandAliases = commands.entrySet().stream().filter(c -> c.getValue().subcommandOnly()).map(Map.Entry::getKey).toArray(String[]::new);

        commands.put(command, AliasType.main(DEFAULT));
        commands.put(shorthand, AliasType.sub(DEFAULT));
    }

    @Override

    public String[] getAliases() {
        return aliases;
    }

    @Override
    public String[] getSubcommandAliases() {
        return subcommandAliases;
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
        AliasType type = commands.get(data.command());
        if (type == null) {
            state.setStatus(ExecutionStatus.ERROR);
        } else {
            state.setScope(type.scope());
            if (checkPermission(executor, type.scope())) {
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

    protected record AliasType(ActionScope scope, boolean subcommandOnly) {
        private static AliasType main(ActionScope scope) {
            return new AliasType(scope, false);
        }
        private static AliasType sub(ActionScope scope) {
            return new AliasType(scope, true);
        }
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
