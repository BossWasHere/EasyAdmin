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

package com.backwardsnode.easyadmin.core.i18n;

import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.config.LocaleConfiguration;
import com.backwardsnode.easyadmin.api.entity.CommandExecutor;
import com.backwardsnode.easyadmin.api.internal.MessageFactory;
import com.backwardsnode.easyadmin.api.internal.MessageKey;
import com.backwardsnode.easyadmin.core.EasyAdminService;
import com.backwardsnode.easyadmin.core.i18n.time.CustomTimeUnit;
import com.backwardsnode.easyadmin.core.i18n.time.TimeRangeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.backwardsnode.easyadmin.core.i18n.CommonMessages.EASYADMIN;

public class MessageProvider implements MessageFactory {

    public static final String DEFAULT_LANGUAGE = "en_US";
    private static final String FALLBACK_DATE_FORMAT = "yyyy-MM-dd HH:mm:ssZ";

    private final Logger logger = LoggerFactory.getLogger(MessageProvider.class);

    private final EasyAdminService service;
    private final String defaultLanguage;
    private final Map<String, LanguageGroup> loadedLanguages;

    private LanguageGroup defaultLanguageGroup;
    private DateTimeFormatter dateFormat;
    private TimeRangeFormat timeRangeFormat;

    public MessageProvider(EasyAdminService service, boolean loadDefault) {
        this(service, loadDefault, DEFAULT_LANGUAGE);
    }

    public MessageProvider(EasyAdminService service, boolean loadDefault, String defaultLanguage) {
        this.service = service;
        this.defaultLanguage = defaultLanguage;
        loadedLanguages = new HashMap<>();

        if (loadDefault) {
            defaultLanguageGroup = loadLanguage(defaultLanguage, false);
        }

        LocaleConfiguration localeConfig = service.getConfigurationManager().getLocaleConfiguration();

        try {
            dateFormat = DateTimeFormatter.ofPattern(localeConfig.getDateFormat(), Locale.ROOT);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid date format '" + localeConfig.getDateFormat() + "', using default format", e);
            dateFormat = DateTimeFormatter.ofPattern(FALLBACK_DATE_FORMAT, Locale.ROOT);
        }

        try {
            timeRangeFormat = new TimeRangeFormat(localeConfig.getTimeRangeFormat());
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid time range format '" + localeConfig.getTimeRangeFormat() + "', using default format", e);
            timeRangeFormat = new TimeRangeFormat(CustomTimeUnit.DAY, CustomTimeUnit.HOUR, CustomTimeUnit.MINUTE);
        }
    }

    public void reloadLanguage(String language) {
        loadLanguage(language, true);
    }

    private LanguageGroup loadLanguage(String language, boolean forceReload) {
        LanguageGroup lg = loadedLanguages.get(language);
        if (lg != null && !forceReload) {
            return lg;
        }

        logger.debug("Loading language: " + language);

        Path langFilePath;
        try {
            langFilePath = service.loadLanguageFile(language + ".yml", false);
        } catch (IOException e) {
            logger.error("Failed to load language file: " + language, e);
            return null;
        }

        Yaml yaml = new Yaml();
        try (InputStream is = Files.newInputStream(langFilePath)) {
            Map<String, ?> languageMap = yaml.load(is);

            LanguageGroup newLanguage = new LanguageGroup(languageMap);
            loadedLanguages.put(language, newLanguage);
            return newLanguage;
        } catch (IOException e) {
            logger.error("Failed to load language: " + language, e);
            return null;
        }
    }

    private LanguageGroup loadLanguageOrDefault(String language) {
        LanguageGroup lg = loadLanguage(language, false);
        if (lg == null) {
            if (defaultLanguageGroup == null) {
                defaultLanguageGroup = loadLanguage(defaultLanguage, false);

                if (defaultLanguageGroup == null && !DEFAULT_LANGUAGE.equals(language)) {
                    logger.warn("Failed to load default language, trying " + DEFAULT_LANGUAGE);
                    defaultLanguageGroup = loadLanguage(DEFAULT_LANGUAGE, false);

                    if (defaultLanguageGroup == null) {
                        throw new RuntimeException("Failed to load default and/or fallback languages");
                    }
                }
            }

            return defaultLanguageGroup;
        }
        return lg;
    }

    public void sendMessage(CommandExecutor executor, MessageKey message, Object... args) {
        executor.sendMessage(getMessage(message, executor.getLocale(), args));
    }

    @Override
    public String getMessage(MessageKey key, String language, Object... args) {
        LanguageGroup lg = loadLanguageOrDefault(language);
        String msg = lg.getMessage(key);

        if (args.length > 0) {
            Object[] newArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof MessageKey) {
                    newArgs[i] = lg.getMessage((MessageKey) arg);
                } else if (arg instanceof Duration) {
                    newArgs[i] = timeRangeFormat.format((Duration) arg, lg.getTimeUnitMap());
                } else if (arg instanceof LocalDateTime) {
                    newArgs[i] = dateFormat.format((LocalDateTime) arg);
                } else {
                    newArgs[i] = arg;
                }
            }
            msg = MessageFormat.format(msg, newArgs);
        }
        if (key.addPrefix()) {
            msg = lg.getMessage(EASYADMIN.PREFIX) + msg;
        }
        return service.translateAlternateColorCodes('&', msg);
    }

    @Override
    public String getMessageDefault(MessageKey key, Object... args) {
        return getMessage(key, defaultLanguage, args);
    }

}
