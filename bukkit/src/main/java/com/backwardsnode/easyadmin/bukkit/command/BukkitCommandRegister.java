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

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.bukkit.EasyAdminBukkit;
import com.backwardsnode.easyadmin.core.command.Command;
import com.backwardsnode.easyadmin.core.command.CommandRegistration;
import com.backwardsnode.easyadmin.core.command.CommandRegistrationProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public final class BukkitCommandRegister implements CommandRegistrationProvider {

    private final EasyAdminBukkit plugin;

    public BukkitCommandRegister(EasyAdminBukkit plugin) {
        this.plugin = plugin;
    }

    private CommandMap commandMap;

    private CommandMap getCommandMap() {
        if (commandMap != null) return commandMap;

        try {
            Field field = SimplePluginManager.class.getDeclaredField("commandMap");
            field.setAccessible(true);
            commandMap = (CommandMap) field.get(Bukkit.getServer().getPluginManager());

        } catch (IllegalAccessException | NoSuchFieldException e) {
            plugin.getLogger().log(Level.SEVERE, "An error occured while building the CommandMap for " + plugin.getName(), e);
        }

        return commandMap;
    }

    @Override
    public void registerCommand(Command<?> command) {
        CommandMap map = getCommandMap();
        if (map != null) {
            CommandRegistration registration = command.getRegistration();
            String mainName = null;
            List<String> aliases = new ArrayList<>();

            for (Map.Entry<String, String[]> nameAndAliases : registration.commandNames().entrySet()) {
                if (mainName == null) {
                    mainName = nameAndAliases.getKey();
                } else {
                    aliases.add(nameAndAliases.getKey());
                }
                aliases.addAll(Arrays.asList(nameAndAliases.getValue()));
            }

            map.register(EasyAdmin.NAMESPACE, new BukkitCommand<>(plugin, command, mainName, aliases));
        }
    }

    @Override
    public void unregisterCommands() { }
}
