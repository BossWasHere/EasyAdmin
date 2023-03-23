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

package com.backwardsnode.easyadmin.api.internal;

import java.io.IOException;

/**
 * Enables the initial creation and verification of EasyAdmin plugin files.
 */
public interface FileSystemInitializer {

    /**
     * Creates the necessary directories for the plugin if they do not exist.
     * @throws IOException if the directories could not be created.
     */
    void createDirectories() throws IOException;

    /**
     * Initializes all plugin files if they do not exist.
     * @throws IOException if an error occurs while creating the files.
     */
    void initialize() throws IOException;

    /**
     * Re-extracts the main configuration file for the plugin.
     * @throws IOException if the file could not be extracted.
     */
    void resetConfig() throws IOException;

    /**
     * Re-extracts the given language file from within the plugin.
     * @param languageFile the language file to re-extract.
     * @throws IOException if the file could not be extracted.
     */
    void resetLanguage(String languageFile) throws IOException;

}
