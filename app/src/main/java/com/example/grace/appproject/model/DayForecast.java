package com.example.grace.appproject.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hosun on 2018-08-30.
 */

public class DayForecast {

    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd, E");
    public Weather weather = new Weather();
    public ForecastTemp forecastTemp = new ForecastTemp();
    public long timestamp;

    public class ForecastTemp {

        public float minMorning;
        public float maxMorning;

        public float minAfternoon;
        public float maxAfternoon;

        public float minEvening;
        public float maxEvening;


    }

    public String getStringDate() {
        return sdf.format(new Date(timestamp));
    }
}
