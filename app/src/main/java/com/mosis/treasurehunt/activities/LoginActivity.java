package com.mosis.treasurehunt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.data.UserDao;
import com.mosis.treasurehunt.models.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences pref;
    Button noAccountButton;
    Button signInButton;
    EditText usernameEditText;
    EditText passwordEditText;
    Intent homeIntent;
    UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        userDao = new UserDao();

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        homeIntent = new Intent(LoginActivity.this, HomeActivity.class);

        if(pref.contains("username")) {
            startActivity(homeIntent);
        }

        noAccountButton = findViewById(R.id.button_no_account);
        signInButton = findViewById(R.id.button_sign_in);
        usernameEditText = findViewById(R.id.et_login_username);
        passwordEditText = findViewById(R.id.et_login_password);

        noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                ArrayList<User> registeredUsers = userDao.getAll();

                for (User user : registeredUsers) {
                    if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", username);
                        editor.commit();
                        startActivity(homeIntent);
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        homeIntent = new Intent(LoginActivity.this, HomeActivity.class);

        if(pref.contains("username")) {
            startActivity(homeIntent);
        }
    }
}
