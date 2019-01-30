package com.ingic.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EntityPushCheck {

    @SerializedName("is_marked")
    @Expose
    private int is_marked;

    public int getIs_marked() {
        return is_marked;
    }

    public void setIs_marked(int is_marked) {
        this.is_marked = is_marked;
    }
}
