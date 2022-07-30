package com.example.fyp_22_s22_21_mobile.support;

public class barChartData {

    int date;
    int value;

    public barChartData(int date, int value) {
        this.date = date;
        this.value = value;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public double getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
