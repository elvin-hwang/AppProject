package com.example.grace.appproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment implements View.OnClickListener{
    private View view;
    private Button btnAdd;
    private Context context;

    private ListView lv;
    DbHelper dbHelper;
    TaskAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_task, container, false);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(p);
        context = getActivity();
        init();
        loadTaskList();
        return view;
    }

    private void init() {
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        lv = (ListView) view.findViewById(R.id.lv);
        dbHelper = new DbHelper(context);

        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent myIntent = new Intent(context, PopActivity.class);
        startActivityForResult(myIntent, 1);
    }


    private void loadTaskList() {
        ArrayList<Task> taskList = dbHelper.getTaskList();
        if(mAdapter==null){
            mAdapter = new TaskAdapter(context,R.layout.row, taskList);
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
        AlertDialog alertDialog = new AlertDialog.Builder(context)
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

}
