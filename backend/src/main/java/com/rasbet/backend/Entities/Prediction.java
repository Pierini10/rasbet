package com.rasbet.backend.Entities;

public class Prediction {
    private String prediction;
    private Float odd;
    private String event;
    private String betState;

    public Prediction(String prediction, Float odd, String event, String betState) {
        this.prediction = prediction;
        this.odd = odd;
        this.event = event;
        this.betState = betState;
    }

    //basic getters
    public String getPrediction() {
        return prediction;
    }

    public Float getOdd() {
        return odd;
    }

    public String getEvent() {
        return event;
    }

    public String getBetState() {
        return betState;
    }

    //basic setters
    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }

    public void setOdd(Float odd) {
        this.odd = odd;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setIdBetState(String betState) {
        this.betState = betState;
    }


    //clone
    public Prediction clone() {
        return new Prediction(prediction, odd, event, betState);
    }
}
