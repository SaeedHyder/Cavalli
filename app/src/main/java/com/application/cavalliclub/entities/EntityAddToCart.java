package com.application.cavalliclub.entities;

public class EntityAddToCart {

    String drink_name;
    String quantity;
    String amount;

    public EntityAddToCart(String drink_name, String quantity, String amount) {
        this.drink_name = drink_name;
        this.quantity = quantity;
        this.amount = amount;
    }

    public String getDrink_name() {
        return drink_name;
    }

    public void setDrink_name(String drink_name) {
        this.drink_name = drink_name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
