package com.mosis.treasurehunt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.adapters.FeedAdapter;
import com.mosis.treasurehunt.models.Feed;
import com.mosis.treasurehunt.models.Hunt;
import com.mosis.treasurehunt.models.User;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private ListView mFeedsList;
    private FeedAdapter mFeedsAdapter;
    private TextView userTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, NewHuntActivity.class);
                startActivity(i);
            }
        });

        mFeedsList = findViewById(R.id.feeds_list);
        List<Feed> feedslist = new ArrayList<>();
        feedslist.add(new Feed(
                new User("Stefan", "Covic", "scovic996@gmail.com"),
                new Hunt("Hunt1"),
                Feed.Type.FINISH
        ));
        feedslist.add(new Feed(
                new User("Nevena", "Colic", "scovic996@gmail.com"),
                new Hunt("Hunt1"),
                Feed.Type.CREATE
        ));

        mFeedsAdapter = new FeedAdapter(this, feedslist);
        mFeedsList.setAdapter(mFeedsAdapter);

        mFeedsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
                intent.putExtra("friendProfile", i);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.show_user_profile) {
            Intent i = new Intent(this, UserProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.show_leaderboard) {
            Intent i = new Intent(this, LeaderboardActivity.class);
            startActivity(i);

        } else if (id == R.id.show_friends) {
            Intent i = new Intent(this, FriendListActivity.class);
            startActivity(i);
        } else if (id == R.id.signout) {
            Intent i = new Intent(this, LogOutActivity.class);
            startActivityForResult(i, 1);
        } else if (id == R.id.show_map_home) {
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                // when you logut, this should prevent user from goind on home acivity if he pres back button
                Intent i = new Intent(this, LogInActivity.class);
                startActivity(i);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
