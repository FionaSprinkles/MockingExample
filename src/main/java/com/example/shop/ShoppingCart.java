package com.example.shop;

import java.util.HashSet;
import java.util.Set;

public class ShoppingCart {

    private Set<String> items = new HashSet<String>();


    public void addItem (String itemName){
        items.add(itemName);
    }
    public boolean containsItem(String itemName){
        return items.contains(itemName);
    }
    public void deleteItem(String itemName){
        items.remove(itemName);
    }

}
