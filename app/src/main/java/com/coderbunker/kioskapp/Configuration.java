package com.coderbunker.kioskapp;

import android.content.Context;
import android.content.SharedPreferences;

public class Configuration {
    private SharedPreferences preferences;
    private static final String NAME = "com.coderbunker.kioskapp";
    private String url;
    private String passphrase;
    private int hotpCounter;
    private String uuid;

    public Configuration(Context context, String url, String passphrase, int hotpCounter, String uuid) {
        this.url = url;
        this.passphrase = passphrase;
        this.hotpCounter = hotpCounter;
        this.uuid = uuid;
        preferences = context.getSharedPreferences(
                NAME, Context.MODE_PRIVATE);
    }

    private Configuration(Context context){
        preferences = context.getSharedPreferences(
                NAME, Context.MODE_PRIVATE);
        url = preferences.getString("url", "https://coderbunker.github.io/kiosk-web/");
        passphrase = preferences.getString("passphrase", null);
        hotpCounter = preferences.getInt("hotp_counter", 0);
        uuid = preferences.getString("uuid", null);
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
        return uuid;
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

    public static Configuration loadFromPreferences(Context context) {
        return new Configuration(context);
    }
}
