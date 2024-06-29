package com.example.hydrationtracker_git;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditUserActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    private EditText etUsername, etPassword, etWasserbedarf;
    private SeekBar seekBarAlter, seekBarGroesse;
    private TextView textViewAlter, textViewGroesse;
    private RadioGroup radioGroupGeschlecht;
    private Button btnSaveChanges, btnSelectImage;
    private ImageView imageViewProfile;
    private UserPreferences userPreferences;
    private String username, profileImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etWasserbedarf = findViewById(R.id.etWasserbedarf);
        seekBarAlter = findViewById(R.id.seekBarAlter);
        textViewAlter = findViewById(R.id.textViewAlter);
        seekBarGroesse = findViewById(R.id.seekBarGroesse);
        textViewGroesse = findViewById(R.id.textViewGroesse);
        radioGroupGeschlecht = findViewById(R.id.radioGroupGeschlecht);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        userPreferences = new UserPreferences(this);

        // Set SeekBar ranges
        seekBarAlter.setMin(7);
        seekBarAlter.setMax(100);
        seekBarGroesse.setMin(90);
        seekBarGroesse.setMax(220);

        // Load user data
        username = getIntent().getStringExtra("username");
        loadUserData(username);

        // SeekBar listeners
        seekBarAlter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewAlter.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBarGroesse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewGroesse.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });
    }

    private void loadUserData(String username) {
        if (username != null) {
            etUsername.setText(userPreferences.getUsername(username));
            etPassword.setText(userPreferences.getPassword(username));
            seekBarAlter.setProgress(userPreferences.getAlter(username));
            seekBarGroesse.setProgress(userPreferences.getGroesse(username));
            etWasserbedarf.setText(String.valueOf(userPreferences.getWasserbedarf(username)));
            profileImagePath = userPreferences.getProfileImagePath(username);

            String geschlecht = userPreferences.getGeschlecht(username);
            if (geschlecht.equals("Männlich")) {
                ((RadioButton) findViewById(R.id.rbMale)).setChecked(true);
            } else if (geschlecht.equals("Weiblich")) {
                ((RadioButton) findViewById(R.id.rbFemale)).setChecked(true);
            } else if (geschlecht.equals("Divers")) {
                ((RadioButton) findViewById(R.id.rbDivers)).setChecked(true);
            }

            if (profileImagePath != null && !profileImagePath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(profileImagePath);
                imageViewProfile.setImageBitmap(bitmap);
            }
        }
    }

    private void saveChanges() {
        String newUsername = etUsername.getText().toString();
        String newPassword = etPassword.getText().toString();
        int newAlter = seekBarAlter.getProgress();
        int newGroesse = seekBarGroesse.getProgress();
        int newWasserbedarf = Integer.parseInt(etWasserbedarf.getText().toString());
        int selectedGeschlechtId = radioGroupGeschlecht.getCheckedRadioButtonId();
        String newGeschlecht = ((RadioButton) findViewById(selectedGeschlechtId)).getText().toString();

        userPreferences.saveUser(newUsername, newPassword, newAlter, newGroesse, newGeschlecht, profileImagePath);
        userPreferences.saveWasserbedarf(newUsername, newWasserbedarf);

        Intent intent = new Intent(EditUserActivity.this, UserActivity.class);
        intent.putExtra("username", newUsername);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    bitmap = rotateBitmap(bitmap, 90);
                    imageViewProfile.setImageBitmap(bitmap);
                    profileImagePath = saveImageToInternalStorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private String saveImageToInternalStorage(Bitmap bitmap) {
        File directory = getDir("profile_images", MODE_PRIVATE);
        File imageFile = new File(directory, "profile_image.png");
        try (FileOutputStream fos = new FileOutputStream(imageFile)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
