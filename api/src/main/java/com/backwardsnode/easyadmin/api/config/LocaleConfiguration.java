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

package com.backwardsnode.easyadmin.api.config;

/**
 * Holds configuration entries for language and localization.
 */
public final class LocaleConfiguration {

    private String defaultLanguage;

    private String dateFormat;

    private String timeRangeFormat;

    /**
     * Gets the default language for the plugin.
     * @return The default language.
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * Gets the date format for the plugin.
     * @return an acceptable {@link java.text.SimpleDateFormat} format string
     */
    public String getDateFormat() {
        return dateFormat;
    }

    /**
     * Gets the time range format for the plugin.
     * @return a built-in formatting function, such as "dhms" or "wdhm".
     */
    public String getTimeRangeFormat() {
        return timeRangeFormat;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setTimeRangeFormat(String timeRangeFormat) {
        this.timeRangeFormat = timeRangeFormat;
    }
}
