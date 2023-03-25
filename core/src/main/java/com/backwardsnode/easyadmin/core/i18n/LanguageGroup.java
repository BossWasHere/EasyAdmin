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

import com.backwardsnode.easyadmin.api.internal.MessageKey;
import com.backwardsnode.easyadmin.core.i18n.time.TimeUnitLocaleMap;

import java.util.Map;

public class LanguageGroup {

    private final Map<String, ?> messages;
    private final TimeUnitLocaleMap timeUnits;

    public LanguageGroup(Map<String, ?> messages) {
        this.messages = messages;
        timeUnits = new TimeUnitLocaleMap(this);
    }

    @SuppressWarnings("unchecked")
    public String getMessage(MessageKey key) {
        Map<String, ?> map = messages;
        String[] parts = key.key().split("\\.");

        int i = 0;
        while (map != null && i < parts.length - 1) {
            map = (Map<String, ?>) map.get(parts[i++]);
        }

        if (map != null && map.get(parts[i]) instanceof String msg) {
            return msg;
        }
        return null;
    }

    public TimeUnitLocaleMap getTimeUnitMap() {
        return timeUnits;
    }

}
