package com.example.shop;

import java.util.*;

public class ShoppingCart {

    private Map<ShopItems, Integer> items = new HashMap<>();
    private double discountPercentage = 0.0;


    public void addItem (ShopItems item){
        items.merge(item, 1, (Integer::sum));
    }
    public boolean containsItem(ShopItems item){
        return items.containsKey(item);
    }
    public void deleteItem(ShopItems item){
        items.remove(item);
    }
    public double totalShoppingCartCost() {

        double total = items.entrySet().stream()
                .mapToDouble(e -> e.getKey().getItemPrice() * e.getValue())
                .sum();

        return total * (1 - discountPercentage / 100);
    }
    public void addDiscount(Double discountPercentage) {
        this.discountPercentage = discountPercentage;

    }
    public int getQuantity(ShopItems item){
        return items.getOrDefault(item, 0);
    }

}
