package com.example.grace.appproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by hosun on 2018-08-26.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME ="EHDev";
    private static final int DB_VER = 5;
    public static final String DB_TABLE= "Tasks";
    public static final String DB_COLUMN1 = "Task";
    public static final String DB_COLUMN2 = "com.example.grace.appproject.Location";
    public static final String DB_COLUMN3 = "Date";
    public static final String DB_COLUMN4 = "Time";
    public static final String DB_COLUMN5 = "Priority";


    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);",
                                    DB_TABLE,DB_COLUMN1, DB_COLUMN2, DB_COLUMN3, DB_COLUMN4, DB_COLUMN5);
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = String.format("DROP TABLE IF EXISTS %s", DB_TABLE);
        db.execSQL(query);
        onCreate(db);
    }

    // TODO maybe remove after project
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertNewTask(String title, String location, String date, String time, String priority){
        SQLiteDatabase db= this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_COLUMN1, title);
        values.put(DB_COLUMN2, location);
        values.put(DB_COLUMN3, date);
        values.put(DB_COLUMN4, time);
        values.put(DB_COLUMN5, priority);
        db.insertWithOnConflict(DB_TABLE,null,values,SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    // TODO - must fix: tasks with same title are all deleted together
    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_TABLE,DB_COLUMN1 + " = ?",new String[]{task});
        db.close();
    }

    public ArrayList<Task> getTaskList(){
        ArrayList<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DB_TABLE,new String[]{DB_COLUMN1, DB_COLUMN2, DB_COLUMN3, DB_COLUMN4, DB_COLUMN5},null,null,null,null,null);
        while(cursor.moveToNext()){
            Task t = new Task(cursor.getString(cursor.getColumnIndex(DB_COLUMN1)),
                    cursor.getString(cursor.getColumnIndex(DB_COLUMN2)),
                    cursor.getString(cursor.getColumnIndex(DB_COLUMN3)),
                    cursor.getString(cursor.getColumnIndex(DB_COLUMN4)),
                    cursor.getString(cursor.getColumnIndex(DB_COLUMN5)));
            taskList.add(t);
        }
        cursor.close();
        db.close();
        return taskList;
    }
}