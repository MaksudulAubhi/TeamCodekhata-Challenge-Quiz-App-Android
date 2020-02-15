package com.example.challenge;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TipsActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    LinearLayout mLinLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tips);

        setTitle("Tips");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
       // String userId = user.getUid();
        mLinLay = (LinearLayout) findViewById(R.id.linearLayout);


        if (user == null) {
            startActivity(new Intent(TipsActivity.this, MainActivity.class));
        }
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Tips");


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    String key=snapshot.getKey();
                    String value=dataSnapshot.child(key).getValue().toString();

                    TextView textView = new TextView(getApplicationContext());

                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );

                    textView.setLayoutParams(lp2);
                    textView.setText(key+"\n"+value+"\n\n");
                    textView.setTextColor(Color.parseColor("#000000"));
                    textView.setTextSize(20);
                    textView.setBackgroundColor(Color.parseColor("#f2f2f2"));
                    textView.setPadding(10, 10, 10, 10);
                    textView.setTextIsSelectable(true);

                    mLinLay.addView(textView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
