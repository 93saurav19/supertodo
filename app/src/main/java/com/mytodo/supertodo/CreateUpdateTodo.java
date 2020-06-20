package com.mytodo.supertodo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Create new todos or update todos
 */
public class CreateUpdateTodo extends AppCompatActivity {

    private TextInputEditText dueDate,todoTitle,todoDescription;
    private MaterialButtonToggleGroup categoryGroup, priorityGroup;
    final Calendar myCalendar = Calendar.getInstance();
    private int userId,todoId;
    private TodoViewModel tvm;
    Button addButton, editButton,addSubmit,updateSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_create_update_todo);

        //initialize viewmodel
        tvm = new TodoViewModel(getApplication());

        //check if valid user
        SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_prefs), MODE_PRIVATE);
        userId = preferences.getInt("user_id",0);

        if(userId<1){
            Intent i = new Intent(CreateUpdateTodo.this, UserDashboard.class);
            startActivity(i);
            finish();
        }

        //set UI
        todoTitle=findViewById(R.id.edit_activity_todo_title);
        todoDescription=findViewById(R.id.edit_activity_todo_description);
        dueDate = findViewById(R.id.edit_activity_todo_duedate);
        categoryGroup = findViewById(R.id.edit_activity_category_group);
        priorityGroup = findViewById(R.id.edit_activity_priority_group);

        addButton =findViewById(R.id.edit_activity_add_button);
        editButton=findViewById(R.id.edit_activity_edit_button);
        addSubmit=findViewById(R.id.add_todo_submit);
        updateSubmit=findViewById(R.id.update_todo_submit);



        //check if edit mode
        if(intent.hasExtra(UserDashboard.TODO_ID)){


            todoId = intent.getIntExtra(UserDashboard.TODO_ID,0);

            //update UI
            addButton.setVisibility(View.GONE);
            editButton.setVisibility(View.VISIBLE);
            addSubmit.setVisibility(View.GONE);
            updateSubmit.setVisibility(View.VISIBLE);

            todoTitle.setText(intent.getStringExtra(UserDashboard.TODO_TITLE));
            todoDescription.setText(intent.getStringExtra(UserDashboard.TODO_DESCRIPTION));
            dueDate.setText(intent.getStringExtra(UserDashboard.TODO_DUEDATE));
            checkPriorityButtonGroup(intent.getIntExtra(UserDashboard.TODO_PRIORITY,0));
            checkCategoryButtonGroup(intent.getIntExtra(UserDashboard.TODO_CATEGORY,0));

        }


        dueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(CreateUpdateTodo.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }


    /**
     * Change checked status of category button group
     * @param category
     */
    private void checkCategoryButtonGroup(int category) {
        if(category == Category.HOME.getValue()){
            categoryGroup.check(R.id.edit_activity_category_home);
            return;
        }
        if(category == Category.FRIENDS.getValue()){
            categoryGroup.check(R.id.edit_activity_category_friends);
            return;
        }
        if(category == Category.FAMILY.getValue()){
            categoryGroup.check(R.id.edit_activity_category_family);
            return;
        }
        if(category == Category.WORK.getValue()){
            categoryGroup.check(R.id.edit_activity_category_work);
            return;
        }

        categoryGroup.check(R.id.edit_activity_category_home);
    }

    /**
     * change checked status of priority button group
     * @param priority
     */
    private void checkPriorityButtonGroup(int priority) {
        if(priority == Priority.HIGH.getValue()){
            priorityGroup.check(R.id.edit_activity_priority_high);
            return;
        }
        if(priority == Priority.MEDIUM.getValue()){
            priorityGroup.check(R.id.edit_activity_priority_medium);
            return;
        }
        if(priority == Priority.LOW.getValue()){
            priorityGroup.check(R.id.edit_activity_priority_low);
            return;
        }

        priorityGroup.check(R.id.edit_activity_priority_medium);
    }


    MaterialStyledDatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            myCalendar.set(year,month,day);

            String dateFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);

            dueDate.setText(sdf.format(myCalendar.getTime()));

        }
    };

    /**Go back to dasboard
     *
     * @param view
     */
    public void gotoDashboard(View view) {
        Intent i = new Intent(CreateUpdateTodo.this, UserDashboard.class);
        startActivity(i);
    }


    /**Handler for edit todo
     *
     * @param view
     */
    public void handleEditTodo(View view) {
        String title = todoTitle.getText().toString();
        String due = dueDate.getText().toString();
        String description = todoDescription.getText().toString();

        int categoryId = categoryGroup.getCheckedButtonId();
        int priorityId = priorityGroup.getCheckedButtonId();

        int myCat = getCategoryValueFromButtonId(categoryId);
        int myPriority = getPriorityValueFromButtonId(priorityId);

        Log.d("category" ,Integer.toString(categoryId));
        Log.d("Priority",Integer.toString(priorityId));

        Intent i = new Intent(CreateUpdateTodo.this, UserDashboard.class);
        i.putExtra(UserDashboard.TODO_ID, todoId);
        i.putExtra(UserDashboard.TODO_TITLE, title);
        i.putExtra(UserDashboard.TODO_CATEGORY,myCat);
        i.putExtra(UserDashboard.TODO_DESCRIPTION,description);
        i.putExtra(UserDashboard.TODO_DUEDATE,due);
        i.putExtra(UserDashboard.TODO_PRIORITY,myPriority);

        setResult(RESULT_OK,i);
        finish();
    }

    /**
     * handler for add todo
     * @param view
     */
    public void handleAddTodo(View view) {

        String title = todoTitle.getText().toString();
        String due = dueDate.getText().toString();
        String description = todoDescription.getText().toString();

        int categoryId = categoryGroup.getCheckedButtonId();
        int priorityId = priorityGroup.getCheckedButtonId();

        int myCat = getCategoryValueFromButtonId(categoryId);
        int myPriority = getPriorityValueFromButtonId(priorityId);

        Intent i = new Intent(CreateUpdateTodo.this, UserDashboard.class);
        i.putExtra(UserDashboard.TODO_TITLE, title);
        i.putExtra(UserDashboard.TODO_CATEGORY,myCat);
        i.putExtra(UserDashboard.TODO_DESCRIPTION,description);
        i.putExtra(UserDashboard.TODO_DUEDATE,due);
        i.putExtra(UserDashboard.TODO_PRIORITY,myPriority);

        setResult(RESULT_OK,i);
        finish();
    }

    /**
     * get category value from button id
     * @param categoryId
     * @return
     */
    private int getCategoryValueFromButtonId(int categoryId) {
        switch (categoryId){
            case R.id.edit_activity_category_home:
                return Category.HOME.getValue();
            case R.id.edit_activity_category_work:
                return Category.WORK.getValue();
            case R.id.edit_activity_category_family:
                return Category.FAMILY.getValue();
            case R.id.edit_activity_category_friends:
                return Category.FRIENDS.getValue();
            default:
                return Category.HOME.getValue();
        }
    }

    /**
     * get priority value from button id
     * @param priorityId
     * @return
     */
    private int getPriorityValueFromButtonId(int priorityId) {
        switch (priorityId){
            case R.id.edit_activity_priority_high:
                return Priority.HIGH.getValue();
            case R.id.edit_activity_priority_medium:
                return Priority.MEDIUM.getValue();
            case R.id.edit_activity_priority_low:
                return Priority.LOW.getValue();
            default:
                return Priority.MEDIUM.getValue();
        }
    }

//    public void pickDate(View view) {
//
//        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
//        builder.setTitleText(R.string.due_date);
//        MaterialDatePicker<Long> picker = builder.build();
//        picker.show(getSupportFragmentManager(), "Date Picker");
//    }
}