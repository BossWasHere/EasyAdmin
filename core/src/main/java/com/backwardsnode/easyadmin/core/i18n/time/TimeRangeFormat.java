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

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.LinkedList;

public class TimeRangeFormat {

    private final CustomTimeUnit[] units;

    public TimeRangeFormat(CustomTimeUnit unit, CustomTimeUnit... moreUnits) {
        units = new CustomTimeUnit[moreUnits.length + 1];
        System.arraycopy(moreUnits, 0, units, 1, moreUnits.length);
        units[0] = unit;
    }

    public TimeRangeFormat(String unitString) throws DateTimeParseException {
        LinkedList<CustomTimeUnit> unitList = new LinkedList<>();

        char[] chars = unitString.toCharArray();
        if (chars.length == 0) {
            throw new DateTimeParseException("Empty time unit string", unitString, 0);
        }

        CustomTimeUnit lastUnit = null;
        for (int i = 0; i < chars.length; i++) {
            CustomTimeUnit ctu = CustomTimeUnit.byDesignation(chars[i]);
            if (ctu == null) {
                throw new DateTimeParseException("Invalid time unit character", unitString, i);
            }
            if (lastUnit != null && lastUnit.getDurationInMilliseconds() <= ctu.getDurationInMilliseconds()) {
                throw new DateTimeParseException("Time unit characters must be in descending order", unitString, i);
            }
            lastUnit = ctu;
            unitList.add(ctu);
        }
        units = unitList.toArray(new CustomTimeUnit[0]);
    }

    public CustomTimeUnit[] getUnits() {
        return Arrays.copyOf(units, units.length);
    }

    public String format(Duration duration, TimeUnitLocaleMap timeUnitNames) {
        return format(duration.toMillis(), timeUnitNames);
    }

    public String format(long millis, TimeUnitLocaleMap timeUnitNames) {
        long[] values = new long[units.length];

        StringBuilder sb = new StringBuilder(50);
        if (millis < 0) {
            millis = -millis;
            sb.append('-');
        }

        for (int i = 0; i < units.length - 1; i++) {
            values[i] = units[i].countMod(millis, units[i + 1]);
        }
        values[units.length - 1] = units[units.length - 1].count(millis);
        for (int i = 0; i < units.length; i++) {
            if (values[i] > 0) {
                sb.append(values[i]).append(" ").append(timeUnitNames.get(units[i], values[i])).append(", ");
            }
        }

        if (sb.length() > 1) {
            return sb.substring(0, sb.length() - 2);
        }
        return "";
    }

}
