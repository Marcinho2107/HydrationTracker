package com.example.hydrationtracker_git.Register_and_Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.hydrationtracker_git.MainMenu.MainScreen;
import com.example.hydrationtracker_git.R;
import com.example.hydrationtracker_git.TemperatureMonitoring.TemperatureMonitoringService;
import com.example.hydrationtracker_git.User_Progress.UserPreferences;

/**
 * Die Klasse {@code Login} ermöglicht es dem Benutzer, sich anzumelden oder zur Registrierungsseite zu wechseln.
 */

public class Login extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startService(new Intent(this, TemperatureMonitoringService.class));

        EditText etUsername = findViewById(R.id.etUsername);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnGoToRegister = findViewById(R.id.btnGoToRegister);

        UserPreferences userPreferences = new UserPreferences(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (userPreferences.login(username, password)) {
                    Toast.makeText(Login.this, "The login was successful!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Login.this, MainScreen.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
