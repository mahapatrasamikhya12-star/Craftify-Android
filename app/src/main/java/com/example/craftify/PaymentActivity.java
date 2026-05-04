package com.example.craftify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PaymentActivity extends AppCompatActivity {

    TextView tvPaymentTotal, tvPaymentName, tvPaymentAddress;
    RadioGroup radioPayment;
    RadioButton radioUPI, radioCard, radioCOD;
    Button btnPay;

    int totalAmount;
    int itemCount;
    String name, address;
    PaymentDAO paymentDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        paymentDAO = new PaymentDAO(this);

        tvPaymentTotal   = findViewById(R.id.tvPaymentTotal);
        tvPaymentName    = findViewById(R.id.tvPaymentName);
        tvPaymentAddress = findViewById(R.id.tvPaymentAddress);
        radioPayment     = findViewById(R.id.radioPayment);
        radioUPI         = findViewById(R.id.radioUPI);
        radioCard        = findViewById(R.id.radioCard);
        radioCOD         = findViewById(R.id.radioCOD);
        btnPay           = findViewById(R.id.btnPay);

        // Get ALL data from intent
        totalAmount = getIntent().getIntExtra("total", 0);
        itemCount   = getIntent().getIntExtra("item_count", 1);
        name        = getIntent().getStringExtra("name");
        address     = getIntent().getStringExtra("address");

        tvPaymentTotal.setText("₹" + totalAmount);
        btnPay.setText("Pay ₹" + totalAmount);

        if (name != null)    tvPaymentName.setText(name);
        if (address != null) tvPaymentAddress.setText(address);

        btnPay.setOnClickListener(v -> processPayment());
    }

    private void processPayment() {
        int selectedId = radioPayment.getCheckedRadioButtonId();

        if (selectedId == -1) {
            Toast.makeText(this,
                    "Please select a payment method",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        String method = "";
        if (selectedId == R.id.radioUPI)  method = "UPI";
        if (selectedId == R.id.radioCard) method = "Card";
        if (selectedId == R.id.radioCOD)  method = "Cash on Delivery";

        // Save payment to database
        String orderId   = "ORDER_" + System.currentTimeMillis();
        String paymentId = "PAY_"   + System.currentTimeMillis();

        paymentDAO.savePayment(orderId, paymentId, totalAmount, "SUCCESS");

        // Clear cart
        new DatabaseHelper(this).clearCart();

        // ─────────────────────────────────────
        // Pass ALL data to OrderConfirmation ← FIXED
        // ─────────────────────────────────────
        Intent intent = new Intent(this, OrderSplashActivity.class);
        intent.putExtra("payment_id",     paymentId);
        intent.putExtra("total",          totalAmount);  // ← "total" not "amount"
        intent.putExtra("payment_method", method);
        intent.putExtra("name",           name);         // ← pass name
        intent.putExtra("address",        address);      // ← pass address
        intent.putExtra("item_count",     itemCount);    // ← pass item count
        startActivity(intent);
        finish();
    }
}
