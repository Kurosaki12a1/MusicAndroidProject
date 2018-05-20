package com.bku.musicandroid.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.musicandroid.Model.PlayListOnlineInfo;
import com.bku.musicandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import org.w3c.dom.Text;

/**
 * Created by Welcome on 5/14/2018.
 */

public class CreatePlayListPopUp extends Activity {
    public static final String PlayList_Path = "All_PlayList_Info_Database";
    public static final String Database_Path = "All_Users_Info_Database";
    public static final String Liked_Path = "All_Liked_PlayList_Database";
    public static final String View_Path = "All_View_PlayList_Database";

    private EditText inputName;
    private TextView confirm, cancel;
    FirebaseAuth mAuth;
    String username = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_create_playlist);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        mAuth = FirebaseAuth.getInstance();
        final String userId = mAuth.getCurrentUser().getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path).child("users");
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("userName").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        inputName = (EditText) findViewById(R.id.inputNewPlayList);
        confirm = (TextView) findViewById(R.id.confirm);
        cancel = (TextView) findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputName.getText().equals("")) {
                    TastyToast.makeText(CreatePlayListPopUp.this, "You must input your name playlist before confirm", TastyToast.LENGTH_LONG, TastyToast.WARNING).show();
                } else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(PlayList_Path).child(userId);
                    String keyPlayList = databaseReference.push().getKey();
                    PlayListOnlineInfo playListOnlineInfo = new PlayListOnlineInfo(keyPlayList, inputName.getText().toString().trim(), userId, username, "0", "0");
                    databaseReference.child(keyPlayList).setValue(playListOnlineInfo);
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(View_Path).child(keyPlayList);
                    databaseReference1.setValue("View Of This PlayList");
                    DatabaseReference databaseReference2 = FirebaseDatabase.getInstance().getReference(Liked_Path).child(keyPlayList);
                    databaseReference2.setValue("Liked Of This PlayList");

                    Intent intent = new Intent(CreatePlayListPopUp.this, ActivityPlayListOnline.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}