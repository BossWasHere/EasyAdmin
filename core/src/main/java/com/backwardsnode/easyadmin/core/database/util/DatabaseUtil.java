/*
 * MIT License
 *
 * Copyright (c) 2022-2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

package com.backwardsnode.easyadmin.core.database.util;

import com.backwardsnode.easyadmin.core.EasyAdminService;
import com.backwardsnode.easyadmin.core.config.DatabaseConfig;
import com.backwardsnode.easyadmin.core.database.DatabaseController;
import com.backwardsnode.easyadmin.core.database.DatabasePlatform;
import com.backwardsnode.easyadmin.core.database.config.H2ConfigLoader;
import com.backwardsnode.easyadmin.core.database.config.MySQLConfigLoader;
import com.backwardsnode.easyadmin.core.database.config.PostgresConfigLoader;
import com.backwardsnode.easyadmin.core.database.config.SQLiteConfigLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public final class DatabaseUtil {

    public static DatabaseController createController(final EasyAdminService service, final DatabaseConfig config) {
        final DatabasePlatform platform = config.getDatabasePlatform();
        if (platform == null) {
            return null;
        }
        return switch (platform) {
            case H2 -> new DatabaseController(
                    new H2ConfigLoader(
                            service.getDataFile("easyadmin.h2").toAbsolutePath().toString(),
                            config.getDatabaseUser()
                    )
            );
            case H2_SECURE -> new DatabaseController(
                    new H2ConfigLoader(
                            service.getDataFile("easyadmin_s.h2").toAbsolutePath().toString(),
                            config.getDatabaseUser(),
                            config.getDatabasePassword()
                    )
            );
            case MYSQL -> new DatabaseController(
                    new MySQLConfigLoader(
                            config.getDatabaseHost(),
                            config.getDatabasePort(),
                            config.getDatabaseUser(),
                            config.getDatabasePassword(),
                            config.getDatabaseName()
                    )
            );
            case POSTGRESQL -> new DatabaseController(
                    new PostgresConfigLoader(
                            config.getDatabaseHost(),
                            config.getDatabasePort(),
                            config.getDatabaseUser(),
                            config.getDatabasePassword(),
                            config.getDatabaseName()
                    )
            );
            case SQLITE -> new DatabaseController(
                    new SQLiteConfigLoader(
                            service.getDataFile("easyadmin.db").toAbsolutePath().toString()
                    )
            );
        };
    }

    public static List<String> loadSchemaStatements(String schemaFile) throws IOException {
        try (InputStream is = DatabaseUtil.class.getResourceAsStream("/schema/" + schemaFile)) {

            if (is == null) {
                throw new FileNotFoundException("Schema file " + schemaFile + " not found");
            }

            List<String> statements = new LinkedList<>();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                StringBuilder statement = new StringBuilder();
                String line;
                boolean inComment = false;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("--")) {
                        continue;
                    }
                    int startCommentIndex = 0;
                    int endCommentIndex;
                    do {
                        if (!inComment) {
                            startCommentIndex = line.indexOf("/*");
                            if (startCommentIndex != -1) {
                                inComment = true;
                            }
                        }

                        endCommentIndex = -1;
                        if (inComment) {
                            endCommentIndex = line.indexOf("*/", startCommentIndex);
                            if (endCommentIndex != -1) {
                                inComment = false;
                                line = line.substring(0, startCommentIndex) + line.substring(endCommentIndex + 2);
                            }
                        }
                    } while (endCommentIndex != -1);

                    if (inComment) {
                        statement.append(line, 0, startCommentIndex);
                    } else {
                        statement.append(line);
                    }

                    int length = statement.length();

                    if (length > 0 && statement.charAt(length - 1) == ';') {
                        statements.add(statement.toString().replaceAll(" +", " "));
                        statement.setLength(0);
                    }
                }
            }

            return statements;

        } catch (Exception e) {
            throw new IOException("Failed to load schema file " + schemaFile, e);
        }
    }

}
