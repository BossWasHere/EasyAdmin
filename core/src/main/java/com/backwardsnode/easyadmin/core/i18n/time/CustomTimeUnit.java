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

package com.backwardsnode.easyadmin.core.i18n.time;

import com.backwardsnode.easyadmin.core.i18n.MessageKey;

import java.util.HashMap;
import java.util.Map;

public enum CustomTimeUnit {

    MILLISECOND("millisecond", "ms", 'S', 1),
    SECOND("second", 's', 1000),
    MINUTE("minute", 'm', 60 * 1000),
    HOUR("hour", 'h', 60 * 60 * 1000),
    DAY("day", 'd', 24 * 60 * 60 * 1000),
    WEEK("week", 'w', 7 * 24 * 60 * 60 * 1000),
    MONTH("month", "mo", 'M', 304375L * 24 * 60 * 6), // 30.4375 days
    YEAR("year", 'y', 12L * 304375 * 24 * 60 * 6);

    private static final Map<Character, CustomTimeUnit> BY_DESIGNATION = new HashMap<>();

    private final String defaultName;
    private final String unit;
    private final char designation;
    private final long inMillis;
    private final MessageKey singular;
    private final MessageKey plural;

    CustomTimeUnit(String defaultName, char designation, long inMillis) {
        this(defaultName, String.valueOf(designation), designation, inMillis);
    }

    CustomTimeUnit(String defaultName, String unit, char designation, long inMillis) {
        this.defaultName = defaultName;
        this.unit = unit;
        this.designation = designation;
        this.inMillis = inMillis;
        singular = new MessageKey("time." + defaultName, false);
        plural = new MessageKey("time." + defaultName + "s", false);
    }

    public String getDefaultName() {
        return defaultName;
    }

    public String getUnit() {
        return unit;
    }

    public char getDesignation() {
        return designation;
    }

    public long getDurationInMilliseconds() {
        return inMillis;
    }

    public long count(long millis) {
        return millis / inMillis;
    }

    public long countMod(long millis, CustomTimeUnit modUnit) {
        return (millis % modUnit.inMillis) / inMillis;
    }

    MessageKey getSingularKey() {
        return singular;
    }

    MessageKey getPluralKey() {
        return plural;
    }

    static {
        for (CustomTimeUnit unit : values()) {
            BY_DESIGNATION.put(unit.designation, unit);
        }
    }

    public static CustomTimeUnit byDesignation(char designation) {
        return BY_DESIGNATION.get(designation);
    }
}
