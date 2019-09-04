package com.mosis.treasurehunt.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.models.Clue;
import com.mosis.treasurehunt.models.Hunt;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

public class AnswerClueActivity extends AppCompatActivity {
    TextView tv_question;
    TextView tv_answer1;
    TextView tv_answer2;
    TextView tv_answer3;
    TextView tv_answer4;
    Button btn_submit_answer;
    Button btn_cancel;

    String[] answers;
    User user;
    Clue clue;
    Hunt hunt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_clue);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent clueInten = getIntent();
        Bundle bundle = clueInten.getExtras();

        if (bundle != null) {
            String title = bundle.getString(MapsActivity.CLUE_TITLE);
            user = UserRepository.getInstance()
                    .getUserByUsername(SharedPreferencesWrapper.getInstance().getUsername());
            hunt = user.getJoinedHunt(title);
            clue = hunt.findFirstUnansweredClue();
            String question = clue.getQuestion();
//            answers = (String[])  clue.getAnswers().keySet().toArray();
            answers = new String[4];
            for (int i = 0; i < clue.getAnswers().keySet().toArray().length; i++) {
                answers [i] = clue.getAnswers().keySet().toArray()[i].toString();
            }

            tv_question = findViewById(R.id.tv_answer_clue_question);
            tv_answer1 = findViewById(R.id.tv_answer1_answer_clue);
            tv_answer2 = findViewById(R.id.tv_answer2_answer_clue);
            tv_answer3 = findViewById(R.id.tv_answer3_answer_clue);
            tv_answer4 = findViewById(R.id.tv_answer4_answer_clue);

            tv_question.setText(question);
            tv_answer1.setText(answers[0]);
            tv_answer2.setText(answers[1]);
            tv_answer3.setText(answers[2]);
            tv_answer4.setText(answers[3]);

            btn_cancel = findViewById(R.id.btn_cancel);
            btn_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            });

            btn_submit_answer = findViewById(R.id.btn_submit_answer);
            btn_submit_answer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String answer = getAnswer();
                    if (answer != null) {
                        if (clue.getAnswers().get(answer)) {
                            Toast.makeText(AnswerClueActivity.this,"Correct!", Toast.LENGTH_SHORT).show();
                            hunt.answerClue(clue);
                            user.addPoints(10);
                            if (hunt.isCompleted()) {
                                User realOwner = UserRepository.getInstance().getUserByUsername(hunt.getOwner());
                                realOwner.completeHunt(hunt.getTitle());
                                UserRepository.getInstance().updateUser(realOwner);
                                user.addPoints(30);
                            }

                            UserRepository.getInstance().updateUser(user);
                            setResult(Activity.RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(AnswerClueActivity.this,"Sorry, that is not correct!", Toast.LENGTH_SHORT).show();
                            setResult(Activity.RESULT_CANCELED);
                            finish();
                        }
                    } else {
                        Toast.makeText(AnswerClueActivity.this,"Please choose an answer!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Nullable
    public String getAnswer() {
        RadioGroup radioGroup = findViewById(R.id.radio_group_answer_clue);
        int checkedId = radioGroup.getCheckedRadioButtonId();
        String answer;
        switch (checkedId) {
            case R.id.rdbtn_answer1_answer_clue:
                answer = answers[0];
                break;
            case R.id.rdbtn_answer2_answer_clue:
                answer = answers[1];
                break;
            case R.id.rdbtn_answer3_answer_clue:
                answer = answers[2];
                break;
            case R.id.rdbtn_answer4_answer_clue:
                answer = answers[3];
                break;
            default:
                answer = null;
        }

        return answer;
    }

}
