package com.bku.musicandroid.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bku.musicandroid.Adapter.OfflinePlaylistRecycleAdapter;
import com.bku.musicandroid.Database.OfflineDatabaseHelper;
import com.bku.musicandroid.Model.OfflinePlaylist;
import com.bku.musicandroid.R;

import java.util.ArrayList;

/**
 * Created by Son on 5/19/2018.
 */
public class ChooseOfflinePlaylistActivity extends AppCompatActivity {
    RecyclerView lvPlaylist;
    OfflinePlaylistRecycleAdapter adapter;
    OfflineDatabaseHelper db;
    ImageView btnAddPlaylist;
    int _position;

    /**
     * Created by Son on 5/19/2018.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_offline_playlist);

        _position = getIntent().getIntExtra("position", 0);
        db = new OfflineDatabaseHelper(this);

        bindViews();
    }

    /**
     * Created by Son on 5/19/2018.
     */
    @Override
    public void onResume() {
        super.onResume();
        //Restore from db
        restoreFromDb();
    }

    /**
     * Created by Son on 5/19/2018.
     */
    private void bindViews() {
        lvPlaylist = findViewById(R.id.lvPlaylist);
        lvPlaylist.setLayoutManager(new LinearLayoutManager(this));
        adapter = new OfflinePlaylistRecycleAdapter(this, _position, true);
        lvPlaylist.setAdapter(adapter);

        btnAddPlaylist = findViewById(R.id.btnAddPlaylist);
        btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseOfflinePlaylistActivity.this);
                builder.setTitle("Create playlist");
                final EditText edtPlaylistName = new EditText(ChooseOfflinePlaylistActivity.this);
                builder.setView(edtPlaylistName);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String input = edtPlaylistName.getText().toString();
                        OfflinePlaylist o = new OfflinePlaylist(input);

                        int id = db.insertPlaylist(o);
                        o.setId(id);

                        adapter.add(o);

                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.show();
            }
        });
    }

    /**
     * Created by Son on 5/19/2018.
     */
    private void restoreFromDb() {
        ArrayList<OfflinePlaylist> listPlaylist = new ArrayList<>(db.getAllPlaylist());
        adapter.setListPlaylist(listPlaylist);
    }
}
