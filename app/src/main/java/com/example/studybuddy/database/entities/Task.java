package com.example.studybuddy.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "is_completed")
    public boolean isCompleted;

    public Task(String title) {
        this.title = title;
        this.isCompleted = false;
    }

    public int getId()            { return id; }
    public String getTitle()      { return title; }
    public boolean isCompleted()  { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}