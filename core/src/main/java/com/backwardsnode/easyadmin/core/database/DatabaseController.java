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

package com.backwardsnode.easyadmin.core.database;

import com.backwardsnode.easyadmin.api.data.LookupOptions;
import com.backwardsnode.easyadmin.api.data.PunishmentStatus;
import com.backwardsnode.easyadmin.api.record.*;
import com.backwardsnode.easyadmin.api.record.modify.BanRecordModifier;
import com.backwardsnode.easyadmin.api.record.modify.MuteRecordModifier;
import com.backwardsnode.easyadmin.api.record.modify.PlayerRecordModifier;
import com.backwardsnode.easyadmin.core.database.config.LocalConfigLoader;
import com.backwardsnode.easyadmin.core.database.config.RemoteConfigLoader;
import com.backwardsnode.easyadmin.core.database.util.DatabaseUtil;
import com.backwardsnode.easyadmin.core.database.util.SQLBiFunction;
import com.backwardsnode.easyadmin.core.database.util.SQLFunction;
import com.backwardsnode.easyadmin.core.record.RecordLoader;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.*;

public final class DatabaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseController.class);

    private final DatabaseStatementFactory statementFactory;
    private final boolean autoInit;

    private final boolean isRemoteDatabase;

    // Remote
    private final HikariConfig hikariConfig;
    private HikariDataSource dataSource;

    // Local
    private final LocalConfigLoader<?> localConfig;

    private boolean initialized = false;

    public DatabaseController(@NotNull RemoteConfigLoader<?> config) {
        this(config, config.getStatementFactory());
    }

    public DatabaseController(@NotNull RemoteConfigLoader<?> config, @NotNull DatabaseStatementFactory statementFactory) {
        this(config, statementFactory, true);
    }

    public DatabaseController(@NotNull RemoteConfigLoader<?> config, @NotNull DatabaseStatementFactory statementFactory, boolean autoInit) {
        if (!config.isStatementFactoryCompatible(statementFactory)) {
            throw new IllegalArgumentException("Statement factory is not compatible with config loader");
        }
        this.hikariConfig = config.toHikariConfig();
        localConfig = null;
        this.statementFactory = statementFactory;
        this.autoInit = autoInit;
        isRemoteDatabase = true;
    }

    public DatabaseController(@NotNull LocalConfigLoader<?> config) {
        this(config, config.getStatementFactory());
    }

    public DatabaseController(@NotNull LocalConfigLoader<?> config, @NotNull DatabaseStatementFactory statementFactory) {
        this(config, statementFactory, true);
    }

    public DatabaseController(@NotNull LocalConfigLoader<?> config, @NotNull DatabaseStatementFactory statementFactory, boolean autoInit) {
        if (!config.isStatementFactoryCompatible(statementFactory)) {
            throw new IllegalArgumentException("Statement factory is not compatible with config loader");
        }
        this.hikariConfig = null;
        localConfig = config;
        this.statementFactory = statementFactory;
        this.autoInit = autoInit;
        isRemoteDatabase = false;

        try {
            localConfig.testConnection();
            if (autoInit) {
                initDatabase();
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find required database driver", e);
        }
    }

    public @Nullable PlayerRecord getPlayerRecord(@NotNull UUID playerUUID) {
        return singletonResponse(c -> statementFactory.getRetrievePlayerRecordStatement(c, playerUUID), RecordLoader::loadPlayerRecord);
    }

    public void insertPlayerRecord(@NotNull PlayerRecord playerRecord) {
        insertOrUpdate(statementFactory::getCreatePlayerRecordStatement, playerRecord);
    }

    public boolean updatePlayerRecord(@NotNull PlayerRecordModifier recordModification) {
        if (recordModification.hasChanged()) {
            updateOrInsertPlayerRecord(recordModification);
            return true;
        }
        return false;
    }

    public void updateOrInsertPlayerRecord(@NotNull PlayerRecordModifier recordModification) {
        insertOrUpdate(statementFactory::getUpdatePlayerRecordStatement, recordModification);
    }

    public Collection<BanRecord> getPlayerBans(@NotNull UUID playerUUID, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerBansStatement(c, playerUUID, lookupOptions), RecordLoader::loadBanRecord);
    }

    public Collection<BanRecord> getPlayerBansByStatus(@NotNull UUID playerUUID, @NotNull PunishmentStatus status, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerBansByStatusStatement(c, playerUUID, status, lookupOptions), RecordLoader::loadBanRecord);
    }

    public Collection<BanRecord> getIPBans(@NotNull String ipAddress, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrieveIPBansStatement(c, ipAddress, lookupOptions), RecordLoader::loadBanRecord);
    }

    public Collection<BanRecord> getIPBansByStatus(@NotNull String ipAddress, @NotNull PunishmentStatus status, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrieveIPBansByStatusStatement(c, ipAddress, status, lookupOptions), RecordLoader::loadBanRecord);
    }

    public Collection<BanRecord> getPlayerBansOrIPBans(@NotNull UUID playerUUID, @NotNull String ipAddress, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerBansOrIPBansStatement(c, playerUUID, ipAddress, lookupOptions), RecordLoader::loadBanRecord);
    }

    public Collection<BanRecord> getPlayerBansOrIPBansByStatus(@NotNull UUID playerUUID, @NotNull String ipAddress, @NotNull PunishmentStatus status, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerBansOrIPBansByStatusStatement(c, playerUUID, ipAddress, status, lookupOptions), RecordLoader::loadBanRecord);
    }

    public void insertBan(@NotNull BanRecord banRecord) {
        insertOrUpdate(statementFactory::getCreateBanStatement, banRecord);
    }

    public boolean updateBan(@NotNull BanRecordModifier recordModification) {
        if (recordModification.hasChanged()) {
            updateOrInsertBan(recordModification);
            return true;
        }
        return false;
    }

    public void updateOrInsertBan(@NotNull BanRecordModifier recordModification) {
        insertOrUpdate(statementFactory::getUpdatePlayerBanStatement, recordModification);
    }

    public Collection<CommentRecord> getPlayerComments(@NotNull UUID playerUUID, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerCommentsStatement(c, playerUUID, lookupOptions), RecordLoader::loadCommentRecord);
    }

    public Collection<CommentRecord> getPlayerCommentsByType(@NotNull UUID playerUUID, boolean isWarning, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerCommentsByTypeStatement(c, playerUUID, isWarning, lookupOptions), RecordLoader::loadCommentRecord);
    }

    public void insertComment(@NotNull CommentRecord commentRecord) {
        insertOrUpdate(statementFactory::getCreateCommentStatement, commentRecord);
    }

    public Collection<KickRecord> getPlayerKicks(@NotNull UUID playerUUID, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerKicksStatement(c, playerUUID, lookupOptions), RecordLoader::loadKickRecord);
    }

    public void insertKick(@NotNull KickRecord kickRecord) {
        insertOrUpdate(statementFactory::getCreateKickStatement, kickRecord);
    }

    public Collection<MuteRecord> getPlayerMutes(@NotNull UUID playerUUID, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerMutesStatement(c, playerUUID, lookupOptions), RecordLoader::loadMuteRecord);
    }

    public Collection<MuteRecord> getPlayerMutesByStatus(@NotNull UUID playerUUID, @NotNull PunishmentStatus status, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerMutesByStatusStatement(c, playerUUID, status, lookupOptions), RecordLoader::loadMuteRecord);
    }

    public Collection<MuteRecord> getIPMutes(@NotNull String ipAddress, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrieveIPMutesStatement(c, ipAddress, lookupOptions), RecordLoader::loadMuteRecord);
    }

    public Collection<MuteRecord> getIPMutesByStatus(@NotNull String ipAddress, @NotNull PunishmentStatus status, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrieveIPMutesByStatusStatement(c, ipAddress, status, lookupOptions), RecordLoader::loadMuteRecord);
    }

    public Collection<MuteRecord> getPlayerMutesOrIPMutes(@NotNull UUID playerUUID, @NotNull String ipAddress, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerMutesOrIPMutesStatement(c, playerUUID, ipAddress, lookupOptions), RecordLoader::loadMuteRecord);
    }

    public Collection<MuteRecord> getPlayerMutesOrIPMutesByStatus(@NotNull UUID playerUUID, @NotNull String ipAddress, @NotNull PunishmentStatus status, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrievePlayerMutesOrIPMutesByStatusStatement(c, playerUUID, ipAddress, status, lookupOptions), RecordLoader::loadMuteRecord);
    }

    public void insertMute(@NotNull MuteRecord muteRecord) {
        insertOrUpdate(statementFactory::getCreateMuteStatement, muteRecord);
    }

    public boolean updateMute(@NotNull MuteRecordModifier recordModification) {
        if (recordModification.hasChanged()) {
            updateOrInsertMute(recordModification);
            return true;
        }
        return false;
    }

    public void updateOrInsertMute(@NotNull MuteRecordModifier recordModification) {
        insertOrUpdate(statementFactory::getUpdatePlayerMuteStatement, recordModification);
    }

    public Collection<BanRecord> getStaffBans(@NotNull UUID staffUUID, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrieveStaffBansStatement(c, staffUUID, lookupOptions), RecordLoader::loadBanRecord);
    }

    public Collection<CommentRecord> getStaffComments(@NotNull UUID staffUUID, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrieveStaffCommentsStatement(c, staffUUID, lookupOptions), RecordLoader::loadCommentRecord);
    }

    public Collection<KickRecord> getStaffKicks(@NotNull UUID staffUUID, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrieveStaffKicksStatement(c, staffUUID, lookupOptions), RecordLoader::loadKickRecord);
    }

    public Collection<MuteRecord> getStaffMutes(@NotNull UUID staffUUID, LookupOptions lookupOptions) {
        return collectionResponse(c -> statementFactory.getRetrieveStaffMutesStatement(c, staffUUID, lookupOptions), RecordLoader::loadMuteRecord);
    }

    public int countPlayerBans(@NotNull UUID playerUUID) {
        Integer i = singletonResponse(c -> statementFactory.getCountPlayerBansStatement(c, playerUUID), RecordLoader::getCountColumn);
        return i == null ? 0 : i;
    }

    public int countPlayerComments(@NotNull UUID playerUUID) {
        Integer i = singletonResponse(c -> statementFactory.getCountPlayerCommentsStatement(c, playerUUID), RecordLoader::getCountColumn);
        return i == null ? 0 : i;
    }

    public int countPlayerKicks(@NotNull UUID playerUUID) {
        Integer i = singletonResponse(c -> statementFactory.getCountPlayerKicksStatement(c, playerUUID), RecordLoader::getCountColumn);
        return i == null ? 0 : i;
    }

    public int countPlayerMutes(@NotNull UUID playerUUID) {
        Integer i = singletonResponse(c -> statementFactory.getCountPlayerMutesStatement(c, playerUUID), RecordLoader::getCountColumn);
        return i == null ? 0 : i;
    }

    public int countStaffBans(@NotNull UUID staffUUID) {
        Integer i = singletonResponse(c -> statementFactory.getCountStaffBansStatement(c, staffUUID), RecordLoader::getCountColumn);
        return i == null ? 0 : i;
    }

    public int countStaffComments(@NotNull UUID staffUUID) {
        Integer i = singletonResponse(c -> statementFactory.getCountStaffCommentsStatement(c, staffUUID), RecordLoader::getCountColumn);
        return i == null ? 0 : i;
    }

    public int countStaffKicks(@NotNull UUID staffUUID) {
        Integer i = singletonResponse(c -> statementFactory.getCountStaffKicksStatement(c, staffUUID), RecordLoader::getCountColumn);
        return i == null ? 0 : i;
    }

    public int countStaffMutes(@NotNull UUID staffUUID) {
        Integer i = singletonResponse(c -> statementFactory.getCountStaffMutesStatement(c, staffUUID), RecordLoader::getCountColumn);
        return i == null ? 0 : i;
    }

    private <T> int insertOrUpdate(SQLBiFunction<Connection, T, PreparedStatement> statementFunc, T entity) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = statementFunc.apply(connection, entity)) {
                return statement.executeUpdate();

            } catch (SQLException e) {
                LOGGER.error("Exception while trying to retrieve record", e);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to database", e);
        }
        return -1;
    }

    private <T> @Nullable T singletonResponse(SQLFunction<Connection, PreparedStatement> statementFunc, SQLFunction<ResultSet, T> applyerFunc) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = statementFunc.apply(connection);
                 ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return applyerFunc.apply(result);
                }

            } catch (SQLException e) {
                LOGGER.error("Exception while trying to retrieve record", e);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to database", e);
        }
        return null;
    }

    private <T> @NotNull Collection<T> collectionResponse(SQLFunction<Connection, PreparedStatement> statementFunc, SQLFunction<ResultSet, T> applyerFunc) {
        try (Connection connection = getConnection()) {
            try (PreparedStatement statement = statementFunc.apply(connection);
                 ResultSet result = statement.executeQuery()) {

                LinkedList<T> list = new LinkedList<>();

                while (result.next()) {
                    list.add(applyerFunc.apply(result));
                }

                return list;

            } catch (SQLException e) {
                LOGGER.error("Exception while trying to retrieve records", e);
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to database", e);
        }
        return Collections.emptySet();
    }

    public void logMetadata() {
        try (Connection connection = getConnection()) {
            DatabaseMetaData meta = connection.getMetaData();
            LOGGER.debug("Database Metadata Report -");
            LOGGER.debug("Database driver: " + meta.getDriverName());
            LOGGER.debug("Driver Version: " + meta.getDriverVersion());
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to database", e);
        }
    }

    private Connection getConnection() throws SQLException {
        if (isRemoteDatabase) {
            if (dataSource == null) {
                dataSource = new HikariDataSource(hikariConfig);

                if (autoInit) {
                    initDatabase();
                }
            }
            return dataSource.getConnection();
        }

        if (autoInit) {
            initDatabase();
        }
        return localConfig.getConnection();
    }

    public void initDatabase() {
        if (initialized) {
            return;
        }
        initialized = true;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            List<String> initStatements = DatabaseUtil.loadSchemaStatements(statementFactory.getSchemaInitScriptName());
            for (String initStatement : initStatements) {
                statement.addBatch(initStatement);
            }
            statement.executeBatch();

        } catch (SQLException | IOException e) {
            LOGGER.error("Failed to run database initialization script", e);
        }
    }

    public void disconnect() {
        if (isRemoteDatabase) {
            if (dataSource != null) {
                dataSource.close();
                dataSource = null;
            }
        }
        initialized = false;
    }
}
