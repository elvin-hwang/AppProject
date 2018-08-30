package com.example.grace.appproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnAlarm, btnWeather, btnAdd;
    private ListView lv;
    DbHelper dbHelper;
    TaskAdapter mAdapter;

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
