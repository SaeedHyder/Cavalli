package com.ingic.cavalliclub.entities;

import com.ingic.cavalliclub.helpers.DateHelper;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by saeedhyder on 9/15/2017.
 */

public class NotificationEnt {

  String text;
  String date;
  String time;

    public NotificationEnt(String text, String date, String time) {
        this.text = text;
        this.date = date;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}