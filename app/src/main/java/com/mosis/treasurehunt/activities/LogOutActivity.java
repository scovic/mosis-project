package com.mosis.treasurehunt.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

public class LogOutActivity extends AppCompatActivity {
    Button cancelButton;
    Button logoutButton;
    SharedPreferencesWrapper sharedPrefWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_out);
        sharedPrefWrapper = SharedPreferencesWrapper.getInstance();

        cancelButton = findViewById(R.id.btn_logout_cancel);
        logoutButton = findViewById(R.id.btn_logout_submit);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPrefWrapper.clearUserDetails();
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
            }
        });

    }

}
