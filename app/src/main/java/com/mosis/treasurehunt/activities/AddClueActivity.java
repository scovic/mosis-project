package com.mosis.treasurehunt.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mosis.treasurehunt.R;

public class AddClueActivity extends AppCompatActivity {
    Button btnCancel;
    Button btnSave;
    EditText etQuestion;
    EditText etAnswer1;
    EditText etAnswer2;
    EditText etAnswer3;
    EditText etAnswer4;
    TextView tvLatitude;
    TextView tvLongitude;
    RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clue);

        btnCancel = findViewById(R.id.btn_cancel_new_clue);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave = findViewById(R.id.btn_save_clue);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveClue();
            }
        });

        Button getLocation = findViewById(R.id.btn_choose_location);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddClueActivity.this, MapsActivity.class);
                i.putExtra("state", MapsActivity.SELECT_COORDINATES);
                startActivityForResult(i, 1);
            }
        });

        Button btnMyLocatioon = findViewById(R.id.btn_my_location);
        btnMyLocatioon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddClueActivity.this, MapsActivity.class);
                i.putExtra("state", MapsActivity.SELECT_CURRENT_COORDS);
                startActivityForResult(i, 1);
            }
        });
    }

    public void saveClue() {

        etQuestion = findViewById(R.id.et_clue_question);
        etAnswer1 = findViewById(R.id.et_answer1);
        etAnswer2 = findViewById(R.id.et_answer2);
        etAnswer3 = findViewById(R.id.et_answer3);
        etAnswer4 = findViewById(R.id.et_answer4);
        tvLongitude = findViewById(R.id.tv_longitude);
        tvLatitude = findViewById(R.id.tv_latitude);

        String question = etQuestion.getText().toString();
        String answer1 = etAnswer1.getText().toString();
        String answer2 = etAnswer2.getText().toString();
        String answer3 = etAnswer3.getText().toString();
        String answer4 = etAnswer4.getText().toString();
        String correctAnswer = getCorrectAnswer();
        double latitude = Double.parseDouble(tvLatitude.getText().toString());
        double longitude = Double.parseDouble(tvLongitude.getText().toString());

        if (correctAnswer != null) {
            Intent i = new Intent();
            i.putExtra("question", question);
            i.putExtra("answer1", answer1);
            i.putExtra("answer2", answer2);
            i.putExtra("answer3", answer3);
            i.putExtra("answer4", answer4);
            i.putExtra("correct_answer", correctAnswer);
            i.putExtra("lat", latitude);
            i.putExtra("lon", longitude);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    @Nullable
    public String getCorrectAnswer () {
        radioGroup = findViewById(R.id.radio_group);
        int checkedId = radioGroup.getCheckedRadioButtonId();
        String correctAnswer;
        switch (checkedId) {
            case R.id.rdbtn_answer1:
                correctAnswer = "answer1";
                break;
            case R.id.rdbtn_answer2:
                correctAnswer = "answer2";
                break;
            case R.id.rdbtn_answer3:
                correctAnswer = "answer3";
                break;
            case R.id.rdbtn_answer4:
                correctAnswer = "answer4";
                break;
            default:
                correctAnswer = null;
        }

        return  correctAnswer;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            String latitude = data.getExtras().getString("lat");
            String longitude = data.getExtras().getString("lon");

            tvLatitude = findViewById(R.id.tv_latitude);
            tvLongitude = findViewById(R.id.tv_longitude);

            tvLatitude.setText(latitude);
            tvLongitude.setText(longitude);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
