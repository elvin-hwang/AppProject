package com.example.grace.appproject.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by hosun on 2018-08-30.
 */

public class DayForecast {

    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
    private static SimpleDateFormat df = new SimpleDateFormat("E");
    Calendar now = Calendar.getInstance();
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

        public float minTemp;
        public float maxTemp;


    }

    public String getStringDate() {
        sdf.setTimeZone(TimeZone.getTimeZone(now.getTimeZone().getDisplayName()));
        return sdf.format(new Date(timestamp));
    }

    public String getStringDay() {
        df.setTimeZone(TimeZone.getTimeZone(now.getTimeZone().getDisplayName()));
        String day = df.format(new Date(timestamp));
        return getDay(day);
    }


    private String getDay(String day) {
        switch (day) {
            case "Sun":
                return "Sunday";
            case "Mon":
                return "Monday";
            case "Tue":
                return "Tuesday";
            case "Wed":
                return "Wednesday";
            case "Thu":
                return "Thursday";
            case "Fri":
                return "Friday";
            default:
                return "Saturday";
        }
    }
}
