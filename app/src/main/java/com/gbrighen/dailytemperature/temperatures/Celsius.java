package com.gbrighen.dailytemperature.temperatures;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.gbrighen.dailytemperature.R;

public class Celsius implements ITemperature {

    private Context context;
    private Float value;


    public Celsius(Context context,Float value) {
        this.context=context;
        this.value=value;
    }

    @Override
    public String getAbbreviation() {
        return context.getString(R.string.abbreviation_celsius);
    }

    @Override
    public String getName() {
        return context.getString(R.string.label_celsius);
    }

    @Override
    public Float getValue() {
        return value;
    }

    @Override
    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public Drawable getIcon() {
        return context.getResources().getDrawable(R.drawable.temp_celsius);
    }
}
