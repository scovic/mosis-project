package com.mosis.treasurehunt.activities;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

public class MainEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent activityIntent;
        SharedPreferencesWrapper.createInstance(this);
        UserRepository.init();
        SharedPreferencesWrapper pref = SharedPreferencesWrapper.getInstance();

        if (pref.containsUsername("username")) {
            activityIntent = new Intent(this, HomeActivity.class);
        } else {
            activityIntent = new Intent(this, LogInActivity.class);
        }

        startActivity(activityIntent);
        finish();
    }
}
