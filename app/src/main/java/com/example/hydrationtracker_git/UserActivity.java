package com.example.hydrationtracker_git;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        ImageView imageViewProfile = findViewById(R.id.imageViewProfile);
        TextView tvUsername = findViewById(R.id.tvUsername);
        TextView tvAlter = findViewById(R.id.tvAlter);
        TextView tvGroesse = findViewById(R.id.tvGroesse);
        TextView tvGeschlecht = findViewById(R.id.tvGeschlecht);
        TextView tvWasserbedarf = findViewById(R.id.tvWasserbedarf);
        Button buttonEdit = findViewById(R.id.button2);
        Button buttonBackToMainMenu = findViewById(R.id.buttonBackToMainMenu);


        UserPreferences userPreferences = new UserPreferences(this);
        String username = getIntent().getStringExtra("username");
        Log.d(TAG, "Username from Intent: " + username);

        if (username == null || username.isEmpty()) {
            Log.e(TAG, "No username provided!");
            tvUsername.setText("Fehler: Kein Benutzername angegeben");
            return;
        }

        int alter = userPreferences.getAlter(username);
        int groesse = userPreferences.getGroesse(username);
        String geschlecht = userPreferences.getGeschlecht(username);
        String profileImagePath = userPreferences.getProfileImagePath(username);
        int wasserbedarf = userPreferences.getWasserbedarf(username);

        Log.d(TAG, "Alter: " + alter);
        Log.d(TAG, "Groesse: " + groesse);
        Log.d(TAG, "Geschlecht: " + geschlecht);
        Log.d(TAG, "ProfileImagePath: " + profileImagePath);
        Log.d(TAG, "Wasserbedarf: " + wasserbedarf);

        tvUsername.setText("Username: " + username);
        tvAlter.setText("Alter: " + alter);
        tvGroesse.setText("Körpergröße: " + groesse + " cm");
        tvGeschlecht.setText("Geschlecht: " + geschlecht);
        tvWasserbedarf.setText("Täglicher Wasserbedarf: " + wasserbedarf + " ml");

        if (profileImagePath != null && !profileImagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(profileImagePath);
            if (bitmap != null) {
                imageViewProfile.setImageBitmap(bitmap);
            } else {
                imageViewProfile.setImageResource(R.drawable.person_dummy);
            }
        } else {
            imageViewProfile.setImageResource(R.drawable.person_dummy);
        }

        buttonEdit.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, EditUserActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });

        buttonBackToMainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, MainScreen.class);
            startActivity(intent);
            finish();
        });
    }
}
