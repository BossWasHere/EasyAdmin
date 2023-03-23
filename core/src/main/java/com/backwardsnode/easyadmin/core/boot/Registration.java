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

package com.backwardsnode.easyadmin.core.boot;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.EasyAdminProvider;

import java.lang.reflect.Method;

public class Registration {

    private static Registration instance;

    private final Method register;
    private final Method unregister;

    private Registration() {
        try {
            register = EasyAdminProvider.class.getDeclaredMethod("register", EasyAdmin.class);
            unregister = EasyAdminProvider.class.getDeclaredMethod("unregister");

            register.setAccessible(true);
            unregister.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Failed to find register/unregister method", e);
        }
    }

    public void register(EasyAdmin instance) {
        try {
            register.invoke(null, instance);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register EasyAdmin", e);
        }
    }

    public void unregister() {
        try {
            unregister.invoke(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to unregister EasyAdmin", e);
        }
    }

    public static Registration get() {
        if (instance == null) {
            instance = new Registration();
        }
        return instance;
    }

}
