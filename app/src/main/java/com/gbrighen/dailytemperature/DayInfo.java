package com.gbrighen.dailytemperature;

import android.graphics.drawable.Drawable;

import com.gbrighen.dailytemperature.temperatures.ITemperature;

/**
 * This class has information for each day object on Activity.
 * It contains all the information to be displayed in the card view like:
 * Unit is currently being used (Celsius or Farenheit),
 * Day temperature_old,
 * Day Image
 * Day Name
 */
public class DayInfo {
    private String name;
    private Drawable image;
    private boolean isCelsius;
    private ITemperature temperature;

    public boolean isCelsiusUnit() {
        return isCelsius;
    }

    public void setIsCelsiusUnit(boolean celsius) {
        this.isCelsius = celsius;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public ITemperature getTemperature() {
        return temperature;
    }

    public void setTemperature(ITemperature temperature) {
        this.temperature = temperature;
    }

    public String getName() { return this.name; }

    public void setName(String name) {
        this.name = name;
    }
}
