package com.ingic.cavalliclub.entities;

/**
 * Created by saeedhyder on 1/29/2018.
 */

public class FavoriteEnt {

    String image;
    String title;
    String description;

    public FavoriteEnt(String image, String title, String description) {
        this.image = image;
        this.title = title;
        this.description = description;
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
}
