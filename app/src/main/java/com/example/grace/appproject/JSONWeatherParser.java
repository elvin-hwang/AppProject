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

    public static Weather getWeather(String data) throws JSONException {
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
        String firstDay = form.format(d);

        int index = 0;

        WeatherForecast forecast = new WeatherForecast();

        // We create out JSONObject from the data
        JSONObject jObj = new JSONObject(data);

        JSONArray jArr = jObj.getJSONArray("list");
        // We traverse all the array and parse the data
        for (int i=0; i < jArr.length(); i++) {
            JSONObject jDayForecast = jArr.getJSONObject(i);

            long parseDate = jDayForecast.getLong("dt")*1000;
            SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

            //dayFormat.setTimeZone(TimeZone.getTimeZone(now.getTimeZone().getDisplayName()));
            String date = dayFormat.format(new Date(parseDate));
            if (date.equals(firstDay)) continue;
            index = i;
            break;
        }
        
        for (int i = index; i < index + 4*8; i += 8) {
            DayForecast df = new DayForecast();
            JSONObject jDayForecast = jArr.getJSONObject(i);
            long parseDate = jDayForecast.getLong("dt")*1000;
            df.timestamp = parseDate;
            getMinMax(jArr, i, df);

            jDayForecast = jArr.getJSONObject(i + 2);
            setMorning(jDayForecast, jDayForecast.getJSONObject("main"), df);

            jDayForecast = jArr.getJSONObject(i + 4);
            setAfterNoon(jDayForecast, jDayForecast.getJSONObject("main"), df);

            jDayForecast = jArr.getJSONObject(i + 6);
            setEvening(jDayForecast, jDayForecast.getJSONObject("main"), df);

            forecast.addForecast(df);
        }

        return forecast;
    }

    private static void setMorning(JSONObject jDayForecast, JSONObject jTempObj, DayForecast df) throws JSONException {
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
    private static void setAfterNoon(JSONObject jDayForecast, JSONObject jTempObj, DayForecast df) throws JSONException {
        df.forecastTemp.minAfternoon = (float) jTempObj.getDouble("temp_min");
        df.forecastTemp.maxAfternoon = (float) jTempObj.getDouble("temp_max");

        df.weather.currentCondition.setAftHumidity((float) jTempObj.getDouble("humidity"));

        JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
        JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
        df.weather.currentCondition.setAftDescr(getString("description", jWeatherObj));
        df.weather.currentCondition.setAftIcon(getString("icon", jWeatherObj));

    }
    private static void setEvening(JSONObject jDayForecast, JSONObject jTempObj, DayForecast df) throws JSONException {
        df.forecastTemp.minEvening = (float) jTempObj.getDouble("temp_min");
        df.forecastTemp.maxEvening = (float) jTempObj.getDouble("temp_max");

        df.weather.currentCondition.setEvenHumidity((float) jTempObj.getDouble("humidity"));

        JSONArray jWeatherArr = jDayForecast.getJSONArray("weather");
        JSONObject jWeatherObj = jWeatherArr.getJSONObject(0);
        df.weather.currentCondition.setEvenDescr(getString("description", jWeatherObj));
        df.weather.currentCondition.setEvenIcon(getString("icon", jWeatherObj));
    }
    private static void getMinMax(JSONArray jArr, int j, DayForecast df) throws JSONException {
        float minTemp = 10000;
        float maxTemp = 0;
        for (int i=j; i < j + 8; i++) {
            JSONObject jDayForecast = jArr.getJSONObject(i);
            JSONObject jTempObj = jDayForecast.getJSONObject("main");

            float min = (float) jTempObj.getDouble("temp_min");
            float max = (float) jTempObj.getDouble("temp_max");

            if (min < minTemp) minTemp = min;
            if (max > maxTemp) maxTemp = max;
        }
        df.forecastTemp.minTemp = minTemp;
        df.forecastTemp.maxTemp = maxTemp;
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
