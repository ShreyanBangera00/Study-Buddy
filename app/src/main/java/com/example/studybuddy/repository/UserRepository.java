package com.example.studybuddy.repository;

import android.app.Application;

import com.example.studybuddy.database.StudyBuddyDatabase;
import com.example.studybuddy.database.dao.UserDao;
import com.example.studybuddy.database.entities.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private UserDao userDao;
    private ExecutorService executor;

    public UserRepository(Application application) {
        StudyBuddyDatabase db = StudyBuddyDatabase.getInstance(application);
        userDao  = db.userDao();
        executor = Executors.newSingleThreadExecutor();
    }

    public interface LoginCallback {
        void onResult(User user);
    }

    public interface EmailCheckCallback {
        void onResult(User user);
    }

    public interface InsertCallback {
        void onInserted();
    }

    // Insert and notify when done
    public void insertAndConfirm(User user, InsertCallback callback) {
        executor.execute(() -> {
            userDao.insert(user);
            callback.onInserted();
        });
    }

    public void login(String email, String password, LoginCallback callback) {
        executor.execute(() -> {
            User user = userDao.login(email.toLowerCase(), password);
            callback.onResult(user);
        });
    }

    public void getUserByEmail(String email, EmailCheckCallback callback) {
        executor.execute(() -> {
            User user = userDao.getUserByEmail(email.toLowerCase());
            callback.onResult(user);
        });
    }
}