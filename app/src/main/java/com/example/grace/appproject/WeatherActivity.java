package com.example.grace.appproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnAlarm, btnTodo;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        init();
    }

    private void init() {
        btnAlarm = (Button) findViewById(R.id.btnAlarm);
        btnTodo = (Button) findViewById(R.id.btnTodo);
        lv = (ListView) findViewById(R.id.lv);
        ArrayAdapter<String> mAdapter;
        ArrayList<String> add = new ArrayList<>();
        add.add("abc");
        mAdapter = new ArrayAdapter<String>(this, R.layout.weather_content, R.id.example, add);
        lv.setAdapter(mAdapter);

        btnTodo.setOnClickListener(this);
        btnAlarm.setOnClickListener(this);
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
}
