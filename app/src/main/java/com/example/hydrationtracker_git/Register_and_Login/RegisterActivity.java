package com.example.hydrationtracker_git.Register_and_Login;

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
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hydrationtracker_git.R;
import com.example.hydrationtracker_git.User_Progress.UserPreferences;
import com.example.hydrationtracker_git.Water.WasserbedarfActivity;
import com.example.hydrationtracker_git.Water.Wasserbedarfsrechner;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Die Klasse {@code RegisterActivity} ermöglicht es dem User, sich zu registrieren, indem er seine Daten eingibt und ein Profilbild auswählt.
 */

public class RegisterActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    private ImageView imageViewProfile;
    private String profileImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        SeekBar seekBarAlter = findViewById(R.id.seekBarAlter);
        TextView textViewAlter = findViewById(R.id.textViewAlter);
        SeekBar seekBarGroesse = findViewById(R.id.seekBarGroesse);
        TextView textViewGroesse = findViewById(R.id.textViewGroesse);
        RadioGroup radioGroupGeschlecht = findViewById(R.id.radioGroupGeschlecht);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        Button btnRegister = findViewById(R.id.btnRegister);

        UserPreferences userPreferences = new UserPreferences(this);


        seekBarAlter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewAlter.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        seekBarGroesse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewGroesse.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                int alter = seekBarAlter.getProgress();
                int groesse = seekBarGroesse.getProgress();
                int selectedGeschlechtId = radioGroupGeschlecht.getCheckedRadioButtonId();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please enter all your data!", Toast.LENGTH_SHORT).show();
                } else if (userPreferences.isUsernameTaken(username)) {
                    Toast.makeText(RegisterActivity.this, "Username already taken!", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedGeschlechtId == -1) {
                        Toast.makeText(RegisterActivity.this, "Please select your gender!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String geschlecht = ((RadioButton) findViewById(selectedGeschlechtId)).getText().toString();

                    if (profileImagePath == null) {
                        profileImagePath = saveDefaultProfileImage();
                    }
                    int wasserbedarf = Wasserbedarfsrechner.berechneWasserbedarf(alter, groesse);
                    userPreferences.saveUser(username, password, alter, groesse, geschlecht, profileImagePath);
                    userPreferences.saveWasserbedarf(username, wasserbedarf);


                    Intent intent = new Intent(RegisterActivity.this, WasserbedarfActivity.class);
                    intent.putExtra("profileImagePath", profileImagePath);
                    intent.putExtra("wasserbedarf", wasserbedarf);
                    intent.putExtra("username", username);
                    startActivity(intent);




                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);

                    bitmap = rotateBitmap(bitmap, 270);
                    imageViewProfile.setImageBitmap(bitmap);
                    profileImagePath = saveImageToInternalStorage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Dreht das Bitmap Bild , man kann Grad eingeben.
     *
     * @param bitmap Das Bitmap-Bild, das gedreht wird.
     * @param degrees Die Anzahl der Grad, um die das Bild gedreht werden soll.
     * @return Das gedrehte Bitmap-Bild.
     */

    private Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * Speichert das Bitmap-Bild im internen Speicher.
     *
     * @param bitmap Das Bitmap-Bild, das gespeichert werden soll.
     * @return Der Pfad des gespeicherten Bitmap-Bildes.
     */

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

    /**
     * Speichert das Default-Profilbild im internen Speicher.
     *
     * @return Der Pfad des gespeicherten Standard-Profilbildes.
     */

    private String saveDefaultProfileImage() {
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person_dummy);
        return saveImageToInternalStorage(defaultBitmap);
    }
}
