package com.rasbet.backend.Entities;

import java.util.ArrayList;
import java.util.List;

public class HistoryBets {
    private Float winPercentage;
    private List<Bet> bets;

    public HistoryBets(Float winPercentage, List<Bet> bets) {
        this.winPercentage = winPercentage;
        this.setBets(bets);
    }

    // basic getters
    public Float getWinPercentage() {
        return winPercentage;
    }

    public List<Bet> getBets() {
        List<Bet> res = new ArrayList<>();

        for (Bet bet : this.bets) {
            res.add(bet.clone());
        }

        return bets;
    }

    // basic setters
    public void setWinPercentage(Float winPercentage) {
        this.winPercentage = winPercentage;
    }

    public void setBets(List<Bet> bets) {
        this.bets = new ArrayList<>();

        for (Bet bet : bets) {
            this.bets.add(bet.clone());
        }
    }


    // calculate percentage
    public void calculateWPercentage() {
        int wins = 0;

        for (Bet bet : this.bets) {
            if (bet.getBetState().equals("Win")) wins++;
        }
        this.winPercentage = (float) (wins / this.bets.size()) * 100;
    }
}
