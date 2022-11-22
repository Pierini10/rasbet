package com.rasbet.backend.Entities;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class Event {
    private String id;
    private String sport;
    private String competition;
    private String state;
    private LocalDateTime datetime;
    private String description;
    private String result;
    private Map<String, Odd> odds;

    public Event(String id, String sport, String competition, LocalDateTime datetime, String description, String result, String state,
            Map<String, Odd> odds) {
        if (id == null)
            id = generateId(description, datetime.toString());
        this.id = id;
        this.sport = sport;
        this.competition = competition;
        this.datetime = datetime;
        setDescription(description);
        this.result = result;
        this.state = state;
        if (odds == null) {
            String[] r = description.split(" v ", 2);
            odds = new HashMap<>();
            odds.put(r[0], new Odd(r[0], -1, false));
            odds.put(r[1], new Odd(r[1], -1, false));
            odds.put("Draw", new Odd("Draw", -1, false));
        }
        this.odds = odds;
    }

    public Event(String id, String description, String result) {
        this.id = id;
        this.sport = null;
        this.competition = null;
        this.datetime = null;
        setDescription(description);
        this.result = result;
        this.state = null;
        this.odds = null;
    }

    private static String generateId(String des, String date) {
        String id = "";
        char[] de = des.toCharArray();
        for (int i = 0; i < des.length(); i++)
            id += de[i] + 10;

        char[] da = date.toCharArray();
        for (int i = 0; i < date.length(); i++)
            id += da[i] + 10;

        return id;
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

    public String getCompetition() {
        return this.competition;
    }

    public void setCompetition(String competition) {
        this.competition = competition;
    }

    public String getState() {
        return this.state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getDatetime() {
        return this.datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        if (Pattern.compile("^.+[ ]v[ ].+$").matcher(description).matches())
            this.description = description;
        else
            throw new IllegalArgumentException("Event Description is not valid");
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
