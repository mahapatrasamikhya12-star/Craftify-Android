package com.example.craftify;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddressActivity extends AppCompatActivity {

    private EditText etName, etPhone, etAddress, etCity, etPincode;
    private int cartTotal, itemCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        cartTotal = getIntent().getIntExtra("total", 0);
        itemCount = getIntent().getIntExtra("item_count", 0);

        TextView tvBack = findViewById(R.id.tvBack);
        if (tvBack != null) tvBack.setOnClickListener(v -> finish());

        etName    = findViewById(R.id.etName);
        etPhone   = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etCity    = findViewById(R.id.etCity);
        etPincode = findViewById(R.id.etPincode);

        TextView tvTotal = findViewById(R.id.tvAddressTotal);
        if (tvTotal != null) tvTotal.setText("₹" + cartTotal);

        // Load saved data
        SharedPreferences prefs = getSharedPreferences("user_data", MODE_PRIVATE);
        etName.setText(prefs.getString("name", ""));
        etPhone.setText(prefs.getString("phone", ""));
        etAddress.setText(prefs.getString("address", ""));
        etCity.setText(prefs.getString("city", ""));
        etPincode.setText(prefs.getString("pincode", ""));

        Button btnContinue = findViewById(R.id.btnAddressContinue);
        btnContinue.setOnClickListener(v -> {

            String name    = etName.getText().toString().trim();
            String phone   = etPhone.getText().toString().trim();
            String address = etAddress.getText().toString().trim();
            String city    = etCity.getText().toString().trim();
            String pincode = etPincode.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()
                    || city.isEmpty() || pincode.isEmpty()) {
                Toast.makeText(this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.length() != 10) {
                Toast.makeText(this,
                        "Enter valid 10-digit phone number",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (pincode.length() != 6) {
                Toast.makeText(this,
                        "Enter valid 6-digit pincode",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Save data permanently
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name",    name);
            editor.putString("phone",   phone);
            editor.putString("address", address);
            editor.putString("city",    city);
            editor.putString("pincode", pincode);
            editor.apply();

            // ← Removed "Address saved" toast

            // Go to PaymentActivity
            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra("total",      cartTotal);
            intent.putExtra("item_count", itemCount);
            intent.putExtra("name",       name);
            intent.putExtra("phone",      phone);
            intent.putExtra("address",    address + ", " + city + " - " + pincode);
            startActivity(intent);
        });
    }
}