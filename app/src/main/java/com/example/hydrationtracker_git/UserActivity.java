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
            tvUsername.setText("Error: No user given! ");
            return;
        }

        int alter = userPreferences.getAlter(username);
        int groesse = userPreferences.getGroesse(username);
        String geschlecht = userPreferences.getGeschlecht(username);
        String profileImagePath = userPreferences.getProfileImagePath(username);
        int wasserbedarf = userPreferences.getWasserbedarf(username);



        tvUsername.setText("Nickname: " + username);
        tvAlter.setText("Age: " + alter);
        tvGroesse.setText("Bodyheight: " + groesse + " cm");
        tvGeschlecht.setText("Gender: " + geschlecht);
        tvWasserbedarf.setText("Daily Water Demand: " + wasserbedarf + " ml");

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
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        });
    }
}
