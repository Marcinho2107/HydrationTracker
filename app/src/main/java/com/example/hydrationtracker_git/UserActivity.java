package com.example.hydrationtracker_git;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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
        UserPreferences userPreferences = new UserPreferences(this);

        // Angenommen der Benutzername wird in einem Intent extra übergeben
        String username = getIntent().getStringExtra("username");
        Log.d(TAG, "Username from Intent: " + username);

        if (username == null || username.isEmpty()) {
            Log.e(TAG, "No username provided!");
            tvUsername.setText("Fehler: Kein Benutzername angegeben");
            return;
        }

        // Daten aus den SharedPreferences abrufen
        int alter = userPreferences.getAlter(username);
        int groesse = userPreferences.getGroesse(username);
        String geschlecht = userPreferences.getGeschlecht(username);
        String profileImagePath = userPreferences.getProfileImagePath(username);
        int wasserbedarf = userPreferences.getWasserbedarf(username);

        // Protokollierung der abgerufenen Daten
        Log.d(TAG, "Alter: " + alter);
        Log.d(TAG, "Groesse: " + groesse);
        Log.d(TAG, "Geschlecht: " + geschlecht);
        Log.d(TAG, "ProfileImagePath: " + profileImagePath);
        Log.d(TAG, "Wasserbedarf: " + wasserbedarf);

        // Daten in den TextViews anzeigen
        tvUsername.setText("Username: " + username);
        tvAlter.setText("Alter: " + alter);
        tvGroesse.setText("Körpergröße: " + groesse + " cm");
        tvGeschlecht.setText("Geschlecht: " + geschlecht);
        tvWasserbedarf.setText("Täglicher Wasserbedarf: " + wasserbedarf + " ml");

        // Profilbild anzeigen falls vorhanden
        if (profileImagePath != null && !profileImagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(profileImagePath);
            if (bitmap != null) {
                imageViewProfile.setImageBitmap(bitmap);
            } else {
                imageViewProfile.setImageResource(R.drawable.person_dummy); // Dummy-Bild anzeigen falls Dekodierung fehlschlägt
            }
        } else {
            imageViewProfile.setImageResource(R.drawable.person_dummy); // Dummy-Bild anzeigen falls kein Bildpfad vorhanden ist
        }
    }

}
