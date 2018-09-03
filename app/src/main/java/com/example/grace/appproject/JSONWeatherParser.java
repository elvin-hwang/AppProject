package com.example.grace.appproject;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.grace.appproject.model.DayForecast;
import com.example.grace.appproject.model.Location;
import com.example.grace.appproject.model.Weather;
import com.example.grace.appproject.model.WeatherForecast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by hosun on 2018-08-30.
 */

public class JSONWeatherParser {

    public static Weather getWeather(String data) throws JSONException  {
        Weather weather = new Weather();
        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        // We start extracting the info
        Location loc = new Location();

        JSONObject coordObj = getObject("coord", jObj);
        loc.setLatitude(getFloat("lat", coordObj));
        loc.setLongitude(getFloat("lon", coordObj));

        JSONObject sysObj = getObject("sys", jObj);
        loc.setCountry(getString("country", sysObj));
        loc.setSunrise(getInt("sunrise", sysObj));
        loc.setSunset(getInt("sunset", sysObj));
        loc.setCity(getString("name", jObj));
        weather.location = loc;

        // We get weather info (This is an array)
        JSONArray jArr = jObj.getJSONArray("weather");

        // We use only the first value
        JSONObject JSONWeather = jArr.getJSONObject(0);
        weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
        weather.currentCondition.setDescr(getString("description", JSONWeather));
        weather.currentCondition.setCondition(getString("main", JSONWeather));
        weather.currentCondition.setIcon(getString("icon", JSONWeather));

        JSONObject mainObj = getObject("main", jObj);
        weather.currentCondition.setHumidity(getInt("humidity", mainObj));
        weather.currentCondition.setPressure(getInt("pressure", mainObj));
        weather.temperature.setMaxTemp(getFloat("temp_max", mainObj));
        weather.temperature.setMinTemp(getFloat("temp_min", mainObj));
        weather.temperature.setTemp(getFloat("temp", mainObj));

        // Wind
        JSONObject wObj = getObject("wind", jObj);
        weather.wind.setSpeed(getFloat("speed", wObj));
        weather.wind.setDeg(getFloat("deg", wObj));

        // Clouds
        JSONObject cObj = getObject("clouds", jObj);
        weather.clouds.setPerc(getInt("all", cObj));


        return weather;
    }

    public static WeatherForecast getForecastWeather(String data) throws JSONException  {

        Calendar now = Calendar.getInstance();
        DecimalFormat form = new DecimalFormat("00");
        int d = now.get(Calendar.DAY_OF_MONTH);
        String day = form.format(d);
        String exit = form.format(d+5);


        float minTemp = 10000;
        float maxTemp = 0;


        DayForecast df = new DayForecast();
        WeatherForecast forecast = new WeatherForecast();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        JSONArray jArr = jObj.getJSONArray("list"); // Here we have the forecast for every day
        // We traverse all the array and parse the data
        for (int i=0; i < jArr.length(); i++) {
            JSONObject jDayForecast = jArr.getJSONObject(i);

            long parseDate = jDayForecast.getLong("dt")*1000;
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
            //dayFormat.setTimeZone(TimeZone.getTimeZone(now.getTimeZone().getDisplayName()));
            String date = dayFormat.format(new Date(parseDate));
            if (day.equals(date)) continue;
            if (date.equals(exit)) break;

            JSONObject jTempObj = jDayForecast.getJSONObject("main");
            float min = (float) jTempObj.getDouble("temp_min");
            float max = (float) jTempObj.getDouble("temp_max");
            if (min < minTemp) minTemp = min;
            if (max > maxTemp) maxTemp = max;

            // Now we have the json object so we can extract the data
            SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
            //hourFormat.setTimeZone(TimeZone.getTimeZone(now.getTimeZone().getDisplayName()));
            String hour = hourFormat.format(new Date(parseDate));
            boolean nineAM = hour.equals("08");
            boolean threePM = hour.equals("14");
            boolean ninePM = hour.equals("20");

            if (nineAM) {
                // Initialize DayForecast
                df.timestamp = parseDate;

                // Temp is an object
                df.forecastTemp.minMorning = (float) jTempObj.getDouble("temp_min");
                df.forecastTemp.maxMorning = (float) jTempObj.getDouble("temp_max");

                // Humidity
                df.weather.currentCondition.setMornHumidity((float) jTempObj.getDouble("humidity"));

                // ...and now the weather
                JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
                JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
                df.weather.currentCondition.setMornDescr(getString("description", jWeatherObj));
                df.weather.currentCondition.setMornIcon(getString("icon", jWeatherObj));
            }

            if (threePM) {
                df.forecastTemp.minAfternoon = (float) jTempObj.getDouble("temp_min");
                df.forecastTemp.maxAfternoon = (float) jTempObj.getDouble("temp_max");

                df.weather.currentCondition.setAftHumidity((float) jTempObj.getDouble("humidity"));

                JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
                JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
                df.weather.currentCondition.setAftDescr(getString("description", jWeatherObj));
                df.weather.currentCondition.setAftIcon(getString("icon", jWeatherObj));
            }
            if (ninePM) {
                df.forecastTemp.minEvening = (float) jTempObj.getDouble("temp_min");
                df.forecastTemp.maxEvening = (float) jTempObj.getDouble("temp_max");

                df.weather.currentCondition.setEvenHumidity((float) jTempObj.getDouble("humidity"));

                JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
                JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
                df.weather.currentCondition.setEvenDescr(getString("description", jWeatherObj));
                df.weather.currentCondition.setEvenIcon(getString("icon", jWeatherObj));

                df.forecastTemp.minTemp = minTemp;
                df.forecastTemp.maxTemp = maxTemp;

                forecast.addForecast(df);
                df = new DayForecast();
            }
        }



        return forecast;
    }


    private static JSONObject getObject(String tagName, JSONObject jObj)  throws JSONException {
        JSONObject subObj = jObj.getJSONObject(tagName);
        return subObj;
    }

    private static String getString(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getString(tagName);
    }

    private static float  getFloat(String tagName, JSONObject jObj) throws JSONException {
        return (float) jObj.getDouble(tagName);
    }

    private static int  getInt(String tagName, JSONObject jObj) throws JSONException {
        return jObj.getInt(tagName);
    }

}
