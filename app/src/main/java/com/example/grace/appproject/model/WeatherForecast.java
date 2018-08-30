package com.example.grace.appproject.model;

import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hosun on 2018-08-30.
 */

public class WeatherForecast {

    private ArrayList<DayForecast> daysForecast = new ArrayList<DayForecast>();

    public void addForecast(DayForecast forecast) {
        daysForecast.add(forecast);
        System.out.println("Add forecast ["+forecast+"]");
    }

    public DayForecast getForecast(int dayNum) {
        return daysForecast.get(dayNum);
    }

    public ArrayList<DayForecast> getForecasts() {
        return daysForecast;
    }
}
