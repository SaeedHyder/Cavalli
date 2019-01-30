package com.ingic.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntityGetTotalList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("guest_status_id")
    @Expose
    private String guestStatusId;
    @SerializedName("guest_category_id")
    @Expose
    private String guest_category_id;
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
    @SerializedName("guest_list_member")
    @Expose
    private List<EntityGuestListMember> guestListMember;

    public Integer getId() {
        return id;
    }

    public String getGuest_category_id() {
        return guest_category_id;
    }

    public void setGuest_category_id(String guest_category_id) {
        this.guest_category_id = guest_category_id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGuestStatusId() {
        return guestStatusId;
    }

    public void setGuestStatusId(String guestStatusId) {
        this.guestStatusId = guestStatusId;
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

    public List<EntityGuestListMember> getGuestListMember() {
        return guestListMember;
    }

    public void setGuestListMember(List<EntityGuestListMember> guestListMember) {
        this.guestListMember = guestListMember;
    }
}
