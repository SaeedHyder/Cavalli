package com.ingic.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntityMemberships {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("membership_type")
    @Expose
    private String membershipType;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("term_condition")
    @Expose
    private String term_condition;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("days")
    @Expose
    private String days;
    @SerializedName("period")
    @Expose
    private String period;
    @SerializedName("logo_image")
    @Expose
    private String logoImage;
    @SerializedName("is_active")
    @Expose
    private String isActive;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("member_ship_images")
    @Expose
    private List<EntityMemberShipImage> memberShipImages;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getLogoImage() {
        return logoImage;
    }

    public void setLogoImage(String logoImage) {
        this.logoImage = logoImage;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<EntityMemberShipImage> getMemberShipImages() {
        return memberShipImages;
    }

    public void setMemberShipImages(List<EntityMemberShipImage> memberShipImages) {
        this.memberShipImages = memberShipImages;
    }

    public String getTerm_condition() {
        return term_condition;
    }

    public void setTerm_condition(String term_condition) {
        this.term_condition = term_condition;
    }
}
