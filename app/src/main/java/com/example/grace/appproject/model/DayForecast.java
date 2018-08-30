package com.example.grace.appproject.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by hosun on 2018-08-30.
 */

public class DayForecast {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public Weather weather = new Weather();
    public ForecastTemp forecastTemp = new ForecastTemp();
    public long timestamp;

    public class ForecastTemp {
        public float day;
        public float min;
        public float max;
        public float night;
        public float eve;
        public float morning;

        public float getDay() {
            return day;
        }

        public void setDay(float day) {
            this.day = day;
        }
    }

    public String getStringDate() {
        return sdf.format(new Date(timestamp));
    }
}
