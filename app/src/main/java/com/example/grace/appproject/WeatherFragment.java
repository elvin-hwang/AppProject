package com.example.grace.appproject;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.grace.appproject.model.DayForecast;
import com.example.grace.appproject.model.WeatherForecast;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment implements LocationListener{

    private View view;
    private Context context;

    private ExpandableListView lv;

    ExpandableWeatherAdapter nAdapter;

    LocationManager locationManager;
    String provider;
    static double lat, lng;
    int MY_PERMISSION = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weather, container, false);
        context = getActivity();
        init();
        initLocation();
        return view;
    }


    private void init() {
        lv = (ExpandableListView) view.findViewById(R.id.lv);
    }

    private void initLocation() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            ActivityCompat.requestPermissions(getActivity(), new String[]{
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

        nAdapter = new ExpandableWeatherAdapter(context, forecasts, contents);
        lv.setAdapter(nAdapter);
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        JSONForecastWeatherTask task1 = new JSONForecastWeatherTask();
        task1.execute(new String[] {String.valueOf(lat), String.valueOf(lng)});
    }

    @Override
    public void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
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
    public void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
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

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}
    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onProviderDisabled(String s) {}

}
