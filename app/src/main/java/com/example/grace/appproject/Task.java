package com.example.grace.appproject;

/**
 * Created by hosun on 2018-08-27.
 */

public class Task {
    String title;
    String location;
    String date;
    String time;
    String priority;

    public Task(String t, String l, String d, String ti, String p) {
        title = t;
        location = l;
        date = d;
        time = ti;
        priority = p;
    }

    public String getTitle() {
        return title;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPriority() {
        return priority;
    }

}
