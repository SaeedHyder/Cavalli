package com.application.cavalliclub.entities;

/**
 * Created by saeedhyder on 1/30/2018.
 */

public class MenuEnt {

    String image;
    String menuItem;

    public MenuEnt(String image, String menuItem) {
        this.image = image;
        this.menuItem = menuItem;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }
}
