package com.example.challenge;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText mEmailEditText;
    private EditText mPassEditText;
    private EditText mConfirmPassEditText;
    private EditText mUserNameEditText;
    private Button   mSignUpButton;
    private TextView mSignInReqTextView;
    ProgressDialog   mProgressDialog;


    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef,mDatabaseRef1,mDatabaseRef3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null && user.isEmailVerified()){
            startActivity(new Intent(RegisterActivity.this,CoursesActivity.class));
            finish();
        }
        mDatabaseRef=FirebaseDatabase.getInstance().getReference("Profile");


        mEmailEditText=(EditText)findViewById(R.id.RegisterEmailEditText);
        mPassEditText=(EditText)findViewById(R.id.RegisterPassEditText);
        mConfirmPassEditText=(EditText)findViewById(R.id.RegisterConfirmPassEditText);
        mUserNameEditText=(EditText)findViewById(R.id.RegisterUserNameEditText);
        mSignUpButton=(Button)findViewById(R.id.SignUpButton);
        mSignInReqTextView=(TextView)findViewById(R.id.signInReqTextView);
        mProgressDialog=new ProgressDialog(this);

        mSignUpButton.setOnClickListener(this);
        mSignInReqTextView.setOnClickListener(this);

        if(savedInstanceState!=null){
            mEmailEditText.setText(savedInstanceState.getString("Email"));
            mPassEditText.setText(savedInstanceState.getString("Password"));
            mConfirmPassEditText.setText(savedInstanceState.getString("ConPass"));
            mUserNameEditText.setText(savedInstanceState.getString("UserName"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Email",mEmailEditText.getText().toString());
        outState.putString("Password",mPassEditText.getText().toString());
        outState.putString("ConPass",mConfirmPassEditText.getText().toString());
        outState.putString("UserName",mUserNameEditText.getText().toString());
    }

    public void registerUser(){
        String email=mEmailEditText.getText().toString().trim();
        String password=mPassEditText.getText().toString().trim();
        String conPassword=mConfirmPassEditText.getText().toString().trim();
        final String userName=mUserNameEditText.getText().toString().trim();



        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password) || password.length()<6){
            Toast.makeText(getApplicationContext(), "Enter a password at least 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(conPassword) || !conPassword.equals(password)){
            Toast.makeText(getApplicationContext(), "Confirm password not match.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(getApplicationContext(), "Enter your username.", Toast.LENGTH_SHORT).show();
            return;
        }


        mProgressDialog.setMessage("Registering please wait....");
        mProgressDialog.show();


        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful()){

                                    createUserChild();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(userName).build();
                                    mAuth.getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));//start new activity...........
                                    Toast.makeText(getApplicationContext(),
                                            "Registration successful.Please verify your email address and sign in your account. ", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(), "Something wrong please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                    });
                }
                else{
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
                mProgressDialog.dismiss();

            }
        });

    }
    public void createUserChild(){
        final String name="";
        final String gender="";
        final String institution="";
        final String imageUri="";
        final String userId=mAuth.getCurrentUser().getUid();
        final String userName=mUserNameEditText.getText().toString().trim();
        mDatabaseRef1=FirebaseDatabase.getInstance().getReference("Total").child(userId);

        ProfileUpload profile = new ProfileUpload(name, userName, gender, institution,imageUri);
        mDatabaseRef.child(userId).setValue(profile);
        TotalScoreUpdate totalScoreUpdate=new TotalScoreUpdate(0,userName,institution);
        mDatabaseRef1.setValue(totalScoreUpdate);

        mDatabaseRef3 =FirebaseDatabase.getInstance().getReference("score").child(userId);
        UserName username=new UserName(userName);
        mDatabaseRef3.setValue(username);
    }


    @Override
    public void onClick(View view) {
        if(view==mSignUpButton){
            registerUser();

        }
        if(view==mSignInReqTextView){
            startActivity(new Intent(RegisterActivity.this,MainActivity.class));
        }
    }
}
