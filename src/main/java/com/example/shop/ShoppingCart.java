package com.example.shop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShoppingCart {

    private List<ShopItems> items = new ArrayList<>();
    private double discountPercentage = 0.0;


    public void addItem (ShopItems item){
        items.add(item);
    }
    public boolean containsItem(ShopItems item){
        return items.contains(item);
    }
    public void deleteItem(ShopItems item){
        items.remove(item);
    }
    public double totalShoppingCartCost() {

        double total =items.stream()
                .mapToDouble(ShopItems::getItemPrice)
                .sum();

        return total * (1 - discountPercentage / 100);
    }
    public void addDiscount(Double discountPercentage) {
        this.discountPercentage = discountPercentage;

    }
    public int getQuantity(ShopItems item){
        return 0;
    }

}
