package com.example.exchangeapp.Bean;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Rate extends RealmObject {
    @PrimaryKey
    private String country;
    private double value;
    private boolean isSelected;

    public Rate() {
        this.country = "";
        this.value = 0;
        this.isSelected = false;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
