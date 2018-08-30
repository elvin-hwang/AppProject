package com.example.grace.appproject;


/**
 * Created by hosun on 2018-08-27.
 */

public class Task implements Comparable<Task>{
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

    public int getPriorityInt() {
        if (priority.equals("low")) return 1;
        if (priority.equals("med")) return 2;
        if (priority.equals("high")) return 3;
        return 0;

    }

    @Override
    public int compareTo(Task task) {
        if (getPriorityInt() < task.getPriorityInt()) return 1;
        else return -1;

//        return getPriority().compareTo(task.getPriority());
    }
}
