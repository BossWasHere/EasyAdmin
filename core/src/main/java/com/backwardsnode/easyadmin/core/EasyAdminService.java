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

package com.backwardsnode.easyadmin.core;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.admin.AdminDynamo;
import com.backwardsnode.easyadmin.api.admin.AdminManager;
import com.backwardsnode.easyadmin.api.admin.ChatFilter;
import com.backwardsnode.easyadmin.api.admin.RecommendationEngine;
import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import com.backwardsnode.easyadmin.api.config.ConfigurationManager;
import com.backwardsnode.easyadmin.api.contextual.ContextTester;
import com.backwardsnode.easyadmin.api.data.ServiceSource;
import com.backwardsnode.easyadmin.api.internal.ExternalDataSource;
import com.backwardsnode.easyadmin.api.internal.MessageFactory;
import com.backwardsnode.easyadmin.api.server.NetworkInfo;
import com.backwardsnode.easyadmin.core.boot.Registration;
import com.backwardsnode.easyadmin.core.builder.RecordBuilderImpl;
import com.backwardsnode.easyadmin.core.commit.CommitterMode;
import com.backwardsnode.easyadmin.core.commit.EventFiringCommitter;
import com.backwardsnode.easyadmin.core.commit.WithholdAgent;
import com.backwardsnode.easyadmin.core.component.AdminManagerImpl;
import com.backwardsnode.easyadmin.core.component.PermissionsPlatform;
import com.backwardsnode.easyadmin.core.config.RootConfig;
import com.backwardsnode.easyadmin.core.config.yaml.YamlRootConfig;
import com.backwardsnode.easyadmin.core.database.DatabaseController;
import com.backwardsnode.easyadmin.core.database.util.DatabaseUtil;
import com.backwardsnode.easyadmin.core.event.CommonEventBus;
import com.backwardsnode.easyadmin.core.exception.ConfigurationException;
import com.backwardsnode.easyadmin.core.exception.ServiceInitializationException;
import com.backwardsnode.easyadmin.core.i18n.MessageProvider;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.EnumSet;

public class EasyAdminService implements EasyAdmin, AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyAdminService.class);

    private static final String LANG_DIRECTORY = "lang";
    private static final String CONFIG_YML = "config.yml";

    private final EasyAdminPlugin plugin;

    private final Path dataDirectory;
    private final Path languageDirectory;

    private final EventFiringCommitter apiCommitter;
    private final PermissionsPlatform permissionsPlatform;
    private final WithholdAgent withholdAgent;

    private final DatabaseController databaseController;
    private final AdminManager adminManager;
    private final RecordBuilder apiRecordBuilder;
    private final RootConfig configurationManager;
    private final CommonEventBus eventBus;
    private final MessageFactory messageFactory;

    private boolean closed = false;

    public EasyAdminService(EasyAdminPlugin plugin) throws ServiceInitializationException {
        LOGGER.debug("Initializing EasyAdminService");
        this.plugin = plugin;
        this.dataDirectory = plugin.getDataDirectory();
        this.languageDirectory = dataDirectory.resolve(LANG_DIRECTORY);

        try {
            createDirectories();

            try (InputStream is = Files.newInputStream(loadDataFile(CONFIG_YML, false))) {
                configurationManager = YamlRootConfig.loadConfig(is);
                configurationManager.validate("");
            }
        } catch (IOException e) {
            throw new ServiceInitializationException("Failed to initialize plugin files", e);
        } catch (ConfigurationException e) {
            throw new ServiceInitializationException("Failed to load configuration", e);
        }

        LOGGER.debug("Attempting to initialize database " + configurationManager.getDatabaseConfiguration().getDatabasePlatform());
        databaseController = DatabaseUtil.createController(this, configurationManager.getDatabaseConfiguration());
        if (databaseController == null) {
            throw new ServiceInitializationException("Failed to create database controller");
        }
        databaseController.logMetadata();

        // TODO permissions platform
        permissionsPlatform = new PermissionsPlatform();
        // TODO withhold agent
        withholdAgent = null;


        apiCommitter = new EventFiringCommitter(this, ServiceSource.API, EnumSet.of(CommitterMode.EVENT_ALLOW_CANCELLATIONS));
        adminManager = new AdminManagerImpl(this, databaseController);
        apiRecordBuilder = new RecordBuilderImpl(apiCommitter);
        messageFactory = new MessageProvider(this,true);
        eventBus = new CommonEventBus();
    }

    @Override
    public @NotNull AdminDynamo getAdminDynamo() {
        verifyOpen();
        return null;
    }

    @Override
    public @NotNull AdminManager getAdminManager() {
        verifyOpen();
        return adminManager;
    }

    @Override
    public @NotNull RecordBuilder getRecordBuilder() {
        verifyOpen();
        return apiRecordBuilder;
    }

    @Override
    public @NotNull ChatFilter getChatFilter() {
        verifyOpen();
        return null;
    }

    @Override
    public @NotNull ConfigurationManager getConfigurationManager() {
        verifyOpen();
        return configurationManager;
    }

    @Override
    public @NotNull ContextTester getContextTester() {
        verifyOpen();
        return null;
    }

    @Override
    public @NotNull CommonEventBus getEventBus() {
        verifyOpen();
        return eventBus;
    }

    @Override
    public @NotNull ExternalDataSource getExternalDataSource() {
        verifyOpen();
        return null;
    }

    @Override
    public @NotNull MessageFactory getMessageFactory() {
        verifyOpen();
        return messageFactory;
    }

    @Override
    public @NotNull NetworkInfo getNetworkInformation() {
        verifyOpen();
        return null;
    }

    @Override
    public @NotNull RecommendationEngine getRecommendationEngine() {
        verifyOpen();
        return null;
    }

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        databaseController.disconnect();
        Registration.get().unregister();
    }

    public EasyAdminPlugin getPlugin() {
        return plugin;
    }

    public PermissionsPlatform getPermissionsPlatform() {
        return permissionsPlatform;
    }

    public WithholdAgent getWithholdAgent() {
        return withholdAgent;
    }

    public void resetConfig() throws IOException {
        loadDataFile(CONFIG_YML, true);
    }

    public Path getDataFile(final String dataFile) {
        return dataDirectory.resolve(dataFile);
    }

    public Path loadDataFile(final String dataFile, boolean reset) throws IOException {
        Path dataFilePath = getDataFile(dataFile);

        if (Files.notExists(dataFilePath) || reset) {
            extractResource('/' + dataFile, dataFilePath);
        }

        return dataFilePath;
    }

    public void resetLanguage(final String languageFile) throws IOException {
        loadLanguageFile(languageFile, true);
    }

    public Path getLanguageFile(final String languageFile) {
        return languageDirectory.resolve(languageFile);
    }

    public Path loadLanguageFile(final String languageFile, boolean reset) throws IOException {
        Path languageFilePath = getLanguageFile(languageFile);

        if (Files.notExists(languageFilePath) || reset) {
            extractResource('/' + LANG_DIRECTORY + '/' + languageFile, languageFilePath);
        }

        return languageFilePath;
    }

    public String translateAlternateColorCodes(char altColorChar, String text) {
        return plugin.translateAlternateColorCodes(altColorChar, text);
    }

    private void verifyOpen() {
        if (closed) {
            throw new IllegalStateException("Service is closed");
        }
    }

    private void createDirectories() throws IOException {
        Files.createDirectories(dataDirectory);
        Files.createDirectories(languageDirectory);
    }

    private void extractResource(final String internalName, final Path externalPath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(internalName)) {
            if (is == null) {
                throw new IOException("Could not find internal resource " + internalName);
            }
            try (OutputStream os = Files.newOutputStream(externalPath)) {
                is.transferTo(os);
            }
        }
    }
}
