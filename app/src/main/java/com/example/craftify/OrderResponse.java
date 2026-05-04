package com.example.craftify;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderResponse {

    @SerializedName("id")
    public int id;

    @SerializedName("status")
    public String status;

    @SerializedName("total_price")
    public String totalPrice;

    @SerializedName("shipping_address")
    public String shippingAddress;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("items")
    public List<OrderItemResponse> items;

    public static class OrderItemResponse {
        @SerializedName("product_name")
        public String productName;

        @SerializedName("quantity")
        public int quantity;

        @SerializedName("price")
        public String price;

        @SerializedName("subtotal")
        public double subtotal;

        @SerializedName("product_image")
        public String productImage;
    }
}
