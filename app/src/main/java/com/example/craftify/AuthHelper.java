package com.example.craftify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;

public class AuthHelper {


    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                "auth", Context.MODE_PRIVATE);
        String token = prefs.getString("token", "");
        return !token.isEmpty();
    }


    public static void showLoginDialog(Activity activity, String action) {
        new AlertDialog.Builder(activity)
                .setTitle("Login Required")
                .setMessage("Please login to " + action + ".")
                .setPositiveButton("Login", (dialog, which) -> {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra("from_action", action);
                    activity.startActivity(intent);
                })
                .setNegativeButton("Register", (dialog, which) -> {
                    activity.startActivity(
                            new Intent(activity, RegisterActivity.class));
                })
                .setNeutralButton("Cancel", null)
                .show();
    }
}