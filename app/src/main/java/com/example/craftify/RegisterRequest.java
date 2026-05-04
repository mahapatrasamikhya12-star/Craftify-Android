package com.example.craftify;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("username")
    public String username;

    @SerializedName("email")
    public String email;

    @SerializedName("password")
    public String password;

    @SerializedName("password2")
    public String password2;

    @SerializedName("role")
    public String role;

    @SerializedName("first_name")
    public String firstName;

    @SerializedName("last_name")
    public String lastName;

    public RegisterRequest(String username, String email, String password) {
        this.username  = username;
        this.email     = email;
        this.password  = password;
        this.password2 = password;
        this.role      = "buyer";
        this.firstName = "";
        this.lastName  = "";
    }
}