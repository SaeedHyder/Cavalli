package com.ingic.cavalliclub.entities;

public class EntityPendingOrders {

    String image;
    String number;
    String title;
    String description;
    String amount;
    String iv_status;
    String tv_status;
    int color;

    public EntityPendingOrders(String image, String number, String title, String description, String amount) {
        this.image = image;
        this.number = number;
        this.title = title;
        this.description = description;
        this.amount = amount;
    }

    public EntityPendingOrders(String image, String number, String title, String description, String amount, String iv_status, String tv_status, int color) {
        this.image = image;
        this.number = number;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.iv_status = iv_status;
        this.tv_status = tv_status;
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIv_status() {
        return iv_status;
    }

    public void setIv_status(String iv_status) {
        this.iv_status = iv_status;
    }

    public String getTv_status() {
        return tv_status;
    }

    public void setTv_status(String tv_status) {
        this.tv_status = tv_status;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
