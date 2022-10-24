package com.rasbet.backend.Entities;

import java.util.List;

public class UpdateOddRequest {
    private String userID;
    List<EventOdds> possibleBets;

    public UpdateOddRequest(String userID, List<EventOdds> possibleBets) {
        this.userID = userID;
        this.possibleBets = possibleBets;
    }

    public String getUserID() {
        return this.userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public List<EventOdds> getPossibleBets() {
        return this.possibleBets;
    }

    public void setPossibleBets(List<EventOdds> possibleBets) {
        this.possibleBets = possibleBets;
    }
}
