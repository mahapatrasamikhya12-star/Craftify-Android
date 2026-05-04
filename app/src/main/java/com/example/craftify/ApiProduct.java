package com.example.craftify;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ApiProduct {

    @SerializedName("id")
    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("price")
    public String price;

    @SerializedName("discount_pct")
    public float discountPct;

    @SerializedName("stock_qty")
    public int stockQty;

    @SerializedName("category_name")
    public String categoryName;

    @SerializedName("seller_name")
    public String sellerName;

    @SerializedName("images")
    public List<ApiProductImage> images;

    // ← Fixed: no longer adds base URL manually
    // Django serializer now returns full URL directly
    public String getFirstImageUrl() {
        if (images != null && !images.isEmpty()) {
            String url = images.get(0).image;
            if (url != null && !url.isEmpty()) {
                return url;
            }
        }

        return null;
    }

    public int getPriceAsInt() {
        try {
            return (int) Double.parseDouble(price);
        } catch (Exception e) { return 0; }
    }
}