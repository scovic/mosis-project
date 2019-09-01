package com.mosis.treasurehunt.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.adapters.UserAdapter;
import com.mosis.treasurehunt.databinding.ActivityUserProfileBinding;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.viewmodels.FriendListActivityViewModel;

import java.util.List;

public class FriendListActivity extends AppCompatActivity {

    private FriendListActivityViewModel mFriendListActivityModelView;
    private UserAdapter mUserAdapter;
    private ListView mFriendListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mFriendListActivityModelView = ViewModelProviders.of(this).get(FriendListActivityViewModel.class);
        mFriendListActivityModelView.init(this);

        mFriendListActivityModelView.getIsUpdating().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
            }
        });
        initListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void initListView() {
        final List<User> friends = mFriendListActivityModelView.getFriendList().getValue();
        mUserAdapter = new UserAdapter(this, friends, R.layout.item_list_friend);
        mFriendListView = findViewById(R.id.friend_list);
        mFriendListView.setAdapter(mUserAdapter);

        mFriendListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendListActivity.this, UserProfileActivity.class);
                intent.putExtra("state", friends.get(position).getUsername());
                startActivity(intent);
            }
        });
    }
}
