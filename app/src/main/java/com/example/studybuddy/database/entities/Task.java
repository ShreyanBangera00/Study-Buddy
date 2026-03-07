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

    @ColumnInfo(name = "user_email")
    public String userEmail;

    public Task(String title, String userEmail) {
        this.title      = title;
        this.userEmail  = userEmail;
        this.isCompleted = false;
    }

    public int getId()            { return id; }
    public String getTitle()      { return title; }
    public boolean isCompleted()  { return isCompleted; }
    public String getUserEmail()  { return userEmail; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}