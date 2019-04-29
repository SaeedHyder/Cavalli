package com.application.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddEditGuestListEntity {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("name_title")
    @Expose
    private String nameTitle;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("email_address")
    @Expose
    private String emailAddress;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("date")
    @Expose
    private String date;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
