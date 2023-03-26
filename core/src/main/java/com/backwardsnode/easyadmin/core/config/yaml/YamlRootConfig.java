/*
 * MIT License
 *
 * Copyright (c) 2023 Thomas Stephenson (BackwardsNode) <backwardsnode@gmail.com>
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

package com.backwardsnode.easyadmin.core.config.yaml;

import com.backwardsnode.easyadmin.core.config.CommandConfig;
import com.backwardsnode.easyadmin.core.config.RootConfig;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public final class YamlRootConfig {

    private YamlRootConfig() {}

    public static RootConfig loadConfig(InputStream is) {
        LoaderOptions options = new LoaderOptions();
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);

        Yaml yaml = new Yaml(new Constructor(RootConfig.class, options), representer);
        yaml.setBeanAccess(BeanAccess.FIELD);

        TypeDescription muteCommandConfig = new TypeDescription(CommandConfig.MuteCommandConfig.class);
        muteCommandConfig.addPropertyParameters("blockedCommands", String.class, CommandConfig.ContextBound.class);
        muteCommandConfig.addPropertyParameters("allowedMessages", String.class, CommandConfig.ContextBound.class);
        yaml.addTypeDescription(muteCommandConfig);

        return yaml.load(is);
    }

    public static void saveConfig(RootConfig config, OutputStream os) {
        DumperOptions options = new DumperOptions();
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(4);
        options.setProcessComments(true);
        options.setPrettyFlow(true);

        Yaml yaml = new Yaml(options);

        try (OutputStreamWriter osw = new OutputStreamWriter(os)) {
            yaml.dump(config, osw);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
