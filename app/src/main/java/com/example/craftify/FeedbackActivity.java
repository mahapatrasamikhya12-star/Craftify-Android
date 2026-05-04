package com.example.craftify;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class FeedbackActivity extends AppCompatActivity {

    int selectedRating = 0;
    TextView[] stars = new TextView[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getWindow().setStatusBarColor(0xFF0D2B33);

        findViewById(R.id.tvBack).setOnClickListener(v -> finish());

        // Star rating
        stars[0] = findViewById(R.id.star1);
        stars[1] = findViewById(R.id.star2);
        stars[2] = findViewById(R.id.star3);
        stars[3] = findViewById(R.id.star4);
        stars[4] = findViewById(R.id.star5);

        for (int i = 0; i < 5; i++) {
            final int rating = i + 1;
            stars[i].setOnClickListener(v -> {
                selectedRating = rating;
                updateStars(rating);
            });
        }

        // Feedback type buttons
        TextView btnSuggestion = findViewById(R.id.btnTypeSuggestion);
        TextView btnBug        = findViewById(R.id.btnTypeBug);
        TextView btnOther      = findViewById(R.id.btnTypeOther);

        btnSuggestion.setOnClickListener(v -> {
            setActiveType(btnSuggestion, btnBug, btnOther);
        });
        btnBug.setOnClickListener(v -> {
            setActiveType(btnBug, btnSuggestion, btnOther);
        });
        btnOther.setOnClickListener(v -> {
            setActiveType(btnOther, btnSuggestion, btnBug);
        });

        // Submit
        findViewById(R.id.btnSubmitFeedback).setOnClickListener(v -> {
            String name = ((EditText) findViewById(R.id.etFeedbackName))
                    .getText().toString().trim();
            String msg  = ((EditText) findViewById(R.id.etFeedbackMessage))
                    .getText().toString().trim();

            if (name.isEmpty() || msg.isEmpty()) {
                Toast.makeText(this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedRating == 0) {
                Toast.makeText(this,
                        "Please select a rating",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this,
                    "Thank you for your feedback! ⭐",
                    Toast.LENGTH_LONG).show();
            finish();
        });
    }

    private void updateStars(int rating) {
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                stars[i].setText("★");
                stars[i].setTextColor(
                        getResources().getColor(R.color.accent));
            } else {
                stars[i].setText("☆");
                stars[i].setTextColor(0xFFA8CDD4);
            }
        }
    }

    private void setActiveType(TextView active,
                               TextView inactive1,
                               TextView inactive2) {
        active.setBackgroundResource(R.drawable.type_btn_active);
        active.setTextColor(0xFFFFFFFF);
        inactive1.setBackgroundResource(R.drawable.type_btn_inactive);
        inactive1.setTextColor(0xFFA8CDD4);
        inactive2.setBackgroundResource(R.drawable.type_btn_inactive);
        inactive2.setTextColor(0xFFA8CDD4);
    }
}