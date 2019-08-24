package com.mosis.treasurehunt.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.adapters.UserAdapter;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private ListView mUserListView;
    private UserAdapter mUserAdapter;
    private UserRepository mUserRepository;
    private SharedPreferencesWrapper mSharedPrefWrapper;
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

        mSharedPrefWrapper = SharedPreferencesWrapper.getInstance();
        mUserRepository = UserRepository.getInstance();
        mUserRepository.getUsers();
        TabLayout tabs = findViewById(R.id.tabs_layout);
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mUserListView = findViewById(R.id.user_list);
                if (tab.getPosition() == 0) {
                    mUsersList = mUserRepository.getUsers1();
                } else {
                    String username = mSharedPrefWrapper.getUsername();
                    User logedInUser = mUserRepository.getUserByUsername(username);
                    mUsersList = mUserRepository.getFriendArrayList(logedInUser);
                }
                mUsersList.sort(new Comparator<User>() {
                    @Override
                    public int compare(User o1, User o2) {
                        return o2.getPoints() - o1.getPoints();
                    }
                });
                mUserAdapter = new UserAdapter(LeaderboardActivity.this, mUsersList, R.layout.item_list_user);
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
        mUsersList = mUserRepository.getUsers1();
        mUsersList.sort(new Comparator<User>() {
            @Override
            public int  compare(User o1, User o2) {
                return o2.getPoints() - o1.getPoints();
            }
        });
        mUserAdapter = new UserAdapter(this, mUsersList, R.layout.item_list_user);
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
