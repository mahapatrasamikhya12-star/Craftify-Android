package com.example.craftify;

import com.google.gson.annotations.SerializedName;

public class ApiProductImage {

    @SerializedName("id")
    public int id;

    @SerializedName("image")
    public String image;

    @SerializedName("is_primary")
    public boolean isPrimary;

    @SerializedName("sort_order")
    public int sortOrder;
}