/*
 * MIT License
 *
 * Copyright (c) 2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

package com.backwardsnode.easyadmin.core.config;

import com.backwardsnode.easyadmin.core.database.DatabasePlatform;
import com.backwardsnode.easyadmin.core.exception.ConfigurationException;

public final class DatabaseConfig implements ConfigChecker {

    private String type;
    private boolean cache;
    private DatabaseSettings settings;

    public static final class DatabaseSettings {
        private String user;
        private String pass;
        private String database;
        private String host;
        private int port;
    }

    @Override
    public void validate(final String parentPath) throws ConfigurationException {
        if (getDatabasePlatform() == null) throw new ConfigurationException(parentPath, "type");
        if (settings == null) throw new ConfigurationException(parentPath, "settings");
    }

    public String getDatabasePlatformName() {
        return type;
    }

    public DatabasePlatform getDatabasePlatform() {
        try {
            return DatabasePlatform.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean isCacheEnabled() {
        return cache;
    }

    public String getDatabaseUser() {
        return settings.user;
    }

    public String getDatabasePassword() {
        return settings.pass;
    }

    public String getDatabaseName() {
        return settings.database;
    }

    public String getDatabaseHost() {
        return settings.host;
    }

    public int getDatabasePort() {
        return settings.port;
    }

}