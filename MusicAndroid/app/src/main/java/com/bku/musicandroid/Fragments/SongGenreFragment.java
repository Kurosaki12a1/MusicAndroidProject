/**
 * Created by SonPhan on 4/22/2018.
 */
package com.bku.musicandroid.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bku.musicandroid.Adapter.SongGenreRecycleAdapter;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongGenreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongGenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongGenreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private SongGenreRecycleAdapter songInfoOnlineAdapter;
    private RecyclerView listSongGenre;
    private ArrayList<SongPlayerOnlineInfo> listSong;
    private ProgressBar progressLoadMusic;
    private TextView songGenreTxt;
    private String genreSong;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    DatabaseReference dataRef = ref.child("All_Song_Database_Info");

    public SongGenreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongGenreFragment newInstance(String genre) {
        SongGenreFragment fragment = new SongGenreFragment();
        Bundle args = new Bundle();
        args.putString("GENRE", genre);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            genreSong = getArguments().getString("GENRE");
        }
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            genreSong = bundle.getString("GENRE", "null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_genre, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindViews();

        super.onViewCreated(view, savedInstanceState);

    }

    public SongGenreRecycleAdapter getSongGenreAdapter() {
        return songInfoOnlineAdapter;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    /**
     * Function: bindViews
     * Created by Truong
     */

    @SuppressWarnings("unchecked")
    private void bindViews(){
        listSongGenre = getView().findViewById(R.id.lvSong);
        progressLoadMusic = getView().findViewById(R.id.progressLoadMusic);
        songGenreTxt =getView().findViewById(R.id.song_genre_txt);
        songGenreTxt.setText(genreSong);
        listSong = new ArrayList<>();
        GetOnlineGenreSong asyncTask = new GetOnlineGenreSong();
        for(int i = 0; i<listSong.size();i++){
            Log.d("1abc","List Song: "+listSong.get(i).getSongName());
        }
        asyncTask.execute(listSong);
    }

    /**
     * Class: GetOnlineGenreSong
     * Created by Truong on 11/5/2018.
     */
    @SuppressLint("StaticFieldLeak")
    public class GetOnlineGenreSong extends AsyncTask <ArrayList<SongPlayerOnlineInfo>, ArrayList<SongPlayerOnlineInfo>, ArrayList<SongPlayerOnlineInfo>>{

        GetOnlineGenreSong() {
        }


        @Override
        protected void onPostExecute(ArrayList<SongPlayerOnlineInfo> songPlayerOnlineInfos) {
            super.onPostExecute(songPlayerOnlineInfos);
        }

        @Override
        protected void onProgressUpdate(ArrayList<SongPlayerOnlineInfo>... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<SongPlayerOnlineInfo> doInBackground(ArrayList<SongPlayerOnlineInfo>... arrayLists) {

            progressLoadMusic.setVisibility(View.VISIBLE);
            dataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        listSong.clear();
                        for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                            SongPlayerOnlineInfo song = songSnapshot.getValue(SongPlayerOnlineInfo.class);
                            if (song != null && song.getSongGenre().equals(genreSong)) { //Test for Pop
                                Log.d("1abc","Song INfo: "+song.getUserName()+song.getSongName());
                                listSong.add(song);
                            }

                        }
//                        TastyToast.makeText(getActivity(),"Loading done",Toast.LENGTH_SHORT).show();
                        songInfoOnlineAdapter = new SongGenreRecycleAdapter(getActivity(), listSong);
                        listSongGenre.setAdapter(songInfoOnlineAdapter);
                        Log.d("1abc","list song length: "+listSong.size());
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        listSongGenre.setLayoutManager(layoutManager);
                        listSongGenre.setAdapter(songInfoOnlineAdapter);
                        progressLoadMusic.setVisibility(View.GONE);
                    }
                }@Override
                public void onCancelled(DatabaseError databaseError) {
                    progressLoadMusic.setVisibility(View.GONE);
                    TastyToast.makeText(getActivity(), "Cannot retrieve data", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING).show();

                }
            });
            //    publishProgress(arrayLists);
            return arrayLists[0];
        }
    }

}
