package com.ingic.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EntityGuestListProfile {


    @SerializedName("upcoming")
    @Expose
    private ArrayList<EntityUpcomingHistoryGuestListProfile> upcoming;
    @SerializedName("history")
    @Expose
    private ArrayList<EntityUpcomingHistoryGuestListProfile> history;

    public ArrayList<EntityUpcomingHistoryGuestListProfile> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(ArrayList<EntityUpcomingHistoryGuestListProfile> upcoming) {
        this.upcoming = upcoming;
    }

    public ArrayList<EntityUpcomingHistoryGuestListProfile> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<EntityUpcomingHistoryGuestListProfile> history) {
        this.history = history;
    }
}
