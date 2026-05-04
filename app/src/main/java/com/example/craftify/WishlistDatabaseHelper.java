package com.example.craftify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class WishlistDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "wishlist.db";
    private static final int    DB_VERSION = 2; // ✅ bumped to 2
    private static final String TABLE      = "wishlist";

    public WishlistDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT UNIQUE, " +
                "category TEXT, " +
                "price INTEGER, " +
                "image_res INTEGER)"); // ✅ image_res not imageRes
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // ✅ ADD
    public void addToWishlist(String name, String category,
                              int price, int imageRes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",      name);
        values.put("category",  category);
        values.put("price",     price);
        values.put("image_res", imageRes); // ✅ fixed column name
        db.insertWithOnConflict(TABLE, null, values,
                SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    // ✅ GET ALL
    public List<WishlistItem> getAllWishlistItems() {
        List<WishlistItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(new WishlistItem(
                        cursor.getString(cursor.getColumnIndexOrThrow("name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("category")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("price")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("image_res"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // ✅ REMOVE
    public void removeFromWishlist(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, "name=?", new String[]{name});
        db.close();
    }

    // ✅ CHECK
    public boolean isInWishlist(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM " + TABLE + " WHERE name=?",
                new String[]{name});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}