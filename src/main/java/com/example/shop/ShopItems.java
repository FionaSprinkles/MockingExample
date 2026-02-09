package com.example.shop;

public class ShopItems {
    private String itemName;
    private double itemPrice;

    public ShopItems(double itemPrice, String itemName) {
        this.itemPrice = itemPrice;
        this.itemName = itemName;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }
}
