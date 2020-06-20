package com.mytodo.supertodo;

import android.app.Application;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TodoRepository {
    private TodoDao todoDao;
    private LiveData<List<Todo>> todosInView;

    public TodoRepository(Application application) {
        SuperTodoDatabase sdb = SuperTodoDatabase.getInstance(application);
        todoDao = sdb.getTodoDao();
    }

    public void insert(Todo newTodo) {
        AppExecutors.getInstance().diskIO().execute(() -> todoDao.insert(newTodo));
    }


    public List<Todo> getAllInCategoryList(int catValue, int userId) {
        return todoDao.getAllinCategory(catValue,userId);
    }

    public void update(Todo item) {
        AppExecutors.getInstance().diskIO().execute(() -> todoDao.update(item));
    }

    public LiveData<List<Todo>> getAllInCategoryLive(int value, int userId) {
        return todoDao.getAllinCategoryLive(value,userId);
    }

    public List<Todo> getAllInPriorityList(int value, int userId) {
        return todoDao.getAllinPriority(value,userId);
    }

    public LiveData<List<Todo>> getAllInPriorityLive(int value, int userId) {
        return todoDao.getAllinPriorityLive(value,userId);
    }

    public List<Todo> getAllCompleted(int userId) {
    return todoDao.getAllCompleted(userId);
    }

    public LiveData<List<Todo>> getAllCompletedLive(int userId) {
    return todoDao.getAllCompletedLive(userId);
    }

    public List<Todo> getAllIncompleteForUser(int userId) {
        return todoDao.getAllIncompleteForUser(userId);
    }
    public LiveData<List<Todo>> getAllIncompleteForUserLive(int userId) {
        return todoDao.getAllIncompleteForUserLive(userId);
    }
}
