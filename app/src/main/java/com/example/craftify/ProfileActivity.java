package com.example.craftify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    TextView tvUsername, tvEmail, tvWishlistCount;

    LinearLayout btnEditProfile, btnOrderHistory, btnWishlist,
            btnVisualization, btnTrackProgress,
            btnFeedback, btnContact, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Bind views
        tvUsername = findViewById(R.id.tvUsername);
        tvEmail = findViewById(R.id.tvEmail);
        tvWishlistCount = findViewById(R.id.tvWishlistCount);

        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnOrderHistory = findViewById(R.id.btnOrderHistory);
        btnWishlist = findViewById(R.id.btnWishlist);

        btnVisualization = findViewById(R.id.btnVisualization);
        btnTrackProgress = findViewById(R.id.btnTrackProgress);
        btnFeedback = findViewById(R.id.btnFeedback);
        btnContact = findViewById(R.id.btnContact);

        btnLogout = findViewById(R.id.btnLogout);

        // Back button
        findViewById(R.id.tvBack).setOnClickListener(v -> finish());

        // Load user data
        SharedPreferences prefs = getSharedPreferences("auth", MODE_PRIVATE);
        tvUsername.setText(prefs.getString("username", "User"));
        tvEmail.setText(prefs.getString("email", "email@gmail.com"));

        tvWishlistCount.setText("0 items");

        // Clicks
        btnEditProfile.setOnClickListener(v ->
                Toast.makeText(this, "Edit Profile coming soon", Toast.LENGTH_SHORT).show()
        );

        btnOrderHistory.setOnClickListener(v ->
                startActivity(new Intent(this, OrderConfirmationActivity.class))
        );

        btnWishlist.setOnClickListener(v ->
                startActivity(new Intent(this, WishListActivity.class))
        );

        btnVisualization.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizationActivity.class))
        );

        btnTrackProgress.setOnClickListener(v ->
                startActivity(new Intent(this, TrackProgressActivity.class))
        );

        btnFeedback.setOnClickListener(v ->
                startActivity(new Intent(this, FeedbackActivity.class))
        );

        btnContact.setOnClickListener(v ->
                startActivity(new Intent(this, ContactActivity.class))
        );

        btnLogout.setOnClickListener(v -> {
            getSharedPreferences("auth", MODE_PRIVATE)
                    .edit().clear().apply();

            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}