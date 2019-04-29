package com.application.cavalliclub.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AddCartEntity extends RealmObject {
    @PrimaryKey
    String Id;

    String ProductName;
    int ProductQuantity;
    String ProductPrice;
    String vatTex;
    int count;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getVat() {
        return vatTex;
    }

    public void setVat(String vatTex) {
        this.vatTex = vatTex;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }
}
