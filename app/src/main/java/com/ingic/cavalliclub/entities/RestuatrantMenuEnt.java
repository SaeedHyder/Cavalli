package com.ingic.cavalliclub.entities;

/**
 * Created by saeedhyder on 1/30/2018.
 */

public class RestuatrantMenuEnt {

    String image;
    String title;
    String description;
    String amount;

    public RestuatrantMenuEnt(String image, String title, String description, String amount) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.amount = amount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
