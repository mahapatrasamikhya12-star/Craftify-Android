package com.example.craftify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    private DatabaseHelper dbHelper;

    public CartDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }


    public boolean addItem(CartItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Check if already in cart
        Cursor cursor = db.rawQuery(
                "SELECT id, quantity FROM cart WHERE product_name=?",
                new String[]{item.getProductName()});

        if (cursor.moveToFirst()) {
            // Already exists → increase quantity
            int id  = cursor.getInt(0);
            int qty = cursor.getInt(1);
            ContentValues values = new ContentValues();
            values.put("quantity", qty + 1);
            db.update("cart", values, "id=?",
                    new String[]{String.valueOf(id)});
            cursor.close();
            db.close();
            return true;
        }

        cursor.close();

        // New item → insert with imageUrl
        ContentValues values = new ContentValues();
        values.put("product_name", item.getProductName());
        values.put("category",     item.getCategory());
        values.put("price",        item.getPrice());
        values.put("quantity",     item.getQuantity());
        values.put("image_res",    item.getImageRes());
        values.put("image_url", item.imageUrl);

        long result = db.insert("cart", null, values);
        db.close();
        return result != -1;
    }


    public List<CartItem> getAllItems() {
        List<CartItem> cartList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cart", null);

        if (cursor.moveToFirst()) {
            do {
                CartItem item = new CartItem(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("product_name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("category")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("price")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("image_res"))
                );

                int urlIndex = cursor.getColumnIndex("image_url");
                if (urlIndex != -1) {
                    item.imageUrl = cursor.getString(urlIndex);
                }
                cartList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cartList;
    }


    public boolean updateQuantity(int id, int quantity) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        int rows = db.update("cart", values, "id=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }


    public boolean deleteItem(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = db.delete("cart", "id=?",
                new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }


    public void clearCart() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("cart", null, null);
        db.close();
    }

    public int getCartCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(quantity) FROM cart", null);
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}