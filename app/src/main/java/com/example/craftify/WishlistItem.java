package com.example.craftify;

public class WishlistItem {

    private String name, category;
    private int price, imageRes;

    public WishlistItem(String name, String category, int price, int imageRes) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageRes = imageRes;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getPrice() { return price; }
    public int getImageRes() { return imageRes; }
}