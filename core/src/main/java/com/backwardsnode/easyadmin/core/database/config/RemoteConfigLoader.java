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

package com.backwardsnode.easyadmin.core.database.config;

import com.backwardsnode.easyadmin.core.database.DatabaseStatementFactory;
import com.zaxxer.hikari.HikariConfig;

public abstract class RemoteConfigLoader<T extends DatabaseStatementFactory> implements ConfigLoader<T> {

    protected final String host;
    protected final int port;
    protected final String database;
    protected final String username;
    protected final String password;
    protected final int preparedStatementCacheSize;

    protected RemoteConfigLoader(String host, int port, String database, String username, String password) {
        this(host, port, database, username, password, 256);
    }

    protected RemoteConfigLoader(String host, int port, String database, String username, String password, int preparedStatementCacheSize) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.preparedStatementCacheSize = preparedStatementCacheSize;
    }

    public abstract String getJdbcUrl();
    public abstract HikariConfig toHikariConfig();

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean doCachePreparedStatements() {
        return preparedStatementCacheSize > 0;
    }

    public int getPreparedStatementCacheSize() {
        return preparedStatementCacheSize;
    }
}
