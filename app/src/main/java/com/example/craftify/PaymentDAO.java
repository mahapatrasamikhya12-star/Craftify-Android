package com.example.craftify;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaymentDAO {

    private DatabaseHelper dbHelper;

    public PaymentDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // SAVE payment record
    public boolean savePayment(String orderId, String paymentId,
                               double amount, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String date = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm", Locale.getDefault()
        ).format(new Date());

        ContentValues values = new ContentValues();
        values.put("order_id",   orderId);
        values.put("payment_id", paymentId);
        values.put("amount",     amount);
        values.put("status",     status);
        values.put("date",       date);

        long result = db.insert("payments", null, values);
        db.close();
        return result != -1;
    }
}