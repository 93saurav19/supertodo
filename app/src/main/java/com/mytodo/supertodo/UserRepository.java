package com.mytodo.supertodo;

import android.app.Application;
import android.os.AsyncTask;

public class UserRepository {
    private UserDao userDao;

    public UserRepository(Application application) {

        SuperTodoDatabase database = SuperTodoDatabase.getInstance(application);
        userDao = database.getUserDao();
    }

    public User getUserFromId(int userId) {
        return userDao.getUserFromId(userId);
    }

    public void insert(User user) {
        AppExecutors.getInstance().diskIO().execute(() -> userDao.insert(user));
    }

    public User getUserFromEmail(String email) {
        return userDao.getUserFromEmail(email);
    }

    public User getUser(String userEmail, String userPassword) {
        return userDao.getUser(userEmail, userPassword);
    }

    public void update(User user) {
        AppExecutors.getInstance().diskIO().execute(() -> userDao.update(user));
    }
}
