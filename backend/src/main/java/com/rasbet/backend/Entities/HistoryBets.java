package com.rasbet.backend.Entities;

import java.util.ArrayList;
import java.util.List;

import com.rasbet.backend.Database.BetDB;

public class HistoryBets {
    private float winPercentage;
    private List<Bet> bets;

    public HistoryBets(List<Bet> bets) {
        this.setBets(bets);
        this.calculateWPercentage();
    }

    // basic getters
    public float getWinPercentage() {
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
    public void setWinPercentage(float winPercentage) {
        this.winPercentage = winPercentage;
    }

    public void setBets(List<Bet> bets) {
        this.bets = new ArrayList<>();

        for (Bet bet : bets) {
            this.bets.add(bet.clone());
        }
    }

    // calculate percentage
    private void calculateWPercentage() {
        float wins = 0;

        if (this.bets.size() != 0) {
            for (Bet bet : this.bets) {
                if (bet.getBetState().equals(BetDB.WIN_STATUS)) {
                    wins++;
                }
                this.winPercentage = (float) (wins / this.bets.size()) * 100;
            }

        }
    }
}
