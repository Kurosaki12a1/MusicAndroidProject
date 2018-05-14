package com.bku.musicandroid.Activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bku.musicandroid.Adapter.ViewListOfPlayListAdapter;
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

public class ActivityPlayListOnline extends AppCompatActivity {

    public static final String PlayList_Path="All_PlayList_Info_Database";

    private ImageView backArrow,createPlayList;
    RecyclerView recyclerView;

    FirebaseAuth mAuth;

    ArrayList<PlayListOnlineInfo> lst=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_online);

        backArrow=(ImageView)findViewById(R.id.backArrow);
        createPlayList=(ImageView)findViewById(R.id.createPlayList);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAuth=FirebaseAuth.getInstance();
        String userId=mAuth.getCurrentUser().getUid();

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(PlayList_Path);
        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot :dataSnapshot.getChildren()) {
                    lst.add(singleSnapshot.getValue(PlayListOnlineInfo.class));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        ViewListOfPlayListAdapter viewListOfPlayListAdapter=new ViewListOfPlayListAdapter(this,lst);
        viewListOfPlayListAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(viewListOfPlayListAdapter);
        recyclerView.invalidate();

        //set adapter and recylceview

        createPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ActivityPlayListOnline.this,CreatePlayListPopUp.class);
                startActivity(intent);
            }
        });
    }
}
