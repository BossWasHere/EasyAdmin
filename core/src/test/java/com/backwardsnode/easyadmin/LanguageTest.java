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
import com.backwardsnode.easyadmin.core.EasyAdminService;
import com.backwardsnode.easyadmin.core.config.RootConfig;
import com.backwardsnode.easyadmin.core.i18n.CommonMessages;
import com.backwardsnode.easyadmin.core.i18n.MessageProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LanguageTest {

    private static LocaleConfiguration configuration;

    @BeforeAll
    public static void initConfig() {
        Yaml yaml = new Yaml();
        configuration = yaml.loadAs("defaultLanguage: en_US\ndateFormat: dd/MM/yyyy\ntimeRangeFormat: dhms", RootConfig.class);
    }

    @Test
    public void loadLanguage() throws IOException {
        EasyAdminService eas = mock(EasyAdminService.class);
        when(eas.loadLanguageFile(anyString(), anyBoolean())).then((Answer<Path>) invocation -> Paths.get("src", "test", "resources", "lang", invocation.getArgument(0)));

        ConfigurationManager cm = mock(ConfigurationManager.class);
        when(cm.getLocaleConfiguration()).thenReturn(configuration);

        when(eas.getConfigurationManager()).thenReturn(cm);
        when(eas.translateAlternateColorCodes(anyChar(), anyString())).then((Answer<String>) invocation -> invocation.getArgument(1));

        MessageProvider mp = new MessageProvider(eas, true, "en_US");
        assertEquals("EasyAdmin", mp.getMessage(CommonMessages.EASYADMIN.PREFIX, "en_US"));
    }

}
