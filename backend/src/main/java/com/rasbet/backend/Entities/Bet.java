package com.rasbet.backend.Entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Bet {
    private Integer id;
    private Integer idUser;
    private String betState;
    private Integer gamesLeft;
    private Float amount;
    private LocalDateTime dateTime;
    private Float totalOdds;
    private List<Prediction> predictions;

    public Bet(Integer id, Integer idUser, String betState, Integer gamesLeft, Float amount,
            LocalDateTime dateTime, Float totalOdds, List<Prediction> predictions) {
        this.id = id;
        this.idUser = idUser;
        this.betState = betState;
        this.gamesLeft = gamesLeft;
        this.amount = amount;
        this.dateTime = dateTime;
        this.totalOdds = totalOdds;
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

    public Integer getGamesLeft() {
        return gamesLeft;
    }

    public Float getAmount() {
        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Float getTotalOdds() {
        return totalOdds;
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

    public void setGamesLeft(Integer gamesLeft) {
        this.gamesLeft = gamesLeft;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setTotalOdds(Float totalOdds) {
        this.totalOdds = totalOdds;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = new ArrayList<>();

        for (Prediction prediction : predictions) {
            this.predictions.add(prediction.clone());
        }
    }

    public void update_info(Integer id, Integer idUser, String betState, Integer gamesLeft, Float amount,
            LocalDateTime dateTime) {
        if (id != null)
            this.id = id;
        if (idUser != null)
            this.idUser = idUser;
        if (betState != null)
            this.betState = betState;
        if (gamesLeft != null)
            this.gamesLeft = gamesLeft;
        if (amount != null)
            this.amount = amount;
        if (dateTime != null)
            this.dateTime = dateTime;
        if (predictions != null)
            this.setPredictions(predictions);
    }

    //clone
    public Bet clone() {
        return new Bet(id, idUser, betState, gamesLeft, amount, dateTime, totalOdds, this.getPredictions());
    }


    public void calculateTotalOdds() {
        this.totalOdds = (float) 1;
        for (Prediction prediction : this.predictions) {
            this.totalOdds *= prediction.getOdd();
        }
    }
}
