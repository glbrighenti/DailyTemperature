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

/**
 * Activity that controls the display for application
 * This activity will start by creating random temperature data, loading all UI elements, and
 * registering the Temperature Sensor listeners.
 * The data will be passed to a recyclerview that will take care of rendering the cardview
 * When creating the random temperature, a float array is created. The first position is the data
 * from the sensor(if available), and the rest are each day in order (Mon,Tue,Wed,Thu,Fri)
 * After random number are assigned we use NDK to convert this whole list to fahrenheit.
 *
 */
public class TemperatureActivity extends AppCompatActivity implements SensorEventListener {

    /*
     * Loading .so library that was compiled before using ndk-build.
     * Since we use the native methods on app startup we need the lib loaded immediately
     */

    static {
        System.loadLibrary("conversion_module");
    }


    //Native method that is defined in the conversion_module included above
    private native float[] convertToFahrenheit(float tempCelsius[]);
    private final int NUMBER_OF_CARDS=6;
    private SensorManager sensorManager;
    private Sensor tempSensor;
    private float currentTemperature = 0;
    private ArrayList<DayInfo> daysList;
    private float[] temperaturesCelsiusList ;
    private float[] temperaturesFahrenheitList;
    private WeekCardAdapter cardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);

        /**
         * Populating fake random temperature list in Celsius,
         * and already calculate its fahrenheit equivalent for each element
         */

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


        //initialize sensor
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

    }

    /*
     * Methods implemented from SensorEventListener.
     * These callbacks will provide the sensor data once it is available, or updated.
     */
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // not allowing small increments on temperature to avoid too many updates
        if(Math.abs(currentTemperature-sensorEvent.values[0])<1){
            return;
        }

        currentTemperature = sensorEvent.values[0]; //this data will always come as Celsius
        temperaturesCelsiusList[0]=currentTemperature;//new data from sensor, we need to update the celsius list
        temperaturesFahrenheitList=convertToFahrenheit(temperaturesCelsiusList);//also update fahrenheit list

        if (!daysList.get(0).isCelsiusUnit()) {//if user already changed ambient temperature to F, we need to pass correct data
            currentTemperature = temperaturesFahrenheitList[0];
        }
        //update UI
        daysList.get(0).setTemperature(currentTemperature);
        cardAdapter.updateData(daysList,temperaturesFahrenheitList,temperaturesCelsiusList);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //This callback is not used in our app, since we are not worried at this moment with the sensor accuracy.
        return;
    }

    /*
    * Registering and unregistering the SensorManager events depending on activity lifecycle.
    * We want the callbacks active when the activity is on top, and we don't need the sensor data once it goes to the background.
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

    //Helper methods to fake the random weather data

	/**
	 * Create a list with the week days and their temperature.
	 * Call the appropriate methods for random temperature generation and temperature conversion by NDK
     * @return a list of objects containing information for each day
	 */
    private ArrayList<DayInfo> createList() {
        ArrayList<DayInfo> al = new ArrayList<DayInfo>();
        String[] namesList = getResources().getStringArray(R.array.days_names_array);
        temperaturesCelsiusList = getRandomTemperature();
        temperaturesFahrenheitList = convertToFahrenheit(temperaturesCelsiusList); //Temperature conversion via NDK

        DayInfo day;

        for (int i = 0; i < NUMBER_OF_CARDS; i++) {
            day = new DayInfo();
            day.setName(namesList[i]);
            day.setTemperature(temperaturesCelsiusList[i]);
            day.setImage(getDrawable(getRandomImage(i)));
            day.setIsCelsiusUnit(true);
            al.add(day);
        }
        return al;
    }
    
    /**
     * Fills an array with random temperature value
     * @return an array of size NUMBER_OF_CARDS = 6 (5 week days + sensor temperature) with random temperature values ranging from -20C to 50C.
     */
    private  float[] getRandomTemperature() {
        float[] tempList = new float[NUMBER_OF_CARDS];
        Random rand = new Random();
        float min = -20.0f; //using believable temperatures
        float max = 50.0f;
        for(int i=0;i<NUMBER_OF_CARDS;i++){
            tempList[i]=(rand.nextFloat() * (max - min) + min);
        }
        return tempList;
    }

    /**
     * Populates the image attribute for each day randomly
     * @param i position on the list
	 * @return integer indicating the image to be loaded
     */
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
