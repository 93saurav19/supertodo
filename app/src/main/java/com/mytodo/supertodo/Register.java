package com.mytodo.supertodo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class Register extends AppCompatActivity {

    private TextInputEditText registerEmail;
    private TextInputEditText registerPassword;
    private TextInputEditText registerConfirmPassword;

    private TextInputLayout registerEmailLayout;
    private TextInputLayout registerPasswordLayout;
    private TextInputLayout registerConfirmPasswordLayout;

    UserRepository userRepository;



    private UserDao userDao;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        userRepository = new UserRepository(getApplication());
        setupUI();
        implementListeners();
    }

    private void implementListeners() {
        registerEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                registerEmailLayout.setErrorEnabled(false);
            }
        });


        registerPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                registerPasswordLayout.setErrorEnabled(false);
            }
        });



        registerConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                registerConfirmPasswordLayout.setErrorEnabled(false);
            }
        });

    }

    private void setupUI() {
        registerEmail = findViewById(R.id.register_email);
        registerPassword = findViewById(R.id.register_password);
        registerConfirmPassword = findViewById(R.id.register_password_confirm);
        registerEmailLayout = findViewById(R.id.register_email_layout);
        registerPasswordLayout = findViewById(R.id.register_password_layout);
        registerConfirmPasswordLayout = findViewById(R.id.register_confirmpassword_layout);
    }

    public void handleRegister(View view) {
        clearErrors();
        boolean isValid = validateRegistration();

        if(isValid){
            User newUser = new User(registerEmail.getText().toString(), registerPassword.getText().toString());
            userRepository.insert(newUser);
            startActivity(new Intent(Register.this, Login.class));
            Toast.makeText(Register.this, "User Created",Toast.LENGTH_SHORT).show();
            finish();

        }


    }

    private void clearErrors() {
        registerEmailLayout.setErrorEnabled(false);
        registerPasswordLayout.setErrorEnabled(false);
        registerConfirmPasswordLayout.setErrorEnabled(false);
        registerEmailLayout.setError(null);
        registerPasswordLayout.setError(null);
        registerConfirmPasswordLayout.setError(null);
    }

    private boolean validateRegistration() {
        boolean isValidEmail = validateEmail();
        boolean isValidPassword = validatePassword();
        boolean isValidConfirmPassword = validateConfirmPassword();
        return isValidEmail && isValidPassword && isValidConfirmPassword;
    }

    private boolean validateEmail() {
        if (isEmpty(registerEmail)) {
            setError(registerEmailLayout, "You must enter email");
            return false;
        }

        if (!isEmail(registerEmail)) {
            setError(registerEmailLayout, "Email must be valid");
            return false;
        }

        User user = userRepository.getUserFromEmail(registerEmail.getText().toString());

        if(user != null){
            setError(registerEmailLayout, "Email already registered");
            return false;
        }
        return true;
    }


    private boolean validatePassword() {

        if (isEmpty(registerPassword)) {
            setError(registerPasswordLayout, "You must enter Password");
            return false;
        }
        String password = registerPassword.getText().toString();

        if(password.length()<8){
            setError(registerPasswordLayout, "Password must be at least 8 characters long");
            return false;
        }
        return true;
    }

    private boolean validateConfirmPassword() {

        if (isEmpty(registerConfirmPassword)) {
            setError(registerConfirmPasswordLayout, "You must confirm Password");
            return false;
        }

        String password = registerPassword.getText().toString();
        String cpassword = registerConfirmPassword.getText().toString();

        if(!password.equals(cpassword)){

            setError(registerConfirmPasswordLayout, "Password and confirm password must match");
            return false;
        }
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

    public void callLoginActivity(View view) {
        Intent intent = new Intent(Register.this, Login.class);
        startActivity(intent);
        finish();
    }
}
