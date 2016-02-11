package com.gbrighen.dailytemperature;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class TemperatureActivity extends AppCompatActivity implements SensorEventListener {

    static {
        System.loadLibrary("conversion_module");
    }
    private native float convertToCelsius(float t);


    private final String SETTINGS_TEMPERATURE = "celsius";
    private SensorManager sensorManager;
    private Sensor tempSensor;
    private float currentTemperature = 0;
    private boolean isCelsius=true;
    private TextView textUnit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textUnit=(TextView)findViewById(R.id.text_monday_unit);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toggle betweetn celsius and fahrenheit
                isCelsius=!isCelsius;
                if(isCelsius){
                    textUnit.setText("a");

                }else {
                    textUnit.setText("b");
                }
            }
        });
    }

    /*
    Methods implemented from SensorEventListener. this callbacks will provide the sensor data once it is available, or updated.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        currentTemperature = sensorEvent.values[0];
        String title = String.format(getResources().getString(R.string.label_current_temperature), currentTemperature);
        getSupportActionBar().setTitle(title+getTempAbbreviation());
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /*
    Registering and unregistering the SensorManager events depending on activity lifecycle.
    We want the callbacks active when the activity is on top, and we don't need the sensor data once is goes to the background.
     */
    @Override
    protected void onResume() {
        super.onResume();
        isCelsius=isCelsiusSetting();
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setCelsiusSettings(isCelsius);
        sensorManager.unregisterListener(this);
    }

    /*
    When user set temperature preference we need to save it so when he comes back we can keep the last used settings
    For this simple preference SharedPreference is the best choice
    */
    private boolean isCelsiusSetting() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean(SETTINGS_TEMPERATURE, true);
    }

    private void setCelsiusSettings(boolean pref) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putBoolean(SETTINGS_TEMPERATURE, pref).commit();
    }

    private String getTempAbbreviation(){
        if(isCelsius) {
            return "°C";
        }
        else{
            return "°F";
        }
    }
}
