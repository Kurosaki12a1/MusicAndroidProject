package com.bku.musicandroid.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import com.bku.musicandroid.Adapter.AddSongToPlayListAdapter;
import com.bku.musicandroid.Model.PlayListOnlineInfo;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.bku.musicandroid.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Welcome on 5/14/2018.
 */

public class AddSongToPlayListPopUp extends Activity   {
    public static final String PlayList_Path="All_PlayList_Info_Database";
    public static final String Database_Path="All_Users_Info_Database";


    FirebaseAuth mAuth;
    String username="";
    RecyclerView recyclerView;
    SongPlayerOnlineInfo songPlayerOnlineInfo;
    ArrayList<PlayListOnlineInfo> lst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_playlist);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .6));

        mAuth=FirebaseAuth.getInstance();
        String userId=mAuth.getCurrentUser().getUid();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            songPlayerOnlineInfo=new SongPlayerOnlineInfo(extras.getString("songId"),
                    extras.getString("nameSong"),extras.getString("nameArtist"),
                    extras.getString("songURL"),extras.getString("ImageSongURL"),
                    extras.getString("Liked"),extras.getString("userId"),
                    extras.getString("songGenre"),extras.getString("userUpload"),
                    extras.getString("Download"),extras.getString("viewListen"));


        }
        lst=new ArrayList<>();
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(PlayList_Path);
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lst=new ArrayList<>();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    lst.add(singleSnapshot.getValue(PlayListOnlineInfo.class));
                }
                AddSongToPlayListAdapter addSongToPlayListAdapter=new AddSongToPlayListAdapter(AddSongToPlayListPopUp.this,songPlayerOnlineInfo,lst);
                recyclerView.setAdapter(addSongToPlayListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
