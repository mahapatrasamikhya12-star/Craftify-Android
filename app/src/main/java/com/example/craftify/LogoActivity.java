package com.example.craftify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        // 1. Initialize the TextView
        final TextView craftifyText = findViewById(R.id.craftify_text);

        // 2. Load the "iPad-style" reveal animation from the anim folder
        Animation ipadReveal = AnimationUtils.loadAnimation(this, R.anim.fade_slide_up);

        if (craftifyText != null) {
            // Make text visible and start the animation
            craftifyText.setVisibility(View.VISIBLE);
            craftifyText.startAnimation(ipadReveal);
        }

        // 3. Wait for 3.5 seconds then jump to LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LogoActivity.this, LoginActivity.class);
                startActivity(intent);

                // Smooth fade transition between the two screens
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                // Close LogoActivity so the user can't go back to it
                finish();
            }
        }, 3500);
    }
}