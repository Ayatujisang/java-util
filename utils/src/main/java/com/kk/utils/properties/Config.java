package com.kk.utils.properties;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import java.util.List;

public class Config {
    private static PropertiesConfiguration config = null;

    static {
        try {
            config = new PropertiesConfiguration(
                    "xx.properties");
            // 自动重新加载
            // config.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static String getString(String key) {
        return config.getString(key);
    }

    public static String getString(String key, String def) {
        return config.getString(key, def);
    }

    public static int getInt(String key) {
        return config.getInt(key);
    }

    public static int getInt(String key, int def) {
        return config.getInt(key, def);
    }

    public static long getLong(String key) {
        return config.getLong(key);
    }

    public static long getLong(String key, long def) {
        return config.getLong(key, def);
    }

    public static float getFloat(String key) {
        return config.getFloat(key);
    }

    public static float getFloat(String key, float def) {
        return config.getFloat(key, def);
    }

    public static double getDouble(String key) {
        return config.getDouble(key);
    }

    public static double getDouble(String key, double def) {
        return config.getDouble(key, def);
    }

    public static boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public static boolean getBoolean(String key, boolean def) {
        return config.getBoolean(key, def);
    }

    // 用逗号分割 或者  多行的同名键值对，来返回一组值
    public static String[] getStringArray(String key) {
        return config.getStringArray(key);
    }

    @SuppressWarnings("unchecked")
    public static List getList(String key) {
        return config.getList(key);
    }

    @SuppressWarnings("unchecked")
    public static List getList(String key, List def) {
        return config.getList(key, def);
    }

    public static void setProperty(String key, Object value) {
        config.setProperty(key, value);
    }

    // 调用 setProperty后，再调用save方法，  保存配置信息， 或者设置自动保存：config.setAutoSave(true);
    public static void save() throws ConfigurationException {
        config.save();
    }

} 
