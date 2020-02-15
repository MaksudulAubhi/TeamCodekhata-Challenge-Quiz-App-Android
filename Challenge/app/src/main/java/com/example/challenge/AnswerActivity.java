package com.example.challenge;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnswerActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    LinearLayout mLinLay;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        setTitle("Answer");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(AnswerActivity.this, MainActivity.class));
            finish();
        }

        final String course = getIntent().getStringExtra("course");
        final String level = getIntent().getStringExtra("level");
        final String tag=getIntent().getStringExtra("tag");

        mLinLay=(LinearLayout)findViewById(R.id.linearLayout);

        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Quiz").child(tag).child(course).child(level);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String id=snapshot.getKey();

                    String question=dataSnapshot.child(id).child("Question").getValue().toString();
                    String option1=dataSnapshot.child(id).child("a").getValue().toString();
                    String option2=dataSnapshot.child(id).child("b").getValue().toString();
                    String option3=dataSnapshot.child(id).child("c").getValue().toString();
                    String option4=dataSnapshot.child(id).child("d").getValue().toString();
                    String answer=dataSnapshot.child(id).child("answer").getValue().toString();

                    TextView textView = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    lp2.setMargins(11, 11, 11, 11);
                    textView.setLayoutParams(lp2);
                    textView.setText(id+". question: "+question+"\n"+
                                     "a. "+option1+"\n"+
                                    "b. "+option2+"\n"+
                                    "c. "+option3+"\n"+
                                    "d. "+option4+"\n"+
                                    "answer: "+answer);
                    textView.setTextColor(Color.parseColor("#0000ff"));
                    textView.setTextSize(20);
                    textView.setBackgroundColor(Color.CYAN);
                    textView.setPadding(4, 4, 4, 4);
                    mLinLay.addView(textView);




                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}
