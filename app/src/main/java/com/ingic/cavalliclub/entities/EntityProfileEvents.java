package com.ingic.cavalliclub.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EntityProfileEvents {

    @SerializedName("upcoming_events")
    @Expose
    private ArrayList<EntityUpcomingEvent> upcomingEvents ;
    @SerializedName("recent_events")
    @Expose
    private ArrayList<EntityUpcomingEvent> recentEvents;

    public ArrayList<EntityUpcomingEvent> getUpcomingEvents() {
        return upcomingEvents;
    }

    public void setUpcomingEvents(ArrayList<EntityUpcomingEvent> upcomingEvents) {
        this.upcomingEvents = upcomingEvents;
    }

    public ArrayList<EntityUpcomingEvent> getRecentEvents() {
        return recentEvents;
    }

    public void setRecentEvents(ArrayList<EntityUpcomingEvent> recentEvents) {
        this.recentEvents = recentEvents;
    }
}
