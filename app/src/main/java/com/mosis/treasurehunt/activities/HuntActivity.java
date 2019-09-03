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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.adapters.UserAdapter;
import com.mosis.treasurehunt.databinding.ActivityHuntBinding;
import com.mosis.treasurehunt.models.Hunt;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import java.util.ArrayList;
import java.util.List;

public class HuntActivity extends AppCompatActivity {

    Button joinHuntButton;
    ListView mUserListView;
    UserAdapter mUserAdapter;

    ActivityHuntBinding mHuntBinding;
    SharedPreferencesWrapper mSharedPrefWrapper;
    UserRepository mUserRepo;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);
        mHuntBinding = DataBindingUtil.setContentView(this, R.layout.activity_hunt);
        mSharedPrefWrapper = SharedPreferencesWrapper.getInstance();
        mUserRepo = UserRepository.getInstance();

        mUserListView = findViewById(R.id.lv_hunters);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Hunt bindHunt = null;
        try {
            Intent listIntent = getIntent();
            Bundle indexBundle = listIntent.getExtras();
            if (indexBundle == null) {
                Toast.makeText(this, "Nothing to show", Toast.LENGTH_SHORT).show();
            } else {
                String huntTitle = indexBundle.getString("huntTitle");
                String huntType = indexBundle.getString("huntType");
                String username = indexBundle.getString("username");
                mUser = mUserRepo.getUserByUsername(username);
                List<Hunt> userHunts;
                if (huntTitle.equals("No active hunts currently") || huntTitle.equals("You haven't completed any hunts") || huntTitle.equals("You haven't created any hunts")) {
                    Toast.makeText(this, "Play a little, nothing to show here :(", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if (huntType.equals("active")) {
                        userHunts = mUser.filterActiveHunts();
                    } else if (huntType.equals("completed")) {
                        userHunts = mUser.getCompletedHunts();
                    } else userHunts = mUser.getCreatedHunts();

                    for (Hunt hunt : userHunts) {
                        if (hunt.getTitle().equals(huntTitle)) {
                            bindHunt = hunt;
//                            bindHunt.setOwner(mUser.getUsername());
                            mHuntBinding.setHunt(bindHunt);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        joinHuntButton = findViewById(R.id.btn_hunt);
        final User currentUser = mUserRepo.getUserByUsername(mSharedPrefWrapper.getUsername());
        List<Hunt> activeHunts = mUserRepo.getActiveHunts(currentUser);
        List<Hunt> completedHunts = mUserRepo.getCompletedHunts(currentUser);
        List<Hunt> createdHunts = mUserRepo.getCreatedHunts(currentUser);
        boolean myHunt = false;
        boolean found = false;

        if (completedHunts != null) {
            for (Hunt hunt : completedHunts) {
                if (hunt.getTitle().equals(bindHunt.getTitle())) {
                    myHunt = true;
                    joinHuntButton.setText("Leave Hunt");
                    break;
                }
            }
        }

        if (activeHunts != null && !myHunt) {
            for (Hunt hunt : activeHunts) {
                if (hunt.getTitle().equals(bindHunt.getTitle())) {
                    found = true;
                    joinHuntButton.setText("Leave Hunt");
                    break;
                }
            }
        }

        if (!found && createdHunts != null && !myHunt) {
            for (Hunt hunt : createdHunts) {
                if (hunt.getTitle().equals(bindHunt.getTitle())) {
                    joinHuntButton.setEnabled(false);
                    joinHuntButton.setVisibility(View.GONE);
//                    joinHuntButton.setText("Leave Hunt");
                    break;
                }
            }
        }

        final Hunt huntToAdd = bindHunt;
        joinHuntButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (joinHuntButton.getText().equals("Join Hunt")) {
                    mUserRepo.joinHunt(currentUser, huntToAdd);
                    joinHuntButton.setText("Leave Hunt");
                    Toast.makeText(HuntActivity.this, "Welcome! Enjoy the hunt!", Toast.LENGTH_SHORT).show();
                } else {
                    mUserRepo.leaveHunt(currentUser, huntToAdd);
                    joinHuntButton.setText("Join Hunt");
                    Toast.makeText(HuntActivity.this, "Bye bye birdie!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        List<User> usersList = mUserRepo.getUsers1();
        final List<User> hunters = new ArrayList<>();
        for (User user : usersList) {
            List<Hunt> active = mUserRepo.getActiveHunts(user);
            for (Hunt joinedHunt : active) {
                if (joinedHunt.getTitle().equals(bindHunt.getTitle())) {
                    hunters.add(user);
                }
            }
        }
        mUserAdapter = new UserAdapter(HuntActivity.this, hunters, R.layout.item_list_user);
        mUserListView.setAdapter(mUserAdapter);

        mUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HuntActivity.this, UserProfileActivity.class);
                i.putExtra("state", hunters.get(position).getUsername());
                startActivity(i);
            }
        });
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
