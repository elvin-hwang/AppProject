package com.example.grace.appproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.LightingColorFilter;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.grace.appproject.model.Weather;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private Button btnAlarm, btnWeather, btnAdd;
    private ListView lv;
    private TextView city, temp, cond, maxMin;
    private ImageView icon;
    DbHelper dbHelper;
    TaskAdapter mAdapter;

    static double lat, lng;
    int MY_PERMISSION = 0;
    LocationManager locationManager;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadTaskList();
        initLocation();
    }

    private void init() {
        btnAlarm = (Button) findViewById(R.id.btnAlarm);
        btnWeather = (Button) findViewById(R.id.btnWeather);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        lv = (ListView) findViewById(R.id.lv);
        dbHelper = new DbHelper(this);

        city = (TextView) findViewById(R.id.city);
        temp = (TextView) findViewById(R.id.temp);
        cond = (TextView) findViewById(R.id.cond);
        maxMin = (TextView) findViewById(R.id.maxmin);
        icon = (ImageView) findViewById(R.id.icon);

        btnAdd.setOnClickListener(this);
        btnAlarm.setOnClickListener(this);
        btnWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent myIntent;
        switch (view.getId()) {
            case R.id.btnAdd:
                myIntent = new Intent(MainActivity.this, PopActivity.class);
                startActivityForResult(myIntent,1);
                break;
            case R.id.btnWeather:
                myIntent = new Intent(view.getContext(), WeatherActivity.class);
                startActivity(myIntent);
                break;
            case R.id.btnAlarm:
                myIntent = new Intent(view.getContext(), AlarmActivity.class);
                startActivity(myIntent);
                break;
        }
    }

    private void loadTaskList() {
        ArrayList<Task> taskList = dbHelper.getTaskList();
        if(mAdapter==null){
            mAdapter = new TaskAdapter(MainActivity.this,R.layout.row, taskList);
            mAdapter.sort(new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    return task.compareTo(t1);
                }
            });
            lv.setAdapter(mAdapter);
            Collections.sort(taskList);
            mAdapter.notifyDataSetChanged();
        }
        else{
            mAdapter.clear();
            Collections.sort(taskList);
            mAdapter.addAll(taskList);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (1) : {
                if (resultCode == Activity.RESULT_OK) {
                    String[] task = data.getStringArrayExtra("result");
                    dbHelper.insertNewTask(task[0], task[1], task[2], task[3], task[4]);
                    loadTaskList();
                }
                break;
            }
        }
    }

    public void exitDialog(View view) {
        final View v = view;
        CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        checkBox.setChecked(false);
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                //set icon
                .setIcon(R.drawable.checkmark)

                //set title
                .setTitle("You Completed a Task!")
                //set message
                .setMessage("Would you like to remove this task?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteTask(v);
                    }
                })
                //set negative button
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
        alertDialog.getWindow().getDecorView().getBackground().setColorFilter(new LightingColorFilter(0xFF000000, 0xffc9b8d1));
    }

    public void deleteTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.task_title);
        Log.e("String", (String) taskTextView.getText());
        String task = String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTaskList();
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
