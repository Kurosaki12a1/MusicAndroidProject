package com.bku.musicandroid.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
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
import com.google.firebase.storage.UploadTask;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Thinh on 4/22/2018.
 */

public class UploadSongActivity extends AppCompatActivity {
    String Storage_Path = "Music Storage";
    public static final String Database_Path = "All_Song_Database_Info";
    public static final String USER_PATH = "All_Users_Info_Database";
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Uri FilePathUri;
    ProgressDialog progressDialog;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    EditText songName, singerName;
    Spinner genreSpinner;
    Button openFile, Upload, Exit;
    String imageSong = "";
    String currentId = "";
    String currentUserName = "";
    byte[] bytePicSong = null;
    String[] gerne = {
            "Pop",
            "Rock",
            "Jazz",
            "Blues",
            "R&B/Soul",
            "Hip Hop",
            "Country",
            "Modern Folk",
            "Electronic",
            "Dance",
            "Easy Listening",
            "Avant-Garde",
            "UK-US",
            "JPop",
            "VPop",
            "KPop"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_song);

        mAuth = FirebaseAuth.getInstance();
        currentId = mAuth.getCurrentUser().getUid().toString();

        progressBar = (ProgressBar) findViewById(R.id.determinateBar);

        songName = (EditText) findViewById(R.id.inputNameMusic);

        singerName = (EditText) findViewById(R.id.inputAuthorName);

        genreSpinner = (Spinner) findViewById(R.id.genreSpinner);

        openFile = (Button) findViewById(R.id.btnSelectFile);

        Upload = (Button) findViewById(R.id.UpLoad);

        Exit = (Button) findViewById(R.id.exit);

        progressBar.setProgress(0);


        progressDialog = new ProgressDialog(UploadSongActivity.this);

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(USER_PATH).child("users");
        databaseReference1.child(currentId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentUserName = dataSnapshot.child("userName").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gerne);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        genreSpinner.setAdapter(adapter);
        //set Default Spinner
        genreSpinner.setSelection(0);

        Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadMusicToStorage();
            }
        });
        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creating intent.
                Intent intent = new Intent();

                // Setting intent type as image to select image from phone storage.
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Please Select Music"), 7);
            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                FilePathUri = data.getData();
                //This below code will get image of audio
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//                File file = new File(FilePathUri.getPath());
//                String filePath = file.getAbsolutePath();
//                retriever.setDataSource(FilePathUri.getPath());
                retriever.setDataSource(this, FilePathUri);
                //if there is image of audio
                if (retriever.getEmbeddedPicture() != null) {
                    byte[] data1 = retriever.getEmbeddedPicture();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    bytePicSong = new byte[byteArrayOutputStream.size()];
                    bytePicSong = byteArrayOutputStream.toByteArray();
                }
            } catch (IllegalArgumentException e) {
                TastyToast.makeText(UploadSongActivity.this, "Please choose song which name don't have special character ", TastyToast.LENGTH_SHORT, TastyToast.WARNING).show();
            }


        }
    }

    public void UploadMusicToStorage() {
        if (FilePathUri != null) {

            // Setting progressDialog Title.
            progressDialog.setTitle("Mp3 is Uploading...");

            // Showing progressDialog.
            progressDialog.show();

            // Creating second StorageReference.
            StorageReference storageReference2nd = storageReference.child(Storage_Path + System.currentTimeMillis() + "." + ".mp3");
            StorageReference storageReference3rd = storageReference.child("Image " + System.currentTimeMillis() + " ." + ".png");

            //check If ImageSong available
            if (bytePicSong != null) {
                storageReference3rd.putBytes(bytePicSong).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageSong = taskSnapshot.getDownloadUrl().toString();
                    }
                });
            } else {
                //Default Image
                imageSong = "https://firebasestorage.googleapis.com/v0/b/android-music-app-player.appspot.com/o/ic_audiotrack_dark.png?alt=media&token=ce75b506-1a36-4863-bede-ceff31f88b46";
            }

            storageReference2nd.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            // Getting image name from EditText and store into string variable.
                            // String TempImageName = songName.getText().toString().trim();

                            // Hiding the progressDialog after done uploading.
                            progressDialog.dismiss();

                            // Showing toast message after done uploading.
                            TastyToast.makeText(getApplicationContext(), "Music Uploaded Successfully ", TastyToast.LENGTH_LONG, TastyToast.SUCCESS).show();

                            // Getting image upload ID.
                            String musicUploadId = databaseReference.push().getKey();

                            //get UserId
                            //  mAuth= FirebaseAuth.getInstance();

                            // String userId=mAuth.getCurrentUser().getUid().toString();


                            SongPlayerOnlineInfo songPlayerOnlineInfo = new SongPlayerOnlineInfo(musicUploadId, songName.getText().toString(), singerName.getText().toString(), taskSnapshot.getDownloadUrl().toString(), imageSong, "0", currentId, genreSpinner.getSelectedItem().toString(), currentUserName, "0", "0");
                            databaseReference.child(musicUploadId).setValue(songPlayerOnlineInfo);

                        }
                    })
                    // If something goes wrong .
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {

                            // Hiding the progressDialog.
                            progressDialog.dismiss();

                            // Showing exception erro message.
                            TastyToast.makeText(UploadSongActivity.this, exception.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR).show();
                        }
                    })

                    // On progress change upload time.
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            // Setting progressDialog Title.
                            progressDialog.setTitle("Music is Uploading...");
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress((int) progress + 1);
                            if (progress >= 100) {
                                Intent intent = new Intent(UploadSongActivity.this, MainScreenActivity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    });
        } else {

            TastyToast.makeText(UploadSongActivity.this, "Please Select Music or Add Music Name", TastyToast.LENGTH_LONG, TastyToast.WARNING).show();

        }
    }

}
