package com.example.craftify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class CategoriesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        getWindow().setStatusBarColor(0xFF0D2B33);

        // Back button
        findViewById(R.id.tvBack).setOnClickListener(v -> finish());

        // Category clicks → open SearchActivity with filter
        findViewById(R.id.catJewellery).setOnClickListener(v ->
                openSearch("Jewellery"));
        findViewById(R.id.catHomeDecor).setOnClickListener(v ->
                openSearch("Home Decor"));
        findViewById(R.id.catGifts).setOnClickListener(v ->
                openSearch("Gift"));
        findViewById(R.id.catCalendar).setOnClickListener(v ->
                openSearch("Calendar"));
        findViewById(R.id.catGiftHamper).setOnClickListener(v ->
                openSearch("Gift Hamper"));
        findViewById(R.id.catCards).setOnClickListener(v ->
                openSearch("Cards"));
        findViewById(R.id.catAll).setOnClickListener(v ->
                openSearch(""));
    }

    private void openSearch(String query) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
    }
}