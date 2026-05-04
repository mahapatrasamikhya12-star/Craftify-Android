package com.example.craftify;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class OrderConfirmationActivity extends AppCompatActivity {

    String orderId;
    int total;
    String name, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Get data from intent
        total         = getIntent().getIntExtra("total", 0);
        int itemCount = getIntent().getIntExtra("item_count", 0);
        name          = getIntent().getStringExtra("name");
        address       = getIntent().getStringExtra("address");

        // Generate order ID
        orderId = "CRF" + (10000 + new Random().nextInt(90000));

        // Expected delivery date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 5);
        String deliveryDate = new SimpleDateFormat(
                "dd MMM yyyy", Locale.getDefault())
                .format(calendar.getTime());

        // Bind views
        TextView     tvEmoji            = findViewById(R.id.tvEmoji);
        TextView     tvSuccessMessage   = findViewById(R.id.tvSuccessMessage);
        TextView     tvOrderId          = findViewById(R.id.tvOrderId);
        TextView     tvItemCount        = findViewById(R.id.tvItemCount);
        TextView     tvConfirmedTotal   = findViewById(R.id.tvConfirmedTotal);
        TextView     tvDeliveryDate     = findViewById(R.id.tvDeliveryDate);
        TextView     tvConfirmedName    = findViewById(R.id.tvConfirmedName);
        TextView     tvConfirmedAddress = findViewById(R.id.tvConfirmedAddress);
        LinearLayout cardSummary        = findViewById(R.id.cardSummary);
        LinearLayout cardAddress        = findViewById(R.id.cardAddress);
        Button       btnContinue        = findViewById(R.id.btnContinueShopping);
        TextView     tvBack             = findViewById(R.id.tvBack);

        // ─────────────────────────────────────
        // Hide everything initially
        // ─────────────────────────────────────
        tvSuccessMessage.setAlpha(0f);
        tvOrderId.setAlpha(0f);
        tvItemCount.setAlpha(0f);
        btnContinue.setAlpha(0f);
        if (cardSummary != null)  cardSummary.setAlpha(0f);
        if (cardAddress != null)  cardAddress.setAlpha(0f);
        if (tvBack != null)       tvBack.setAlpha(0f);

        // Set values
        tvOrderId.setText("Order ID: " + orderId);
        if (tvConfirmedTotal != null)
            tvConfirmedTotal.setText("₹" + total);
        if (tvConfirmedAddress != null)
            tvConfirmedAddress.setText(address != null ? address : "");
        if (tvConfirmedName != null)
            tvConfirmedName.setText(name != null ? name : "");
        if (tvDeliveryDate != null)
            tvDeliveryDate.setText("Expected delivery: " + deliveryDate);
        tvItemCount.setText(
                itemCount > 0
                        ? itemCount + " item" + (itemCount != 1 ? "s" : "") + " ordered"
                        : "Your order will be shipped in 5-7 days."
        );

        // Back button
        if (tvBack != null) {
            tvBack.setOnClickListener(v -> finish());
        }

        // ─────────────────────────────────────
        // ANIMATION SEQUENCE
        // ─────────────────────────────────────

        // Step 1 (0ms) — Emoji bounces in
        tvEmoji.setScaleX(0f);
        tvEmoji.setScaleY(0f);
        tvEmoji.setAlpha(0f);
        tvEmoji.animate()
                .scaleX(1f)
                .scaleY(1f)
                .alpha(1f)
                .setDuration(800)
                .setInterpolator(new OvershootInterpolator(3f))
                .start();

        // Step 2 (800ms) — Success text fades in
        new Handler().postDelayed(() -> {
            fadeIn(tvSuccessMessage, 0);
            fadeIn(tvOrderId, 200);
            fadeIn(tvItemCount, 400);
            if (tvBack != null) fadeIn(tvBack, 600);
        }, 800);

        // Step 3 (1600ms) — Cards slide up
        new Handler().postDelayed(() -> {
            if (cardSummary != null) slideUp(cardSummary, 0);
            if (cardAddress != null) slideUp(cardAddress, 200);
        }, 1600);

        // Step 4 (2200ms) — Button bounces in
        new Handler().postDelayed(() -> {
            btnContinue.setAlpha(0f);
            btnContinue.setScaleX(0.5f);
            btnContinue.setScaleY(0.5f);
            btnContinue.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(500)
                    .setInterpolator(new OvershootInterpolator(1.5f))
                    .start();
        }, 2200);

        // ─────────────────────────────────────
        // Continue Shopping → Home
        // ─────────────────────────────────────
        btnContinue.setOnClickListener(v -> {
            // Fade out animation before navigating
            btnContinue.animate()
                    .alpha(0f)
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .setDuration(200)
                    .withEndAction(() -> {
                        Intent intent = new Intent(this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        overridePendingTransition(
                                android.R.anim.fade_in,
                                android.R.anim.fade_out
                        );
                        finish();
                    })
                    .start();
        });
    }

    // ─────────────────────────────────────
    // Fade in animation
    // ─────────────────────────────────────
    private void fadeIn(View view, long delay) {
        if (view == null) return;
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        anim.setDuration(500);
        anim.setStartDelay(delay);
        anim.start();
    }

    // ─────────────────────────────────────
    // Slide up animation
    // ─────────────────────────────────────
    private void slideUp(View view, long delay) {
        if (view == null) return;
        view.setAlpha(0f);
        view.setTranslationY(100f);

        ObjectAnimator alpha     = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        ObjectAnimator translate = ObjectAnimator.ofFloat(view, "translationY", 100f, 0f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(alpha, translate);
        set.setDuration(600);
        set.setStartDelay(delay);
        set.setInterpolator(new OvershootInterpolator(0.8f));
        set.start();
    }
}
