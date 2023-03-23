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

import com.backwardsnode.easyadmin.core.i18n.LanguageGroup;

import java.util.HashMap;
import java.util.Map;

public final class TimeUnitLocaleMap {

    private final Map<CustomTimeUnit, TimeUnitPair> timeUnitMap = new HashMap<>();

    public TimeUnitLocaleMap(LanguageGroup lg) {
        for (CustomTimeUnit unit : CustomTimeUnit.values()) {
            timeUnitMap.put(unit, new TimeUnitPair(lg.getMessage(unit.getSingularKey()), lg.getMessage(unit.getPluralKey())));
        }
    }

    public String get(CustomTimeUnit unit, long amount) {
        return amount > 0 ? getPlural(unit) : getSingular(unit);
    }

    public String getSingular(CustomTimeUnit unit) {
        TimeUnitPair tup = timeUnitMap.get(unit);
        return tup == null || tup.singular == null ? unit.getDefaultName() : tup.singular;
    }

    public String getPlural(CustomTimeUnit unit) {
        TimeUnitPair tup = timeUnitMap.get(unit);
        return tup == null || tup.plural == null ? unit.getDefaultName() + 's' : tup.plural;
    }

    private record TimeUnitPair(String singular, String plural) {}

}
