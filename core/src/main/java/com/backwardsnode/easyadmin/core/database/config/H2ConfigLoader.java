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

package com.backwardsnode.easyadmin.core.database.config;

import com.backwardsnode.easyadmin.core.database.DatabaseStatementFactory;
import com.backwardsnode.easyadmin.core.database.impl.EasyAdminH2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class H2ConfigLoader extends LocalConfigLoader<EasyAdminH2> {

    public H2ConfigLoader(String filePath, String username) {
        this(filePath, username, null);
    }

    public H2ConfigLoader(String filePath, String username, String password) {
        super(filePath, username, password);
    }

    public void testConnection() throws ClassNotFoundException {
        Class.forName("org.h2.Driver");
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:h2:" + getFilePath(), getUsername(), getPassword());
    }

    @Override
    public EasyAdminH2 getStatementFactory() {
        return new EasyAdminH2();
    }

    @Override
    public boolean isStatementFactoryCompatible(DatabaseStatementFactory factory) {
        return factory instanceof EasyAdminH2;
    }
}