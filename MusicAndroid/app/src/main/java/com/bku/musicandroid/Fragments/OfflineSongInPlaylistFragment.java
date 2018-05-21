package com.bku.musicandroid.Fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.bku.musicandroid.Adapter.SongInfoOfflineListAdapter;
import com.bku.musicandroid.Database.OfflineDatabaseHelper;
import com.bku.musicandroid.Model.SongPlayerOfflineInfo;
import com.bku.musicandroid.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfflineSongInPlaylistFragment extends Fragment {

    private SongInfoOfflineListAdapter songInfoOfflineAdapter;

    private ListView lvSong;
    private ArrayList<SongPlayerOfflineInfo> listSong;
    private OfflineDatabaseHelper db;
    private int playlistId;
    private String playlistName;

    public OfflineSongInPlaylistFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new OfflineDatabaseHelper(getContext());
        Bundle bundle = getArguments();
        if (bundle != null) {
            playlistId = bundle.getInt("playlistId");
            playlistName = bundle.getString("playlistName");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_in_playlist, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews();

    }

    private void bindViews() {
        lvSong = getView().findViewById(R.id.lvSong);

        TextView txtPlaylistName = getView().findViewById(R.id.txtPlaylistName);
        txtPlaylistName.setText(playlistName);

        listSong = new ArrayList<>();
        GetOfflineSongInPlaylistAsyncTask asyncTask = new GetOfflineSongInPlaylistAsyncTask();
        songInfoOfflineAdapter = new SongInfoOfflineListAdapter(getActivity(), R.layout.songs_item, listSong, true);
        lvSong.setAdapter(songInfoOfflineAdapter);
        asyncTask.execute(listSong);
    }

    @SuppressLint("StaticFieldLeak")
    public class GetOfflineSongInPlaylistAsyncTask extends AsyncTask<ArrayList<SongPlayerOfflineInfo>, ArrayList<SongPlayerOfflineInfo>, ArrayList<SongPlayerOfflineInfo>> {

        GetOfflineSongInPlaylistAsyncTask() {
        }


        @Override
        protected void onPostExecute(ArrayList<SongPlayerOfflineInfo> songPlayerOfflineInfos) {
            super.onPostExecute(songPlayerOfflineInfos);
        }

        @Override
        protected void onProgressUpdate(ArrayList<SongPlayerOfflineInfo>... values) {
            super.onProgressUpdate(values);
            songInfoOfflineAdapter.setDataSet(values[0]);
            songInfoOfflineAdapter.notifyDataSetChanged();

        }

        @Override
        protected ArrayList<SongPlayerOfflineInfo> doInBackground(ArrayList<SongPlayerOfflineInfo>... arrayLists) {

            ArrayList<SongPlayerOfflineInfo> listSong = db.getAllSongFromPlaylist(playlistId);
            Collections.sort(listSong, new Comparator<SongPlayerOfflineInfo>() {
                @Override
                public int compare(SongPlayerOfflineInfo s1, SongPlayerOfflineInfo s2) {
                    return s1.getSongName().compareTo(s2.getSongName());
                }
            });
            arrayLists[0] = listSong;
            publishProgress(arrayLists);
            return arrayLists[0];
        }
    }

}
