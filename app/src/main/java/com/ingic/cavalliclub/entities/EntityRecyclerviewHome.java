package com.ingic.cavalliclub.entities;

public class EntityRecyclerviewHome {

    String image;
    String image_name;

    public EntityRecyclerviewHome(String image, String image_name) {
        this.image = image;
        this.image_name = image_name;
    }

    public EntityRecyclerviewHome(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
