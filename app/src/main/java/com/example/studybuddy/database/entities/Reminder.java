package com.example.studybuddy.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "reminders")
public class Reminder {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "time")
    public String time;

    public Reminder(String title, String time) {
        this.title = title;
        this.time  = time;
    }

    public int getId()       { return id; }
    public String getTitle() { return title; }
    public String getTime()  { return time; }
}