package com.rasbet.backend.Entities;

public class Transaction {
    private String date;
    private String description;
    private Double quantity;
    private Double balenceAfterTran;

    // TODO: we need to add hour and change transiction type to a string?
    public Transaction(String date, String description, Double quantity, Double balenceAfterTran) {
        this.date = date;
        this.description = description;
        this.quantity = quantity;
        this.balenceAfterTran = balenceAfterTran;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getBalenceAfterTran() {
        return this.balenceAfterTran;
    }

    public void setBalenceAfterTran(Double balenceAfterTran) {
        this.balenceAfterTran = balenceAfterTran;
    }

}
