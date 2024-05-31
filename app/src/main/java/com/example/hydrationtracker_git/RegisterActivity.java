package com.example.hydrationtracker_git;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    private static final int SELECT_PICTURE = 1;
    private ImageView ivProfileImage;
    private Bitmap selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etRegUsername = findViewById(R.id.etRegUsername);
        EditText etRegPassword = findViewById(R.id.etRegPassword);
        SeekBar seekBarAge = findViewById(R.id.seekBarAge);
        SeekBar seekBarHeight = findViewById(R.id.seekBarHeight);
        TextView tvAge = findViewById(R.id.tvAge);
        TextView tvHeight = findViewById(R.id.tvHeight);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        Button btnRegister = findViewById(R.id.btnRegister);

        seekBarAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAge.setText("Alter: " + (progress + 8));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvHeight.setText("Körpergröße: " + (progress + 100) + "cm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Registrierung Logik hier einfügen
                String username = etRegUsername.getText().toString();
                String password = etRegPassword.getText().toString();
                int age = seekBarAge.getProgress() + 8;
                int height = seekBarHeight.getProgress() + 100;

                // Bildpfad speichern
                String profileImagePath = ""; // Hier den tatsächlichen Pfad der Bilddatei angeben

                UserPreferences userPreferences = new UserPreferences(RegisterActivity.this);
                userPreferences.saveUser(username, password, age, height, profileImagePath);

                // Zurück zur Login-Seite
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                ivProfileImage.setImageBitmap(selectedImage);
                ivProfileImage.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
