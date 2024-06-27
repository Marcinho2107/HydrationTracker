package com.example.hydrationtracker_git;

import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_ALTER = "alter";
    private static final String KEY_GROESSE = "groesse";
    private static final String KEY_GESCHLECHT = "geschlecht";
    private static final String KEY_PROFILE_IMAGE_PATH = "profile_image_path";
    private static final String KEY_WASSERBEDARF = "wasserbedarf";

    private SharedPreferences preferences;

    public UserPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(String username, String password, int alter, int groesse, String geschlecht, String profileImagePath) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_PASSWORD, password);
        editor.putInt(KEY_ALTER, alter);
        editor.putInt(KEY_GROESSE, groesse);
        editor.putString(KEY_GESCHLECHT, geschlecht);
        editor.putString(KEY_PROFILE_IMAGE_PATH, profileImagePath);
        editor.apply();
    }

    public void saveWasserbedarf(int wasserbedarf) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(KEY_WASSERBEDARF, wasserbedarf);
        editor.apply();
    }

    public boolean login(String username, String password) {
        String storedUsername = preferences.getString(KEY_USERNAME, null);
        String storedPassword = preferences.getString(KEY_PASSWORD, null);
        return username.equals(storedUsername) && password.equals(storedPassword);
    }

    public String getUsername() {
        return preferences.getString(KEY_USERNAME, null);
    }

    public String getPassword() {
        return preferences.getString(KEY_PASSWORD, null);
    }

    public int getAlter() {
        return preferences.getInt(KEY_ALTER, 0);
    }

    public int getGroesse() {
        return preferences.getInt(KEY_GROESSE, 0);
    }

    public String getGeschlecht() {
        return preferences.getString(KEY_GESCHLECHT, null);
    }

    public String getProfileImagePath() {
        return preferences.getString(KEY_PROFILE_IMAGE_PATH, null);
    }

    public int getWasserbedarf() {
        return preferences.getInt(KEY_WASSERBEDARF, 0);
    }
}
