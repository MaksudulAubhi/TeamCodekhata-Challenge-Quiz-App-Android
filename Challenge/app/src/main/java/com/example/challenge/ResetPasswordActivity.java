package com.example.challenge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity implements  View.OnClickListener{

    private EditText mResetEmailEditText;
    private Button   mResetSendEmailButton;
    ProgressDialog mProgressDialog;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        mAuth=FirebaseAuth.getInstance();

        mResetEmailEditText=(EditText)findViewById(R.id.ResetEmailEditText);
        mResetSendEmailButton=(Button) findViewById(R.id.ResetPassEmailButton);
        mProgressDialog=new ProgressDialog(this);

        mResetSendEmailButton.setOnClickListener(this);

        if(savedInstanceState!=null){
            mResetEmailEditText.setText(savedInstanceState.getString("ResetPassEmail"));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("ResetPassEmail",mResetEmailEditText.getText().toString());
    }

    public void resetPassword(){
        String email=mResetEmailEditText.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.setMessage("Sending email.Please wait....");
        mProgressDialog.show();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"check your email to reset password",Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                }

                mProgressDialog.dismiss();

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view==mResetSendEmailButton){
            resetPassword();
        }

    }
}
