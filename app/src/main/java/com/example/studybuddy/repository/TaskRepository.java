package com.example.studybuddy.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.studybuddy.database.StudyBuddyDatabase;
import com.example.studybuddy.database.dao.TaskDao;
import com.example.studybuddy.database.entities.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRepository {

    private TaskDao taskDao;
    private ExecutorService executor;

    public TaskRepository(Application application) {
        StudyBuddyDatabase db = StudyBuddyDatabase.getInstance(application);
        taskDao  = db.taskDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public void insert(Task task) {
        executor.execute(() -> taskDao.insert(task));
    }

    public void delete(Task task) {
        executor.execute(() -> taskDao.delete(task));
    }

    public void update(Task task) {
        executor.execute(() -> taskDao.update(task));
    }

    public LiveData<List<Task>> getTasksForUser(String userEmail) {
        return taskDao.getTasksForUser(userEmail);
    }
}