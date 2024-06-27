package com.example.hydrationtracker_git;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {
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

        UserPreferences userPreferences = new UserPreferences(this);

        // Daten aus den SharedPreferences abrufen
        String username = userPreferences.getUsername();
        int alter = userPreferences.getAlter();
        int groesse = userPreferences.getGroesse();
        String geschlecht = userPreferences.getGeschlecht();
        String profileImagePath = userPreferences.getProfileImagePath();
        int wasserbedarf = userPreferences.getWasserbedarf();

        // Daten in den TextViews anzeigen
        tvUsername.setText("Username: " + username);
        tvAlter.setText("Alter: " + alter);
        tvGroesse.setText("Körpergröße: " + groesse + " cm");
        tvGeschlecht.setText("Geschlecht: " + geschlecht);
        tvWasserbedarf.setText("Täglicher Wasserbedarf: " + wasserbedarf + " ml");

        // Profilbild anzeigen, falls vorhanden
        if (profileImagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(profileImagePath);
            imageViewProfile.setImageBitmap(bitmap);
        }
    }
}
