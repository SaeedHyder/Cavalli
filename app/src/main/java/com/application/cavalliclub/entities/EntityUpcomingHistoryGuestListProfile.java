package com.application.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EntityUpcomingHistoryGuestListProfile {

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
    private ArrayList<EntityGuestListMemberProfile> guestListMember;
    @SerializedName("guest_list_category")
    @Expose
    private GuestListCategoryEntity guest_list_category;

    public GuestListCategoryEntity getGuest_list_category() {
        return guest_list_category;
    }

    public void setGuest_list_category(GuestListCategoryEntity guest_list_category) {
        this.guest_list_category = guest_list_category;
    }

    public Integer getId() {
        return id;
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

    public ArrayList<EntityGuestListMemberProfile> getGuestListMember() {
        return guestListMember;
    }

    public void setGuestListMember(ArrayList<EntityGuestListMemberProfile> guestListMember) {
        this.guestListMember = guestListMember;
    }
}
