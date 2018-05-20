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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bku.musicandroid.Adapter.SongInfoOfflineListAdapter;
import com.bku.musicandroid.Model.OfflineMusicManager;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.SongPlayerOfflineInfo;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SongsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SongsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SongsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private SongInfoOfflineListAdapter songInfoOfflineAdapter;
    private ListView lvSong;
    private ArrayList<SongPlayerOfflineInfo> listSong;
    private OfflineMusicManager offlineMusicManager;
    private ProgressBar progressLoadMusic;



    public SongsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SongsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SongsFragment newInstance(String param1, String param2) {
        SongsFragment fragment = new SongsFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_songs, container, false);
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

    public SongInfoOfflineListAdapter getSongInfoOfflineAdapter() {
        return songInfoOfflineAdapter;
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
     * Created by SonPhan
     */

    @SuppressWarnings("unchecked")
    private void bindViews(){
        lvSong = getView().findViewById(R.id.lvSong);
        progressLoadMusic = getView().findViewById(R.id.progressLoadMusic);

        listSong = new ArrayList<>();
        offlineMusicManager = new OfflineMusicManager(getContext());
        GetOfflineSongListAsyncTask asyncTask = new GetOfflineSongListAsyncTask();
        songInfoOfflineAdapter = new SongInfoOfflineListAdapter(getActivity(), R.layout.songs_item, listSong, false);
        lvSong.setAdapter(songInfoOfflineAdapter);
        asyncTask.execute(listSong);
    }

    /**
     * Class: GetOfflineSongListAsyncTask
     * Created by SonPhan on 4/26/2018.
     */
    @SuppressLint("StaticFieldLeak")
    public class GetOfflineSongListAsyncTask extends AsyncTask <ArrayList<SongPlayerOfflineInfo>, ArrayList<SongPlayerOfflineInfo>, ArrayList<SongPlayerOfflineInfo>>{

        GetOfflineSongListAsyncTask() {
        }


        @Override
        protected void onPostExecute(ArrayList<SongPlayerOfflineInfo> songPlayerOfflineInfos) {
            super.onPostExecute(songPlayerOfflineInfos);
            if (songPlayerOfflineInfos.size() == 0){
                TastyToast.makeText(getContext(), "Insufficient memory or empty offline song, please try again later.", TastyToast.LENGTH_SHORT, TastyToast.CONFUSING);
            } else {
                TastyToast.makeText(getContext(), "Load music successfully.", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
            }
        }

        @Override
        protected void onProgressUpdate(ArrayList<SongPlayerOfflineInfo>... values) {
            super.onProgressUpdate(values);
            if (values[0].size() == 0){
                progressLoadMusic.setVisibility(View.VISIBLE);

            } else {
                progressLoadMusic.setVisibility(View.GONE);
                songInfoOfflineAdapter.setDataSet(values[0]);
                songInfoOfflineAdapter.notifyDataSetChanged();
            }

        }

        @Override
        protected ArrayList<SongPlayerOfflineInfo> doInBackground(ArrayList<SongPlayerOfflineInfo>... arrayLists) {
            publishProgress(arrayLists);
            OfflineMusicManager offlineMusicManager = new OfflineMusicManager(getContext());
            arrayLists[0] = offlineMusicManager.scanAllOfflineMusic();
            publishProgress(arrayLists);
            return arrayLists[0];
        }
    }

}
