package com.rasbet.backend.Entities;

import com.rasbet.backend.Database.PromotionDB;
import com.rasbet.backend.Exceptions.NoMinimumValueException;

public class Promotion {
    private double value;
    private double minValue;
    private int type;

    public Promotion(double value, double minValue, int type) {
        this.value = value;
        this.minValue = minValue;
        this.type = type;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getMinValue() {
        return this.minValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double calculatePromotionValue(double value) throws NoMinimumValueException {

        if (this.minValue <= value) {
            if (this.type == PromotionDB.ABSOLUTE_TYPE) {
                value += this.value;
            } else if (this.type == PromotionDB.PERCENTAGE_TYPE) {
                value += (value * this.value / 100);

            }
            return value;
        } else {
            throw new NoMinimumValueException("The minimum value for this promotion is " + this.minValue);
        }
    }

}
