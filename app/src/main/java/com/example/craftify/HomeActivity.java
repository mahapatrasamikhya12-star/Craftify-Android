package com.example.craftify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rvProducts;
    ProductAdapter adapter;
    List<Product> productList = new ArrayList<>();
    List<Product> allProducts = new ArrayList<>();
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // ✅ INIT
        rvProducts = findViewById(R.id.rvProducts);
        etSearch   = findViewById(R.id.etSearch);

        if (rvProducts != null) {
            rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
            adapter = new ProductAdapter(this, productList);
            rvProducts.setAdapter(adapter);
        }

        // ✅ SEARCH
        setClick(R.id.layoutSearch, () ->
                startActivity(new Intent(this, SearchActivity.class)));

        setClick(R.id.etSearch, () ->
                startActivity(new Intent(this, SearchActivity.class)));

        // ✅ TOP ICON (ONLY WISHLIST EXISTS IN YOUR XML)
        setClick(R.id.tvWishlist, () ->
                startActivity(new Intent(this, WishListActivity.class)));

        // ✅ BOTTOM NAV
        setClick(R.id.navHome, () -> {});

        setClick(R.id.navCategories, () ->
                startActivity(new Intent(this, CategoriesActivity.class)));

        setClick(R.id.navCart, () ->
                startActivity(new Intent(this, CartActivity.class)));

        setClick(R.id.navProfile, () ->
                startActivity(new Intent(this, ProfileActivity.class)));

        // ✅ CATEGORY CLICKS (OPEN NEW SCREEN)
        setCategoryClick(R.id.catJewellery, "Jewellery");
        setCategoryClick(R.id.catHomeDecor, "Home Decor");
        setCategoryClick(R.id.catCalendar, "Calendar");
        setCategoryClick(R.id.catGifts, "Gift");
        setCategoryClick(R.id.catGiftHamper, "Gift Hamper");

        // ✅ LOAD DATA
        loadProductsFromApi();
    }

    // ✅ SAFE CLICK (NO CRASH)
    private void setClick(int id, Runnable action) {
        View v = findViewById(id);
        if (v != null) {
            v.setOnClickListener(view -> action.run());
        }
    }

    // ✅ CATEGORY CLICK
    private void setCategoryClick(int id, String category) {
        View v = findViewById(id);
        if (v != null) {
            v.setOnClickListener(view -> openCategory(category));
        }
    }

    // ✅ OPEN CATEGORY SCREEN (IMPORTANT CHANGE)
    private void openCategory(String category) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    // ✅ API CALL (SAFE VERSION)
    private void loadProductsFromApi() {

        ApiClient.getService().getProducts().enqueue(new Callback<ProductListResponse>() {

            @Override
            public void onResponse(Call<ProductListResponse> call,
                                   Response<ProductListResponse> response) {

                if (response.isSuccessful() && response.body() != null
                        && response.body().results != null
                        && !response.body().results.isEmpty()) {

                    allProducts.clear();

                    for (ApiProduct ap : response.body().results) {

                        Product p = new Product(
                                ap.title,
                                ap.categoryName,
                                ap.getPriceAsInt(),
                                0
                        );

                        p.imageUrl    = ap.getFirstImageUrl();
                        p.description = ap.description;

                        allProducts.add(p);
                    }

                    productList.clear();
                    productList.addAll(allProducts);

                    if (adapter != null) adapter.notifyDataSetChanged();

                } else {
                    loadHardcodedProducts();
                }
            }

            @Override
            public void onFailure(Call<ProductListResponse> call, Throwable t) {
                loadHardcodedProducts();
            }
        });
    }

    // ✅ FALLBACK DATA
    private void loadHardcodedProducts() {

        allProducts.clear();

        allProducts.add(new Product("Room Decor", "Home Decor", 349, R.drawable.product1));

        Product bouquet = new Product("Flower Bouquet", "Gift", 399, R.drawable.bouquetimage1);
        allProducts.add(bouquet);

        Product earring = new Product("Earrings", "Jewellery", 299, R.drawable.earingproduct2);
        allProducts.add(earring);

        allProducts.add(new Product("Calendar", "Calendar", 399, R.drawable.photocalender));
        allProducts.add(new Product("Gift Hamper", "Gift Hamper", 499, R.drawable.gifthamper));

        productList.clear();
        productList.addAll(allProducts);

        if (adapter != null) adapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (etSearch != null) {
            etSearch.setText("");
        }

        loadProductsFromApi();
    }
}