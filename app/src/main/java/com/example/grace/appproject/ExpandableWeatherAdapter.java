package com.example.grace.appproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grace.appproject.model.DayForecast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by hosun on 2018-09-01.
 */

public class ExpandableWeatherAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<DayForecast> data; // header titles
    // child data in format of header title, child title

    public ExpandableWeatherAdapter(Context context, ArrayList<DayForecast> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final DayForecast curr = data.get(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.weather_content, null);
        }

        TextView mornName = (TextView) convertView.findViewById(R.id.mornName);
        TextView mornMax = (TextView) convertView.findViewById(R.id.mornMax);
        TextView mornHumid = (TextView) convertView.findViewById(R.id.mornHumid);
        ImageView mornIcon = (ImageView) convertView.findViewById(R.id.mornIcon);


        TextView aftName = (TextView) convertView.findViewById(R.id.aftName);
        TextView aftMax = (TextView) convertView.findViewById(R.id.aftMax);
        TextView aftHumid = (TextView) convertView.findViewById(R.id.aftHumid);
        ImageView aftIcon = (ImageView) convertView.findViewById(R.id.aftIcon);


        TextView evenName = (TextView) convertView.findViewById(R.id.evenName);
        TextView evenMax = (TextView) convertView.findViewById(R.id.evenMax);
        TextView evenHumid = (TextView) convertView.findViewById(R.id.evenHumid);
        ImageView evenIcon = (ImageView) convertView.findViewById(R.id.evenIcon);


        mornName.setText(curr.weather.currentCondition.getMornDescr());
        mornMax.setText((int) (curr.forecastTemp.maxMorning - 273.15) + "°C");
        mornHumid.setText("%" + (int) curr.weather.currentCondition.getMornHumidity());

        aftName.setText(curr.weather.currentCondition.getAftDescr());
        aftMax.setText((int) (curr.forecastTemp.maxAfternoon - 273.15) + "°C");
        aftHumid.setText("%" + (int) curr.weather.currentCondition.getAftHumidity());

        evenName.setText(curr.weather.currentCondition.getEvenDescr());
        evenMax.setText((int) (curr.forecastTemp.maxEvening - 273.15) + "°C");
        evenHumid.setText("%" + (int) curr.weather.currentCondition.getEvenHumidity());

        Picasso.with(context).load("http://openweathermap.org/img/w/"
                + curr.weather.currentCondition.getMornIcon() + ".png")
                .into(mornIcon);
        Picasso.with(context).load("http://openweathermap.org/img/w/"
                + curr.weather.currentCondition.getAftIcon() + ".png")
                .into(aftIcon);
        Picasso.with(context).load("http://openweathermap.org/img/w/"
                + curr.weather.currentCondition.getEvenIcon() + ".png")
                .into(evenIcon);
        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final DayForecast curr = data.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.weather_header, null);
        }

        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(curr.getStringDate());

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
