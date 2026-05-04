package com.example.craftify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button; // 1. Ensure this import exists
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 2. Initialize the button
        Button btnGetStarted = findViewById(R.id.btnGetStarted);

        // 3. Set the click listener to move to Login
        btnGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Closes splash so user can't go back to it
        });
    }
}