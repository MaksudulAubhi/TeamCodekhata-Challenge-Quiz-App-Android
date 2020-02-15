package com.example.challenge;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LevelActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private FirebaseAuth mAuth;
    LinearLayout mLinLay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user==null){
            startActivity(new Intent(LevelActivity.this,MainActivity.class));
            finish();
        }

        mLinLay=(LinearLayout)findViewById(R.id.linearLayout);
        final String tag=getIntent().getStringExtra("tag");
        final String course=getIntent().getStringExtra("course");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Quiz").child(tag).child(course);
        setTitle(tag+": "+course);


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot levelSnapshot: dataSnapshot.getChildren()){
                    final String level=levelSnapshot.getKey();

                   Button btn=new Button(getApplicationContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    lp.setMargins(11,11,11,11);

                    btn.setLayoutParams(lp);
                    btn.setText(level);
                    btn.setTextSize(19);
                    btn.setPadding(11,11,11,11);
                    btn.setHeight(100);


                    mLinLay.addView(btn);


                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(getApplicationContext(),level,Toast.LENGTH_SHORT).show();
                            Intent myIntent=new Intent(view.getContext(), QuizActivity.class);
                            myIntent.putExtra("tag",tag);
                            myIntent.putExtra("course",course);
                            myIntent.putExtra("level",level);
                            startActivity(myIntent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
