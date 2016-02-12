package com.gbrighen.dailytemperature;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Random;

public class TemperatureActivity extends AppCompatActivity implements SensorEventListener {
    //Loading .so library that was compiled before using ndk-build
    static {
        System.loadLibrary("conversion_module");
    }

    //native methods that are defined in the conversion module
    private native float[] convertToFahrenheit(float t[]);

    private SensorManager sensorManager;
    private Sensor tempSensor;
    private float currentTemperature = 0;
    private ArrayList<DayInfo> daysList;
    private float[] temperaturesCelsiusList ;
    private float[] temperaturesFahrenheitList;
    private WeekCardAdapter cardAdapter;
    private long previusUpdate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        //populating fake temperature list in celsius, and already calculate its fahrenheits equivalents for each element
        daysList = createList();

        //setup UI components
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        cardAdapter = new WeekCardAdapter(daysList,temperaturesFahrenheitList,temperaturesCelsiusList);
        recyclerView.setAdapter(cardAdapter);


        //init sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

    }

    /*
    Methods implemented from SensorEventListener. this callbacks will provide the sensor data once it is available, or updated.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //allowing only .5 C increment to avoid too much unnecessary update
        if(Math.abs(currentTemperature-sensorEvent.values[0])<1){
            return;
        }

        currentTemperature = sensorEvent.values[0]; //this data will always come as Celsius
        temperaturesCelsiusList[0]=currentTemperature;
        temperaturesFahrenheitList=convertToFahrenheit(temperaturesCelsiusList);
        if (!daysList.get(0).getCelsius()) {
            currentTemperature = temperaturesFahrenheitList[0];
        }
        daysList.get(0).setTemperature(currentTemperature);
        cardAdapter.updateData(daysList,temperaturesFahrenheitList,temperaturesCelsiusList);


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
        sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    /*
    Helper methods to fake the weather data

     */
    private ArrayList<DayInfo> createList() {
        ArrayList<DayInfo> al = new ArrayList<DayInfo>();
        String[] namesList = getResources().getStringArray(R.array.days_names_array);
        temperaturesCelsiusList = getRandomTemperature();
        temperaturesFahrenheitList = convertToFahrenheit(temperaturesCelsiusList);

        DayInfo day;

        for (int i = 0; i < 6; i++) {
            day = new DayInfo();
            day.setName(namesList[i]);
            day.setTemperature(temperaturesCelsiusList[i]);
            day.setImage(getDrawable(getRandomImage(i)));
            day.setCelsius(true);
            al.add(day);
        }
        return al;
    }
    //fills an array with random  temperature value
    private  float[] getRandomTemperature() {
        float[] tempList = new float[6];
        Random rand = new Random();
        float min = -20.0f; //using believable temperatures
        float max = 50.0f;
        for(int i=0;i<6;i++){
            tempList[i]=(rand.nextFloat() * (max - min) + min);
        }
        return tempList;
    }

    private int getRandomImage(int i) {
        if(i==0){
            return R.drawable.device;
        }
        int max = 4;
        Random rand = new Random();
        int randomTemp = rand.nextInt(max);
        switch (randomTemp) {
            case 0:
                return R.drawable.cloudy;
            case 1:
                return R.drawable.sunny;
            case 2:
                return R.drawable.snowy;
            case 3:
                return R.drawable.thunderstorm;
            default:
                return R.drawable.sunny;
        }


    }
}
