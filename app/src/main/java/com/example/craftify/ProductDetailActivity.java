package com.example.craftify;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    TextView tvBack, tvDetailName, tvDetailCategory, tvDetailPrice,
            tvDetailDescription, tvMRP, tvDiscount, tvImageCounter,
            tvWishlist, btnShare;

    Button btnAddToCart, btnBuyNow;
    ViewPager2 viewPagerImages;
    LinearLayout layoutDots;
    WishListDAO wishlistDAO;

    String productName, category, sellerName, imageUrl, description;
    int price, imageRes, imageRes2, imageRes3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Initialize views
        tvBack              = findViewById(R.id.tvBack);
        tvDetailName        = findViewById(R.id.tvDetailName);
        tvDetailCategory    = findViewById(R.id.tvDetailCategory);
        tvDetailPrice       = findViewById(R.id.tvDetailPrice);
        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvMRP               = findViewById(R.id.tvMRP);
        tvDiscount          = findViewById(R.id.tvDiscount);
        tvImageCounter      = findViewById(R.id.tvImageCounter);
        tvWishlist          = findViewById(R.id.tvWishlist);
        btnShare            = findViewById(R.id.btnShare);

        btnAddToCart        = findViewById(R.id.btnAddToCart);
        btnBuyNow           = findViewById(R.id.btnBuyNow);
        viewPagerImages     = findViewById(R.id.viewPagerImages);
        layoutDots          = findViewById(R.id.layoutDots);

        wishlistDAO = new WishListDAO(this);

        // Get data from intent
        productName = getIntent().getStringExtra("name");
        category    = getIntent().getStringExtra("category");
        price       = getIntent().getIntExtra("price", 0);
        imageRes    = getIntent().getIntExtra("imageRes", 0);
        imageRes2   = getIntent().getIntExtra("imageRes2", 0);
        imageRes3   = getIntent().getIntExtra("imageRes3", 0);
        imageUrl    = getIntent().getStringExtra("imageUrl");
        description = getIntent().getStringExtra("description");
        sellerName  = getIntent().getStringExtra("seller_name");

        int mrp      = (int)(price / 0.70);
        int discount = Math.round(((mrp - price) * 100f) / mrp);

        tvDetailName.setText(productName != null ? productName : "");
        tvDetailCategory.setText(category != null ? category : "");
        tvDetailPrice.setText("₹" + price);
        tvDetailDescription.setText(description != null ? description : "");
        tvMRP.setText("₹" + mrp);
        tvMRP.setPaintFlags(tvMRP.getPaintFlags() |
                android.graphics.Paint.STRIKE_THRU_TEXT_FLAG);
        tvDiscount.setText("-" + discount + "%");

        // Image list
        List<Object> images = new ArrayList<>();
        if (imageUrl  != null && !imageUrl.isEmpty()) images.add(imageUrl);
        if (imageRes  != 0) images.add(imageRes);
        if (imageRes2 != 0) images.add(imageRes2);
        if (imageRes3 != 0) images.add(imageRes3);
        if (images.isEmpty()) images.add(0);

        // Slider
        ImageSliderAdapter sliderAdapter = new ImageSliderAdapter(this, images);
        viewPagerImages.setAdapter(sliderAdapter);

        final int total = images.size();
        tvImageCounter.setText("1/" + total);
        tvImageCounter.setVisibility(total > 1 ? View.VISIBLE : View.GONE);

        viewPagerImages.registerOnPageChangeCallback(
                new ViewPager2.OnPageChangeCallback() {
                    @Override
                    public void onPageSelected(int position) {
                        tvImageCounter.setText((position + 1) + "/" + total);
                        updateDots(position, total);
                    }
                });

        setupDots(total);
        updateWishlistIcon();

        // Wishlist
        tvWishlist.setOnClickListener(v -> {
            if (wishlistDAO.isInWishlist(productName)) {
                wishlistDAO.removeFromWishlist(productName);
                tvWishlist.setText("🤍");
                Toast.makeText(this, "Removed from wishlist",
                        Toast.LENGTH_SHORT).show();
            } else {
                Product p = new Product(
                        productName, category, price, imageRes);
                p.imageUrl = imageUrl;
                wishlistDAO.addToWishlist(p);
                tvWishlist.setText("❤️");
                Toast.makeText(this, "Added to wishlist! ❤️",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ SHARE BUTTON (ADDED)
        btnShare.setOnClickListener(v -> {

            String shareText = productName + "\n"
                    + "Price: ₹" + price + "\n"
                    + "Check this product in Craftify App!";

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, shareText);

            startActivity(Intent.createChooser(intent, "Share via"));
        });

        // Navigation
        tvBack.setOnClickListener(v -> finish());
        findViewById(R.id.tvCartTop).setOnClickListener(v ->
                startActivity(new Intent(this, CartActivity.class)));

        // Add to Cart
        btnAddToCart.setOnClickListener(v -> {
            CartItem item = new CartItem(
                    0, productName, category, price, 1, imageRes);
            item.imageUrl = imageUrl;
            new CartDAO(this).addItem(item);
            Toast.makeText(this,
                    productName + " added to cart! 🛒",
                    Toast.LENGTH_SHORT).show();
        });

        // Buy Now
        btnBuyNow.setOnClickListener(v -> {
            CartItem item = new CartItem(
                    0, productName, category, price, 1, imageRes);
            item.imageUrl = imageUrl;
            new CartDAO(this).addItem(item);

            Intent intent = new Intent(this, AddressActivity.class);
            intent.putExtra("total", price);
            intent.putExtra("item_count", 1);
            intent.putExtra("name", productName);
            startActivity(intent);
        });
    }

    private void updateWishlistIcon() {
        tvWishlist.setText(
                wishlistDAO.isInWishlist(productName) ? "❤️" : "🤍");
    }

    private void setupDots(int count) {
        if (count <= 1) {
            layoutDots.setVisibility(View.GONE);
            return;
        }
        layoutDots.removeAllViews();
        for (int i = 0; i < count; i++) {
            TextView dot = new TextView(this);
            dot.setText(i == 0 ? "●" : "○");
            dot.setTextColor(getResources().getColor(R.color.white));
            dot.setTextSize(10);
            dot.setPadding(4, 0, 4, 0);
            layoutDots.addView(dot);
        }
    }

    private void updateDots(int current, int count) {
        for (int i = 0; i < layoutDots.getChildCount(); i++) {
            ((TextView) layoutDots.getChildAt(i))
                    .setText(i == current ? "●" : "○");
        }
    }
}