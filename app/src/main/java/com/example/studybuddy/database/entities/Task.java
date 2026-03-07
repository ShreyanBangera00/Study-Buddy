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

    @ColumnInfo(name = "due_date")
    public String dueDate;

    @ColumnInfo(name = "priority")
    public String priority; // "High", "Medium", "Low"

    public Task(String title, String userEmail) {
        this.title       = title;
        this.userEmail   = userEmail;
        this.isCompleted = false;
        this.dueDate     = "";
        this.priority    = "Medium";
    }

    public int getId()            { return id; }
    public String getTitle()      { return title; }
    public boolean isCompleted()  { return isCompleted; }
    public String getUserEmail()  { return userEmail; }
    public String getDueDate()    { return dueDate; }
    public String getPriority()   { return priority; }

    public void setCompleted(boolean completed) { isCompleted = completed; }
    public void setDueDate(String dueDate)      { this.dueDate = dueDate; }
    public void setPriority(String priority)    { this.priority = priority; }
}