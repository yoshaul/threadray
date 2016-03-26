package com.yossis.threadray.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

/**
 * General configuration handler.
 *
 * @author Yossi Shaul
 */
public class Config {

    private final File configFile;
    private Properties props;

    public Config(File configFile) {
        this.configFile = configFile;
    }

    protected void load() throws IOException {
        props = new Properties();
        if (configFile.exists()) {
            props.load(new FileInputStream(configFile));
        }
    }

    public void save() {
        if (!configFile.getParentFile().exists()) {
            configFile.getParentFile().mkdirs();
        }
        try (FileOutputStream out = new FileOutputStream(configFile)) {
            props.store(out, "Updated " + new Date().toString());
        } catch (Exception e) {
            //log.error("Failed to store config file", e);
        }
    }

    public String getProperty(String key) {
        return props.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue) {
        String property = getProperty(key);
        if (property != null) {
            return Integer.parseInt(property);
        }
        return defaultValue;
    }

    public void setProperty(String key, Object value) {
        props.setProperty(key, value + "");
    }
}
