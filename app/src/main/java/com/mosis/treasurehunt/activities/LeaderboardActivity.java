package com.mosis.treasurehunt.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.adapters.UserAdapter;
import com.mosis.treasurehunt.models.User;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private ListView mUserListView;
    private UserAdapter mUserAdapter;
    private List<User> mUsersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leaderboard");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TabLayout tabs = findViewById(R.id.tabs_layout);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mUserListView = findViewById(R.id.user_list);
                if (tab.getPosition() == 0) {
                    mUsersList = new ArrayList<>();
                    mUsersList.add(new User("Stefan", "Covic", "scovic996@gmail.com", "pass",1005));
                    mUsersList.add(new User("Nevena", "Colic", "nensika996@gmail.com", "pass",100));
                    mUsersList.add(new User("Jon", "Snow", "snowflake@gmail.com", "pass",65));
                } else {
                    mUsersList = new ArrayList<>();
                    mUsersList.add(new User("Stefan", "Covic", "scovic996@gmail.com", "pass",1005));
                    mUsersList.add(new User("Nevena", "Colic", "nensika996@gmail.com", "pass",100));
                }
                mUserAdapter = new UserAdapter(LeaderboardActivity.this, mUsersList);
                mUserListView.setAdapter(mUserAdapter);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mUserListView = findViewById(R.id.user_list);
        List<User> usersList = new ArrayList<>();
        usersList.add(new User("Stefan", "Covic", "scovic996@gmail.com", "pass", 1005));
        usersList.add(new User("Nevena", "Colic", "nensika996@gmail.com", "pass", 100));
        usersList.add(new User("Jon", "Snow", "snowflake@gmail.com", "pass", 65));
        mUserAdapter = new UserAdapter(this, usersList);
        mUserListView.setAdapter(mUserAdapter);
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
