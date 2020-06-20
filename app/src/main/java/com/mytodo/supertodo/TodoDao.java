package com.mytodo.supertodo;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TodoDao {

    @Insert
    void insert(Todo todo);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("SELECT * FROM todo where completed = 0 and user_id=:userId")
    List<Todo> getAllIncompleteForUser(int userId);

    @Query("SELECT * FROM todo where completed = 0 and user_id=:userId and category=:catValue")
    List<Todo> getAllinCategory(int catValue, int userId);

    @Query("SELECT * FROM todo where completed = 0 and user_id=:userId and category=:value")
    LiveData<List<Todo>> getAllinCategoryLive(int value, int userId);

    @Query("SELECT * FROM todo where completed = 0 and user_id=:userId and priority=:value")
    List<Todo> getAllinPriority(int value, int userId);

    @Query("SELECT * FROM todo where completed = 0 and user_id=:userId and priority=:value")
    LiveData<List<Todo>> getAllinPriorityLive(int value, int userId);

    @Query("SELECT * FROM todo where completed = 1 and user_id=:userId")
    List<Todo> getAllCompleted(int userId);
    @Query("SELECT * FROM todo where completed = 1 and user_id=:userId")
    LiveData<List<Todo>> getAllCompletedLive(int userId);

    @Query("SELECT * FROM todo where completed = 0 and user_id=:userId")
    LiveData<List<Todo>> getAllIncompleteForUserLive(int userId);
}
