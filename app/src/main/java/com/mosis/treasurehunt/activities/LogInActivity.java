package com.mosis.treasurehunt.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.data.UserDao;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import java.util.ArrayList;

public class LogInActivity extends AppCompatActivity {
    Button noAccountButton;
    Button signInButton;
    EditText usernameEditText;
    EditText passwordEditText;
    Intent homeIntent;
    UserDao userDao;
    SharedPreferencesWrapper sharedPrefWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        userDao = new UserDao();
        sharedPrefWrapper = SharedPreferencesWrapper.getInstance();

        homeIntent = new Intent(LogInActivity.this, HomeActivity.class);

        if(sharedPrefWrapper.containsUsername("username")) {
            startActivity(homeIntent);
        }

        noAccountButton = findViewById(R.id.button_no_account);
        signInButton = findViewById(R.id.button_sign_in);
        usernameEditText = findViewById(R.id.et_login_username);
        passwordEditText = findViewById(R.id.et_login_password);

        noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                ArrayList<User> registeredUsers = userDao.getAll();

                for (User user : registeredUsers) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        sharedPrefWrapper.putUsername(username);
                        sharedPrefWrapper.putFullName(user.getFullName());

                        startActivity(homeIntent);
                        finish();
                    }
                }
            }
        });
    }
}
