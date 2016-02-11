package com.gbrighen.dailytemperature;

import android.graphics.drawable.Drawable;

public class DayInfo {
    private String name;
    private Float temperature;
    private Drawable image;
    private boolean isCelsius;

    public boolean getCelsius() {
        return isCelsius;
    }

    public void setCelsius(boolean celsius) {
        this.isCelsius = celsius;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
