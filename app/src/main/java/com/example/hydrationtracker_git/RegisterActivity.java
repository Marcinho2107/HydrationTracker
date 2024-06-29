package com.example.hydrationtracker_git;

import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {
    private static final int SELECT_PICTURE = 1;
    private ImageView imageViewProfile;
    private String profileImagePath;
    private DrawerLayout dl;

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

        // Listener für SeekBar Alter
        seekBarAlter.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewAlter.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // nicht verwendet
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // nicht verwendet
            }
        });

        // Listener für SeekBar Körpergröße
        seekBarGroesse.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textViewGroesse.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // nicht verwendet
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // nicht verwendet
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
                    Toast.makeText(RegisterActivity.this, "Bitte trage alle deine Daten ein.", Toast.LENGTH_SHORT).show();
                } else if (userPreferences.isUsernameTaken(username)) {
                    Toast.makeText(RegisterActivity.this, "Benutzername bereits vergeben.", Toast.LENGTH_SHORT).show();
                } else {
                    if (selectedGeschlechtId == -1) {
                        Toast.makeText(RegisterActivity.this, "Bitte wähle dein Geschlecht aus.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String geschlecht = ((RadioButton) findViewById(selectedGeschlechtId)).getText().toString();

                    if (profileImagePath == null) {
                        profileImagePath = saveDefaultProfileImage();
                    }
                    int wasserbedarf = Wasserbedarfsrechner.berechneWasserbedarf(alter, groesse);
                    userPreferences.saveUser(username, password, alter, groesse, geschlecht, profileImagePath);
                    userPreferences.saveWasserbedarf(username, wasserbedarf);

                    // Daten an WasserbedarfActivity übergeben
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
                    // Bild um 90 Grad nach rechts drehen
                    bitmap = rotateBitmap(bitmap, 270);
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

    private String saveDefaultProfileImage() {
        Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.person_dummy);
        return saveImageToInternalStorage(defaultBitmap);
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START); // Schließe den Drawer, wenn er offen ist
        } else {
            // Zeige einen Bestätigungsdialog
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("App verlassen")
                    .setMessage("Möchten Sie die App wirklich verlassen?")
                    .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // App vollständig beenden
                            finishAffinity();
                        }
                    })
                    .setNegativeButton("Nein", null)
                    .create();

            dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorBlack));
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorBlack));
                }
            });

            dialog.show();
        }
    }
    }


