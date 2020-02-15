package com.example.challenge;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
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

public class NotifiacationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    DatabaseReference mDatabaseRef;
    LinearLayout mLinLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifiacation);
        setTitle("Notifications");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        mLinLay = (LinearLayout) findViewById(R.id.linearLayout);


        if (user == null) {
            startActivity(new Intent(NotifiacationActivity.this, MainActivity.class));
        }
        mDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Notifications");
        notificationsShow();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);





    }

    public void notificationsShow(){
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    final String key=snapshot.getKey();
                    mDatabaseRef.orderByChild(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String notification=dataSnapshot.child(key).getValue().toString();


                            TextView textView = new TextView(getApplicationContext());
                            TextView textView1 = new TextView(getApplicationContext());
                            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );
                            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT
                            );

                            lp1.setMargins(11, 11, 11, 6);

                            lp2.setMargins(11, 6, 11, 70);
                            textView1.setLayoutParams(lp1);
                            textView1.setText(key);
                            textView1.setTextColor(Color.parseColor("#a6a6a6"));
                            textView1.setTextSize(20);
                            textView1.setPadding(10, 10, 10, 10);
                            mLinLay.addView(textView1);


                            textView.setLayoutParams(lp2);
                            textView.setText(notification);
                            textView.setTextColor(Color.parseColor("#000000"));
                            textView.setTextSize(20);
                            textView.setBackgroundColor(Color.parseColor("#f2f2f2"));
                            textView.setPadding(10, 10, 10, 10);
                            textView.setTextIsSelectable(true);

                            //Linkify.addLinks(textView,Linkify.WEB_URLS);
                            //textView.setLinksClickable(true);
                            mLinLay.addView(textView);




                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

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
