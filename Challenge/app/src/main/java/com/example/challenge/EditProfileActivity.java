package com.example.challenge;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int IMAGE_REQUEST = 1;
    ProgressDialog mProgressDialog;
    private ImageView mProfileImage;
    private Button mChooseImageButton;
    private EditText mNameEditText;
    private EditText mUserNameEditText;
    private Spinner mGenderSpinner;
    private EditText mInstitutionEditText;
    private Button mSaveButton;
    private TextView mChangePassTextView;
    private TextView mChangeEmailTextView;
    private TextView mDeleteProPhotoTextView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseRef1,mDatabaseRef2,mDatabaseRef3,mDatabaseRef4;
    private StorageReference mStorageRef;
    private StorageReference mStorageRefDel;
    private Uri mImageUri;
    private Uri mDownloadUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        setTitle("Profile");

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        if (user == null) {
            startActivity(new Intent(EditProfileActivity.this, MainActivity.class));
            finish();
        }


        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Profile");
        mDatabaseRef1 = FirebaseDatabase.getInstance().getReference().child("Profile").child(userId);
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePhoto").child(userId);
        mStorageRefDel = mStorageRef.child(userId);

        mDatabaseRef2=FirebaseDatabase.getInstance().getReference("Total").child(userId);
        mDatabaseRef3 =FirebaseDatabase.getInstance().getReference("score").child(userId);

        mDatabaseRef4=FirebaseDatabase.getInstance().getReference("Total").child(userId);


        mProfileImage = (ImageView) findViewById(R.id.EditProfileImage);
        mChooseImageButton = (Button) findViewById(R.id.ChooseImageEditPro);
        mNameEditText = (EditText) findViewById(R.id.FullNameEditText);
        mUserNameEditText = (EditText) findViewById(R.id.UserNameEditText);
        mGenderSpinner = (Spinner) findViewById(R.id.SelectGenderSpinner);
        mInstitutionEditText = (EditText) findViewById(R.id.InstitutionNameEditText);
        mSaveButton = (Button) findViewById(R.id.EditProfileSaveButton);
        mChangePassTextView = (TextView) findViewById(R.id.ChangePasswordTextViewEditProfile);
        mChangeEmailTextView = (TextView) findViewById(R.id.ChangeEmailTextViewEditProfile);
        mDeleteProPhotoTextView = (TextView) findViewById(R.id.DeleteUserPhoto);

        mProgressDialog = new ProgressDialog(this);

        mChooseImageButton.setOnClickListener(this);
        mSaveButton.setOnClickListener(this);
        mChangePassTextView.setOnClickListener(this);
        mChangeEmailTextView.setOnClickListener(this);
        mDeleteProPhotoTextView.setOnClickListener(this);

        if (savedInstanceState != null) {
            mImageUri = savedInstanceState.getParcelable("url");
            mProfileImage.setImageURI(mImageUri);
            mNameEditText.setText(savedInstanceState.getString("name"));
            mUserNameEditText.setText(savedInstanceState.getString("UserName"));
            mInstitutionEditText.setText(savedInstanceState.getString("Institution"));
        }


        mDatabaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String url = dataSnapshot.child("imageUri").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String username = dataSnapshot.child("username").getValue().toString();
                String gender = dataSnapshot.child("gender").getValue().toString();
                String Institution = dataSnapshot.child("institution").getValue().toString();


                mNameEditText.setText(name);
                mUserNameEditText.setText(username);
                mInstitutionEditText.setText(Institution);
                if (url.isEmpty() == false) {
                    Picasso.with(EditProfileActivity.this)
                            .load(url).resize(500, 500)
                            .centerInside()
                            .placeholder(R.drawable.ic_action_camera)
                            .into(mProfileImage);

                    mNameEditText.setText(name);
                    mUserNameEditText.setText(username);
                    mInstitutionEditText.setText(Institution);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mImageUri != null) {
            outState.putParcelable("url", mImageUri);
        }
        outState.putString("Name", mNameEditText.getText().toString());
        outState.putString("UserName", mUserNameEditText.getText().toString());
        outState.putString("Institution", mInstitutionEditText.getText().toString());


    }


    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).resize(500, 500).centerInside().into(mProfileImage);
        }

    }

    public String getImageExtension(Uri ImageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(ImageUri));
    }


    public void saveData() {
        final String name = mNameEditText.getText().toString().trim();
        final String userName = mUserNameEditText.getText().toString().trim();
        final String gender = mGenderSpinner.getSelectedItem().toString();
        final String institution = mInstitutionEditText.getText().toString().trim();
        final String userId = mAuth.getCurrentUser().getUid();

        //StorageReference ref = mStorageRef.child( System.currentTimeMillis()+ "." + getImageExtension(mImageUri));
        StorageReference ref = mStorageRef.child(userId);

        mProgressDialog.setMessage("uploading......");
        mProgressDialog.show();

        if (mImageUri != null) {

            ref.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isSuccessful());

                    mDownloadUri = uriTask.getResult();
                    String mUri = mDownloadUri.toString();
                    if (mUri != null){
                        ProfileUpload profile = new ProfileUpload(name, userName, gender, institution, mUri);
                        mDatabaseRef.child(userId).setValue(profile);     // error is method replace with save photo and data;;;;;;;;;;;;;;;;;;;;;;;
                        mDatabaseRef2.child("userName").setValue(userName);
                        mDatabaseRef3.child("userName").setValue(userName);
                        mDatabaseRef4.child("institution").setValue(institution);

                        Toast.makeText(getApplicationContext(), "Data uploaded successfully", Toast.LENGTH_SHORT).show();


                        mProgressDialog.dismiss();
                    }

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            createUserChild();
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Data uploaded successfully", Toast.LENGTH_SHORT).show();

        }
    }


    public void createUserChild() {

        final String imageUri ="";
        final String name = mNameEditText.getText().toString().trim();
        final String userName = mUserNameEditText.getText().toString().trim();
        final String gender = mGenderSpinner.getSelectedItem().toString();
        final String institution = mInstitutionEditText.getText().toString().trim();
        final String userId = mAuth.getCurrentUser().getUid();


        ProfileUpload profile = new ProfileUpload(name, userName, gender, institution, imageUri);
        mDatabaseRef.child(userId).setValue(profile);
        mDatabaseRef2.child("userName").setValue(userName);
        mDatabaseRef3.child("userName").setValue(userName);
        mDatabaseRef4.child("institution").setValue(institution);


    }

    public void deletePhoto() {
        mProgressDialog.setMessage("Deleting......");
        mProgressDialog.show();
        mStorageRefDel.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                createUserChild();
                mProfileImage.setImageDrawable(null);
                Toast.makeText(getApplicationContext(), "photo deleted successfully", Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
                startActivity(new Intent(EditProfileActivity.this, ProfileActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == mChangePassTextView) {
            startActivity(new Intent(EditProfileActivity.this, ResetPasswordActivity.class));
        }
        if (view == mChooseImageButton) {
            openFileChooser();
        }
        if (view == mSaveButton) {
            saveData();

        }
        if (view == mDeleteProPhotoTextView) {
            deletePhoto();
        }
        if (view == mChangeEmailTextView) {
            startActivity(new Intent(EditProfileActivity.this, ResetEmailActivity.class));
        }
    }
}
