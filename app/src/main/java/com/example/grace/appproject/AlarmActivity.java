package com.example.grace.appproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAlarm, btnTodo, btnWeather;
    private ScrollView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
    }

    private void init() {
        btnAlarm = (Button) findViewById(R.id.btnAlarm);
        btnTodo = (Button) findViewById(R.id.btnTodo);
        btnWeather = (Button) findViewById(R.id.btnWeather);
        sv = (ScrollView) findViewById(R.id.sv);

        btnTodo.setOnClickListener(this);
        btnAlarm.setOnClickListener(this);
        btnWeather.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent myIntent;
        switch (view.getId()) {
            case R.id.btnTodo:
                myIntent = new Intent(view.getContext(), MainActivity.class);
                startActivity(myIntent);
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
}
