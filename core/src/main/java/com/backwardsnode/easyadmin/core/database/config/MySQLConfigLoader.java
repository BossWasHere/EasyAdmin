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
import com.backwardsnode.easyadmin.core.database.impl.EasyAdminMySQL;
import com.zaxxer.hikari.HikariConfig;

public final class MySQLConfigLoader extends RemoteConfigLoader<EasyAdminMySQL> {

    private final int preparedStatementCacheSqlLimit;
    private boolean useServerPreparedStatements = true;
    private boolean useLocalSessionState = true;
    private boolean rewriteBatchedStatements = true;
    private boolean cacheResultSetMetadata = true;
    private boolean cacheServerConfiguration = true;
    private boolean elideSetAutoCommits = true;
    private boolean maintainTimeStats = false;

    public MySQLConfigLoader(String host, int port, String database, String username, String password) {
        this(host, port, database, username, password, 256, 2048);
    }

    public MySQLConfigLoader(String host, int port, String database, String username, String password, int preparedStatementCacheSize, int preparedStatementCacheSqlLimit) {
        super(host, port, database, username, password, preparedStatementCacheSize);
        this.preparedStatementCacheSqlLimit = preparedStatementCacheSqlLimit;
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:mysql://" + host + ':' + port + "/" + database;
    }

    @Override
    public HikariConfig toHikariConfig() {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(username);
        config.setPassword(password);

        if (doCachePreparedStatements()) {
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", String.valueOf(preparedStatementCacheSize));
            config.addDataSourceProperty("prepStmtCacheSqlLimit", String.valueOf(preparedStatementCacheSqlLimit));
        } else {
            config.addDataSourceProperty("cachePrepStmts", "false");
        }

        config.addDataSourceProperty("useServerPrepStmts", String.valueOf(useServerPreparedStatements));
        config.addDataSourceProperty("useLocalSessionState", String.valueOf(useLocalSessionState));
        config.addDataSourceProperty("rewriteBatchedStatements", String.valueOf(rewriteBatchedStatements));
        config.addDataSourceProperty("cacheResultSetMetadata", String.valueOf(cacheResultSetMetadata));
        config.addDataSourceProperty("cacheServerConfiguration", String.valueOf(cacheServerConfiguration));
        config.addDataSourceProperty("elideSetAutoCommits", String.valueOf(elideSetAutoCommits));
        config.addDataSourceProperty("maintainTimeStats", String.valueOf(maintainTimeStats));

        return config;
    }

    @Override
    public EasyAdminMySQL getStatementFactory() {
        return new EasyAdminMySQL();
    }

    @Override
    public boolean isStatementFactoryCompatible(DatabaseStatementFactory factory) {
        return factory instanceof EasyAdminMySQL;
    }

    public int getPreparedStatementCacheSQLLimit() {
        return preparedStatementCacheSqlLimit;
    }

    public boolean isUseServerPreparedStatements() {
        return useServerPreparedStatements;
    }

    public void setUseServerPreparedStatements(boolean useServerPreparedStatements) {
        this.useServerPreparedStatements = useServerPreparedStatements;
    }

    public boolean isUseLocalSessionState() {
        return useLocalSessionState;
    }

    public void setUseLocalSessionState(boolean useLocalSessionState) {
        this.useLocalSessionState = useLocalSessionState;
    }

    public boolean isRewriteBatchedStatements() {
        return rewriteBatchedStatements;
    }

    public void setRewriteBatchedStatements(boolean rewriteBatchedStatements) {
        this.rewriteBatchedStatements = rewriteBatchedStatements;
    }

    public boolean isCacheResultSetMetadata() {
        return cacheResultSetMetadata;
    }

    public void setCacheResultSetMetadata(boolean cacheResultSetMetadata) {
        this.cacheResultSetMetadata = cacheResultSetMetadata;
    }

    public boolean isCacheServerConfiguration() {
        return cacheServerConfiguration;
    }

    public void setCacheServerConfiguration(boolean cacheServerConfiguration) {
        this.cacheServerConfiguration = cacheServerConfiguration;
    }

    public boolean isElideSetAutoCommits() {
        return elideSetAutoCommits;
    }

    public void setElideSetAutoCommits(boolean elideSetAutoCommits) {
        this.elideSetAutoCommits = elideSetAutoCommits;
    }

    public boolean isMaintainTimeStats() {
        return maintainTimeStats;
    }

    public void setMaintainTimeStats(boolean maintainTimeStats) {
        this.maintainTimeStats = maintainTimeStats;
    }
}