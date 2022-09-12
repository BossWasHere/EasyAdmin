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
import com.backwardsnode.easyadmin.api.config.CommandConfiguration;
import com.backwardsnode.easyadmin.api.data.ActionScope;
import com.backwardsnode.easyadmin.core.commands.*;

public class CommandManager {

    private final EasyAdminPlugin instance;
    private final CommandRegistrationProvider registrationProvider;

    public CommandManager(EasyAdminPlugin instance, CommandRegistrationProvider registrationProvider) {
        this.instance = instance;
        this.registrationProvider = registrationProvider;
    }

    public void registerBanCommand() {
        CommandConfiguration configuration = getCommandConfiguration();
        if (configuration.isBanCommandEnabled()) {
            registrationProvider.registerCommand(new Ban(configuration.getEnabledBanScopes()));
            registrationProvider.registerCommand(new Unban(configuration.getEnabledBanScopes().isIP() ? ActionScope.IP : ActionScope.DEFAULT));
        }
    }

    public void registerCommentCommand() {
        CommandConfiguration configuration = getCommandConfiguration();
        boolean comment = configuration.isCommentCommandEnabled();
        boolean warning = configuration.isWarningCommandEnabled();
        if (comment || warning) {
            registrationProvider.registerCommand(new Comment(comment, warning));
        }
    }

    public void registerKickCommand() {
        CommandConfiguration configuration = getCommandConfiguration();
        if (configuration.isKickCommandEnabled()) {
            registrationProvider.registerCommand(new Kick(configuration.allowGlobalKicks(), configuration.allowLocalKicks()));
        }
    }

    public void registerMuteCommand() {
        CommandConfiguration configuration = getCommandConfiguration();
        if (configuration.isMuteCommandEnabled()) {
            registrationProvider.registerCommand(new Mute(configuration.getEnabledMuteScopes()));
            registrationProvider.registerCommand(new Unmute(configuration.getEnabledMuteScopes().isIP() ? ActionScope.IP : ActionScope.DEFAULT));
        }
    }

    public void registerLookupCommand() {
        CommandConfiguration configuration = getCommandConfiguration();
        if (configuration.isLookupCommandEnabled()) {
            registrationProvider.registerCommand(new Lookup());
        }
    }

    public void registerAllCommands() {
        registerBanCommand();
        registerCommentCommand();
        registerKickCommand();
        registerMuteCommand();
        registerLookupCommand();
    }

    public void unregisterAllCommands() {
        registrationProvider.unregisterCommands();
    }

    private CommandConfiguration getCommandConfiguration() {
        return instance.getInstance().getConfigurationManager().getCommandConfiguration();
    }

}
