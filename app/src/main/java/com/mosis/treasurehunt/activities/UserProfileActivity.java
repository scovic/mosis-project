package com.mosis.treasurehunt.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.adapters.HuntAdapter;
import com.mosis.treasurehunt.databinding.ActivityUserProfileBinding;
import com.mosis.treasurehunt.helpers.ServiceHelper;
import com.mosis.treasurehunt.models.Hunt;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.services.LocationTrackerService;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    private Spinner spinner;
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private Button btnAddHunt;
    private Button btnDiscoverFriends;
    private ListView mHuntsList;
    private HuntAdapter mHuntsAdapter;
    private Intent mLocationTrackerServiceIntent;

    ActivityUserProfileBinding mBinding;
    private User mUser; // logged in user
    UserRepository mUserRepo;
    SharedPreferencesWrapper mSharedPrefWrapper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_profile);
        mUserRepo = UserRepository.getInstance();
        mSharedPrefWrapper = SharedPreferencesWrapper.getInstance();

        String username = "";

        try {
            Intent listIntent = getIntent();
            Bundle indexBundle = listIntent.getExtras();
            if (indexBundle == null) {
                mUser = mUserRepo.getUserByUsername(mSharedPrefWrapper.getUsername());
            } else {
                username = indexBundle.getString("state");
                mUser = mUserRepo.getUserByUsername(username);
            }
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }

        mBinding.setUser(mUser);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnDiscoverFriends = findViewById(R.id.btn_discover_friends);
        btnDiscoverFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserProfileActivity.this, BluetoothActivity.class);
                startActivity(i);
            }
        });

        spinner = findViewById(R.id.spinner_user_profile);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.type_of_hunts_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                setHuntAdapter(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnAddHunt = findViewById(R.id.btn_new_hunt);
        btnAddHunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(UserProfileActivity.this, NewHuntActivity.class);
                startActivity(i);
            }
        });

        mLocationTrackerServiceIntent = new Intent(this, LocationTrackerService.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.show_friends) {
            Intent i = new Intent(this, FriendListActivity.class);
            startActivity(i);
        } else if (id == R.id.show_leaderboard) {
            Intent i = new Intent(this, LeaderboardActivity.class);
            startActivity(i);
        } else if (id == android.R.id.home) {
            finish();
        } else if (id == R.id.start_location_tracker) {
            if (ServiceHelper.isServiceRunning(UserProfileActivity.this, LocationTrackerService.class)) {
                stopService(mLocationTrackerServiceIntent);
            } else {
                startService(mLocationTrackerServiceIntent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void setHuntAdapter(String item) {
        mHuntsList = findViewById(R.id.hunts_list);
        ArrayList<Hunt> huntsList = new ArrayList<>();

        if (item.equals("Active Hunts")) {
            // filter baze za huntove kod kojih je completed=false
            huntsList.add(new Hunt("Test Active Hunt"));
            mHuntsAdapter = new HuntAdapter(this, huntsList);
            mHuntsAdapter.setmFilter(HuntAdapter.FilterType.ACTIVE);
            mHuntsList.setAdapter(mHuntsAdapter);
        } else if (item.equals("Completed Hunts")) {
            // ovde filter svih huntova kod kojih je completed=true
            huntsList.add(new Hunt("Test Completed Hunt"));
            mHuntsAdapter = new HuntAdapter(this, huntsList);
            mHuntsAdapter.setmFilter(HuntAdapter.FilterType.COMPLETED);
            mHuntsList.setAdapter(mHuntsAdapter);
        } else if (item.equals("My Hunts")) {
            // filter gde je user == ulogovani user
            huntsList.add(new Hunt("Test My Hunt"));
            mHuntsAdapter = new HuntAdapter(this, huntsList);
            mHuntsAdapter.setmFilter(HuntAdapter.FilterType.MINE);
            mHuntsList.setAdapter(mHuntsAdapter);
        }
    }
}
