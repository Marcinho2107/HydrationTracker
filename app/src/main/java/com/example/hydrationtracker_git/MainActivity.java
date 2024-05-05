package com.example.hydrationtracker_git;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    Button logInButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logInButton=findViewById(R.id.Button);
        logInButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,MainScreen.class);
            startActivity(intent);
        });
    }
}