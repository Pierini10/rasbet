package com.rasbet.backend.Entities;

public class OddSimple {
    private String entity;
    private double odd;

    public OddSimple() {
    }

    public OddSimple(String entity, double odd) {
        this.entity = entity;
        this.odd = odd;
    }

    public String getEntity() {
        return this.entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }

    public double getOdd() {
        return this.odd;
    }

    public void setOdd(double odd) {
        this.odd = odd;
    }

}
