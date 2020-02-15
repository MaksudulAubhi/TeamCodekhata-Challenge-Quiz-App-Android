package com.example.challenge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

public class CoursesActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    LinearLayout mLinLay;
    String tagText, courseText;

    DatabaseReference mDataBaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                startActivity(new Intent(CoursesActivity.this, NotifiacationActivity.class));

            }
        });

        //cheacking connection.....................{{{{{{{
        if (!isConnected(CoursesActivity.this)) {
            buildDialog(CoursesActivity.this).show();
        } else {
            // Toast.makeText(MainActivity.this, "LogIn here please", Toast.LENGTH_SHORT).show();
            // setContentView(R.layout.activity_main);
        }
        //////////..............}}}}}}


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(CoursesActivity.this, MainActivity.class));
            finish();
        }

        mDataBaseRef = FirebaseDatabase.getInstance().getReference().child("Quiz");
        mLinLay = (LinearLayout) findViewById(R.id.linearLayout);


        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot tag1Snapshot : dataSnapshot.getChildren()) {
                    final String tag = tag1Snapshot.getKey().toString();
                    String key = tag1Snapshot.getKey();


                    TextView textView = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    lp2.setMargins(11, 11, 11, 11);
                    textView.setLayoutParams(lp2);
                    textView.setText(tag);
                    textView.setTextColor(Color.parseColor("#0000ff"));
                    textView.setTextSize(19);
                    textView.setBackgroundColor(Color.CYAN);
                    textView.setPadding(4, 4, 4, 4);
                    mLinLay.addView(textView);
                    ////
                    for (DataSnapshot courseSnapshot : dataSnapshot.child(tag).getChildren()) {
                        final String course = courseSnapshot.getKey().toString();


                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        lp.setMargins(5, 5, 5, 5);
                        LinearLayout linearLayout = new LinearLayout(getApplicationContext());

                        linearLayout.setLayoutParams(lp);

                        Button btn = new Button(getApplicationContext());

                        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        lp1.setMargins(11, 11, 11, 11);
                        btn.setLayoutParams(lp1);
                        btn.setHeight(200);

                        btn.setText(course);

                        linearLayout.addView(btn);

                        mLinLay.addView(linearLayout);


                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), course, Toast.LENGTH_SHORT).show();
                                tagText = tag;
                                courseText = course;


                                Intent myIntent = new Intent(view.getContext(), LevelActivity.class);
                                myIntent.putExtra("tag", tagText);
                                myIntent.putExtra("course", courseText);
                                startActivity(myIntent);
                            }
                        });


                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    public boolean isConnected(Context context) {    //cheak network connection
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo np = cm.getActiveNetworkInfo();
        if (np != null && np.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mobile != null && mobile.isConnectedOrConnecting() || wifi != null && wifi.isConnectedOrConnecting()) {
                return true;
            } else
                return false;
        } else return false;
    }

    AlertDialog.Builder buildDialog(Context context) {        ////////for dialog alert message;;;;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sorry, No Internet Connection.");
        builder.setMessage("You need to have Mobile Data or Wifi to access this. Press OK to exit.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        return builder;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.profileLogo:
                startActivity(new Intent(CoursesActivity.this, ProfileActivity.class));
                return true;
            case R.id.aboutText:
                startActivity(new Intent(CoursesActivity.this, AboutActivity.class));
                return true;
            case R.id.rankText:
                startActivity(new Intent(CoursesActivity.this, RankActivity.class));
                return true;
            case R.id.settingText:
                startActivity(new Intent(CoursesActivity.this,SettingsActivity.class));
                return true;
            case R.id.logoutTextCourses:
                mAuth.signOut();
                startActivity(new Intent(CoursesActivity.this, MainActivity.class));
                finish();
                return true;
            case R.id.RefreshTextCourses:
                startActivity(new Intent(CoursesActivity.this, CoursesActivity.class));
                finish();
                return true;
            case R.id.tipText:
                startActivity(new Intent(CoursesActivity.this, TipsActivity.class));
                return true;
            case R.id.blogText:
                startActivity(new Intent(CoursesActivity.this, WebViewActivity.class));
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
