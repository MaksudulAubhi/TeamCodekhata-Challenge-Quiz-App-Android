package com.example.challenge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetEmailActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog mProgressDialog;
    private EditText mUpdateEmailEditText;
    private Button mUpdateSendEmailButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_email);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(ResetEmailActivity.this, MainActivity.class));
            finish();
        }

        mUpdateEmailEditText = (EditText) findViewById(R.id.ResetEmailEditText);
        mUpdateSendEmailButton = (Button) findViewById(R.id.ResetPassEmailButton);
        mProgressDialog = new ProgressDialog(this);

        mUpdateSendEmailButton.setOnClickListener(this);

        if (savedInstanceState != null) {
            mUpdateEmailEditText.setText(savedInstanceState.getString("ResetPassEmail"));
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("ResetPassEmail", mUpdateEmailEditText.getText().toString());
    }

    public void resetEmail() {
        String email = mUpdateEmailEditText.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.setMessage("Sending email.Please wait....");
        mProgressDialog.show();

        mAuth.getCurrentUser().updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                mProgressDialog.dismiss();
                                finish();
                                Toast.makeText(getApplicationContext(),
                                        "New email successfully updated.Check your email to verify your email.", Toast.LENGTH_SHORT).show();
                                mAuth.signOut();
                                startActivity(new Intent(ResetEmailActivity.this, MainActivity.class));

                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
         if(view==mUpdateSendEmailButton){
             resetEmail();
         }
    }
}
