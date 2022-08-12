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

    protected final String HOST;
    protected final int PORT;
    protected final String DATABASE;
    protected final String USERNAME;
    protected final String PASSWORD;
    protected final boolean CACHE_PREP_STATEMENTS;
    protected final int PREP_STATEMENT_CACHE_SIZE;
    protected final int PREP_STATEMENT_CACHE_SQL_LIMIT;

    protected RemoteConfigLoader(String host, int port, String database, String username, String password) {
        this(host, port, database, username, password, true, 250, 2048);
    }

    protected RemoteConfigLoader(String host, int port, String database, String username, String password, boolean cachePreparedStatements, int preparedStatementCacheSize, int preparedStatementCacheSqlLimit) {
        HOST = host;
        PORT = port;
        DATABASE = database;
        USERNAME = username;
        PASSWORD = password;
        CACHE_PREP_STATEMENTS = cachePreparedStatements;
        PREP_STATEMENT_CACHE_SIZE = preparedStatementCacheSize;
        PREP_STATEMENT_CACHE_SQL_LIMIT = preparedStatementCacheSqlLimit;
    }

    public abstract String getJdbcUrl();
    public HikariConfig toHikariConfig() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);

        if (CACHE_PREP_STATEMENTS) {
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", String.valueOf(PREP_STATEMENT_CACHE_SIZE));
            config.addDataSourceProperty("prepStmtCacheSqlLimit", String.valueOf(PREP_STATEMENT_CACHE_SQL_LIMIT));
        } else {
            config.addDataSourceProperty("cachePrepStmts", "false");
        }

        return config;
    }

    public String getHost() {
        return HOST;
    }

    public int getPort() {
        return PORT;
    }

    public String getDatabase() {
        return DATABASE;
    }

    public String getUsername() {
        return USERNAME;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public boolean doCachePreparedStatements() {
        return CACHE_PREP_STATEMENTS;
    }

    public int getPreparedStatementCacheSize() {
        return PREP_STATEMENT_CACHE_SIZE;
    }

    public int getPreparedStatementCacheSQLLimit() {
        return PREP_STATEMENT_CACHE_SQL_LIMIT;
    }
}
