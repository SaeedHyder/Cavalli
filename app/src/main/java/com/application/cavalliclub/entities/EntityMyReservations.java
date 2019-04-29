package com.application.cavalliclub.entities;

public class EntityMyReservations {

    String title;
    String date;
    String noOfPeople;
    String imageStatus;
    String status;
    int color;

    public EntityMyReservations(String title, String date, String noOfPeople, String imageStatus, String status,int color) {
        this.title = title;
        this.date = date;
        this.noOfPeople = noOfPeople;
        this.imageStatus = imageStatus;
        this.status = status;
        this.color=color;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNoOfPeople() {
        return noOfPeople;
    }

    public void setNoOfPeople(String noOfPeople) {
        this.noOfPeople = noOfPeople;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
