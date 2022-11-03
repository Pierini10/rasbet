package com.rasbet.backend.Entities;

import java.util.List;

public class UpdateOddRequest {
    private int userID;
    private List<EventOdds> possibleBets;

    public UpdateOddRequest() {
    }

    public UpdateOddRequest(int userID, List<EventOdds> possibleBets) {
        this.userID = userID;
        this.possibleBets = possibleBets;
    }

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public List<EventOdds> getPossibleBets() {
        return this.possibleBets;
    }

    public void setPossibleBets(List<EventOdds> possibleBets) {
        this.possibleBets = possibleBets;
    }
}
