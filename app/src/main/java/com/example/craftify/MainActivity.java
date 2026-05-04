package com.example.craftify;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ─────────────────────────────────────
        // Fix system UI — app stays inside
        // status bar and navigation bar
        // ─────────────────────────────────────
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getWindow().setDecorFitsSystemWindows(true);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
        }

        ImageView logo = findViewById(R.id.ivLogo);

        // Fade + scale in animation
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        fadeIn.setDuration(1200);

        ScaleAnimation scaleIn = new ScaleAnimation(
                0.7f, 1f, 0.7f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
        );
        scaleIn.setDuration(1200);

        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(fadeIn);
        animSet.addAnimation(scaleIn);
        animSet.setFillAfter(true);

        logo.startAnimation(animSet);

        // Navigate after 3.5 seconds
        new Handler().postDelayed(() -> {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
        }, 3500);
    }
}