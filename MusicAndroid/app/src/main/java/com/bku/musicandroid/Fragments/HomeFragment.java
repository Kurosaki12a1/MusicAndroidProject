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
import android.widget.Toast;

import com.bku.musicandroid.Adapter.SongGenreRecycleAdapter;
import com.bku.musicandroid.Adapter.TopTenSongRecyclerAdapter;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.bku.musicandroid.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by SonPhan on 3/24/2018.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<SongPlayerOnlineInfo> topLikeList;
    private ArrayList<SongPlayerOnlineInfo> topDownloadList;
    private ArrayList<SongPlayerOnlineInfo> topListenList;
    private ArrayList<SongPlayerOnlineInfo> songList;
    private RecyclerView topLike, topDownload, topListen;
    private OnFragmentInteractionListener mListener;
    private ProgressBar progressLoadMusicLike, progressLoadMusicDownload, progressLoadMusicListen;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference();
    DatabaseReference dataRef = ref.child("All_Song_Database_Info");
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindView();
        super.onViewCreated(view, savedInstanceState);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void bindView(){
        topLike = getView().findViewById(R.id.topLikeList);
        topDownload = getView().findViewById(R.id.topDownloadList);
        topListen = getView().findViewById(R.id.topListenList);
        progressLoadMusicLike = getView().findViewById(R.id.progressLike);
        progressLoadMusicDownload = getView().findViewById(R.id.progressDownload);
        progressLoadMusicListen = getView().findViewById(R.id.progressListen);
        topDownloadList = new ArrayList<>();
        topListenList = new ArrayList<>();
        songList = new ArrayList<>();
        topLikeList = new ArrayList<>();
        GetTopSong asyncTask = new GetTopSong();
        asyncTask.execute(songList);
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
     * Class: GetTopSong
     * Created by Truong on 11/5/2018.
     */
    @SuppressLint("StaticFieldLeak")
    public class GetTopSong extends AsyncTask<ArrayList<SongPlayerOnlineInfo>, ArrayList<SongPlayerOnlineInfo>, ArrayList<SongPlayerOnlineInfo>> {

        GetTopSong() {
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
            progressLoadMusicLike.setVisibility(View.VISIBLE);
            progressLoadMusicDownload.setVisibility(View.VISIBLE);
            progressLoadMusicListen.setVisibility(View.VISIBLE);
            dataRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        songList.clear();
                        topLikeList.clear();
                        topDownloadList.clear();
                        for (DataSnapshot songSnapshot : dataSnapshot.getChildren()) {
                            SongPlayerOnlineInfo song = songSnapshot.getValue(SongPlayerOnlineInfo.class);
                            if (song != null) { //Test for Pop
                                Log.d("1abc","Song INfo: "+song.getUserName()+song.getSongName()+"   "+song.getDownload());
                                songList.add(song);
                            }

                        }
                        Collections.sort(songList, new Comparator<SongPlayerOnlineInfo>() {
                            @Override
                            public int compare(SongPlayerOnlineInfo song1, SongPlayerOnlineInfo song2) {
                                int compareResult = 0;
                                Integer like1 = Integer.parseInt(song1.getLiked());
                                Integer like2 = Integer.parseInt(song2.getLiked());
                                try {
                                    compareResult = like1.compareTo(like2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(),"Error when sort like: "+e,Toast.LENGTH_SHORT).show();
                                    compareResult = like1.compareTo(like2);
                                }
                                return compareResult;
                            }
                        });
                        Collections.reverse(songList);
                        if(songList.size()>10) {
                            for (int i = 0; i < 10; i++) {
                                topLikeList.add(songList.get(i));
                            }
                        }else{
                            for (int i = 0; i < songList.size(); i++) {
                                topLikeList.add(songList.get(i));
                            }
                        }
                        TopTenSongRecyclerAdapter topLikeAdapter = new TopTenSongRecyclerAdapter(getActivity(), topLikeList);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        topLike.setLayoutManager(layoutManager);
                        topLike.setAdapter(topLikeAdapter);
                        progressLoadMusicLike.setVisibility(View.GONE);
                        Collections.sort(songList, new Comparator<SongPlayerOnlineInfo>() {
                            @Override
                            public int compare(SongPlayerOnlineInfo song1, SongPlayerOnlineInfo song2) {
                                int compareResult = 0;
                                Integer download1 = Integer.parseInt(song1.getDownload());
                                Integer download2 = Integer.parseInt(song2.getDownload());
                                try {
                                    compareResult = download1.compareTo(download2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(),"Error when sort download: "+e,Toast.LENGTH_SHORT).show();
                                    compareResult = download1.compareTo(download2);
                                }
                                return compareResult;
                            }
                        });
                        Collections.reverse(songList);
                        if(songList.size()>10) {
                            for (int i = 0; i < 10; i++) {
                                topDownloadList.add(songList.get(i));
                            }
                        }else{
                            for (int i = 0; i < songList.size(); i++) {
                                topDownloadList.add(songList.get(i));
                            }
                        }
                        TopTenSongRecyclerAdapter topDownloadAdapter = new TopTenSongRecyclerAdapter(getActivity(), topDownloadList);
                        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
                        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
                        topDownload.setLayoutManager(layoutManager2);
                        topDownload.setAdapter(topDownloadAdapter);
                        Toast.makeText(getActivity(),"Loading done",Toast.LENGTH_SHORT).show();
                        progressLoadMusicDownload.setVisibility(View.GONE);

                        Collections.sort(songList, new Comparator<SongPlayerOnlineInfo>() {
                            @Override
                            public int compare(SongPlayerOnlineInfo song1, SongPlayerOnlineInfo song2) {
                                int compareResult = 0;
                                Integer view1 = Integer.parseInt(song1.getView());
                                Integer view2 = Integer.parseInt(song2.getView());
                                try {
                                    compareResult = view1.compareTo(view2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(),"Error when sort view: "+e,Toast.LENGTH_SHORT).show();
                                    compareResult = view1.compareTo(view2);
                                }
                                return compareResult;
                            }
                        });
                        Collections.reverse(songList);
                        if(songList.size()>10) {
                            for (int i = 0; i < 10; i++) {
                                topListenList.add(songList.get(i));
                            }
                        }else{
                            for (int i = 0; i < songList.size(); i++) {
                                topListenList.add(songList.get(i));
                            }
                        }
                        TopTenSongRecyclerAdapter topListenAdapter = new TopTenSongRecyclerAdapter(getActivity(), topListenList);
                        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
                        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
                        topListen.setLayoutManager(layoutManager3);
                        topListen.setAdapter(topListenAdapter);
                        Toast.makeText(getActivity(),"Loading done",Toast.LENGTH_SHORT).show();
                        progressLoadMusicListen.setVisibility(View.GONE);
                    }
                }@Override
                public void onCancelled(DatabaseError databaseError) {
                    progressLoadMusicLike.setVisibility(View.GONE);
                    progressLoadMusicDownload.setVisibility(View.GONE);
                    progressLoadMusicListen.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Cannot retrieve data", Toast.LENGTH_SHORT).show();

                }
            });
            //    publishProgress(arrayLists);
            return arrayLists[0];
        }
    }
}
