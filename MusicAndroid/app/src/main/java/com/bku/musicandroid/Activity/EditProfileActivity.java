package com.bku.musicandroid.Activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.musicandroid.Fragments.ProfileFragment;
import com.bku.musicandroid.Model.Users;
import com.bku.musicandroid.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

/**
 * Created by Welcome on 5/13/2018.
 */

public class EditProfileActivity extends AppCompatActivity {

    private  ImageView backArrow,saveChanges,profilePhoto,backGroundPhoto,eyes;

    private EditText username,fullname,dateofbirth,email,accountId;

    private TextView changePhoto,changeBackGround;

    private FirebaseAuth mAuth;

    Uri FilePathUri,FilePathUri1;

    private int Image_Request_Code=69;
    private int Image_BackGround_Request_Code=96;

    String avatarURL="" ,backGroundAvatarURL="";
    private boolean isHideProfilePhoto=false;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_edit_profile);

        backArrow=(ImageView) findViewById(R.id.backArrow);
        saveChanges=(ImageView)findViewById(R.id.saveChanges);
        profilePhoto=(ImageView)findViewById(R.id.profile_photo);
        backGroundPhoto=(ImageView)findViewById(R.id.backGround);
        eyes=(ImageView)findViewById(R.id.viewEyes);

        username=(EditText)findViewById(R.id.username);
        fullname=(EditText)findViewById(R.id.display_name);
        dateofbirth=(EditText)findViewById(R.id.dateOfBirth);
        email=(EditText) findViewById(R.id.email);
        accountId=(EditText)findViewById(R.id.accountID);

        changePhoto=(TextView)findViewById(R.id.changeProfilePhoto);
        changeBackGround=(TextView)findViewById(R.id.changeBackGround);

        mAuth=FirebaseAuth.getInstance();
        final String userId=mAuth.getCurrentUser().getUid();

        username.setEnabled(false);
        email.setEnabled(false);
        accountId.setEnabled(false);
        backGroundPhoto.setVisibility(View.INVISIBLE);


        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("All_Users_Info_Database");
        databaseReference.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username.setText(dataSnapshot.child("userName").getValue(String.class));
                fullname.setText(dataSnapshot.child("fullName").getValue(String.class));
                email.setText(dataSnapshot.child("email").getValue(String.class));
                dateofbirth.setText(dataSnapshot.child("dateOfBirth").getValue(String.class));
                avatarURL=dataSnapshot.child("avatarURL").getValue(String.class);
                accountId.setText("Your Account ID : " + dataSnapshot.getKey());
                Glide.with(EditProfileActivity.this).load(dataSnapshot.child("avatarURL").getValue(String.class)).into(profilePhoto);
                if(dataSnapshot.hasChild("backgroundURL")){
                    backGroundAvatarURL=dataSnapshot.child("backgroundURL").getValue(String.class);
                    Glide.with(EditProfileActivity.this).load(dataSnapshot.child("backgroundURL").getValue(String.class)).into(backGroundPhoto);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        changeBackGround.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImageBackGround();
            }
        });

        eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHideProfilePhoto){
                    isHideProfilePhoto=false;
                    backGroundPhoto.setVisibility(View.INVISIBLE);
                    profilePhoto.setVisibility(View.VISIBLE);
                }
                else{
                    isHideProfilePhoto=true;
                    backGroundPhoto.setVisibility(View.VISIBLE);
                    profilePhoto.setVisibility(View.INVISIBLE);
                }
            }
        });

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges.setEnabled(false);
                saveProfile(userId);
            }
        });


    }

    private void chooseImage(){
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code);
    }

    private void chooseImageBackGround(){
        Intent intent = new Intent();

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_BackGround_Request_Code);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null) {

            FilePathUri = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);

                // Setting up bitmap selected image into ImageView.
                profilePhoto.setImageBitmap(bitmap);

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
        else if(requestCode == Image_BackGround_Request_Code && resultCode == RESULT_OK && data != null && data.getData() != null){
            FilePathUri1 = data.getData();

            try {

                // Getting selected image into Bitmap.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri1);

                // Setting up bitmap selected image into ImageView.
                backGroundPhoto.setImageBitmap(bitmap);

            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        // Returning the file Extension.
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }

    private void saveProfile(final String userId){
        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
        if(FilePathUri == null && FilePathUri1!=null){
            StorageReference storageReference2nd = storageReference.child(  System.currentTimeMillis() + "." + GetFileExtension(FilePathUri1));

            // Adding addOnSuccessListener to second StorageReference.
            StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageReference2nd.putFile(FilePathUri1)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Upload Successfully ", Toast.LENGTH_LONG).show();



                            //get ImageURL
                            String imageUrl = taskSnapshot.getDownloadUrl().toString();

                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("All_Users_Info_Database");
                            Users user=new Users(username.getText().toString().trim(),email.getText().toString().trim(),
                                    fullname.getText().toString().trim(),avatarURL,dateofbirth.getText().toString().trim(),imageUrl);
                            databaseReference.child("users").child(userId).setValue(user);

                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.


                            // Showing exception erro message.
                            Toast.makeText(EditProfileActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = 50+(50.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            // Setting progressDialog Title.
                            Toast.makeText(EditProfileActivity.this,"Your Profile is updating " + progress,Toast.LENGTH_SHORT).show();
                            if(progress>=100) {
                                Intent intent = new Intent(EditProfileActivity.this, MainScreenActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
        else if(FilePathUri!=null && FilePathUri1==null){

            StorageReference storageReference2nd = storageReference.child(  System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Upload Successfully ", Toast.LENGTH_LONG).show();



                            //get ImageURL
                            String imageUrl = taskSnapshot.getDownloadUrl().toString();

                            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("All_Users_Info_Database");
                            Users user=new Users(username.getText().toString().trim(),email.getText().toString().trim(),
                                    fullname.getText().toString().trim(),imageUrl,dateofbirth.getText().toString().trim(),backGroundAvatarURL);
                            databaseReference.child("users").child(userId).setValue(user);

                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.


                            // Showing exception erro message.
                            Toast.makeText(EditProfileActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            // Setting progressDialog Title.
                            Toast.makeText(EditProfileActivity.this,"Your Profile is updating " + progress,Toast.LENGTH_SHORT).show();
                            if(progress>=100) {
                                Intent intent = new Intent(EditProfileActivity.this, MainScreenActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
        }
        else if(FilePathUri!=null && FilePathUri1!=null){

            StorageReference storageReference2nd = storageReference.child(  System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));

            // Adding addOnSuccessListener to second StorageReference.
            StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            // Showing toast message after done uploading.
                            Toast.makeText(getApplicationContext(), "Upload Successfully ", Toast.LENGTH_LONG).show();



                            //get ImageURL
                            String imageUrl = taskSnapshot.getDownloadUrl().toString();
                            avatarURL=imageUrl;
                            /*DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("All_Users_Info_Database");
                            Users user=new Users(username.getText().toString().trim(),email.getText().toString().trim(),
                                    fullname.getText().toString().trim(),imageUrl,dateofbirth.getText().toString().trim(),backGroundAvatarURL);
                            databaseReference.child("users").child(userId).setValue(user);
*/
                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.

                            // Showing exception erro message.
                            Toast.makeText(EditProfileActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (50.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            // Setting progressDialog Title.
                            Toast.makeText(EditProfileActivity.this,"Your Profile is updating " + progress,Toast.LENGTH_SHORT).show();
                            if(progress>=50) {
                                finalSaveSettings(userId);
                            }
                        }
                    });

        }
        else {
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("All_Users_Info_Database");
            Users user=new Users(username.getText().toString().trim(),email.getText().toString().trim(),
                    fullname.getText().toString().trim(),avatarURL,dateofbirth.getText().toString().trim(),backGroundAvatarURL);
            databaseReference.child("users").child(userId).setValue(user);
        }
    }

    private void finalSaveSettings(final String userId) {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference2nd = storageReference.child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri1));

        // Adding addOnSuccessListener to second StorageReference.
        StorageTask<UploadTask.TaskSnapshot> taskSnapshotStorageTask = storageReference2nd.putFile(FilePathUri1)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        // Showing toast message after done uploading.
                        Toast.makeText(getApplicationContext(), "Upload Successfully ", Toast.LENGTH_LONG).show();


                        //get ImageURL
                        String imageUrl = taskSnapshot.getDownloadUrl().toString();
                        backGroundAvatarURL = imageUrl;
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("All_Users_Info_Database");
                        Users user = new Users(username.getText().toString().trim(), email.getText().toString().trim(),
                                fullname.getText().toString().trim(), avatarURL, dateofbirth.getText().toString().trim(), backGroundAvatarURL);
                        databaseReference.child("users").child(userId).setValue(user);

                    }
                })
                // If something goes wrong .
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                        // Hiding the progressDialog.

                        // Showing exception erro message.
                        Toast.makeText(EditProfileActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })

                // On progress change upload time.
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        // Setting progressDialog Title.

                        if (progress >= 100) {
                            Intent intent = new Intent(EditProfileActivity.this, MainScreenActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }

}
