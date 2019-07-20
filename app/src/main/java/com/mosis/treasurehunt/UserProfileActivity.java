package com.mosis.treasurehunt;

import android.content.Intent;
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

import com.mosis.treasurehunt.adapters.HuntAdapter;
import com.mosis.treasurehunt.models.Hunt;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {
    private Spinner spinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button btnAddHunt;
    private ListView mHuntsList;
    private HuntAdapter mHuntsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        spinner = findViewById(R.id.spinner_user_profile);
        adapter = ArrayAdapter.createFromResource(this, R.array.type_of_hunts_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
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

    private void setHuntAdapter(String item) {
        mHuntsList = findViewById(R.id.hunts_list);
        ArrayList<Hunt> huntsList = new ArrayList<>();

        if (item == "Active Hunts") {
            // filter baze za huntove kod kojih je completed=false
            huntsList.add(new Hunt("Test Active Hunt"));
            mHuntsAdapter = new HuntAdapter(this, huntsList);
            mHuntsAdapter.setmFilter(HuntAdapter.FilterType.ACTIVE);
            mHuntsList.setAdapter(mHuntsAdapter);
        } else if (item == "Completed Hunts") {
            // ovde filter svih huntova kod kojih je completed=true
            huntsList.add(new Hunt("Test Completed Hunt"));
            mHuntsAdapter = new HuntAdapter(this, huntsList);
            mHuntsAdapter.setmFilter(HuntAdapter.FilterType.COMPLETED);
            mHuntsList.setAdapter(mHuntsAdapter);
        } else if (item == "My Hunts") {
            // filter gde je user == ulogovani user
            huntsList.add(new Hunt("Test My Hunt"));
            mHuntsAdapter = new HuntAdapter(this, huntsList);
            mHuntsAdapter.setmFilter(HuntAdapter.FilterType.MINE);
            mHuntsList.setAdapter(mHuntsAdapter);
        }
    }
}
