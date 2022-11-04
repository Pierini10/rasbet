package com.rasbet.backend.Entities;

import java.util.ArrayList;
import java.util.List;

public class Bet {
    private Integer id;
    private Integer idUser;
    private String betState;
    private Float amount;
    private String dateTime;
    private Float totalOdds;
    private Integer gamesLeft;
    private List<Prediction> predictions;

    public Bet(Integer id, Integer idUser, String betState, Float amount, String dateTime, Float totalOdds,
            Integer gamesLeft, List<Prediction> predictions) {
        this.id = id;
                this.idUser = idUser;
        this.betState = betState;
        this.amount = amount;
        this.dateTime = dateTime;
        this.totalOdds = totalOdds;
        this.gamesLeft = gamesLeft;
        this.setPredictions(predictions);
    }

    // basic getters

    public Integer getId() {
        return id;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public String getBetState() {
        return betState;
    }

    public Float getAmount() {
        return amount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public Float getTotalOdds() {
        return totalOdds;
    }

    public Integer getGamesLeft() {
        return gamesLeft;
    }

    public List<Prediction> getPredictions() {
        List<Prediction> res = new ArrayList<>();

        for (Prediction prediction : this.predictions) {
            res.add(prediction.clone());
        }

        return res;
    }

    // basic setters

    public void setId(Integer id) {
        this.id = id;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public void setBetState(String betState) {
        this.betState = betState;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setTotalOdds(Float totalOdds) {
        this.totalOdds = totalOdds;
    }

    public void setGamesLeft(Integer gamesLeft) {
        this.gamesLeft = gamesLeft;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = new ArrayList<>();

        for (Prediction prediction : predictions) {
            this.predictions.add(prediction.clone());
        }
    }

    // clone
    public Bet clone() {
        return new Bet(id, idUser, betState, amount, dateTime, totalOdds, gamesLeft, this.getPredictions());
    }

    public void calculateTotalOdds() {
        this.totalOdds = (float) 1;
        for (Prediction prediction : this.predictions) {
            this.totalOdds *= prediction.getOdd();
        }
    }
}
