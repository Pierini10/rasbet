package com.rasbet.backend.Entities;

public class Prediction {
    private String prediction;
    private Float odd;
    private String event;
    private String idEvent;
    private String betState;

    public Prediction(String prediction, Float odd, String event, String idEvent, String betState) {
        this.prediction = prediction;
        this.odd = odd;
        this.event = event;
        this.idEvent = idEvent;
        this.betState = betState;
    }

    public Prediction() {
    }

    // basic getters
    public String getPrediction() {
        return prediction;
    }

    public Float getOdd() {
        return odd;
    }

    public String getEvent() {
        return event;
    }

    public String getIdEvent() {
        return idEvent;
    }

    public String getBetState() {
        return betState;
    }

    // basic setters
    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public void setOdd(Float odd) {
        this.odd = odd;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    public void setBetState(String betState) {
        this.betState = betState;
    }

    // clone
    public Prediction clone() {
        return new Prediction(prediction, odd, event, idEvent, betState);
    }
}
