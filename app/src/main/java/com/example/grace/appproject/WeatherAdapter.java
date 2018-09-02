package com.example.grace.appproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grace.appproject.model.DayForecast;
import com.squareup.picasso.Picasso;

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
            holder.mornHumid = (TextView) row.findViewById(R.id.mornHumid);
            holder.mornIcon = (ImageView) row.findViewById(R.id.mornIcon);

            holder.aftName = (TextView) row.findViewById(R.id.aftName);
            holder.aftMax = (TextView) row.findViewById(R.id.aftMax);
            holder.aftHumid = (TextView) row.findViewById(R.id.aftHumid);
            holder.aftIcon = (ImageView) row.findViewById(R.id.aftIcon);

            holder.evenName = (TextView) row.findViewById(R.id.evenName);
            holder.evenMax = (TextView) row.findViewById(R.id.evenMax);
            holder.evenHumid = (TextView) row.findViewById(R.id.evenHumid);
            holder.evenIcon = (ImageView) row.findViewById(R.id.evenIcon);

            row.setTag(holder);

        } else {
            holder = (ViewHolder)row.getTag();
        }

        DayForecast curr = data.get(position);

        holder.dateText.setText(curr.getStringDate());
        holder.mornName.setText(curr.weather.currentCondition.getMornDescr());
        holder.mornMax.setText((int) (curr.forecastTemp.maxMorning - 273.15) + "°C");
        holder.mornHumid.setText("%" + (int) curr.weather.currentCondition.getMornHumidity());

        holder.aftName.setText(curr.weather.currentCondition.getAftDescr());
        holder.aftMax.setText((int) (curr.forecastTemp.maxAfternoon - 273.15) + "°C");
        holder.aftHumid.setText("%" + (int) curr.weather.currentCondition.getAftHumidity());

        holder.evenName.setText(curr.weather.currentCondition.getEvenDescr());
        holder.evenMax.setText((int) (curr.forecastTemp.maxEvening - 273.15) + "°C");
        holder.evenHumid.setText("%" + (int) curr.weather.currentCondition.getEvenHumidity());


        Picasso.with(context).load("http://openweathermap.org/img/w/"
                + curr.weather.currentCondition.getMornIcon() + ".png")
                .into(holder.mornIcon);
        Picasso.with(context).load("http://openweathermap.org/img/w/"
                + curr.weather.currentCondition.getAftIcon() + ".png")
                .into(holder.aftIcon);
        Picasso.with(context).load("http://openweathermap.org/img/w/"
                + curr.weather.currentCondition.getEvenIcon() + ".png")
                .into(holder.evenIcon);

        return row;
    }

    static class ViewHolder
    {
        TextView dateText, mornName, mornMax, mornHumid,
                aftName, aftMax, aftHumid, evenName, evenMax, evenHumid;
        ImageView mornIcon, aftIcon, evenIcon;
    }
}
