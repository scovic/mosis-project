package com.mosis.treasurehunt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

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
        }


        return super.onOptionsItemSelected(item);
    }
}
