package com.example.studybuddy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studybuddy.database.StudyBuddyDatabase;
import com.example.studybuddy.database.dao.ReminderDao;
import com.example.studybuddy.database.entities.Reminder;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReminderRepository {

    private ReminderDao reminderDao;
    private LiveData<List<Reminder>> allReminders;
    private ExecutorService executor;

    public ReminderRepository(Application application) {
        StudyBuddyDatabase db = StudyBuddyDatabase.getInstance(application);
        reminderDao  = db.reminderDao();
        allReminders = reminderDao.getAllReminders();
        executor     = Executors.newSingleThreadExecutor();
    }

    public void insert(Reminder reminder) {
        executor.execute(() -> reminderDao.insert(reminder));
    }

    public void delete(Reminder reminder) {
        executor.execute(() -> reminderDao.delete(reminder));
    }

    public LiveData<List<Reminder>> getAllReminders() {
        return allReminders;
    }
}