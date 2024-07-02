package com.example.hydrationtracker_git.Water;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hydrationtracker_git.R;
import com.example.hydrationtracker_git.Register_and_Login.Login;
import com.example.hydrationtracker_git.User.UserPreferences;

public class WasserbedarfActivity extends AppCompatActivity {
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wasserbedarf);

        ImageView imageViewProfile = findViewById(R.id.imageViewProfile);
        TextView tvWasserbedarf = findViewById(R.id.tvWasserbedarf);
        Button btnOk = findViewById(R.id.btnOk);

        Intent intent = getIntent();
        String profileImagePath = intent.getStringExtra("profileImagePath");
        int wasserbedarf = intent.getIntExtra("wasserbedarf", 0);
        UserPreferences userPreferences = new UserPreferences(this);
        String username = getIntent().getStringExtra("username");

        tvWasserbedarf.setText("Hello "+username+", your daily water demand is:  " + wasserbedarf + " ml");

        if (profileImagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(profileImagePath);
            imageViewProfile.setImageBitmap(bitmap);
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent loginIntent = new Intent(WasserbedarfActivity.this, Login.class);
                loginIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
        });
    }
}
