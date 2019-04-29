package com.application.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class EntityMarkUnmarkCalendar {

    @SerializedName("is_marked")
    @Expose
    private Integer isMarked;

    public Integer getIsMarked() {
        return isMarked;
    }

    public void setIsMarked(Integer isMarked) {
        this.isMarked = isMarked;
    }
}
