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
import com.backwardsnode.easyadmin.core.database.impl.EasyAdminPostgres;
import com.zaxxer.hikari.HikariConfig;

public final class PostgresConfigLoader extends RemoteConfigLoader<EasyAdminPostgres> {

    public PostgresConfigLoader(String host, int port, String database, String username, String password) {
        this(host, port, database, username, password, 256);
    }

    public PostgresConfigLoader(String host, int port, String database, String username, String password, int preparedStatementCacheSize) {
        super(host, port, database, username, password, preparedStatementCacheSize);
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:postgresql://" + host + ':' + port + "/" + database;
    }

    @Override
    public HikariConfig toHikariConfig() {
        HikariConfig config = new HikariConfig();

        config.setDataSourceClassName("org.postgresql.ds.PGSimpleDataSource");
        config.addDataSourceProperty("user", username);
        config.addDataSourceProperty("password", password);
        config.addDataSourceProperty("databaseName", database);
        config.addDataSourceProperty("serverName", host);
        config.addDataSourceProperty("portNumber", String.valueOf(port));
        config.addDataSourceProperty("preparedStatementCacheQueries", String.valueOf(preparedStatementCacheSize));

        return config;
    }

    @Override
    public EasyAdminPostgres getStatementFactory() {
        return new EasyAdminPostgres();
    }

    @Override
    public boolean isStatementFactoryCompatible(DatabaseStatementFactory factory) {
        return factory instanceof EasyAdminPostgres;
    }

}