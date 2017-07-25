/*
 * Copyright 2017 HugeGraph Authors
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.baidu.hugegraph.studio.config;

import com.baidu.hugegraph.config.HugeConfig;
import com.baidu.hugegraph.config.OptionSpace;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;

public class StudioConfiguration {

    private static final String DEFAULT_CONFIGURATION_FILE = "hugestudio.properties";

    static {

        OptionSpace.register(StudioApiOptions.Instance());
    }

    private HugeConfig config = null;

    public StudioConfiguration() {
        try {
            URL configurationUrl = this.getClass().getClassLoader()
                    .getResource(DEFAULT_CONFIGURATION_FILE);
            Preconditions.checkNotNull(configurationUrl);
            config = new HugeConfig(configurationUrl.getFile());
        } catch (org.apache.commons.configuration.ConfigurationException e) {
            throw new RuntimeException(String.format(
                    "Caught exception loading StudioConfiguration from %s: ",
                    DEFAULT_CONFIGURATION_FILE), e);
        }
    }


    public String getConnectionsDirectory() {
        return String.format("%s/%s",
                getBaseUserDataDirectory(),
                this.config.get(StudioApiOptions.STUDIO_DATA_CONNECTIONS_DIR));
    }

    public String getNotebooksDirectory() {
        return String.format("%s/%s",
                getBaseUserDataDirectory(),
                this.config.get(StudioApiOptions.STUDIO_DATA_NOTEBOOKS_DIR));

    }

    public String getBaseUserDataDirectory() {
        String userDataDir = this.config.get(StudioApiOptions.STUDIO_DATA_BASE_DIR);
        if (StringUtils.isEmpty(userDataDir) || userDataDir.equals("null")) {
            userDataDir = "~/.hugestudio";
        }
        return replaceHomeDirReferences(userDataDir);
    }

    private String replaceHomeDirReferences(String confDir) {
        if (confDir != null) {
            if (System.getProperty("user.home") != null) {
                return confDir.replaceFirst("^~", System.getProperty("user.home"));
            }
            return confDir;
        }
        return null;
    }
}