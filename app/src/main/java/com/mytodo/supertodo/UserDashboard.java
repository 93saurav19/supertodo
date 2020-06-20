package com.mytodo.supertodo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final int CREATE_NEW_TODO =1;
    public static final int UPDATE_TODO =2;

    public static final String TODO_ID = "id";
    public static final String TODO_TITLE = "title";
    public static final String TODO_DESCRIPTION = "description";
    public static final String TODO_DUEDATE = "duedate";
    public static final String TODO_CATEGORY = "category";
    public static final String TODO_PRIORITY = "priority";
    int userId;



    DrawerLayout drawerLayout;
    NavigationView navigationView;
    TodoViewModel tvm;
    RecyclerView recyclerView;
    TodoListAdapter adapter;
    TodoRepository repository;

    ImageView navIcon;
    LinearLayout dashboardContent;

    static final float END_SCALE = 0.7f;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_dashboard);

        userId = getUserIdFromPreferences();
        repository = new TodoRepository(getApplication());


        //Menu hooks
        drawerLayout = findViewById(R.id.dashboard_drawer);
        navigationView = findViewById(R.id.dashboard_nav);

        navIcon =findViewById(R.id.dashboard_nav_icon);
        dashboardContent = findViewById(R.id.dashboard_main_content);

        tvm = ViewModelProviders.of(this).get(TodoViewModel.class);
        tvm.setUserId(userId);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

         adapter = new TodoListAdapter(tvm);
        recyclerView.setAdapter(adapter);

        tvm.setUserId(userId);
        tvm.getAllIncompleteForUserLive(userId);

        tvm.getTodosForView(userId).observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> tasks) {
                adapter.setTodoList(tasks);
            }
        });

        setClickListenerForAdapter(adapter);
        navigationHandler();

    }

    private void setClickListenerForAdapter(TodoListAdapter adapter) {
        adapter.setOnItemClickListener(new TodoListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Todo todo) {
                Intent intent = new Intent(UserDashboard.this, CreateUpdateTodo.class);

                Log.d("task id" , Integer.toString(todo.getId()));

                intent.putExtra(TODO_ID, todo.getId());
                intent.putExtra(TODO_TITLE, todo.getTitle());
                intent.putExtra(TODO_DESCRIPTION, todo.getDescription());
                intent.putExtra(TODO_PRIORITY, todo.getPriority());
                intent.putExtra(TODO_CATEGORY, todo.getCategory());
                intent.putExtra(TODO_DUEDATE,todo.getDuedate());

                Log.d("This intent" , String.valueOf(intent.getExtras()));
                startActivityForResult(intent, UPDATE_TODO);
            }
        });
    }


    private int getUserIdFromPreferences() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_prefs), MODE_PRIVATE);
        return preferences.getInt("user_id",0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CREATE_NEW_TODO && resultCode ==RESULT_OK){
            addNewTodo(data);
        }
        else if(requestCode == UPDATE_TODO && resultCode ==RESULT_OK){
            Log.d("update","update todo");
            updateTodo(data);
        }


    }

    private void updateTodo(Intent data){
        int id = data.getIntExtra(TODO_ID,0);
        String todoTitle = data.getStringExtra(TODO_TITLE);
        String todoDueDate = data.getStringExtra(TODO_DUEDATE);
        String todoDescription = data.getStringExtra(TODO_DESCRIPTION);
        int priority = data.getIntExtra(TODO_PRIORITY,0);
        int category = data.getIntExtra(TODO_CATEGORY,0);

        Todo item = new Todo(todoTitle,todoDescription);
        item.setId(id);
        item.setDuedate(todoDueDate);
        item.setPriority(priority);
        item.setCategory(category);
        item.setUser_id(userId);
        tvm.update(item);
        adapter.notifyDataSetChanged();
    }

    private void addNewTodo(Intent data) {
        String todoTitle = data.getStringExtra(TODO_TITLE);
        String todoDueDate = data.getStringExtra(TODO_DUEDATE);
        String todoDescription = data.getStringExtra(TODO_DESCRIPTION);
        int priority = data.getIntExtra(TODO_PRIORITY,0);
        int category = data.getIntExtra(TODO_CATEGORY,0);

        Todo newTodo = new Todo(todoTitle,todoDescription);
        newTodo.setDuedate(todoDueDate);
        newTodo.setPriority(priority);
        newTodo.setCategory(category);
        newTodo.setUser_id(userId);

        tvm.insert(newTodo);
        adapter.notifyDataSetChanged();

    }


    private void navigationHandler() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_category_home);
    }


    public void addTodo(View view) {
        Intent intent = new Intent(UserDashboard.this, CreateUpdateTodo.class);
        startActivityForResult(intent, CREATE_NEW_TODO);


    }

    public void displayNav(View view) {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.nav_category_all:
                adapter.clearData();
                adapter.setTodoList(repository.getAllIncompleteForUser(userId));
                tvm.getAllIncompleteForUserLive(userId);
                adapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_category_work:
               adapter.clearData();
               adapter.setTodoList(repository.getAllInCategoryList(Category.WORK.getValue(),userId));
               tvm.getAllinCategoryLive(Category.WORK.getValue(),userId);
                adapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_category_home:
                adapter.clearData();
                adapter.setTodoList(repository.getAllInCategoryList(Category.HOME.getValue(),userId));
                tvm.getAllinCategoryLive(Category.WORK.getValue(),userId);
                adapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_category_family:
                adapter.clearData();
                adapter.setTodoList(repository.getAllInCategoryList(Category.FAMILY.getValue(),userId));
                tvm.getAllinCategoryLive(Category.WORK.getValue(),userId);
                adapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;


            case R.id.nav_category_friends:
                adapter.clearData();
                adapter.setTodoList(repository.getAllInCategoryList(Category.FRIENDS.getValue(),userId));
                tvm.getAllinCategoryLive(Category.WORK.getValue(),userId);
                adapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_priority_high:
                adapter.clearData();
                adapter.setTodoList(repository.getAllInPriorityList(Priority.HIGH.getValue(),userId));
                tvm.getAllinPriorityLive(Priority.HIGH.getValue(),userId);
                adapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_priority_medium:
                adapter.clearData();
                adapter.setTodoList(repository.getAllInPriorityList(Priority.MEDIUM.getValue(),userId));
                tvm.getAllinPriorityLive(Priority.MEDIUM.getValue(),userId);
                adapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_priority_low:
                adapter.clearData();
                adapter.setTodoList(repository.getAllInPriorityList(Priority.LOW.getValue(),userId));
                tvm.getAllinPriorityLive(Priority.LOW.getValue(),userId);
                adapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_menu_completed:
                adapter.clearData();
                adapter.setTodoList(repository.getAllCompleted(userId));
                tvm.getAllCompletedLive(userId);
                adapter.notifyDataSetChanged();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_profile_logout:
                handleLogout();
                return true;

            default:
                return true;

        }
    }

    private void handleLogout() {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_prefs), MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Intent i = new Intent(UserDashboard.this, Login.class);
        startActivity(i);
        finish();

    }

    //prevent application from closing when back is pressed during navigation
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }
}