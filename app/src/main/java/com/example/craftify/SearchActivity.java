package com.example.craftify;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    EditText      etSearch;
    RecyclerView  rvSearchResults;
    LinearLayout  layoutResults, layoutEmpty;
    ScrollView    layoutDefault;
    TextView      tvResultCount, tvSearchTitle;
    ProductAdapter adapter;

    // ✅ Initialize lists here to avoid null
    List<Product> searchResults = new ArrayList<>();
    List<Product> allProducts   = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch        = findViewById(R.id.etSearchMain);
        rvSearchResults = findViewById(R.id.rvSearchResults);
        layoutResults   = findViewById(R.id.layoutResults);
        layoutDefault   = findViewById(R.id.layoutDefault);
        layoutEmpty     = findViewById(R.id.layoutEmpty);
        tvResultCount   = findViewById(R.id.tvResultCount);
        tvSearchTitle   = findViewById(R.id.tvSearchTitle);

        // ✅ Setup RecyclerView
        rvSearchResults.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(this, searchResults);
        rvSearchResults.setAdapter(adapter);

        // ✅ Load products first
        loadAllProducts();

        // Back button
        findViewById(R.id.tvBack).setOnClickListener(v -> finish());

        // Search on keyboard
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.getText().toString().trim());
                hideKeyboard();
                return true;
            }
            return false;
        });

        // Popular tags
        findViewById(R.id.tagDecor).setOnClickListener(v ->
                quickSearch("Home Decor"));
        findViewById(R.id.tagJewellery).setOnClickListener(v ->
                quickSearch("Jewellery"));
        findViewById(R.id.tagCards).setOnClickListener(v ->
                quickSearch("Cards"));
        findViewById(R.id.tagGifts).setOnClickListener(v ->
                quickSearch("Gift"));

        // Category clicks
        findViewById(R.id.catJewellery).setOnClickListener(v ->
                quickSearch("Jewellery"));
        findViewById(R.id.catHomeDecor).setOnClickListener(v ->
                quickSearch("Home Decor"));
        findViewById(R.id.catGifts).setOnClickListener(v ->
                quickSearch("Gift"));
        findViewById(R.id.catFrames).setOnClickListener(v ->
                quickSearch("Frame"));
        findViewById(R.id.catPortraits).setOnClickListener(v ->
                quickSearch("Portrait"));
        findViewById(R.id.catCalendars).setOnClickListener(v ->
                quickSearch("Calendar"));
        findViewById(R.id.catAll).setOnClickListener(v ->
                quickSearch(""));

        etSearch.requestFocus();
        showKeyboard();

        // ✅ If query passed from HomeActivity
        String query = getIntent().getStringExtra("query");
        if (query != null && !query.isEmpty()) {
            etSearch.setText(query);
            performSearch(query);
        }
    }

    private void quickSearch(String query) {
        etSearch.setText(query);
        performSearch(query);
        hideKeyboard();
    }

    private void performSearch(String query) {
        searchResults.clear();

        if (query.isEmpty()) {
            searchResults.addAll(allProducts);
            tvSearchTitle.setText("All Products");
        } else {
            String lower = query.toLowerCase();
            for (Product p : allProducts) {
                if (p.getName().toLowerCase().contains(lower) ||
                        p.getCategory().toLowerCase().contains(lower)) {
                    searchResults.add(p);
                }
            }
            tvSearchTitle.setText("Results for \"" + query + "\"");
        }

        tvResultCount.setText(searchResults.size() + " items");
        layoutDefault.setVisibility(View.GONE);
        layoutResults.setVisibility(View.VISIBLE);

        if (searchResults.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvSearchResults.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvSearchResults.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }

    private void loadAllProducts() {
        allProducts.clear(); // ✅ clear first

        allProducts.add(new Product(
                "Decorative room decor", "Home Decor", 349, R.drawable.product1));
        allProducts.add(new Product(
                "Decorative room decor", "Home Decor", 349, R.drawable.homedecorproduct1));
        allProducts.add(new Product(
                "Earrings", "Jewellery", 299, R.drawable.earingproduct2));
        allProducts.add(new Product(
                "Earrings", "Jewellery", 299, R.drawable.earingproduct3));
        allProducts.add(new Product(
                "Earrings", "Jewellery", 299, R.drawable.earingproduct4));
        allProducts.add(new Product(
                "Earrings", "Jewellery", 299, R.drawable.earingproduct5));
        allProducts.add(new Product(
                "Jewellery Set", "Jewellery", 350, R.drawable.jewelleryset));
        allProducts.add(new Product(
                "Jewellery Set", "Jewellery", 350, R.drawable.jewelleryset2));
        allProducts.add(new Product(
                "Anniversary Handmade Card", "Cards", 349, 0));
        allProducts.add(new Product(
                "Custom Photo Calendar", "Calendar", 399, R.drawable.photocalender));
        allProducts.add(new Product(
                "Gift Hamper", "Gift", 499, R.drawable.gifthamper));
        allProducts.add(new Product(
                "Gift Hamper", "Gift", 499, R.drawable.gifthamper2));

        // ✅ Fixed null crash — create product then set imageRes2
        Product gift = new Product("Gift", "Gift", 399, R.drawable.gift1);
        gift.imageRes2 = R.drawable.gift2;
        allProducts.add(gift);

        Product bouquet = new Product(
                "Flower Bouquet", "Gift", 399, R.drawable.bouquetimage1);
        bouquet.imageRes2 = R.drawable.bouquetimage2;
        bouquet.imageRes3 = R.drawable.bouquetimage3;
        allProducts.add(bouquet);
    }

    private void showKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);
    }
}