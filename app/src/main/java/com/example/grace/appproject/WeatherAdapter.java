package com.example.grace.appproject;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grace.appproject.model.DayForecast;

import java.util.ArrayList;

/**
 * Created by hosun on 2018-08-30.
 */

public class WeatherAdapter extends ArrayAdapter<DayForecast> {
    private final Context context;
    private final ArrayList<DayForecast> data;
    private final int layoutResourceId;

    public WeatherAdapter(@NonNull Context context, int layoutResourceId, ArrayList<DayForecast> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.dateText = (TextView) row.findViewById(R.id.date);
            holder.mornName = (TextView) row.findViewById(R.id.mornName);
            holder.mornMax = (TextView) row.findViewById(R.id.mornMax);
            holder.mornMin = (TextView) row.findViewById(R.id.mornMin);
            holder.mornPrecip = (TextView) row.findViewById(R.id.mornPrecip);
            holder.mornIcon = (ImageView) row.findViewById(R.id.mornIcon);


            holder.aftName = (TextView) row.findViewById(R.id.aftName);
            holder.aftMax = (TextView) row.findViewById(R.id.aftMax);
            holder.aftMin = (TextView) row.findViewById(R.id.aftMin);
            holder.aftPrecip = (TextView) row.findViewById(R.id.aftPrecip);
            holder.aftIcon = (ImageView) row.findViewById(R.id.aftIcon);

            holder.evenName = (TextView) row.findViewById(R.id.evenName);
            holder.evenMax = (TextView) row.findViewById(R.id.evenMax);
            holder.evenMin = (TextView) row.findViewById(R.id.evenMin);
            holder.evenPrecip = (TextView) row.findViewById(R.id.evenPrecip);
            holder.evenIcon = (ImageView) row.findViewById(R.id.evenIcon);



            row.setTag(holder);

        } else {
            holder = (ViewHolder)row.getTag();
        }

        DayForecast weather = data.get(position);

//        holder.textTitle.setText(task.getTitle());
//        holder.textLocation.setText(task.getLocation());
//        holder.textDate.setText(task.getDate());
//        holder.textTime.setText(task.getTime());

        return row;
    }

    static class ViewHolder
    {
        TextView dateText, mornName, mornMax, mornMin, mornPrecip,
                aftName, aftMax, aftMin, aftPrecip, evenName, evenMax, evenMin, evenPrecip;
        ImageView mornIcon, aftIcon, evenIcon;
    }

}
