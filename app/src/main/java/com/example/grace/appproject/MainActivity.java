package com.example.grace.appproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAlarm, btnWeather, btnAdd;
    private ListView lv;
    DbHelper dbHelper;
    ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadTaskList();
    }

    private void init() {
        btnAlarm = (Button) findViewById(R.id.btnAlarm);
        btnWeather = (Button) findViewById(R.id.btnWeather);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        lv = (ListView) findViewById(R.id.lv);
        dbHelper = new DbHelper(this);

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
        ArrayList<String> taskList = dbHelper.getTaskList();
        if(mAdapter==null){
            mAdapter = new ArrayAdapter<String>(this,R.layout.row,R.id.task_title,taskList);
            lv.setAdapter(mAdapter);
        }
        else{
            mAdapter.clear();
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
                    String task = data.getStringExtra("result");
                    dbHelper.insertNewTask(task);
                    loadTaskList();
                }
                break;
            }
        }
    }



    public void deleteTask(View view){
        View parent = (View)view.getParent();
        TextView taskTextView = (TextView)parent.findViewById(R.id.task_title);
        Log.e("String", (String) taskTextView.getText());
        String task = String.valueOf(taskTextView.getText());
        dbHelper.deleteTask(task);
        loadTaskList();
    }
}
