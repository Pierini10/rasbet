package com.rasbet.backend.Entities;

import java.util.List;

public class EventOdds {
    private String eventID;
    private List<OddSimple> odds;

    public EventOdds() {
    }

    public EventOdds(String eventID, List<OddSimple> odds) {
        this.eventID = eventID;
        this.odds = odds;
    }

    public String getEventID() {
        return this.eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public List<OddSimple> getOdds() {
        return this.odds;
    }

    public void setOdds(List<OddSimple> odds) {
        this.odds = odds;
    }
}
