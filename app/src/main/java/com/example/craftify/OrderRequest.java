package com.example.craftify;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class OrderRequest {

    @SerializedName("shipping_address")
    public String shippingAddress;

    @SerializedName("items")
    public List<OrderItemRequest> items;

    public OrderRequest(String shippingAddress, List<OrderItemRequest> items) {
        this.shippingAddress = shippingAddress;
        this.items           = items;
    }

    public static class OrderItemRequest {
        @SerializedName("product_id")
        public int productId;

        @SerializedName("quantity")
        public int quantity;

        public OrderItemRequest(int productId, int quantity) {
            this.productId = productId;
            this.quantity  = quantity;
        }
    }
}
