package com.ingic.cavalliclub.entities;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AddCartEntity extends RealmObject {
    @PrimaryKey
    String Id;

    String ProductName;
    int ProductQuantity;
    String ProductPrice;

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
