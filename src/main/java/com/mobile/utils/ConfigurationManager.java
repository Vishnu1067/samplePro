package com.mobile.utils;

import java.io.FileInputStream;
import java.util.Properties;


public class ConfigurationManager {

    private static ConfigurationManager instance;
    private Properties prop = new Properties();

    private ConfigurationManager(String configFile)  {

        try {

            FileInputStream inputStream = new FileInputStream(configFile);
            prop.load(inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getProperty(String key) {

        return prop.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {

        return prop.getProperty(key, defaultValue);
    }


    public static ConfigurationManager getInstance() {

        if (instance == null) {
            String configFile = "config.properties";
            if (System.getenv().containsKey("CONFIG_FILE")) {
                configFile = System.getenv().get("CONFIG_FILE");
                System.out.println("Using config file from " + configFile);
            }

            instance = new ConfigurationManager(configFile);
        }
        return instance;
    }

    public boolean containsKey(String key) {

        return prop.containsKey(key);
    }
}
