package com.example.studybuddy.viewmodel;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.AndroidViewModel;

import com.example.studybuddy.database.entities.Task;
import com.example.studybuddy.repository.TaskRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    private TaskRepository repository;

    public TaskViewModel(Application application) {
        super(application);
        repository = new TaskRepository(application);
    }

    public void insert(Task task)  { repository.insert(task); }
    public void delete(Task task)  { repository.delete(task); }
    public void update(Task task)  { repository.update(task); }

    public LiveData<List<Task>> getTasksForUser(String userEmail) {
        return repository.getTasksForUser(userEmail);
    }
}