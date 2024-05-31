package com.example.hydrationtracker_git;



import android.content.Context;
import android.content.SharedPreferences;

public class UserPreferences {
    private static final String PREF_NAME = "UserPrefs";
    private SharedPreferences sharedPreferences;

    public UserPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveUser(String username, String password, int age, int height, String profileImagePath) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putInt("age", age);
        editor.putInt("height", height);
        editor.putString("profileImagePath", profileImagePath);
        editor.apply();
    }

    public boolean login(String username, String password) {
        String savedUsername = sharedPreferences.getString("username", null);
        String savedPassword = sharedPreferences.getString("password", null);

        return username.equals(savedUsername) && password.equals(savedPassword);
    }

    public User getUser() {
        String username = sharedPreferences.getString("username", null);
        int age = sharedPreferences.getInt("age", 0);
        int height = sharedPreferences.getInt("height", 0);
        String profileImagePath = sharedPreferences.getString("profileImagePath", null);

        return new User(username, age, height, profileImagePath);
    }
}
