package com.example.craftify;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private List<Product> productList;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context     = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvCategory.setText(product.getCategory());
        holder.tvPrice.setText("₹" + product.getPrice());

        // ─────────────────────────────────────
        // Image loading
        // ─────────────────────────────────────
        if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(product.getImageUrl())
                    .placeholder(R.color.cream)
                    .error(R.color.cream)
                    .centerCrop()
                    .into(holder.ivProduct);
        } else if (product.getImageRes() != 0) {
            holder.ivProduct.setImageResource(product.getImageRes());
            holder.ivProduct.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            holder.ivProduct.setImageDrawable(null);
            holder.ivProduct.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.ivProduct.setBackgroundColor(
                    context.getResources().getColor(R.color.cream));
        }

        WishListDAO wishlistDAO = new WishListDAO(context);

        // Set initial heart state
        holder.tvWishlist.setText(
                wishlistDAO.isInWishlist(product.getName()) ? "❤️" : "🤍"
        );

        holder.tvWishlist.setOnClickListener(v -> {
            if (wishlistDAO.isInWishlist(product.getName())) {
                wishlistDAO.removeFromWishlist(product.getName());
                holder.tvWishlist.setText("🤍");
                Toast.makeText(context,
                        "Removed from wishlist",
                        Toast.LENGTH_SHORT).show();
            } else {
                wishlistDAO.addToWishlist(product);
                holder.tvWishlist.setText("❤️");
                Toast.makeText(context,
                        "Added to wishlist! ❤️",
                        Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvAddToCart.setOnClickListener(v -> {
            DatabaseHelper db = new DatabaseHelper(context); // ← use DatabaseHelper
            db.addToCart(
                    product.getName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getImageRes(),
                    product.getImageUrl()
            );
            Toast.makeText(context,
                    product.getName() + " added to cart! 🛒",
                    Toast.LENGTH_SHORT).show();
        });


        // ─────────────────────────────────────
        // Click on card → ProductDetailActivity
        // ─────────────────────────────────────
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("name",      product.getName());
            intent.putExtra("category",  product.getCategory());
            intent.putExtra("price",     product.getPrice());
            intent.putExtra("imageRes",  product.getImageRes());
            intent.putExtra("imageUrl",  product.getImageUrl());
            intent.putExtra("imageRes2", product.imageRes2);
            intent.putExtra("imageRes3", product.imageRes3);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateList(List<Product> newList) {
        productList = newList;
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvCategory, tvPrice, tvAddToCart, tvWishlist; // ← tvWishlist added

        ProductViewHolder(View itemView) {
            super(itemView);
            ivProduct   = itemView.findViewById(R.id.ivProduct);
            tvName      = itemView.findViewById(R.id.tvProductName);
            tvCategory  = itemView.findViewById(R.id.tvProductCategory);
            tvPrice     = itemView.findViewById(R.id.tvProductPrice);
            tvAddToCart = itemView.findViewById(R.id.tvAddToCart);
            tvWishlist  = itemView.findViewById(R.id.tvWishlist); // ← NEW
        }
    }
}