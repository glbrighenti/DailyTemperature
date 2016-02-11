package com.gbrighen.dailytemperature;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WeekCardAdapter extends RecyclerView.Adapter<WeekCardAdapter.WeekViewHolder> {


    private ArrayList<DayInfo> daysInfoList;

    public WeekCardAdapter(ArrayList<DayInfo> info){
        this.daysInfoList=info;
    }

    @Override
    public WeekViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.card_layout, parent, false);

        return new WeekViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeekViewHolder holder, int position) {
        DayInfo dayInfo = daysInfoList.get(position);
        holder.mDayName.setText(dayInfo.getName());
        holder.mDayTemperature.setText(String.format("%.1f", dayInfo.getTemperature()));
        holder.mUnit.setText(getTempAbbreviation(dayInfo.getCelsius()));
        holder.mDayImage.setImageDrawable(dayInfo.getImage());
        //TODO holder.mUnit
    }

    @Override
    public int getItemCount() {
        return daysInfoList.size();
    }

    public void updateData(ArrayList<DayInfo> al){
        this.daysInfoList=al;
        notifyDataSetChanged();
    }

    public void updateAmbientTemperature(float temp){

    }
    public static class WeekViewHolder extends RecyclerView.ViewHolder {

        protected TextView mDayName;
        protected TextView mDayTemperature;
        protected ImageView mDayImage;
        protected TextView mUnit;

        public WeekViewHolder(View v) {
            super(v);
            mDayName =  (TextView) v.findViewById(R.id.text_day_name);
            mDayTemperature = (TextView)  v.findViewById(R.id.text_day_temp);
            mDayImage = (ImageView)  v.findViewById(R.id.image_day);
            mUnit = (TextView) v.findViewById(R.id.text_day_unit);
        }
    }

    private String getTempAbbreviation(boolean isCelsius){
        if(isCelsius) {
            return "°C";
        }
        else{
            return "°F";
        }
    }

}
