package com.ingic.cavalliclub.entities;

/**
 * Created by saeedhyder on 1/30/2018.
 */

public class CategoriesEnt {

    String item;
    boolean isSelected=false;


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public CategoriesEnt(String item, boolean isSelected) {
        this.item = item;
        this.isSelected = isSelected;
    }

    public CategoriesEnt(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
