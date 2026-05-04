package com.example.craftify;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private Context context;
    private List<CartItem> cartItems;
    private CartDAO cartDAO;
    private OnCartChangeListener listener;

    public CartAdapter(Context context, List<CartItem> cartItems,
                       OnCartChangeListener listener) {
        this.context   = context;
        this.cartItems = cartItems;
        this.cartDAO   = new CartDAO(context);
        this.listener  = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder h,
                                 @SuppressLint("RecyclerView") int position) {
        CartItem item = cartItems.get(position);

        h.tvName.setText(item.getProductName());
        h.tvCategory.setText(item.getCategory());
        h.tvPrice.setText("₹" + (int) item.getPrice());
        h.tvQuantity.setText(String.valueOf(item.getQuantity()));
        h.tvTotal.setText("₹" + (int) item.getTotal());

        // ✅ Load image — URL first, then drawable, then placeholder
        if (item.imageUrl != null && !item.imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(h.ivProduct);
        } else if (item.getImageRes() != 0) {
            h.ivProduct.setImageResource(item.getImageRes());
        } else {
            h.ivProduct.setImageResource(R.drawable.placeholder);
        }

        // ✅ Increase quantity
        h.btnIncrease.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);
            cartDAO.updateQuantity(item.getId(), item.getQuantity());
            notifyItemChanged(position);
            listener.onCartChanged();
        });

        // ✅ Decrease quantity
        h.btnDecrease.setOnClickListener(v -> {
            if (item.getQuantity() - 1 <= 0) {
                cartDAO.deleteItem(item.getId());
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
            } else {
                item.setQuantity(item.getQuantity() - 1);
                cartDAO.updateQuantity(item.getId(), item.getQuantity());
                notifyItemChanged(position);
            }
            listener.onCartChanged();
        });

        // ✅ Remove item
        h.btnRemove.setOnClickListener(v -> {
            cartDAO.deleteItem(item.getId());
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
            listener.onCartChanged();
        });
    }

    public void updateList(List<CartItem> newItems) {
        this.cartItems = newItems;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() { return cartItems.size(); }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView  tvName, tvCategory, tvPrice, tvQuantity, tvTotal;
        Button    btnIncrease, btnDecrease, btnRemove;

        CartViewHolder(View v) {
            super(v);
            ivProduct   = v.findViewById(R.id.ivCartProduct);
            tvName      = v.findViewById(R.id.tvCartName);
            tvCategory  = v.findViewById(R.id.tvCartCategory);
            tvPrice     = v.findViewById(R.id.tvCartPrice);
            tvQuantity  = v.findViewById(R.id.tvCartQuantity);
            tvTotal     = v.findViewById(R.id.tvCartTotal);
            btnIncrease = v.findViewById(R.id.btnIncrease);
            btnDecrease = v.findViewById(R.id.btnDecrease);
            btnRemove   = v.findViewById(R.id.btnRemove);
        }
    }
}