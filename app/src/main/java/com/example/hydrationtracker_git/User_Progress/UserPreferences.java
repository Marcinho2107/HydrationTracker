package com.example.hydrationtracker_git.User_Progress;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;


public class UserPreferences {
    private static final String PREFS_NAME = "UserPrefs";
    private static final String KEY_WASSERBEDARF = "wasserbedarf";
    private final SharedPreferences preferences;

    public UserPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

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

    public void saveWasserbedarf(String username, int wasserbedarf) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(username + "_wasserbedarf", wasserbedarf);
        editor.apply();
    }

    public boolean login(String username, String password) {
        String storedPassword = preferences.getString(username + "_password", null);
        return password.equals(storedPassword);
    }

    public String getUsername(String username) {
        return preferences.getString(username + "_username", null);
    }

    public String getPassword(String username) {
        return preferences.getString(username + "_password", null);
    }

    public int getAlter(String username) {
        return preferences.getInt(username + "_alter", 0);
    }

    public int getGroesse(String username) {
        return preferences.getInt(username + "_groesse", 0);
    }

    public String getGeschlecht(String username) {
        return preferences.getString(username + "_geschlecht", null);
    }

    public String getProfileImagePath(String username) {
        return preferences.getString(username + "_profile_image_path", null);
    }

    public int getWasserbedarf(String username) {
        return preferences.getInt(username + "_wasserbedarf", 0);
    }

    public boolean isUsernameTaken(String username) {
        return preferences.contains(username + "_username");
    }
    public SharedPreferences getSharedPreferences() {
        return preferences;
    }
    public void saveDailyIntake(String username, int dayOfWeek, int intake) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(username + "_day" + dayOfWeek, intake);
        editor.apply();
    }

    public int getDailyIntake(String username, int dayOfWeek) {
        return preferences.getInt(username + "_day" + dayOfWeek, 0);
    }

    public List<Integer> getWeeklyIntake(String username) {
        List<Integer> weeklyIntake = new ArrayList<>();
        for (int i = 1; i <= 7; i++) {
            weeklyIntake.add(getDailyIntake(username, i));
        }
        return weeklyIntake;
    }
}
