package com.mosis.treasurehunt.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.data.UserDao;
import com.mosis.treasurehunt.models.User;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    UserDao userDao;
    Button haveAccountButton;
    Button signUpButton;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText emailEditText;
    EditText passwordEditText;
    EditText repeatPasswordEditText;
    boolean userAlreadyExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        haveAccountButton = findViewById(R.id.button_have_account);
        haveAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
            }
        });

        userDao = new UserDao();
        firstNameEditText = findViewById(R.id.edit_register_firstname);
        lastNameEditText = findViewById(R.id.edit_register_lastname);
        emailEditText = findViewById(R.id.edit_register_username);
        passwordEditText = findViewById(R.id.edit_register_password);
        repeatPasswordEditText = findViewById(R.id.edit_register_repeat_password);

        signUpButton = findViewById(R.id.button_sign_up);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String username = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String repeatedPassword = repeatPasswordEditText.getText().toString();

                ArrayList<User> users = userDao.getAll();

                for (User user : users) {
                    if (user.getUsername().equals(username)) {
                        userAlreadyExists = true;
                        break;
                    }
                }

                if (!userAlreadyExists) {
                    if (password.equals(repeatedPassword)) {
                        User user = new User(firstName, lastName, username, password);
                        userDao.save(user);
                        if(userDao.getQuerySuccess() == true) {
                            Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                            userDao.setQuerySuccess(false);
                            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to register", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "User with that username already exists", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
