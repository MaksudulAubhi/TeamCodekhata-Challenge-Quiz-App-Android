package com.example.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ResultActivity extends AppCompatActivity {

    String score, scoreString, correct, question, tag, wrong, id, course, level, correct1;
    int totalScore = 0;
    boolean has = false;
    int count = 0;
    private DatabaseReference mDatabaseRef, mDatabaseRef1, mDatabaseRef2, mDatabaseRef3;
    private FirebaseAuth mAuth;
    private TextView mTotal, mCorrect, mWrong;
    private Button mShowAnswerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        setTitle("Result");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(ResultActivity.this, MainActivity.class));
            finish();
        }
        String id = user.getUid();
        final String course = getIntent().getStringExtra("course");
        final String level = getIntent().getStringExtra("level");


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("score").child(id).child(course).child(level);
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("Total").child(id);
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference().child("Total").child(id);
        mDatabaseRef3 = FirebaseDatabase.getInstance().getReference().child("score").child(id);


        mTotal = (TextView) findViewById(R.id.TotalQuestionNumber);
        mCorrect = (TextView) findViewById(R.id.CorrectAnswerNumber);
        mWrong = (TextView) findViewById(R.id.WrongAnswerNumber);
        mShowAnswerButton = (Button) findViewById(R.id.ShowAnswerButton);


        question = getIntent().getStringExtra("total");
        correct = getIntent().getStringExtra("correct");
        tag = getIntent().getStringExtra("tag");
        wrong = getIntent().getStringExtra("wrong");

        correct1 = correct;
        count = Integer.parseInt(correct1);

        if (count < 6) {
            mShowAnswerButton.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Sorry, score not add. At-least 6 answer must be" +
                    " corrected.", Toast.LENGTH_SHORT).show();

        }


        mDatabaseRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {


                    String key = snapshot1.getKey();

                    if (key.equals(course)) {
                        for (DataSnapshot snapshot2 : dataSnapshot.child(key).getChildren()) {
                            String key1 = snapshot2.getKey();
                            if (key1.equals(level)) {

                                String newScore = dataSnapshot.child(key).child(key1).child("score").getValue(String.class);
                                count = 0;
                                mShowAnswerButton.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Sorry, new score not add. You answered before" +
                                        " this question", Toast.LENGTH_SHORT).show();
                            }


                        }
                    } else {
                        scoreAdd();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });


        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(ResultActivity.this, AnswerActivity.class);
                myIntent.putExtra("course", course);
                myIntent.putExtra("level", level);
                myIntent.putExtra("tag", tag);

                startActivity(myIntent);
                finish();

            }
        });


        mTotal.setText(question);
        mCorrect.setText(correct);
        mWrong.setText(wrong);


    }

    public void scoreAdd() {
        if (count >= 6) {
            correct = getIntent().getStringExtra("correct");


            ScoreUpdate scoreUpdate = new ScoreUpdate(correct);

            mDatabaseRef.setValue(scoreUpdate);

            mDatabaseRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    score = dataSnapshot.child("totalScore").getValue().toString();
                    String userName = dataSnapshot.child("userName").getValue().toString(); //previous score
                    String institution=dataSnapshot.child("institution").getValue().toString();
                    totalScore = Integer.parseInt(score) + count;                           //new Score
                    scoreString = String.valueOf(totalScore);


                    TotalScoreUpdate totalScoreUpdate = new TotalScoreUpdate(totalScore, userName,institution);
                    mDatabaseRef1.setValue(totalScoreUpdate);



                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }


    }


}
