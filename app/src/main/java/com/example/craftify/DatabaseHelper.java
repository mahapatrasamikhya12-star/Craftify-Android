package com.example.craftify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME    = "handmade_crafts.db";
    private static final int    DATABASE_VERSION = 5; // ← bumped to 5

    public static final String TABLE_PRODUCTS = "products";
    public static final String TABLE_USERS    = "users";
    public static final String TABLE_ORDERS   = "orders";
    public static final String TABLE_CART     = "cart";
    public static final String TABLE_ADDRESS  = "addresses";
    public static final String TABLE_PAYMENT  = "payments";
    public static final String TABLE_WISHLIST = "wishlist"; // ← NEW

    public static final String COL_PRODUCT_ID        = "id";
    public static final String COL_PRODUCT_NAME      = "name";
    public static final String COL_PRODUCT_PRICE     = "price";
    public static final String COL_PRODUCT_CATEGORY  = "category";
    public static final String COL_PRODUCT_IMAGE_RES = "image_res";

    private static final String CREATE_PRODUCTS_TABLE =
            "CREATE TABLE " + TABLE_PRODUCTS + " (" +
                    COL_PRODUCT_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_PRODUCT_NAME      + " TEXT NOT NULL, " +
                    COL_PRODUCT_CATEGORY  + " TEXT, " +
                    COL_PRODUCT_PRICE     + " INTEGER, " +
                    COL_PRODUCT_IMAGE_RES + " INTEGER);";

    private static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT UNIQUE NOT NULL, " +
                    "email TEXT UNIQUE NOT NULL, " +
                    "password TEXT NOT NULL);";

    private static final String CREATE_ORDERS_TABLE =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER, " +
                    "product_id INTEGER, " +
                    "quantity INTEGER, " +
                    "order_date TEXT, " +
                    "FOREIGN KEY(user_id) REFERENCES users(id), " +
                    "FOREIGN KEY(product_id) REFERENCES products(id));";

    private static final String CREATE_CART_TABLE =
            "CREATE TABLE " + TABLE_CART + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "product_name TEXT, " +
                    "category TEXT, " +
                    "price REAL, " +
                    "quantity INTEGER DEFAULT 1, " +
                    "image_res INTEGER, " +
                    "image_url TEXT);";

    private static final String CREATE_ADDRESS_TABLE =
            "CREATE TABLE " + TABLE_ADDRESS + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "user_id INTEGER, " +
                    "full_name TEXT, " +
                    "phone TEXT, " +
                    "street TEXT, " +
                    "city TEXT, " +
                    "state TEXT, " +
                    "pincode TEXT);";

    private static final String CREATE_PAYMENT_TABLE =
            "CREATE TABLE " + TABLE_PAYMENT + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "order_id TEXT, " +
                    "payment_id TEXT, " +
                    "amount REAL, " +
                    "status TEXT, " +
                    "date TEXT);";

    // ← NEW wishlist table
    private static final String CREATE_WISHLIST_TABLE =
            "CREATE TABLE " + TABLE_WISHLIST + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "product_name TEXT, " +
                    "category TEXT, " +
                    "price INTEGER, " +
                    "image_res INTEGER, " +
                    "image_url TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCTS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
        db.execSQL(CREATE_CART_TABLE);
        db.execSQL(CREATE_ADDRESS_TABLE);
        db.execSQL(CREATE_PAYMENT_TABLE);
        db.execSQL(CREATE_WISHLIST_TABLE); // ← NEW
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WISHLIST); // ← NEW
        onCreate(db);
    }

    // ─────────────────────────────────────
    // CART methods
    // ─────────────────────────────────────
    public void addToCart(String productName, String category,
                          int price, int imageRes) {
        addToCart(productName, category, price, imageRes, null);
    }

    public void addToCart(String productName, String category,
                          int price, int imageRes, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id, quantity FROM " + TABLE_CART +
                        " WHERE product_name=?", new String[]{productName});

        if (cursor.moveToFirst()) {
            int id  = cursor.getInt(0);
            int qty = cursor.getInt(1);
            ContentValues values = new ContentValues();
            values.put("quantity", qty + 1);
            db.update(TABLE_CART, values, "id=?",
                    new String[]{String.valueOf(id)});
        } else {
            ContentValues values = new ContentValues();
            values.put("product_name", productName);
            values.put("category",     category);
            values.put("price",        price);
            values.put("quantity",     1);
            values.put("image_res",    imageRes);
            values.put("image_url",    imageUrl);
            db.insert(TABLE_CART, null, values);
        }
        cursor.close();
        db.close();
    }

    public void clearCart() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, null, null);
        db.close();
    }

    public List<CartItem> getCartItems() {
        List<CartItem> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART, null);

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
                if (urlIndex != -1) item.imageUrl = cursor.getString(urlIndex);
                items.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    public void updateQuantity(int id, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        db.update(TABLE_CART, values, "id=?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void removeFromCart(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int getCartCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT SUM(quantity) FROM " + TABLE_CART, null);
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}