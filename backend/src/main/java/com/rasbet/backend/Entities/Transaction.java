package com.rasbet.backend.Entities;

public class Transaction {
    private String description;
    private Double value;
    private Double balenceAfterTran;
    private String date;
    private String time;

    public Transaction(String description, Double value, Double balenceAfterTran, String date, String time) {
        this.description = description;
        this.value = value;
        this.balenceAfterTran = balenceAfterTran;
        this.date = date;
        this.time = time;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return this.value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getBalenceAfterTran() {
        return this.balenceAfterTran;
    }

    public void setBalenceAfterTran(Double balenceAfterTran) {
        this.balenceAfterTran = balenceAfterTran;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}