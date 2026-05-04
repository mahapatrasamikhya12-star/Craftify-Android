package com.example.craftify;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class TrackProgressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_progress);
        getWindow().setStatusBarColor(0xFF0D2B33);

        findViewById(R.id.tvBack).setOnClickListener(v -> finish());

        findViewById(R.id.btnTrack).setOnClickListener(v -> {
            String orderId = ((EditText) findViewById(R.id.etOrderId))
                    .getText().toString().trim();

            if (orderId.isEmpty()) {
                Toast.makeText(this,
                        "Please enter an Order ID",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (orderId.toUpperCase().startsWith("CRF")) {
                // Show order card
                findViewById(R.id.layoutOrderCard)
                        .setVisibility(View.VISIBLE);
                findViewById(R.id.layoutNoOrders)
                        .setVisibility(View.GONE);
                ((android.widget.TextView)
                        findViewById(R.id.tvOrderIdDisplay))
                        .setText("Order #" + orderId.toUpperCase());
            } else {
                Toast.makeText(this,
                        "Order not found. Try CRF + number",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}