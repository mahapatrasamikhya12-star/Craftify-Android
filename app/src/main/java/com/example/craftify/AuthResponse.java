package com.example.craftify;

import com.google.gson.annotations.SerializedName;

public class AuthResponse {

    @SerializedName("access")
    public String access;

    @SerializedName("refresh")
    public String refresh;

    @SerializedName("user")
    public UserData user;

    public static class UserData {
        @SerializedName("id")
        public int id;

        @SerializedName("username")
        public String username;

        @SerializedName("email")
        public String email;
    }
}