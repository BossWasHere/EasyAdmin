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

import com.backwardsnode.easyadmin.api.EasyAdmin;
import com.backwardsnode.easyadmin.api.EasyAdminPlugin;
import com.backwardsnode.easyadmin.api.admin.AdminDynamo;
import com.backwardsnode.easyadmin.api.admin.AdminManager;
import com.backwardsnode.easyadmin.api.admin.ChatFilter;
import com.backwardsnode.easyadmin.api.admin.RecommendationEngine;
import com.backwardsnode.easyadmin.api.builder.RecordBuilder;
import com.backwardsnode.easyadmin.api.config.ConfigurationManager;
import com.backwardsnode.easyadmin.api.contextual.ContextTester;
import com.backwardsnode.easyadmin.api.event.EventBus;
import com.backwardsnode.easyadmin.api.internal.ExternalDataSource;
import com.backwardsnode.easyadmin.api.internal.MessageFactory;
import com.backwardsnode.easyadmin.api.server.NetworkInfo;
import com.backwardsnode.easyadmin.core.boot.Registration;
import com.backwardsnode.easyadmin.core.event.CommonEventBus;
import org.jetbrains.annotations.NotNull;

public class EasyAdminService implements EasyAdmin, AutoCloseable {

    private final EasyAdminPlugin plugin;

    private final CommonEventBus eventBus;

    public EasyAdminService(EasyAdminPlugin plugin) {
        this.plugin = plugin;

        eventBus = new CommonEventBus();
    }

    @Override
    public void close() {
        Registration.get().unregister();
    }

    @Override
    public @NotNull AdminDynamo getAdminDynamo() {
        return null;
    }

    @Override
    public @NotNull AdminManager getAdminManager() {
        return null;
    }

    @Override
    public @NotNull RecordBuilder getRecordBuilder() {
        return null;
    }

    @Override
    public @NotNull ChatFilter getChatFilter() {
        return null;
    }

    @Override
    public @NotNull ConfigurationManager getConfigurationManager() {
        return null;
    }

    @Override
    public @NotNull ContextTester getContextTester() {
        return null;
    }

    @Override
    public @NotNull EventBus getEventBus() {
        return eventBus;
    }

    @Override
    public @NotNull ExternalDataSource getExternalDataSource() {
        return null;
    }

    @Override
    public @NotNull MessageFactory getMessageFactory() {
        return null;
    }

    @Override
    public @NotNull NetworkInfo getNetworkInformation() {
        return null;
    }

    @Override
    public @NotNull RecommendationEngine getRecommendationEngine() {
        return null;
    }
}
