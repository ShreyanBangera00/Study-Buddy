package com.example.studybuddy.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.studybuddy.database.dao.ReminderDao;
import com.example.studybuddy.database.dao.TaskDao;
import com.example.studybuddy.database.dao.UserDao;
import com.example.studybuddy.database.entities.Reminder;
import com.example.studybuddy.database.entities.Task;
import com.example.studybuddy.database.entities.User;

@Database(entities = {Task.class, Reminder.class, User.class}, version = 1, exportSchema = false)
public abstract class StudyBuddyDatabase extends RoomDatabase {

    private static StudyBuddyDatabase instance;

    public abstract TaskDao taskDao();
    public abstract ReminderDao reminderDao();
    public abstract UserDao userDao();

    // Singleton - only one instance of the database exists at a time
    public static synchronized StudyBuddyDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    StudyBuddyDatabase.class,
                    "studybuddy_database"
            ).fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}