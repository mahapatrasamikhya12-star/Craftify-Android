package com.example.craftify;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_splash);

        // Get data from intent
        int    total     = getIntent().getIntExtra("total", 0);
        int    itemCount = getIntent().getIntExtra("item_count", 0);
        String name      = getIntent().getStringExtra("name");
        String address   = getIntent().getStringExtra("address");

        // Bind views
        TextView tvCheckBadge     = findViewById(R.id.tvCheckBadge);
        TextView tvOrderConfirmed = findViewById(R.id.tvOrderConfirmed);
        TextView tvOrderSubtitle  = findViewById(R.id.tvOrderSubtitle);
        TextView confetti1        = findViewById(R.id.confetti1);
        TextView confetti2        = findViewById(R.id.confetti2);
        TextView confetti3        = findViewById(R.id.confetti3);
        TextView confetti4        = findViewById(R.id.confetti4);
        TextView confetti5        = findViewById(R.id.confetti5);
        TextView confetti6        = findViewById(R.id.confetti6);

        // Force initial state
        tvCheckBadge.setAlpha(0f);
        tvCheckBadge.setScaleX(0f);
        tvCheckBadge.setScaleY(0f);

        // ─────────────────────────────────────
        // Step 1 (200ms) — Badge pops in
        // ─────────────────────────────────────
        new Handler().postDelayed(() -> {
            tvCheckBadge.setAlpha(1f);
            tvCheckBadge.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(700)
                    .setInterpolator(new OvershootInterpolator(3f))
                    .start();
        }, 200);

        // ─────────────────────────────────────
        // Step 2 (800ms) — Confetti flies out
        // ─────────────────────────────────────
        new Handler().postDelayed(() -> {
            showConfetti(confetti1, -30f);
            showConfetti(confetti2,  30f);
            showConfetti(confetti3,  15f);
            showConfetti(confetti4, -15f);
            showConfetti(confetti5, -45f);
            showConfetti(confetti6,  45f);
        }, 800);

        // ─────────────────────────────────────
        // Step 3 (1000ms) — Text fades in
        // ─────────────────────────────────────
        new Handler().postDelayed(() -> {
            tvOrderConfirmed.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(400)
                    .start();
        }, 1000);

        new Handler().postDelayed(() -> {
            tvOrderSubtitle.animate()
                    .alpha(1f)
                    .setDuration(400)
                    .start();
        }, 1200);

        // ─────────────────────────────────────
        // Step 4 (2800ms) — Go to Thanks screen
        // ─────────────────────────────────────
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, OrderConfirmationActivity.class);
            intent.putExtra("total",      total);
            intent.putExtra("item_count", itemCount);
            intent.putExtra("name",       name);
            intent.putExtra("address",    address);
            startActivity(intent);
            overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
            );
            finish();
        }, 2800);
    }

    private void showConfetti(TextView view, float rotation) {
        view.setAlpha(0f);
        view.setRotation(rotation);
        view.animate()
                .alpha(1f)
                .rotation(rotation + 20f)
                .translationYBy(-30f)
                .setDuration(500)
                .start();
    }
}
