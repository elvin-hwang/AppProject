package com.example.grace.appproject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;


/**
 * Created by hosun on 2018-08-27.
 */

public class TaskAdapter extends ArrayAdapter<Task> {
    private final Context context;
    private final ArrayList<Task> data;
    private final int layoutResourceId;

    public TaskAdapter(Context context, int layoutResourceId, ArrayList<Task> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.data = data;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ViewHolder();
            holder.textTitle = (TextView)row.findViewById(R.id.task_title);
            holder.textLocation = (TextView)row.findViewById(R.id.task_location);
            holder.textDate = (TextView)row.findViewById(R.id.task_date);
            holder.textTime = (TextView)row.findViewById(R.id.task_time);
            holder.priority = (ImageView) row.findViewById(R.id.priority);

            row.setTag(holder);

        } else {
            holder = (ViewHolder)row.getTag();
        }

        Task task = data.get(position);

        holder.textTitle.setText(task.getTitle());
        holder.textLocation.setText(task.getLocation());
        holder.textDate.setText(task.getDate());
        holder.textTime.setText(task.getTime());
        setPriority(holder, task.getPriority());

        return row;
    }

    static class ViewHolder
    {
        TextView textTitle, textLocation, textDate, textTime;
        ImageView priority;
    }

    private void setPriority(ViewHolder holder, String s) {
        switch (s) {
            case "low":
                //holder.priority.setImageBitmap();
                break;
            case "med":
                //holder.priority.setBackgroundResource(R.drawable.orange_icon);
                break;
            case "high":
                //holder.priority.setBackgroundResource(R.drawable.red_icon);
                break;
            default:
                break;
        }
    }
}