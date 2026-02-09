package com.example.shop;

public class ShopItems {
    private String itemName;
    private double itemPrice;

    public ShopItems(String itemName, double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }
}
