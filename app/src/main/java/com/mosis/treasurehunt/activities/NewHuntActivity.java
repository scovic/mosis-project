package com.mosis.treasurehunt.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.adapters.ClueAdapter;
import com.mosis.treasurehunt.models.Clue;
import com.mosis.treasurehunt.models.Feed;
import com.mosis.treasurehunt.models.Hunt;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.FeedRepository;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import java.util.HashMap;

public class NewHuntActivity extends AppCompatActivity {
    private Button mBtnAddClue;
    private Button mBtnSaveHunt;
    private Hunt mHunt;
    private UserRepository mUserRepo;
    private FeedRepository mFeedRepo;
    private SharedPreferencesWrapper mSharedPrefWrapper;
    private ClueAdapter mClueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_hunt);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSharedPrefWrapper = SharedPreferencesWrapper.getInstance();
        mUserRepo = UserRepository.getInstance();
//        mFeedRepo = FeedRepository.getInstance();
        mHunt = new Hunt();
        mHunt.setOwner(mSharedPrefWrapper.getUsername());
        mClueAdapter = new ClueAdapter(this, mHunt.getClues(), mHunt);

        mBtnAddClue = findViewById(R.id.btn_add_clue);
        mBtnAddClue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewHuntActivity.this, AddClueActivity.class);
                startActivityForResult(i, 1);
            }
        });

        mBtnSaveHunt = findViewById(R.id.btn_save_hunt);
        mBtnSaveHunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView huntNameTextView = findViewById(R.id.et_hunt_name);
                String huntName = huntNameTextView.getText().toString().trim();
                mHunt.setTitle(huntName);

                String username = mSharedPrefWrapper.getUsername();
                User user = mUserRepo.getUserByUsername(username);
                if (mHunt.getNumberOfClues() > 0 && mHunt.getTitle() != null) {
                    mUserRepo.addHunt(user, mHunt);
//                    Feed feed = new Feed(user, mHunt, Feed.Type.CREATE);
//                    mFeedRepo.addFeed(feed);
                    Toast.makeText(NewHuntActivity.this, "Hunt created", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(NewHuntActivity.this, "Can't create hunt with zero clues!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ListView listViewClues = findViewById(R.id.lv_clue_list);
        listViewClues.setAdapter(mClueAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String question = data.getStringExtra("question");

            String answer1 = data.getStringExtra("answer1");
            String answer2 = data.getStringExtra("answer2");
            String answer3 = data.getStringExtra("answer3");
            String answer4 = data.getStringExtra("answer4");
            String correctAnswer = data.getStringExtra("correct_answer");
            double latitude = data.getDoubleExtra("lat", 0);
            double longitude = data.getDoubleExtra("lon", 0);

            Clue clue = new Clue(question, mHunt);
            clue.setLatitude(latitude);
            clue.setLongitude(longitude);
            boolean isCorrect = false;
            if (correctAnswer.equals("answer1")) {
                isCorrect = true;
            }
            clue.addAnswer(answer1, isCorrect);

            isCorrect = correctAnswer.equals("answer2");
            clue.addAnswer(answer2, isCorrect);

            isCorrect = correctAnswer.equals("answer3");
            clue.addAnswer(answer3, isCorrect);

            isCorrect = correctAnswer.equals("answer4");
            clue.addAnswer(answer4, isCorrect);

            // Need to implement adding a location (:
            mHunt.addClue(clue);
            mClueAdapter.updateContet(mHunt.getClues());
        }
    }

}
