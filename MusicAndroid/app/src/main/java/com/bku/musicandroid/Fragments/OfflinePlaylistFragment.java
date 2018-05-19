package com.bku.musicandroid.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.bku.musicandroid.Adapter.OfflinePlaylistRecycleAdapter;
import com.bku.musicandroid.Database.OfflineDatabaseHelper;
import com.bku.musicandroid.Model.OfflinePlaylist;
import com.bku.musicandroid.R;

import java.util.ArrayList;

/**
 * Created by Son on 5/19/2018.
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflinePlaylistFragment extends Fragment {
    RecyclerView lvPlaylist;
    FloatingActionButton btnAddPlaylist;
    OfflinePlaylistRecycleAdapter adapter;
    OfflineDatabaseHelper db;


    public OfflinePlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline_playlist, container, false);

        db = new OfflineDatabaseHelper(getContext());

        bindView(view);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //Restore from db
        restoreFromDb();
    }

    /**
     * Created by Son on 5/19/2018.
     */
    private void bindView(View view) {
        lvPlaylist = view.findViewById(R.id.lvPlaylist);
        lvPlaylist.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new OfflinePlaylistRecycleAdapter(getContext());
        lvPlaylist.setAdapter(adapter);


        btnAddPlaylist = view.findViewById(R.id.btnAddPlaylist);
        btnAddPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Create playlist");
                final EditText edtPlaylistName = new EditText(getContext());
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

    private void restoreFromDb() {
        ArrayList<OfflinePlaylist> listPlaylist = new ArrayList<>(db.getAllPlaylist());
        adapter.setListPlaylist(listPlaylist);
    }

}
