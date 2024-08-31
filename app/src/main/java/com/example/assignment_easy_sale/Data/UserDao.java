package com.example.assignment_easy_sale.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import com.example.assignment_easy_sale.Classes.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);
    @Insert (onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(List<User> users);

    @Query("SELECT * FROM user")
    LiveData<List<User>> getAllUsers();  // Return LiveData<List<User>>

    @Delete
    void delete(User user);

    @Update
    void update(User user);


}

