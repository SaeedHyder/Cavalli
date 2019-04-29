package com.application.cavalliclub.entities;

public class AddToCardJson {

    String product_id;
    String quantity;
    String price;

    public AddToCardJson(String product_id, String quantity, String price) {
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
