package com.example.grace.appproject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by hosun on 2018-08-30.
 */

public class WeatherHttpClient {

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static String API_KEY = "&APPID=a65b6dcdd25a97b4d5ad3f56afe309a6";
    private static String IMG_URL = "http://openweathermap.org/img/w/";


    private static String BASE_FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?";


    public String getWeatherData(String latitude, String longitude) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {
            String add = String.format("lat=%s&lon=%s%s",latitude,longitude, API_KEY);
            String url = BASE_URL + add;

            con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while (  (line = br.readLine()) != null )
                buffer.append(line + "\r\n");

            is.close();
            con.disconnect();

            return buffer.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }


    public String getForecastWeatherData(String latitude, String longitude) {
        HttpURLConnection con = null ;
        InputStream is = null;

        try {

            // Forecast
            String add = String.format("lat=%s&lon=%s%s",latitude,longitude, API_KEY);
            String url = BASE_FORECAST_URL + add;

            con = (HttpURLConnection) ( new URL(url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer1 = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(is));
            String line1 = null;
            while (  (line1 = br1.readLine()) != null )
                buffer1.append(line1 + "\r\n");

            is.close();
            con.disconnect();

            return buffer1.toString();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }

}

