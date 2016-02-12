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

    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {
        final int pos=position;
        DayInfo dayInfo = daysInfoList.get(position);
        holder.mDayName.setText(dayInfo.getName());
        holder.mDayTemperature.setText(String.format("%.1f", dayInfo.getTemperature()));
        holder.mUnit.setText(getTempAbbreviation(dayInfo.getCelsius()));
        holder.mDayImage.setImageDrawable(dayInfo.getImage());
        ///button that will trigger the conversion
        holder.mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FloatingActionButton buttonInList=(FloatingActionButton) view;
                if(daysInfoList.get(pos).getCelsius()){
                    buttonInList.setImageDrawable(iconFahrenheit);
                    daysInfoList.get(pos).setCelsius(false);
                    daysInfoList.get(pos).setTemperature(temperaturesFahrenheitList[pos]);
                }
                else{
                    buttonInList.setImageDrawable(iconCelsius);
                    daysInfoList.get(pos).setCelsius(true);
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

    private String getTempAbbreviation(boolean isCelsius) {
        if (isCelsius) {
            return "°C";
        } else {
            return "°F";
        }
    }

}
