package com.example.grace.appproject;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grace.appproject.model.DayForecast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hosun on 2018-09-01.
 */

public class ExpandableWeatherAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<DayForecast> dataHeader; // header titles
    private HashMap<DayForecast ,ArrayList<DayForecast>> weatherContent;
    // child data in format of header title, child title

    public ExpandableWeatherAdapter(Context context, ArrayList<DayForecast> listDataHeader, HashMap<DayForecast, ArrayList<DayForecast>> listHashMap) {
        this.context = context;
        this.dataHeader = listDataHeader;
        this.weatherContent = listHashMap;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        DayForecast curr = (DayForecast) getChild(groupPosition,childPosition);

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
        DayForecast curr = dataHeader.get(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.weather_header, null);
        }

        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView day = (TextView) convertView.findViewById(R.id.day);
        TextView temp = (TextView) convertView.findViewById(R.id.temp);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);


        date.setText(curr.getStringDate());
        day.setText(curr.getStringDay());
        day.setTypeface(Typeface.DEFAULT_BOLD);
        temp.setText((int) (curr.forecastTemp.maxTemp - 273.15) + "°C/" + (int) (curr.forecastTemp.minTemp - 273.15) + "°C");

        Picasso.with(context).load("http://openweathermap.org/img/w/"
                + curr.weather.currentCondition.getAftIcon() + ".png")
                .into(icon);


        return convertView;
    }


    @Override
    public int getGroupCount() {
        return dataHeader.size();
    }
    @Override
    public int getChildrenCount(int i) {
        return weatherContent.get(dataHeader.get(i)).size();
    }
    @Override
    public Object getGroup(int i) {
        return dataHeader.get(i);
    }
    @Override
    public Object getChild(int i, int i1) {
        return weatherContent.get(dataHeader.get(i)).get(i1); // i = Group Item , i1 = ChildItem
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }
    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
