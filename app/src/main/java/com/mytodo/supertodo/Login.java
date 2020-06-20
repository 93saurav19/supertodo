package com.mytodo.supertodo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Login extends AppCompatActivity {

    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;
    private UserDao userDao;

    private TextInputLayout loginEmailLayout;
    private TextInputLayout loginPasswordLayout;

    User user;
    private UserRepository userRepository;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        userRepository = new UserRepository(getApplication());
        userDao = SuperTodoDatabase.getInstance(this).getUserDao();

        //setup the ui elements
        setupUI();

        //implement listeners
        setupListeners();
    }

    public void handleLogin(View view) {

        clearErrors();
        boolean isValid = validateLogin();

        if(isValid && user!=null){

            SharedPreferences preferences = getSharedPreferences(getString(R.string.shared_prefs), MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("user_id", user.getId());
            editor.putString("user_email",user.getEmail());
            editor.apply();


            if(user.isFirsttime()) {
                user.setFirsttime(false);

                userRepository.update(user);

                Intent intent = new Intent(Login.this, Onboarding.class);
                startActivity(intent);
                //remove activity from activity list
                finish();
            }
            else {
                Intent intent = new Intent(Login.this, UserDashboard.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private boolean validateLogin() {

        if(isEmpty(loginEmail)){
            setError(loginEmailLayout, "Email cannot be empty");
            return false;
        }

        if(isEmpty(loginPassword)){
            setError(loginPasswordLayout, "Password cannot be empty");
            return false;
        }

        if (!isEmail(loginEmail)) {
            setError(loginEmailLayout, "Email must be valid");
            return false;
        }

        String userEmail = loginEmail.getText().toString();
        String userPassword = loginPassword.getText().toString();

        user = userRepository.getUser(userEmail,userPassword);

        if(user == null){
            setError(loginPasswordLayout,"Credentials do not match our records");
            setError(loginEmailLayout,"Credentials do not match our records");
            return false;
        }

        Toast.makeText(Login.this, user.toString(),Toast.LENGTH_SHORT);
        return true;
    }


    public void setError(TextInputLayout layout, CharSequence error) {
        layout.setError(error);
    }


    boolean isEmpty(TextInputEditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean isEmail(TextInputEditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());


    }

    private void clearErrors() {
        loginEmailLayout.setErrorEnabled(false);
        loginPasswordLayout.setErrorEnabled(false);
        loginEmailLayout.setError(null);
        loginPasswordLayout.setError(null);
    }



    public void callRegisterActivity(View view) {

        Intent intent = new Intent(Login.this, Register.class);
        startActivity(intent);
    }


    private void setupListeners() {

        loginEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                loginEmailLayout.setErrorEnabled(false);
            }
        });


        loginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                loginPasswordLayout.setErrorEnabled(false);
            }
        });
    }


    private void setupUI() {
        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);

        loginEmailLayout=findViewById(R.id.login_email_layout);
        loginPasswordLayout=findViewById(R.id.login_password_layout);
    }


}
