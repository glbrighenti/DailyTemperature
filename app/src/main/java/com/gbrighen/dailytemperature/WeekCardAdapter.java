package com.gbrighen.dailytemperature;

import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Implementation of Recycler view Adapter used for displaying CardView (each day of the week)
 * This adapter is pretty much a standard implementation of a custom adapter where you need to
 * override the onCreateViewHolder to initialize elements used on the custom view and
 * onBindViewHolder to fill the data for each element inside the view.
 * In this case the elemets that needs to filled are the data contained in the DaysInfo.
 * For this the constructor received a ArrayList with all the DaysInfo already filled.
 * The Celsius and Fahreheint temperatures (converted beforehand using NDK) list is also available
 * Keeping 2 list altought might use more moemry provides a much fasted implementation since we
 * dont need to reclaculate the temperature conversion for each time the user presses the button.
 * Instead we just fetch the data from the filled lists temperaturesCelsiusList and
 * temperaturesFahrenheitList
 */
public class WeekCardAdapter extends RecyclerView.Adapter<WeekCardAdapter.WeekViewHolder> {

    private ArrayList<DayInfo> daysInfoList;
    private float[] temperaturesCelsiusList ;
    private float[] temperaturesFahrenheitList;
    private Drawable iconCelsius, iconFahrenheit;

    public WeekCardAdapter(ArrayList<DayInfo> info,float[] tempF,float[] tempC) {
        this.daysInfoList = info;
        this.temperaturesCelsiusList=tempC;
        this.temperaturesFahrenheitList =tempF;
    }

    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);
        iconFahrenheit=parent.getContext().getDrawable(R.drawable.temp_fahrenheit);
        iconCelsius=parent.getContext().getDrawable(R.drawable.temp_celsius);

        return new WeekViewHolder(view);
    }

    /**
     * This method will render all the cardviews, using the appropriate data.
     * Here we set the listeners for each button. this will trigger the conversion which is simply
     * fetch the data that had been processed already before creating this adapter.
     * After we change the data on the appropriate position we need to notifyDataSetChanged to cause
     * android to render the cards again, updating the temperature and the button
     * @param holder ViewHolder that understands our custom design
     * @param position position in the list
     */
    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {
        final int pos=position;
        DayInfo dayInfo = daysInfoList.get(position);
        holder.mDayName.setText(dayInfo.getName());
        holder.mDayTemperature.setText(String.format("%.1f", dayInfo.getTemperature()));
        holder.mUnit.setText(getTempAbbreviation(dayInfo.isCelsiusUnit()));
        holder.mDayImage.setImageDrawable(dayInfo.getImage());
        if(daysInfoList.get(pos).isCelsiusUnit()){
            holder.mFab.setImageDrawable(iconCelsius);
        }
        else {
            holder.mFab.setImageDrawable(iconFahrenheit);
        }
        ///button that will trigger the conversion
        holder.mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton buttonInList=(FloatingActionButton) view;
                if(daysInfoList.get(pos).isCelsiusUnit()){
                    daysInfoList.get(pos).setIsCelsiusUnit(false);
                    daysInfoList.get(pos).setTemperature(temperaturesFahrenheitList[pos]);
                }
                else{
                    daysInfoList.get(pos).setIsCelsiusUnit(true);
                    daysInfoList.get(pos).setTemperature(temperaturesCelsiusList[pos]);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return daysInfoList.size();
    }

    public void updateData(ArrayList<DayInfo> al,float[] tempF,float[] tempC) {
        this.daysInfoList = al;
        this.temperaturesCelsiusList=tempC;
        this.temperaturesFahrenheitList =tempF;
        notifyDataSetChanged();
    }


    public static class WeekViewHolder extends RecyclerView.ViewHolder {

        protected TextView mDayName;
        protected TextView mDayTemperature;
        protected ImageView mDayImage;
        protected TextView mUnit;
        protected FloatingActionButton mFab;



        public WeekViewHolder(View v) {
            super(v);
            mDayName = (TextView) v.findViewById(R.id.text_day_name);
            mDayTemperature = (TextView) v.findViewById(R.id.text_day_temp);
            mDayImage = (ImageView) v.findViewById(R.id.image_day);
            mUnit = (TextView) v.findViewById(R.id.text_day_unit);
            mFab = (FloatingActionButton) v.findViewById(R.id.fab);
        }
    }

    /**
     * Get unit abbreviation
     * @param isCelsius inform which unit we want to fetch
     * @return string with appropriate abbreviation
     */
    private String getTempAbbreviation(boolean isCelsius) {
        if (isCelsius) {
            return "°C";
        } else {
            return "°F";
        }
    }

}
