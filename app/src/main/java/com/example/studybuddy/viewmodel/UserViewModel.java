package com.example.studybuddy.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.studybuddy.database.entities.User;
import com.example.studybuddy.repository.UserRepository;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;

    public UserViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);
    }

    public void insertAndConfirm(User user, UserRepository.InsertCallback callback) {
        repository.insertAndConfirm(user, callback);
    }

    public void login(String email, String password, UserRepository.LoginCallback callback) {
        repository.login(email, password, callback);
    }

    public void getUserByEmail(String email, UserRepository.EmailCheckCallback callback) {
        repository.getUserByEmail(email, callback);
    }
}