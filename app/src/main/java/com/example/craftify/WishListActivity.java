
package com.example.craftify;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WishListActivity extends AppCompatActivity {

    RecyclerView rvWishlist;
    ProductAdapter adapter;
    LinearLayout layoutEmpty;
    TextView tvCount;
    WishListDAO wishlistDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        wishlistDAO = new WishListDAO(this);

        rvWishlist  = findViewById(R.id.rvWishlist);
        layoutEmpty = findViewById(R.id.layoutEmpty);
        tvCount     = findViewById(R.id.tvWishlistCount);

        rvWishlist.setLayoutManager(new GridLayoutManager(this, 2));

        findViewById(R.id.tvBack).setOnClickListener(v -> finish());

        loadWishlist();
    }

    private void loadWishlist() {
        List<Product> items = wishlistDAO.getAllWishlistItems();

        tvCount.setText(items.size() + " items");

        if (items.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rvWishlist.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rvWishlist.setVisibility(View.VISIBLE);
            adapter = new ProductAdapter(this, items);
            rvWishlist.setAdapter(adapter);
        }
    }
}