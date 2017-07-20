package com.midea.cmms.rest.proxy.server;

import java.util.Properties;

/**
 * Created by liyilin on 2017/7/7.
 */
public abstract class Global {
    private static Properties config;

    public static Properties getConfig() {
        return config;
    }

    public static void setConfig(Properties config) {
        Global.config = config;
    }
}