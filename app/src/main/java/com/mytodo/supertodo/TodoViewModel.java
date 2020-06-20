package com.mytodo.supertodo;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class TodoViewModel extends AndroidViewModel {

    //check modelviewviewmodel MVVM android
    private TodoRepository repository;
    private LiveData<List<Todo>> todosForView;
    private int user_id;

    public TodoViewModel(@NonNull Application application) {
        super(application);
        user_id=0;
        repository = new TodoRepository(application);
    }


    public void insert(Todo newTodo) {
        repository.insert(newTodo);
    }

    public LiveData<List<Todo>> getTodosForView(int userId) {
        return todosForView;

    }

    public void setUserId(int userId) {
    this.user_id = userId;
    }

    public int getUser_id(){
        return this.user_id;
    }

    public void update(Todo item) {
        repository.update(item);
    }

    public void getAllinCategoryLive(int value, int userId) {
        todosForView = repository.getAllInCategoryLive(value,userId);
    }

    public void getAllinPriorityLive(int value, int userId) {
        todosForView = repository.getAllInPriorityLive(value,userId);
    }

    public void getAllCompletedLive(int userId) {
        todosForView = repository.getAllCompletedLive(userId);
    }

    public void getAllIncompleteForUserLive(int userId) {
        todosForView = repository.getAllIncompleteForUserLive(userId);
    }

}