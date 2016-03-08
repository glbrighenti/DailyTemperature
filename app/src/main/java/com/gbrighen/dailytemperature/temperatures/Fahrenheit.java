package com.gbrighen.dailytemperature.temperatures;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.gbrighen.dailytemperature.R;

public class Fahrenheit implements ITemperature {

    private Context context;
    private Float value;


    public Fahrenheit(Context context, Float value) {
        this.context=context;
        this.value=value;
    }

    @Override
    public String getAbbreviation() {
        return context.getString(R.string.abbreviation_fahrenheit);
    }

    @Override
    public String getName() {
        return context.getString(R.string.label_fahrenheit);
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
        return context.getResources().getDrawable(R.drawable.temp_fahrenheit);
    }

    @Override
    public TYPE getType() {
        return TYPE.FAHRENHEIT;
    }

}
