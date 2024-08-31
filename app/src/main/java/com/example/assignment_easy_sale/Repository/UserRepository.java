package com.example.assignment_easy_sale.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.assignment_easy_sale.Data.UserDao;
import com.example.assignment_easy_sale.Classes.User;
import com.example.assignment_easy_sale.Data.UserDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private final UserDao userDao;
    private final ExecutorService executorService;


    public UserRepository(Application application) {

        UserDatabase database = UserDatabase.getDatabase(application.getApplicationContext());
        this.userDao = database.userDao();
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<User>> getAllUsers() {
        return userDao.getAllUsers();
    }

    public void insertUsers(List<User> users) {
        executorService.execute(() -> userDao.insertUsers(users));
    }

    public void deleteUser(User user) {
        executorService.execute(() -> userDao.delete(user));
    }

    public void updateUser(User user) {
        executorService.execute(() -> userDao.update(user));
    }

    public void insertUser(User user) {
        executorService.execute(() -> userDao.insertUser(user));
    }
}