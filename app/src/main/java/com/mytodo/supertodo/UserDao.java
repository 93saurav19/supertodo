package com.mytodo.supertodo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Query("SELECT * FROM User where email= :mail and password= :password")
    User getUser(String mail, String password);

    @Query("SELECT * FROM User where email= :mail")
    User getUserFromEmail(String mail);

    @Query("UPDATE user set firsttime =:firsttime where id=:user_id")
    void updateFirstTime(boolean firsttime , int user_id);

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM User where id= :userId")
    User getUserFromId(int userId);
}
