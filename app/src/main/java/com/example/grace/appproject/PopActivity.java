package com.example.grace.appproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DecimalFormat;
import java.util.Calendar;

public class PopActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnEnter, btnCancel, timebtn, datebtn;
    private EditText title, location;
    private RadioButton off, low, med, high;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop);

        initScreen();
        init();
        initDate();
        initTime();

    }
    private void initScreen() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels*0.8);
        int height = (int) (dm.heightPixels*0.6);

        getWindow().setLayout(width, height);
    }

    private void init() {
        off = (RadioButton) findViewById(R.id.off);
        low = (RadioButton) findViewById(R.id.low);
        med = (RadioButton) findViewById(R.id.medium);
        high = (RadioButton) findViewById(R.id.high);
        timebtn = (Button) findViewById(R.id.tvTime);
        datebtn = (Button) findViewById(R.id.tvDate);
        btnEnter = (Button) findViewById(R.id.btnEnter);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setPaintFlags(btnCancel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        title = (EditText) findViewById(R.id.title);
        location = (EditText) findViewById(R.id.location);

    }

    @Override
    public void onClick(View view) {
        Intent myIntent;
        switch (view.getId()) {
            case R.id.btnEnter:
                String state = getPriority();
                myIntent = new Intent();
                myIntent.putExtra("result",new String[] {title.getText().toString(), location.getText().toString(),
                                                                datebtn.getText().toString(), timebtn.getText().toString(),
                                                                state});
                setResult(Activity.RESULT_OK,myIntent);
                finish();
                break;
            case R.id.btnCancel:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;
        }
    }

    private void initDate() {
        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                DatePickerDialog datePickerDialog;
                datePickerDialog = new DatePickerDialog(PopActivity.this, AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                datebtn.setText("Date: " + (monthOfYear + 1) + "-"
                                        + dayOfMonth + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });
    }

    private void initTime() {
        timebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PopActivity.this, AlertDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                    String format;
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        if (selectedHour == 0) {
                            selectedHour += 12;
                            format = "AM";
                        } else if (selectedHour == 12) {
                            format = "PM";
                        } else if (selectedHour > 12) {
                            selectedHour -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }

                        DecimalFormat formatter = new DecimalFormat("00");
                        String sHour = formatter.format(selectedHour);
                        String sMin = formatter.format(selectedMinute);
                        timebtn.setText(sHour + ":" + sMin + " " + format);
                    }
                }, hour, minute, false);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
    }

    public void checkPriority(View view) {

        off.setChecked(false);
        low.setChecked(false);
        med.setChecked(false);
        high.setChecked(false);

        switch (view.getId()) {
            case R.id.off:
                off.setChecked(true);
                break;
            case R.id.low:
                low.setChecked(true);
                break;
            case R.id.medium:
                med.setChecked(true);
                break;
            case R.id.high:
                high.setChecked(true);
                break;
        }
    }

    private String getPriority() {
        if (low.isChecked()) return "low";
        if (med.isChecked()) return "med";
        if (high.isChecked()) return "high";
        return "off";
    }
}

