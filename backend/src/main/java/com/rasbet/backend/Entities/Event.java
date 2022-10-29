package com.rasbet.backend.Entities;

import java.util.Map;

public class Event {
    private String id;
    private String sport;
    private String state;
    private String datetime;
    private String description;
    private String result;
    private Map<String, Odd> odds;


    public Event(String id, String sport, String datetime, String description, String result, String state, Map<String, Odd> odds) {
        this.id = id;
        this.sport = sport;
        this.datetime = datetime;
        this.description = description;
        this.result = result;
        this.state = state;
        this.odds = odds;
    }


    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSport() {
        return this.sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Map<String, Odd> getOdds() {
        return this.odds;
    }

    public Odd getOdd(String entity) {
        return this.odds.get(entity);
    }

    public void setOdds(Map<String, Odd> odds) {
        this.odds = odds;
    }


}
