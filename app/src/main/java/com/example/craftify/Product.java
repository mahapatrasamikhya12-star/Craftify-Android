package com.example.craftify;

public class Product {

    private String name, category;
    private int    price, imageRes;

    public String imageUrl;
    public String description;
    public int    mrp;
    public int    discount;
    public int    imageRes2;
    public int    imageRes3;
    public int    productId;   // ✅ for Django order API
    public String sellerName;  // ✅ for product detail screen

    public Product(String name, String category, int price, int imageRes) {
        this.name     = name;
        this.category = category;
        this.price    = price;
        this.imageRes = imageRes;
        this.mrp      = (int)(price / 0.70);
        this.discount = Math.round(((this.mrp - price) * 100f) / this.mrp);
    }

    // ── Getters ───────────────────────────────────────────────
    public String getName()        { return name; }
    public String getCategory()    { return category; }
    public int    getPrice()       { return price; }
    public int    getImageRes()    { return imageRes; }
    public String getImageUrl()    { return imageUrl; }
    public String getDescription() { return description; }
    public int    getMrp()         { return mrp; }
    public int    getDiscount()    { return discount; }
    public int    getProductId()   { return productId; }
    public String getSellerName()  { return sellerName; }

    // ── Setters ───────────────────────────────────────────────
    public void setName(String name)               { this.name = name; }
    public void setCategory(String category)       { this.category = category; }
    public void setPrice(int price)                { this.price = price; }
    public void setImageRes(int imageRes)          { this.imageRes = imageRes; }
    public void setImageUrl(String imageUrl)       { this.imageUrl = imageUrl; }
    public void setDescription(String description) { this.description = description; }
    public void setImageRes2(int imageRes2)        { this.imageRes2 = imageRes2; }
    public void setImageRes3(int imageRes3)        { this.imageRes3 = imageRes3; }
    public void setProductId(int productId)        { this.productId = productId; }
    public void setSellerName(String sellerName)   { this.sellerName = sellerName; }
}