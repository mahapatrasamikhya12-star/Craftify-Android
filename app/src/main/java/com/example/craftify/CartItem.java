package com.example.craftify;

public class CartItem {

    public int    id;
    public String productName;
    public String category;
    public double price;
    public int    quantity;
    public int    imageRes;
    public String imageUrl;
    public int    productId;

    public CartItem() {}

    public CartItem(int id, String productName, String category,
                    double price, int quantity, int imageRes) {
        this.id          = id;
        this.productName = productName;
        this.category    = category;
        this.price       = price;
        this.quantity    = quantity;
        this.imageRes    = imageRes;
    }

    public int    getId()          { return id; }
    public String getProductName() { return productName; }
    public String getCategory()    { return category; }
    public double getPrice()       { return price; }
    public int    getQuantity()    { return quantity; }
    public int    getImageRes()    { return imageRes; }
    public String getImageUrl()    { return imageUrl; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setImageUrl(String url)   { this.imageUrl = url; }

    public double getTotal()      { return price * quantity; }
    public double getTotalPrice() { return price * quantity; }
}