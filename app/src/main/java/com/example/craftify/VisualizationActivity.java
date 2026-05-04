package com.example.craftify;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class VisualizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualization);

        getWindow().setStatusBarColor(0xFF0D2B33);

        findViewById(R.id.tvBack).setOnClickListener(v -> finish());

        loadStats();
    }

    private void loadStats() {

        CartDAO cartDAO = new CartDAO(this);

        int cartCount = cartDAO.getAllItems().size();

        // ✅ FIXED ID
        ((TextView) findViewById(R.id.tvCart))
                .setText(String.valueOf(cartCount));

        double total = 0;
        for (CartItem item : cartDAO.getAllItems()) {
            total += item.getTotal();
        }

        // ✅ FIXED ID
        ((TextView) findViewById(R.id.tvSpending))
                .setText("₹" + (int) total);

        WishlistDatabaseHelper wishlistDb =
                new WishlistDatabaseHelper(this);

        int wishlistCount = wishlistDb.getAllWishlistItems().size();

        // If you don't have wishlist TextView, you can skip or create it
        // Example:
        // ((TextView) findViewById(R.id.tvWishlist)).setText(String.valueOf(wishlistCount));

    }
}