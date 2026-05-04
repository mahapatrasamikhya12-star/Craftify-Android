package com.example.craftify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class WishListDAO {

    private DatabaseHelper dbHelper;

    public WishListDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // ADD to wishlist
    public boolean addToWishlist(Product product) {
        if (isInWishlist(product.getName())) return false;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("product_name", product.getName());
        values.put("category",     product.getCategory());
        values.put("price",        product.getPrice());
        values.put("image_res",    product.getImageRes());
        values.put("image_url",    product.getImageUrl());

        long result = db.insert("wishlist", null, values);
        db.close();
        return result != -1;
    }

    // REMOVE from wishlist
    public boolean removeFromWishlist(String productName) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("wishlist", "product_name = ?",
                new String[]{productName});
        db.close();
        return rows > 0;
    }

    // CHECK if product is in wishlist
    public boolean isInWishlist(String productName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM wishlist WHERE product_name = ?",
                new String[]{productName}
        );
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }

    // GET all wishlist items
    public List<Product> getAllWishlistItems() {
        List<Product> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM wishlist", null);

        if (cursor.moveToFirst()) {
            do {
                Product p = new Product(
                        cursor.getString(1),  // name
                        cursor.getString(2),  // category
                        cursor.getInt(3),     // price
                        cursor.getInt(4)      // image_res
                );
                p.imageUrl = cursor.getString(5);
                list.add(p);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }

    // GET count
    public int getWishlistCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM wishlist", null);
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}