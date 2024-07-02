package com.example.hydrationtracker_git.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Die Klasse {@code UserPreferences} erledigt die Userdaten Speicherung,
 * dazu gehört die Speicherung und Abrufung von Benutzerdaten und den Wasserbedarf.
 */

public class UserPreferences {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_WASSERBEDARF = "wasserbedarf";
    private final SharedPreferences preferences;

    /**
     * Konstruktor, der ein {@code UserPreferences}-Objekt erstellt und die {@link SharedPreferences} initialisiert.
     *
     * @param context Der Kontext der Anwendung.
     */

    public UserPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Speichert die Userdaten in den {@link SharedPreferences}.
     *
     * @param username Der Nickname des Users.
     * @param password Das Passwort des Users.
     * @param alter Das Alter des Users.
     * @param groesse Die Körpergröße des Users in Zentimetern.
     * @param geschlecht Das Geschlecht des User.
     * @param profileImagePath Der Pfad zum Profilbild des Users.
     */

    public void saveUser(String username, String password, int alter, int groesse, String geschlecht, String profileImagePath) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(username + "_username", username);
        editor.putString(username + "_password", password);
        editor.putInt(username + "_alter", alter);
        editor.putInt(username + "_groesse", groesse);
        editor.putString(username + "_geschlecht", geschlecht);
        editor.putString(username + "_profile_image_path", profileImagePath);
        editor.apply();
        Log.d("UserPreferences", "User saved: " + username);
    }


    /**
     * Speichert den Wasserbedarf des users in den {@link SharedPreferences}.
     *
     * @param username Der Nickname des Users.
     * @param wasserbedarf Der daily Wasserbedarf des Users in Millilitern.
     */
    public void saveWasserbedarf(String username, int wasserbedarf) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(username + "_wasserbedarf", wasserbedarf);
        editor.apply();
    }

    /**
     * Prüft daten des Users, also Passwort + Username.
     *
     * @param username Der Nickrname des Users.
     * @param password Das Passwort des Users.
     * @return {@code true}, wenn die Anmeldedaten korrekt sind, sonst {@code false}.
     */

    public boolean login(String username, String password) {
        String storedPassword = preferences.getString(username + "_password", null);
        return password.equals(storedPassword);
    }

    /**
     * Gibt den Nicknamen zurück.
     *
     * @param username Der Nickname des Users.
     * @return Der gesavede Username.
     */

    public String getUsername(String username) {
        return preferences.getString(username + "_username", null);
    }

    /**
     * Gibt das Passwort zurück.
     *
     * @param username Der Nickname des Users.
     * @return Das gespeicherte Passwort.
     */

    public String getPassword(String username) {
        return preferences.getString(username + "_password", null);
    }

    /**
     * Gibt das Alter des Users zurück.
     *
     * @param username Der Nickname des Users.
     * @return Das gespeicherte Alter.
     */

    public int getAlter(String username) {
        return preferences.getInt(username + "_alter", 0);
    }

    /**
     * Gibt die Körpergröße des Users zurück.
     *
     * @param username Der Nickname des Users.
     * @return Die gespeicherte Körpergröße in Zentimetern.
     */

    public int getGroesse(String username) {
        return preferences.getInt(username + "_groesse", 0);
    }

    /**
     * Gibt das Geschlecht des Users zurück.
     *
     * @param username Der Nickname des Users.
     * @return Das gespeicherte Geschlecht.
     */

    public String getGeschlecht(String username) {
        return preferences.getString(username + "_geschlecht", null);
    }

    /**
     * Gibt den Pfad zum Profilbild des Users zurück.
     *
     * @param username Der Nickname des Users.
     * @return Der gespeicherte Pfad zum Profilbild.
     */

    public String getProfileImagePath(String username) {
        return preferences.getString(username + "_profile_image_path", null);
    }

    /**
     * Gibt den täglichen Wasserbedarf des Users zurück.
     *
     * @param username Der Nickname des Users.
     * @return Der gespeicherte Wasserbedarf in Millilitern.
     */

    public int getWasserbedarf(String username) {
        return preferences.getInt(username + "_wasserbedarf", 0);
    }

    /**
     * Checkt, ob der Nickname bereits vergeben ist.
     *
     * @param username Der Nickname des Users.
     * @return {@code true}, wenn der Nickname bereits vergeben ist, sonst {@code false}.
     */

    public boolean isUsernameTaken(String username) {
        return preferences.contains(username + "_username");
    }

    /**
     * Gibt die {@link SharedPreferences}-Instanz zurück.
     *
     * @return Die {@link SharedPreferences}-Instanz.
     */

    public SharedPreferences getSharedPreferences() {
        return preferences;
    }
}
