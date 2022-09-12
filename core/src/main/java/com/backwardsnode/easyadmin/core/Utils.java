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

import com.backwardsnode.easyadmin.api.data.AddressType;

import java.time.Duration;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public final class Utils {

    public static AddressType getAddressType(String networkAddr) {
        //TODO implementation

        return AddressType.UNKNOWN;
    }

    public static Duration parseDuration(String durationStr) throws DateTimeParseException {
        return Duration.of(parseDurationSeconds(durationStr), ChronoUnit.SECONDS);
    }

    public static long parseDurationSeconds(String durationStr) throws DateTimeParseException {
        char[] c = durationStr.toCharArray();

        long duration = 0;
        long x = 0;

        boolean numChar = true;
        int i = 0;

        try {
            for (; i < c.length; i++) {
                int temp = Character.getNumericValue(c[i]);
                if (temp <= -1) {
                    if (numChar) {
                        break;
                    }
                    duration += x * getUnitMultiplier(c[i]);
                    x = 0;
                    numChar = true;

                } else {
                    x = x * 10 + temp;
                    numChar = false;
                }
            }

            return duration < 0 ? Long.MAX_VALUE : duration;
        } catch (Exception e) {
            throw new DateTimeParseException("Could not parse duration", durationStr, i, e);
        }
    }

    private static long getUnitMultiplier(char unit) {
        return switch (unit) {
            case 'd', 'D' -> 86400;
            case 'h', 'H' -> 3600;
            case 'm', 'M' -> 60;
            case 's', 'S' -> 1;
            case 'w', 'W' -> 604800;
            case 'y', 'Y' -> 31556952; //Too precise?
            default -> 0;
        };
    }

}
