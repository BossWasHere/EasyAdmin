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

package com.backwardsnode.easyadmin;

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.config.ConfigurationManager;
import com.backwardsnode.easyadmin.api.config.LocaleConfiguration;
import com.backwardsnode.easyadmin.api.internal.FileSystemProvider;
import com.backwardsnode.easyadmin.core.i18n.CommonMessages;
import com.backwardsnode.easyadmin.core.i18n.MessageProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.yaml.snakeyaml.Yaml;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LanguageTest {

    private static LocaleConfiguration configuration;

    @BeforeAll
    public static void initConfig() {
        Yaml yaml = new Yaml();
        configuration = yaml.loadAs("defaultLanguage: en_US\ndateFormat: dd/MM/yyyy\ntimeRangeFormat: dhms", LocaleConfiguration.class);
    }

    @Test
    public void loadLanguage() {
        FileSystemProvider fsp = mock(FileSystemProvider.class);
        when(fsp.getLanguageDirectory()).thenReturn(Paths.get("src", "test", "resources", "lang"));

        ConfigurationManager cm = mock(ConfigurationManager.class);
        when(cm.getLocaleConfiguration()).thenReturn(configuration);

        EasyAdmin ea = mock(EasyAdmin.class);
        when(ea.getConfigurationManager()).thenReturn(cm);

        EasyAdminPlugin plugin = mock(EasyAdminPlugin.class);
        when(plugin.getFileSystemProvider()).thenReturn(fsp);
        when(plugin.getInstance()).thenReturn(ea);
        when(plugin.translateAlternateColorCodes(anyChar(), anyString())).then((Answer<String>) invocation -> invocation.getArgument(1));

        MessageProvider mp = new MessageProvider(plugin, true, "en_US");
        assertEquals("EasyAdmin", mp.getMessage(CommonMessages.EASYADMIN.PREFIX, "en_US"));
    }

}
