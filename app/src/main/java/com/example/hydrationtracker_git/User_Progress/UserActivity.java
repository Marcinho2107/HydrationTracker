package com.example.hydrationtracker_git.User_Progress;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hydrationtracker_git.MainMenu.MainScreen;
import com.example.hydrationtracker_git.R;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";

    @SuppressLint("SetTextI18n")
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
        String suggestedAmountNumber2 = getIntent().getStringExtra("suggestedAmountNumber2");
        String certainAmountPercentage = getIntent().getStringExtra("certainAmountPercentage");
        int selectedAmount = getIntent().getIntExtra("selectedAmount", 0);
        int totalSelectedAmount = getIntent().getIntExtra("totalSelectedAmount", 0);

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
            intent.putExtra("suggestedAmountNumber2", suggestedAmountNumber2);
            intent.putExtra("certainAmountPercentage", certainAmountPercentage);
            intent.putExtra("selectedAmount", selectedAmount);
            intent.putExtra("totalSelectedAmount", totalSelectedAmount);
            startActivity(intent);
            finish();
        });
    }
}
