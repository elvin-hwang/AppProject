package com.example.grace.appproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAlarm, btnWeather, btnAdd;
    private ScrollView sv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnAlarm = (Button) findViewById(R.id.btnAlarm);
        btnWeather = (Button) findViewById(R.id.btnWeather);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        sv = (ScrollView) findViewById(R.id.sv);

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
}
