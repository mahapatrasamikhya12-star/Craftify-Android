package com.example.craftify;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDAO {

    private DatabaseHelper dbHelper;

    public AddressDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // SAVE address
    public boolean saveAddress(String fullName, String phone,
                               String street, String city,
                               String state, String pincode, int userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("user_id",   userId);
        values.put("full_name", fullName);
        values.put("phone",     phone);
        values.put("street",    street);
        values.put("city",      city);
        values.put("state",     state);
        values.put("pincode",   pincode);

        long result = db.insert("addresses", null, values);
        db.close();
        return result != -1;
    }

    // GET last saved address
    public Cursor getLastAddress(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM addresses WHERE user_id = ? ORDER BY id DESC LIMIT 1",
                new String[]{String.valueOf(userId)}
        );
    }
}
