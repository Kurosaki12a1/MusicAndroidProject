package com.bku.musicandroid.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.bku.musicandroid.Adapter.SearchAdapter;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by SonPhan on 3/24/2018.
 */

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String SONG_DATABASE="All_Song_Database_Info";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private EditText searchBar;
    private Spinner searchOption;
    private RecyclerView recyclerView;
    private ArrayList<SongPlayerOnlineInfo> listSong;
    private SearchAdapter mAdapter;
    private OnFragmentInteractionListener mListener;
    String [] optionSearch= {
            "Search by Song Name",
            "Search by Song Artist",
            "Search by Song Genre",
            "Search by User Upload"
    };
    String getUserId;
    String dataSearch;
    int currentPosition;
    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        return inflater.inflate(R.layout.fragment_search, container, false);
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindViews();

        super.onViewCreated(view, savedInstanceState);
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

    public void bindViews(){
        searchBar=getView().findViewById(R.id.searchBar);
        searchOption=getView().findViewById(R.id.searchOption);
        recyclerView=getView().findViewById(R.id.recyclerView);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,optionSearch);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        searchOption.setAdapter(adapter);
      //  searchOption.setOnItemClickListener(this);
        searchOption.setSelection(0);
        currentPosition=0;

        listSong=new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initTextListener();

    }

    private void searchForMatch(final String keyword){
        listSong.clear();
        getUserId="";
        dataSearch="";

        if(searchOption.getSelectedItemPosition()==0){
            dataSearch="songName";
        }
        else if(searchOption.getSelectedItemPosition()==1){
            dataSearch="songArtists";
        }
        else if(searchOption.getSelectedItemPosition()==2){
            dataSearch="songGenre";
        }
        else {
            dataSearch="userName";
        }
        if(keyword.length()==0){

        }
        else{
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(SONG_DATABASE);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listSong=new ArrayList<>();
                    for(DataSnapshot singleSnapshot: dataSnapshot.getChildren()){
                        if(singleSnapshot.child(dataSearch).getValue().toString().toLowerCase().contains(keyword)){
                            listSong.add(singleSnapshot.getValue(SongPlayerOnlineInfo.class));
                        }
                    }
                    updateUsersList();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void initTextListener(){
        listSong=new ArrayList<>();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text=searchBar.getText().toString().toLowerCase();
                searchForMatch(text);
            }
        });
    }
    private void updateUsersList(){
        if(isChangeSelected()){
            listSong.clear();
        }
        mAdapter=new SearchAdapter(getContext(),listSong);
        mAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(mAdapter);
        recyclerView.invalidate();
    }

    private boolean isChangeSelected(){
        if(searchOption.getSelectedItemPosition()!=currentPosition){
            currentPosition=searchOption.getSelectedItemPosition();
            return true;
        }
        return false;
    }




}
