package com.example.grace.appproject;

import android.Manifest;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;


import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.grace.appproject.model.*;
import com.example.grace.appproject.Fragments.*;
import com.squareup.picasso.Picasso;

import org.json.JSONException;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private Button btnAlarm, btnWeather, btnTodo;
    private TextView city, temp, cond, maxMin;
    private ImageView icon;
    DbHelper dbHelper;

    static double lat, lng;
    int MY_PERMISSION = 0;
    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initLocation();
    }

    private void init() {
        btnAlarm = (Button) findViewById(R.id.btnAlarm);
        btnWeather = (Button) findViewById(R.id.btnWeather);
        btnTodo = (Button) findViewById(R.id.btnTodo);
        dbHelper = new DbHelper(this);

        city = (TextView) findViewById(R.id.city);
        temp = (TextView) findViewById(R.id.temp);
        cond = (TextView) findViewById(R.id.cond);
        maxMin = (TextView) findViewById(R.id.maxmin);
        icon = (ImageView) findViewById(R.id.icon);
    }

    public void onClick(View view) {
        Fragment fragment;
        FragmentManager fm;
        FragmentTransaction ft;
        switch (view.getId()) {
            case R.id.btnWeather:
                setButton(btnWeather);
                fragment = new WeatherFragment();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment_place,fragment);
                ft.commit();
                break;
            case R.id.btnAlarm:
                setButton(btnAlarm);
                fragment = new AlarmFragment();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment_place,fragment);
                ft.commit();
                break;
            case R.id.btnTodo:
                setButton(btnTodo);
                fragment = new TaskFragment();
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                ft.replace(R.id.fragment_place,fragment);
                ft.commit();
                break;
        }
    }

    private void setButton(Button btn) {
        btnTodo.setBackgroundColor(Color.parseColor("#80000000"));
        btnAlarm.setBackgroundColor(Color.parseColor("#80000000"));
        btnWeather.setBackgroundColor(Color.parseColor("#80000000"));
        btn.setBackgroundColor(Color.parseColor("#ffaaaaaa"));
    }

    private void initLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
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

            JSONWeatherTask task = new JSONWeatherTask();
            task.execute(new String[] {String.valueOf(lat), String.valueOf(lng)});
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{String.valueOf(lat),String.valueOf(lng)});
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
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
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            }, MY_PERMISSION);
        }
        locationManager.requestLocationUpdates(provider, 60000, 100, this);
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
            temp.setText((int) (weather.temperature.getTemp() - 273.15) + "°C");
            cond.setText(weather.currentCondition.getCondition() + " (" + weather.currentCondition.getDescr() + ")");
            maxMin.setText("high/low: "
                    + (int) (weather.temperature.getMaxTemp() - 273.15) + "°C/"
                    + (int) (weather.temperature.getMinTemp() - 273.15) + "°C");
            setImage(weather.currentCondition.getIcon());
        }

    }
    private void setImage(String i) {
        Picasso.with(this).load("http://openweathermap.org/img/w/"
                + i + ".png")
                .into(this.icon);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }
    @Override
    public void onProviderEnabled(String s) {

    }
    @Override
    public void onProviderDisabled(String s) {

    }
}
