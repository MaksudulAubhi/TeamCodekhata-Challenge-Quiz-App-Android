package com.example.challenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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


///////////............Login activity.......////////////

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailEditText;
    private EditText mPassEditText;
    private Button   mSignInButton;
    private TextView mForgetPassTextView;
    private TextView mSignUpReqTextView;
    ProgressDialog mProgressDialog;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //cheacking connection.....................{{{{{{{
        if (!isConnected(MainActivity.this)) {
            buildDialog(MainActivity.this).show();
        } else {
           // Toast.makeText(MainActivity.this, "LogIn here please", Toast.LENGTH_SHORT).show();
            // setContentView(R.layout.activity_main);
        }
        //////////..............}}}}}}


        mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null && user.isEmailVerified()){
            startActivity(new Intent(MainActivity.this,CoursesActivity.class));
            finish();
        }
        mEmailEditText=(EditText)findViewById(R.id.LoginEmailEditText) ;
        mPassEditText=(EditText)findViewById(R.id.LoginPassEditText) ;
        mSignInButton=(Button)findViewById(R.id.LogInButton);
        mForgetPassTextView=(TextView)findViewById(R.id.loginForgetPassTextView);
        mSignUpReqTextView=(TextView)findViewById(R.id.signUpReqTextView);
        mProgressDialog=new ProgressDialog(this);

        mSignInButton.setOnClickListener(this);
        mForgetPassTextView.setOnClickListener(this);
        mSignUpReqTextView.setOnClickListener(this);

        if(savedInstanceState!=null){
           mEmailEditText.setText(savedInstanceState.getString("Email"));
           mPassEditText.setText(savedInstanceState.getString("password"));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Email",mEmailEditText.getText().toString());
        outState.putString("password",mPassEditText.getText().toString());

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

    public void logInUser(){
        String email=mEmailEditText.getText().toString().trim();
        String password=mPassEditText.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password) || password.length()<6){
            Toast.makeText(getApplicationContext(), "Enter a password at least 6 characters.", Toast.LENGTH_SHORT).show();
            return;
        }

        mProgressDialog.setMessage("Signing please wait....");
        mProgressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if(mAuth.getCurrentUser().isEmailVerified()){
                        //finish the process....
                        finish();
                        startActivity(new Intent(getApplicationContext(), CoursesActivity.class));//start new activity...........
                        Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                    }
                    //finish the process....
                    else{
                        Toast.makeText(getApplicationContext(), "Please verify your email address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
                //end progressing..............
                mProgressDialog.dismiss();
            }

        });
    }


    @Override
    public void onClick(View view) {
        if(view==mSignUpReqTextView){
            startActivity(new Intent(MainActivity.this,RegisterActivity.class));
        }
        if(view==mSignInButton){
            logInUser();
        }
        if(view==mForgetPassTextView){
            startActivity(new Intent(MainActivity.this,ResetPasswordActivity.class));
        }

    }
}
