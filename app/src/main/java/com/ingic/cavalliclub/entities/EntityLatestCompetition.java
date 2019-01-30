package com.ingic.cavalliclub.entities;

public class EntityLatestCompetition {

    String image_competition;
    String name_competition;
    String detail_competition;
    String serial_no;
    String date;
    String imageStatus;
    String status;
    int color;

    public EntityLatestCompetition(String image_competition, String name_competition, String detail_competition, String imageStatus, String status, int color) {
        this.image_competition = image_competition;
        this.name_competition = name_competition;
        this.detail_competition = detail_competition;
        this.imageStatus = imageStatus;
        this.status = status;
        this.color = color;
    }

    public EntityLatestCompetition(String image_competition, String name_competition, String detail_competition, String serial_no, String date) {
        this.image_competition = image_competition;
        this.name_competition = name_competition;
        this.detail_competition = detail_competition;
        this.serial_no = serial_no;
        this.date = date;
    }

    public String getImage_competition() {
        return image_competition;
    }

    public void setImage_competition(String image_competition) {
        this.image_competition = image_competition;
    }

    public String getName_competition() {
        return name_competition;
    }

    public void setName_competition(String name_competition) {
        this.name_competition = name_competition;
    }

    public String getDetail_competition() {
        return detail_competition;
    }

    public void setDetail_competition(String detail_competition) {
        this.detail_competition = detail_competition;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
