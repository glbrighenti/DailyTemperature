package com.gbrighen.dailytemperature.temperatures;

import android.graphics.drawable.Drawable;

public interface ITemperature {

    String getAbbreviation();
    String getName();
    Float getValue();
    void setValue(Float f);
    Drawable getIcon();

}
