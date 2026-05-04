package com.example.craftify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderTrackingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);

        // Get data from intent
        String orderId = getIntent().getStringExtra("order_id");
        String name    = getIntent().getStringExtra("name");
        String address = getIntent().getStringExtra("address");
        int    amount  = getIntent().getIntExtra("amount", 0);

        // Bind views
        TextView tvInvoice        = findViewById(R.id.tvInvoice);
        TextView tvOrderName      = findViewById(R.id.tvOrderName);
        TextView tvOrderAmount    = findViewById(R.id.tvOrderAmount);
        TextView tvTrackingName   = findViewById(R.id.tvTrackingName);
        TextView tvTrackingAddress= findViewById(R.id.tvTrackingAddress);
        Button   btnConfirm       = findViewById(R.id.btnConfirmDelivery);

        // Set values
        if (orderId != null) tvInvoice.setText("INVOICE : " + orderId);
        if (amount > 0)      tvOrderAmount.setText("₹" + amount);
        if (name != null)    tvTrackingName.setText(name);
        if (address != null) tvTrackingAddress.setText(address);

        // Back
        findViewById(R.id.tvBack).setOnClickListener(v -> finish());

        // Continue Shopping
        btnConfirm.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}