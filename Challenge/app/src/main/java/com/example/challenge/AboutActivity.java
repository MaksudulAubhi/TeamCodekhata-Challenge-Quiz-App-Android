package com.example.challenge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textView=(TextView)findViewById(R.id.textAbout);

        setTitle("About");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


}
