package com.example.craftify;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    // ── Auth ──────────────────────────────────────────────────
    @POST("api/users/login/")
    Call<AuthResponse> login(@Body LoginRequest body);

    @POST("api/users/register/")
    Call<AuthResponse> register(@Body RegisterRequest body);

    // ── Products ──────────────────────────────────────────────
    @GET("api/products/")
    Call<ProductListResponse> getProducts();

    @GET("api/products/")
    Call<ProductListResponse> getProductsByCategory(
            @Query("category") String category);

    // ── Orders ────────────────────────────────────────────────
    @POST("api/orders/place/")
    Call<OrderResponse> placeOrder(
            @Header("Authorization") String token,
            @Body OrderRequest body);

    @GET("api/orders/")
    Call<List<OrderResponse>> getMyOrders(
            @Header("Authorization") String token);
}