package com.ingic.cavalliclub.entities;

public class PurchaseMembershipEntity {

    String user_id;
    String membership_id;
    String expiry_date;
    String membership_no;
    String membership_amount;
    String updated_at;
    String created_at;
    String id;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMembership_id() {
        return membership_id;
    }

    public void setMembership_id(String membership_id) {
        this.membership_id = membership_id;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public String getMembership_no() {
        return membership_no;
    }

    public void setMembership_no(String membership_no) {
        this.membership_no = membership_no;
    }

    public String getMembership_amount() {
        return membership_amount;
    }

    public void setMembership_amount(String membership_amount) {
        this.membership_amount = membership_amount;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
