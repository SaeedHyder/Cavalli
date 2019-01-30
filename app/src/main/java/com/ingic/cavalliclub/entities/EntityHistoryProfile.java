package com.ingic.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EntityHistoryProfile {

    @SerializedName("current_membership")
    @Expose
    private List<EntityMemberships> currentMembership;
    @SerializedName("membership_history")
    @Expose
    private List<EntityMembershipHistory> membershipHistory;

    public List<EntityMemberships> getCurrentMembership() {
        return currentMembership;
    }

    public void setCurrentMembership(List<EntityMemberships> currentMembership) {
        this.currentMembership = currentMembership;
    }

    public List<EntityMembershipHistory> getMembershipHistory() {
        return membershipHistory;
    }

    public void setMembershipHistory(List<EntityMembershipHistory> membershipHistory) {
        this.membershipHistory = membershipHistory;
    }
}
