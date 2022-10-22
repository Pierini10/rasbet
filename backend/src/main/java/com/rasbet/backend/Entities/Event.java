package com.rasbet.backend.Entities;

import java.util.ArrayList;

public class Event {
    private int id;
    private String sport;
    private String state;
    private String datetime;
    private String description;
    private String result;
    private ArrayList<Odd> odds;


    public Event(int id, String sport, String state, String datetime, String description, String result, ArrayList<Odd> odds) {
        this.id = id;
        this.sport = sport;
        this.state = state;
        this.datetime = datetime;
        this.description = description;
        this.result = result;
        this.odds = odds;
    }


    public int getId() {
        return this.id;
    }

    public void setId(int id) {
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

    public ArrayList<Odd> getOdds() {
        return this.odds;
    }

    public void setOdds(ArrayList<Odd> odds) {
        this.odds = odds;
    }


}
