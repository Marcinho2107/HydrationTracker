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
import java.util.List;

public class ProgressActivity extends AppCompatActivity {
    private static final String TAG = "ProgressActivity";
    private TextView weeklyTrackingTextView;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        ImageView imageViewProfile = findViewById(R.id.imageViewProfile);
        TextView tvUsername = findViewById(R.id.tvUsername);
        Button buttonBackToMainMenu = findViewById(R.id.buttonBackToMainMenu);
        weeklyTrackingTextView = findViewById(R.id.weeklyTrackingTextView);
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

        String profileImagePath = userPreferences.getProfileImagePath(username);
        tvUsername.setText("Nickname: " + username);

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

        buttonBackToMainMenu.setOnClickListener(v -> {
            Intent intent = new Intent(ProgressActivity.this, MainScreen.class);
            intent.putExtra("username", username);
            intent.putExtra("suggestedAmountNumber2", suggestedAmountNumber2);
            intent.putExtra("certainAmountPercentage", certainAmountPercentage);
            intent.putExtra("selectedAmount", selectedAmount);
            intent.putExtra("totalSelectedAmount", totalSelectedAmount);
            startActivity(intent);
            finish();
        });
        displayWeeklyTracking(username);
    }
    private void displayWeeklyTracking(String username) {
        UserPreferences userPreferences = new UserPreferences(this);
        List<Integer> weeklyIntake = userPreferences.getWeeklyIntake(username);

        StringBuilder trackingText = new StringBuilder("Weekly Water Intake:\n");
        String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        for (int i = 0; i < weeklyIntake.size(); i++) {
            trackingText.append(daysOfWeek[i]).append(": ").append(weeklyIntake.get(i)).append(" ml\n");
        }
        weeklyTrackingTextView.setText(trackingText.toString());
    }
}