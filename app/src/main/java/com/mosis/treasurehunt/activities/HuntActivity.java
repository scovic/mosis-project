package com.mosis.treasurehunt.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.databinding.ActivityHuntBinding;
import com.mosis.treasurehunt.models.Hunt;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

public class HuntActivity extends AppCompatActivity {

    ActivityHuntBinding mBinding;
    SharedPreferencesWrapper mSharedPrefWrapper;
    UserRepository mUserRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_hunt);
        mSharedPrefWrapper.getInstance();
        mUserRepo = UserRepository.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

       Hunt hunt;

        try {
            Intent listIntent = getIntent();
            Bundle indexBundle = listIntent.getExtras();
            if (indexBundle == null) {
                Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
            } else {
                String huntTitle = indexBundle.getString("huntTitle");
                String huntType = indexBundle.getString("huntType");
                User user = mUserRepo.getUserByUsername(mSharedPrefWrapper.getUsername());
                // u userovim huntovima nadji po huntTitle & type
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
        mBinding.setHunt(hunt);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hunt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


}
