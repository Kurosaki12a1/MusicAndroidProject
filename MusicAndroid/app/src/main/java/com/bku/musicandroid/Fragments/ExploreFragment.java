package com.bku.musicandroid.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bku.musicandroid.R;

/**
 * Created by SonPhan on 3/24/2018.
 */
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExploreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExploreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExploreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private LinearLayout popLayout,
            rockLayout,
            jazzLayout,
            bluesLayout,
            rbLayout,
            hiphopLayout,
            countryLayout,
            modernFolkLayout,
            electronicLayout,
            danceLayout,
            easyListeningLayout,
            avantGardeLayout,
            ukusLayout,
            jpopLayout,
            vpopLayout,
            kpopLayout;
    private OnFragmentInteractionListener mListener;

    public ExploreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ExploreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ExploreFragment newInstance(String param1, String param2) {
        ExploreFragment fragment = new ExploreFragment();
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
        return inflater.inflate(R.layout.fragment_explorer, container, false);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindView();
        popLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Pop");
            }
        });
        rockLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Rock");
            }
        });
        jazzLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Jazz");
            }
        });
        bluesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Blues");
            }
        });
        rbLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("R&B/Soul");
            }
        });
        hiphopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Hip Hop");
            }
        });
        countryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Country");
            }
        });
        modernFolkLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Modern Folk");
            }
        });
        electronicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Electronic");
            }
        });
        danceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Dance");
            }
        });
        easyListeningLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Easy Listening");
            }
        });
        avantGardeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("Avant-Garde");
            }
        });
        ukusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("UK-US");
            }
        });
        jpopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("JPop");
            }
        });
        vpopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("VPop");
            }
        });
        kpopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGenre("KPop");
            }
        });
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
        popLayout = getView().findViewById(R.id.pop_genre);
        rockLayout= getView().findViewById(R.id.rock_genre);
        jazzLayout= getView().findViewById(R.id.jazz_genre);
        bluesLayout= getView().findViewById(R.id.blues_genre);
        rbLayout= getView().findViewById(R.id.RB_genre);
        hiphopLayout= getView().findViewById(R.id.hiphop_genre);
        countryLayout= getView().findViewById(R.id.country_genre);
        modernFolkLayout= getView().findViewById(R.id.modernFolk_genre);
        electronicLayout= getView().findViewById(R.id.electronic_genre);
        danceLayout= getView().findViewById(R.id.dance_genre);
        easyListeningLayout= getView().findViewById(R.id.easyListenGenre);
        avantGardeLayout= getView().findViewById(R.id.avantGardeGenre);
        ukusLayout= getView().findViewById(R.id.UKUSgenre);
        jpopLayout= getView().findViewById(R.id.jpopGenre);
        vpopLayout= getView().findViewById(R.id.vpopGenre);
        kpopLayout = getView().findViewById(R.id.kpopGenre);
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

    public void goToGenre(String genre){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        SongGenreFragment songGenreFragment = new SongGenreFragment();
        Bundle bundle = new Bundle();
        bundle.putString("GENRE", genre);
        songGenreFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.fragmentExplorer, songGenreFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
        //  fragmentManager.executePendingTransactions();
        //    fragmentTransaction.commit();
    }


}
