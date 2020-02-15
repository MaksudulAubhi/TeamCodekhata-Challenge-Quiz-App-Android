package com.example.challenge;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class QuizActivity extends AppCompatActivity {


    private int total = 0;
    private int correct = 0;
    private int wrong = 0;

    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;

    private TextView mTimeTextView;
    private TextView mScoreTextView;
    private TextView mQuestionNumberTextView;
    private TextView mQuestionTextView;
    private RadioGroup mRadioGroup;
    private RadioButton mOption1, mOption2, mOption3, mOption4;
    CountDownTimer countDownTimer;
    String level,tag,course;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_activity);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(QuizActivity.this, MainActivity.class));
            finish();
        }

         tag = getIntent().getStringExtra("tag");
         course = getIntent().getStringExtra("course");
         level = getIntent().getStringExtra("level");

        setTitle(course + ": " + level);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Quiz").child(tag).child(course).child(level);


        mTimeTextView = (TextView) findViewById(R.id.TimeCount);
        mScoreTextView = (TextView) findViewById(R.id.ScoreCount);
        mQuestionNumberTextView = (TextView) findViewById(R.id.QuestionNumber);
        mQuestionTextView = (TextView) findViewById(R.id.QuestionTextView);
        mRadioGroup = (RadioGroup) findViewById(R.id.QuizRadioGroup);
        mOption1 = (RadioButton) findViewById(R.id.Option1);
        mOption2 = (RadioButton) findViewById(R.id.Option2);
        mOption3 = (RadioButton) findViewById(R.id.Option3);
        mOption4 = (RadioButton) findViewById(R.id.Option4);


        updateQuestion();
        reverseTimer(300, mTimeTextView);


    }

    public void updateQuestion() {
        mRadioGroup.clearCheck();
        mScoreTextView.setText(Integer.toString(correct));
        total++;
        if (10 < total) {
            countDownTimer.cancel();
            Intent myIntent = new Intent(QuizActivity.this, ResultActivity.class);
            myIntent.putExtra("total", String.valueOf(10));
            myIntent.putExtra("correct", String.valueOf(correct));
            myIntent.putExtra("wrong", String.valueOf(wrong));
            myIntent.putExtra("tag",tag);

            myIntent.putExtra("course",course);
            myIntent.putExtra("level",level);
            startActivity(myIntent);
            finish();


        } else {
            mDatabaseRef.child(String.valueOf(total)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String questionNumber = dataSnapshot.getKey();
                    String question = dataSnapshot.child("Question").getValue().toString();
                    String option1 = dataSnapshot.child("a").getValue().toString();
                    String option2 = dataSnapshot.child("b").getValue().toString();
                    String option3 = dataSnapshot.child("c").getValue().toString();
                    String option4 = dataSnapshot.child("d").getValue().toString();
                    final String answer = dataSnapshot.child("answer").getValue().toString();

                    mQuestionNumberTextView.setText(questionNumber+". ");
                    mQuestionTextView.setText(question);
                    mOption1.setText(option1);
                    mOption2.setText(option2);
                    mOption3.setText(option3);
                    mOption4.setText(option4);

                    mOption1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean checked = ((RadioButton) view).isChecked();
                            switch (view.getId()) {
                                case R.id.Option1:
                                    if (checked) {
                                        if (mOption1.getText().equals(answer)) {
                                            mOption1.setTextColor(Color.GREEN);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    correct++;
                                                    mOption1.setTextColor(Color.BLACK);
                                                    updateQuestion();


                                                }
                                            }, 1500);
                                        } else {
                                            wrong++;
                                            mOption1.setTextColor(Color.RED);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    mOption1.setTextColor(Color.BLACK);
                                                    updateQuestion();


                                                }
                                            }, 1500);


                                        }
                                    }

                                default:
                                    break;
                            }

                        }
                    });
                    mOption2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean checked = ((RadioButton) view).isChecked();
                            switch (view.getId()) {
                                case R.id.Option2:
                                    if (checked) {
                                        if (mOption2.getText().equals(answer)) {
                                            mOption2.setTextColor(Color.GREEN);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    correct++;
                                                    mOption2.setTextColor(Color.BLACK);
                                                    updateQuestion();


                                                }
                                            }, 1500);
                                        } else {
                                            wrong++;
                                            mOption2.setTextColor(Color.RED);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    mOption2.setTextColor(Color.BLACK);
                                                    updateQuestion();


                                                }
                                            }, 1500);


                                        }
                                    }

                                default:
                                    break;
                            }

                        }
                    });
                    mOption3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean checked = ((RadioButton) view).isChecked();
                            switch (view.getId()) {
                                case R.id.Option3:
                                    if (checked) {
                                        if (mOption3.getText().equals(answer)) {
                                            mOption3.setTextColor(Color.GREEN);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    correct++;
                                                    mOption3.setTextColor(Color.BLACK);
                                                    updateQuestion();


                                                }
                                            }, 1500);
                                        } else {
                                            wrong++;
                                            mOption3.setTextColor(Color.RED);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    mOption3.setTextColor(Color.BLACK);
                                                    updateQuestion();


                                                }
                                            }, 1500);


                                        }
                                    }

                                default:
                                    break;
                            }

                        }
                    });
                    mOption4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean checked = ((RadioButton) view).isChecked();
                            switch (view.getId()) {
                                case R.id.Option4:
                                    if (checked) {
                                        if (mOption4.getText().equals(answer)) {

                                            mOption4.setTextColor(Color.GREEN);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    correct++;
                                                    mOption4.setTextColor(Color.BLACK);
                                                    updateQuestion();


                                                }
                                            }, 1500);
                                        } else {
                                            wrong++;
                                            mOption4.setTextColor(Color.RED);
                                            Handler handler = new Handler();
                                            handler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {

                                                    mOption4.setTextColor(Color.BLACK);
                                                    updateQuestion();


                                                }
                                            }, 1500);


                                        }
                                    }


                            }

                        }
                    });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    public void reverseTimer(int seconds, final TextView tv) {

        countDownTimer=new CountDownTimer(seconds * 1000 + 1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                int seconds = (int) (millisUntilFinished / 1000);
                int minute = seconds / 60;
                seconds = seconds % 60;
                tv.setText(String.format("%02d", minute) + ":" + String.format("%02d", seconds));

            }

            @Override
            public void onFinish() {
                tv.setText("completed");
                Intent myIntent = new Intent(QuizActivity.this, ResultActivity.class);
                myIntent.putExtra("total", String.valueOf(10));
                myIntent.putExtra("correct", String.valueOf(correct));
                myIntent.putExtra("wrong", String.valueOf(wrong));

                myIntent.putExtra("course",course);
                myIntent.putExtra("level",level);

                startActivity(myIntent);
                finish();

            }
        }.start();

    }

}




