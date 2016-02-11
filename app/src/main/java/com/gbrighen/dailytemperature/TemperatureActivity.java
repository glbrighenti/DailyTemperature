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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

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
        ArrayList<DayInfo> al=createList();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        WeekCardAdapter cardAdapter = new WeekCardAdapter(al);
        recyclerView.setAdapter(cardAdapter);



        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toggle betweetn celsius and fahrenheit
                isCelsius=!isCelsius;

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

    private ArrayList<DayInfo> createList(){
        ArrayList<DayInfo> al = new ArrayList<DayInfo>();
        String[] namesList = getResources().getStringArray(R.array.days_names_array);
        DayInfo day;
        for(int i=0;i<5;i++) {
            day = new DayInfo();
            day.setName(namesList[i]);
            day.setTemperature(getRandomTemperature());
            day.setImage(getDrawable(getRandonImage()));
            al.add(day);
        }
        return al;
    }

    private float getRandomTemperature(){
        Random rand = new Random();
        float min = -20.0f;
        float max = 50.0f;
        float randonTemp = rand.nextFloat() * (max - min) + min;
        return randonTemp;
    }

    private int getRandonImage(){

        int max=5;
        Random rand = new Random();
        int randonTemp = rand.nextInt(max);
        switch (randonTemp){
            case 0:
                return R.drawable.cloudy;
            case 1:
                return R.drawable.sunny;
            default:
                return R.drawable.sunny;
        }


    }
}
