package com.example.craftify;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getWindow().setStatusBarColor(0xFF0D2B33);

        findViewById(R.id.tvBack).setOnClickListener(v -> finish());

        // Email button
        findViewById(R.id.btnEmail).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(
                    "mailto:support@craftify.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "Support Request");
            startActivity(Intent.createChooser(intent, "Send Email"));
        });

        // Phone button
        findViewById(R.id.btnPhone).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:+919876543210"));
            startActivity(intent);
        });

        // WhatsApp button
        findViewById(R.id.btnWhatsApp).setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(
                        "https://wa.me/919876543210"));
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this,
                        "WhatsApp not installed",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Send message
        findViewById(R.id.btnSendMessage).setOnClickListener(v -> {
            String name  = ((EditText) findViewById(R.id.etContactName))
                    .getText().toString().trim();
            String email = ((EditText) findViewById(R.id.etContactEmail))
                    .getText().toString().trim();
            String msg   = ((EditText) findViewById(R.id.etContactMessage))
                    .getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || msg.isEmpty()) {
                Toast.makeText(this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // Send via email
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse(
                    "mailto:support@craftify.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT,
                    "Message from " + name);
            intent.putExtra(Intent.EXTRA_TEXT, msg);
            startActivity(Intent.createChooser(
                    intent, "Send Message"));
        });
    }
}