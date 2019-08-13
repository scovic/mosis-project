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

import com.mosis.treasurehunt.R;

public class AddClueActivity extends AppCompatActivity {
    Button btnCancel;
    Button btnSave;
    EditText etQuestion;
    EditText etAnswer1;
    EditText etAnswer2;
    EditText etAnswer3;
    EditText etAnswer4;
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


    }

    public void saveClue() {

        etQuestion = findViewById(R.id.et_clue_question);
        etAnswer1 = findViewById(R.id.et_answer1);
        etAnswer2 = findViewById(R.id.et_answer2);
        etAnswer3 = findViewById(R.id.et_answer3);
        etAnswer4 = findViewById(R.id.et_answer4);

        String question = etQuestion.getText().toString();
        String answer1 = etAnswer1.getText().toString();
        String answer2 = etAnswer2.getText().toString();
        String answer3 = etAnswer3.getText().toString();
        String answer4 = etAnswer4.getText().toString();
        String correctAnswer = getCorrectAnswer();

        if (correctAnswer != null) {
            Intent i = new Intent();
            i.putExtra("question", question);
            i.putExtra("answer1", answer1);
            i.putExtra("answer2", answer2);
            i.putExtra("answer3", answer3);
            i.putExtra("answer4", answer4);
            i.putExtra("correct_answer", correctAnswer);
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
}
