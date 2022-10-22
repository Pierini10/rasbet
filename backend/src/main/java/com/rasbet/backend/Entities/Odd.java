package com.rasbet.backend.Entities;

public class Odd {
    private String entity;
    private double odd;
    private boolean OddSup;

    public Odd(String entity, double odd, boolean Odd_Sup) {
        this.entity = entity;
        this.odd = odd;
        this.OddSup = Odd_Sup;
    }


    // Getters and Setters
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

    public boolean isOdd_Sup() {
        return this.OddSup;
    }

    public boolean getOdd_Sup() {
        return this.OddSup;
    }

    public void setOdd_Sup(boolean Odd_Sup) {
        this.OddSup = Odd_Sup;
    }


}
