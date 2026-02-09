package com.example.shop;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ShoppingCart {

    private List<ShopItems> items = new ArrayList<>();


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
        double total = 0;
        return total;
    }

}
