package com.example.challenge;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    String url;
    ProgressDialog mProgressDialog;
    private TextView mProfileScoreTextView;
    private TextView mRankTextView;
    private TextView mNameTextView;
    private TextView mUserNameTextView;
    private TextView mGenderTextView;
    private TextView mInstitutionTextView;
    private TextView mEditProReqTextView;
    private TextView mDeleteAccountTextView;
    private TextView mLogOutTextView;
    private ImageView mProfileImageView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef, mDatabaseRef1, mScoreDatabaseRef, mDatabaseRef2, mDatabaseRefDel;
    private StorageReference mStorageRef;
    private StorageReference mStorageRefDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        if (user == null) {
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
        }


        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Profile").child(userId);
        mDatabaseRefDel = FirebaseDatabase.getInstance().getReference("Profile").child(userId);
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference("score").child(userId);
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("Total").child(userId);
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePhoto").child(userId);
        mStorageRefDel = mStorageRef.child(userId);
        mScoreDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Total").child(userId);

        mProfileScoreTextView = (TextView) findViewById(R.id.profile_Score_textView);
        mRankTextView = (TextView) findViewById(R.id.profile_rank_textView);
        mProfileImageView = (ImageView) findViewById(R.id.EditProfileImage);
        mNameTextView = (TextView) findViewById(R.id.profile_name_textView);
        mUserNameTextView = (TextView) findViewById(R.id.profile_UserName_textView);
        mGenderTextView = (TextView) findViewById(R.id.profile_gender_textView);
        mInstitutionTextView = (TextView) findViewById(R.id.profile_Institution_textView);
        mEditProReqTextView = (TextView) findViewById(R.id.EditProfileReq);
        mDeleteAccountTextView = (TextView) findViewById(R.id.DeleteProfileReq);
        mLogOutTextView = (TextView) findViewById(R.id.LogoutProfileReq);
        mProgressDialog = new ProgressDialog(this);


        mEditProReqTextView.setOnClickListener(this);
        mDeleteAccountTextView.setOnClickListener(this);
        mLogOutTextView.setOnClickListener(this);


        mScoreDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String score = dataSnapshot.child("totalScore").getValue().toString();
                mProfileScoreTextView.setText(score);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                url = dataSnapshot.child("imageUri").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String username = dataSnapshot.child("username").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String Institution = dataSnapshot.child("institution").getValue().toString();


                mNameTextView.setText(name);
                mUserNameTextView.setText(username);
                mGenderTextView.setText(gender);
                mInstitutionTextView.setText(Institution);

                if (url.isEmpty() == false) {
                    Picasso.with(ProfileActivity.this)
                            .load(url)
                            .resize(500, 500)
                            .centerInside()
                            .placeholder(R.drawable.ic_action_camera)
                            .into(mProfileImageView);


                    mNameTextView.setText(name);
                    mUserNameTextView.setText(username);
                    mGenderTextView.setText(gender);
                    mInstitutionTextView.setText(Institution);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteInfo() {
        //FirebaseUser user=mAuth.getCurrentUser();
        //String UserId=user.getUid();
       // mDatabaseRefDel.removeValue();
       // mDatabaseRef1.removeValue();
        mDatabaseRef2.removeValue();

    }

    public void deleteAccount() {
        final FirebaseUser user = mAuth.getCurrentUser();

        mProgressDialog.setMessage("Deleting.....");
        mProgressDialog.show();


        assert user != null;
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(ProfileActivity.this, MainActivity.class));

                    mProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });

    }


    public void deletePhoto() {
        mStorageRefDel.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    AlertDialog.Builder buildDialog(Context context) {////////for dialog alert message;;;;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // builder.setTitle("");
        builder.setMessage("Delete your account?");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                deleteAccount();
                deletePhoto();
                deleteInfo();
                //startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                //finish();


            }
        });
        return builder;
    }


    @Override
    public void onClick(View view) {
        if (view == mEditProReqTextView) {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        }
        if (view == mDeleteAccountTextView) {

            //deletePhoto();
            buildDialog(ProfileActivity.this).show();

            // deleteAccount();


            // deleteInfo();

        }
        if(view == mLogOutTextView) {

            mAuth.signOut();
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();

        }

    }

}
