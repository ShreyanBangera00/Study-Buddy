package com.example.studybuddy.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studybuddy.database.entities.Reminder;
import com.example.studybuddy.repository.ReminderRepository;

import java.util.List;

public class ReminderViewModel extends AndroidViewModel {

    private ReminderRepository repository;

    public ReminderViewModel(Application application) {
        super(application);
        repository = new ReminderRepository(application);
    }

    public void insert(Reminder reminder) { repository.insert(reminder); }
    public void delete(Reminder reminder) { repository.delete(reminder); }

    public LiveData<List<Reminder>> getRemindersForUser(String userEmail) {
        return repository.getRemindersForUser(userEmail);
    }
}