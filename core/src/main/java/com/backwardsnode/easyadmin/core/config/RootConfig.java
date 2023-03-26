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

import com.backwardsnode.easyadmin.api.config.CommandConfiguration;
import com.backwardsnode.easyadmin.api.config.ConfigurationManager;
import com.backwardsnode.easyadmin.api.config.FeatureConfiguration;
import com.backwardsnode.easyadmin.api.config.LocaleConfiguration;
import com.backwardsnode.easyadmin.core.exception.ConfigurationException;

public final class RootConfig implements ConfigurationManager, LocaleConfiguration, ConfigChecker {

    private String language;
    private boolean debugMode;
    private String dateFormat;
    private String timeFormat;

    private DatabaseConfig storage;
    private UUIDResolutionConfig uuid;
    private CommandConfig commands;

    @Override
    public void validate(final String parentPath) throws ConfigurationException {
        if (storage == null) throw new ConfigurationException(parentPath, "storage");
        if (uuid == null) throw new ConfigurationException(parentPath, "uuid");
        if (commands == null) throw new ConfigurationException(parentPath, "commands");

        storage.validate("storage");
        uuid.validate("uuid");
        commands.validate("commands");
    }

    public DatabaseConfig getDatabaseConfiguration() {
        return storage;
    }

    @Override
    public CommandConfiguration getCommandConfiguration() {
        return commands;
    }

    @Override
    public FeatureConfiguration getFeatureConfiguration() {
        return null;
    }

    @Override
    public LocaleConfiguration getLocaleConfiguration() {
        return this;
    }

    @Override
    public String getDefaultLanguage() {
        return language;
    }


    @Override
    public String getDateFormat() {
        return dateFormat;
    }


    @Override
    public String getTimeRangeFormat() {
        return timeFormat;
    }

    public void setDefaultLanguage(String language) {
        this.language = language;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setTimeRangeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

}
