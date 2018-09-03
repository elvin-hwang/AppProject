package com.example.grace.appproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grace.appproject.model.DayForecast;
import com.example.grace.appproject.model.Weather;
import com.example.grace.appproject.model.WeatherForecast;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private Button btnAlarm, btnTodo;
    private ExpandableListView lv;
    private TextView city, temp, cond;
    private ImageView icon;
    ExpandableWeatherAdapter nAdapter;

    LocationManager locationManager;
    String provider;
    static double lat, lng;
    int MY_PERMISSION = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        init();
        initLocation();
    }

    private void init() {
        btnAlarm = (Button) findViewById(R.id.btnAlarm);
        btnTodo = (Button) findViewById(R.id.btnTodo);

        city = (TextView) findViewById(R.id.city);
        temp = (TextView) findViewById(R.id.temp);
        cond = (TextView) findViewById(R.id.cond);
        icon = (ImageView) findViewById(R.id.icon);

        lv = (ExpandableListView) findViewById(R.id.lv);

        btnTodo.setOnClickListener(this);
        btnAlarm.setOnClickListener(this);
    }

    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) {
            lat = 49.2796;
            lng = 122.7985;

            JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
            task1.execute(new String[] {String.valueOf(lat), String.valueOf(lng)});
        }
    }

    @Override
    public void onClick(View view) {
        Intent myIntent;
        switch (view.getId()) {
            case R.id.btnTodo:
                myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivity(myIntent);
                break;
            case R.id.btnAlarm:
                myIntent = new Intent(view.getContext(), AlarmActivity.class);
                startActivity(myIntent);
                break;
        }
    }


    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0], params[1]));

            try {
                weather = JSONWeatherParser.getWeather(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }


        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            city.setText(weather.location.getCity() + ", " + weather.location.getCountry());
            temp.setText(Math.round((weather.temperature.getTemp() - 275.15)) + "°C");
            cond.setText(weather.currentCondition.getCondition() + " (" + weather.currentCondition.getDescr() + ")");
            setImage(weather.currentCondition.getIcon());
        }

    }
    private void setImage(String i) {
        Picasso.with(this).load("http://openweathermap.org/img/w/"
                + i + ".png")
                .into(this.icon);
    }


    private class JSONForecastWeatherTask extends AsyncTask<String, Void, WeatherForecast> {

        @Override
        protected WeatherForecast doInBackground(String... params) {

            String data = ((new WeatherHttpClient()).getForecastWeatherData(params[0], params[1]));
            WeatherForecast forecast = new WeatherForecast();
            try {
                forecast = JSONWeatherParser.getForecastWeather(data);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return forecast;

        }


        @Override
        protected void onPostExecute(WeatherForecast forecastWeather) {
            super.onPostExecute(forecastWeather);

            loadTaskList(forecastWeather.getForecasts());
        }
    }


    private void loadTaskList(ArrayList<DayForecast> forecasts) {
        HashMap<DayForecast, ArrayList<DayForecast>> contents = new HashMap<>();
        ArrayList<DayForecast> dayOne = new ArrayList<>();
        ArrayList<DayForecast> dayTwo = new ArrayList<>();
        ArrayList<DayForecast> dayThree = new ArrayList<>();
        ArrayList<DayForecast> dayFour = new ArrayList<>();
        dayOne.add(forecasts.get(0));
        dayTwo.add(forecasts.get(1));
        dayThree.add(forecasts.get(2));
        dayFour.add(forecasts.get(3));

        contents.put(forecasts.get(0), dayOne);
        contents.put(forecasts.get(1), dayTwo);
        contents.put(forecasts.get(2), dayThree);
        contents.put(forecasts.get(3), dayFour);

        nAdapter = new ExpandableWeatherAdapter(this, forecasts, contents);
        lv.setAdapter(nAdapter);
    }


    // TODO LocationListener
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();


        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{String.valueOf(lat),String.valueOf(lng)});

        JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
        task1.execute(new String[] {String.valueOf(lat), String.valueOf(lng)});
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        locationManager.removeUpdates(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}
    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onProviderDisabled(String s) {}
}
