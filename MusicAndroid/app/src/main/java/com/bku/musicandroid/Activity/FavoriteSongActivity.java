package com.bku.musicandroid.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bku.musicandroid.Adapter.SearchAdapter;
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
 * Created by Welcome on 5/20/2018.
 */

public class FavoriteSongActivity extends AppCompatActivity {

    public static final String Liked_Database="All_Liked_Song_Database";
    public static final String Song_Database="All_Song_Database_Info";
    FirebaseAuth mAuth;
    ImageView backArrow;
    RecyclerView recyclerView;
    ArrayList<String> lstIdSong;
    ArrayList<SongPlayerOnlineInfo> lstSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_song_online);

        backArrow=(ImageView)findViewById(R.id.backArrow);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth=FirebaseAuth.getInstance();
        final String userId=mAuth.getCurrentUser().getUid();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(Liked_Database);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              lstIdSong = new ArrayList<>();
                for(DataSnapshot singleSnapshot:dataSnapshot.getChildren()){
                    if(singleSnapshot.hasChild(userId)){
                        lstIdSong.add(singleSnapshot.getKey());
                    }
                }
                setRecycleView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void setRecycleView(){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(Song_Database);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstSong=new ArrayList<>();
                for(int i=0;i<lstIdSong.size();i++){
                    lstSong.add(dataSnapshot.child(lstIdSong.get(i)).getValue(SongPlayerOnlineInfo.class));
                }
                //ve co ban searchAdapter list item y chang nay nen copy voa cho le
                SearchAdapter searchAdapter=new SearchAdapter(FavoriteSongActivity.this,lstSong);
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
