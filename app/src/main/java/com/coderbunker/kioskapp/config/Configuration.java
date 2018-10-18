package com.coderbunker.kioskapp.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.coderbunker.kioskapp.DatabaseConnection;
import com.coderbunker.kioskapp.config.encryption.EncryptionException;

import java.util.UUID;

public class Configuration {
    private SharedPreferences preferences;
    private static final String NAME = "com.coderbunker.kioskapp";
    private String url;
    private String passphrase;
    private int hotpCounter;
    private String uuid;
    private String groupLabel;
    private String deviceLabel;

    public Configuration(Context context, String url, String passphrase, int hotpCounter, String uuid, String groupLabel, String deviceLabel) {
        preferences = context.getSharedPreferences(
                NAME, Context.MODE_PRIVATE);
        setUrl(url);
        setPassphrase(passphrase);
        setHotpCounter(hotpCounter);
        setUuid(uuid);
        setGroupLabel(groupLabel);
        setDeviceLabel(deviceLabel);
    }

    private Configuration(Context context) {
        preferences = context.getSharedPreferences(
                NAME, Context.MODE_PRIVATE);
        url = preferences.getString("url", "https://coderbunker.github.io/kiosk-web/");
        passphrase = preferences.getString("passphrase", null); //TODO change def value
        hotpCounter = preferences.getInt("hotp_counter", 0);
        uuid = preferences.getString("uuid", null);
        groupLabel = preferences.getString("groupLabel", null);
        deviceLabel = preferences.getString("deviceLabel", null);
    }

    public void save() throws EncryptionException {
        if (getPassphrase() != null) {
            DatabaseConnection databaseConnection = new DatabaseConnection();
            databaseConnection.saveConfiguration(this);
        }
    }

    public String getUrl() {
        return url;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public int getHotpCounter() {
        return hotpCounter;
    }

    public String getUuid() {
        if (uuid == null) {
            String uuid = UUID.randomUUID().toString();
            setUuid(uuid);
        }
        return uuid;
    }

    public String getGroupLabel() {
        return groupLabel;
    }

    public String getDeviceLabel() {
        return deviceLabel;
    }

    private void set(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    private void set(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    public void setUrl(String url) {
        this.url = url;
        set("url", url);
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
        set("passphrase", passphrase);
    }

    public void setHotpCounter(int hotpCounter) {
        this.hotpCounter = hotpCounter;
        set("hotp_counter", hotpCounter);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
        set("uuid", uuid);
    }

    public void setDeviceLabel(String deviceLabel) {
        this.deviceLabel = deviceLabel;
        set("deviceLabel", deviceLabel);
    }

    public void setGroupLabel(String groupLabel) {
        this.groupLabel = groupLabel;
        set("groupLabel", groupLabel);
    }

    public static Configuration loadFromPreferences(Context context) {
        return new Configuration(context);
    }


    public static void onConfigChanges(Context context, DatabaseConnection.OnConfigChanged onConfigChanged) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Configuration localConfig = loadFromPreferences(context);
        if (localConfig.getPassphrase() != null) {
            boolean callOnce = false;
            databaseConnection.getConfiguration(localConfig.getPassphrase(), localConfig.getUuid(), context, onConfigChanged, callOnce);
        } else {
            onConfigChanged.OnConfigChanged(localConfig);
        }
    }

    public static void withConfigFromServer(Context context, DatabaseConnection.OnConfigChanged onConfigChanged) {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Configuration localConfig = loadFromPreferences(context);
        if (localConfig.getPassphrase() != null) {
            boolean callOnce = true;
            databaseConnection.getConfiguration(localConfig.getPassphrase(), localConfig.getUuid(), context, onConfigChanged, callOnce);
        } else {
            onConfigChanged.OnConfigChanged(localConfig);
        }
    }

    public static class ConfigurationBuilder {
        private String url;
        private String passphrase;
        private int hotpCounter;
        private String uuid;
        private String groupLabel;
        private String deviceLabel;

        public void setUrl(String url) {
            this.url = url;
        }

        public void setPassphrase(String passphrase) {
            this.passphrase = passphrase;
        }

        public void setHotpCounter(int hotpCounter) {
            this.hotpCounter = hotpCounter;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public void setGroupLabel(String groupLabel) {
            this.groupLabel = groupLabel;
        }

        public void setDeviceLabel(String deviceLabel) {
            this.deviceLabel = deviceLabel;
        }

        public Configuration build(Context context) {
            return new Configuration(context, url, passphrase, hotpCounter, uuid, groupLabel, deviceLabel);
        }
    }
}
