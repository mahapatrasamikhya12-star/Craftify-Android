package com.example.craftify;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView   recyclerView;
    private CartAdapter    adapter;
    private CartDAO        cartDAO;        // ✅ declare here
    private List<CartItem> cartItems;
    private TextView       tvSubtotal, tvTotal, tvItemCount, tvDelivery;
    private Button         btnCheckout;
    private LinearLayout   layoutEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getWindow().setStatusBarColor(getResources().getColor(R.color.primary));

        // ── Bind views ────────────────────────────────────────
        recyclerView = findViewById(R.id.recyclerCart);
        tvSubtotal   = findViewById(R.id.tvSubtotal);
        tvTotal      = findViewById(R.id.tvGrandTotal);
        tvItemCount  = findViewById(R.id.tvItemCount);
        tvDelivery   = findViewById(R.id.tvDelivery);
        btnCheckout  = findViewById(R.id.btnCheckout);
        layoutEmpty  = findViewById(R.id.layoutEmpty);

        // ✅ Initialize cartDAO FIRST before anything else
        cartDAO   = new CartDAO(this);
        cartItems = new ArrayList<>(cartDAO.getAllItems());

        // ── Setup RecyclerView ────────────────────────────────
        adapter = new CartAdapter(this, cartItems, () -> refreshCart());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ── Load cart ─────────────────────────────────────────
        refreshCart();

        // ── Checkout button ───────────────────────────────────
        btnCheckout.setOnClickListener(v -> {
            if (!cartItems.isEmpty()) {
                Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                intent.putExtra("total",      getGrandTotal());
                intent.putExtra("item_count", getTotalItems());
                startActivity(intent);
            }
        });
    }

    private void refreshCart() {
        cartItems.clear();
        cartItems.addAll(cartDAO.getAllItems()); // ✅ cartDAO never null now
        adapter.notifyDataSetChanged();
        updateSummary();
    }

    private void updateSummary() {
        int subtotal = 0, totalItems = 0;

        for (CartItem item : cartItems) {
            subtotal   += (int) item.getTotal();
            totalItems += item.getQuantity();
        }

        int delivery   = subtotal > 0 ? (subtotal >= 500 ? 0 : 49) : 0;
        int grandTotal = subtotal + delivery;

        tvItemCount.setText(totalItems + " item" +
                (totalItems != 1 ? "s" : "") + " in cart");
        tvSubtotal.setText("₹" + subtotal);

        if (tvDelivery != null)
            tvDelivery.setText(delivery == 0 ? "FREE" : "₹" + delivery);

        tvTotal.setText("₹" + grandTotal);

        if (cartItems.isEmpty()) {
            layoutEmpty.setVisibility(LinearLayout.VISIBLE);
            recyclerView.setVisibility(LinearLayout.GONE);
            btnCheckout.setEnabled(false);
        } else {
            layoutEmpty.setVisibility(LinearLayout.GONE);
            recyclerView.setVisibility(LinearLayout.VISIBLE);
            btnCheckout.setEnabled(true);
        }
    }

    private int getGrandTotal() {
        int subtotal = 0;
        for (CartItem item : cartItems) subtotal += (int) item.getTotal();
        return subtotal + (subtotal >= 500 ? 0 : 49);
    }

    private int getTotalItems() {
        int count = 0;
        for (CartItem item : cartItems) count += item.getQuantity();
        return count;
    }
}