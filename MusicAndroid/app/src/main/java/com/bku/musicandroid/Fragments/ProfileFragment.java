package com.bku.musicandroid.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.musicandroid.Activity.ActivityPlayListOnline;
import com.bku.musicandroid.Activity.ChangePasswordActivity;
import com.bku.musicandroid.Activity.EditProfileActivity;
import com.bku.musicandroid.Activity.LoginActivity;
import com.bku.musicandroid.Activity.UploadSongActivity;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Service.SongPlayerService;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Welcome on 5/13/2018.
 */

public class ProfileFragment extends Fragment {

    private ImageView profileImage, backGroundImage;
    private TextView txtBar, txtName, playList, signOutTxt, changePasswordTxt;
    private AppCompatButton menu;
    String userId = "";
    FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;
    RelativeLayout rlayout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProfileFragment.OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        //khong co cai nay thi khoi resize image
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
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
        if (context instanceof ProfileFragment.OnFragmentInteractionListener) {
            mListener = (ProfileFragment.OnFragmentInteractionListener) context;
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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindView();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("All_Users_Info_Database");
        databaseReference.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Glide.with(getContext()).load(dataSnapshot.child("avatarURL").getValue(String.class)).into(profileImage);
                txtBar.setText("Hi , " + dataSnapshot.child("userName").getValue(String.class) + " !");
                txtName.setText(dataSnapshot.child("fullName").getValue(String.class));
                if (dataSnapshot.hasChild("backgroundURL")) {
                    Glide.with(getContext()).load(dataSnapshot.child("backgroundURL").getValue(String.class)).centerCrop().into(backGroundImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        playList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityPlayListOnline.class);
                getContext().startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowMenu();
            }
        });
        signOutTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        changePasswordTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);

            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public void bindView() {
        profileImage = getView().findViewById(R.id.profile_image);
        backGroundImage = getView().findViewById(R.id.profile_background);
        txtBar = getView().findViewById(R.id.username);
        txtName = getView().findViewById(R.id.nameUser);
        menu = getView().findViewById(R.id.profileMenu);
        rlayout = getView().findViewById(R.id.mainRel);
        playList = getView().findViewById(R.id.PlayList);
        signOutTxt = getView().findViewById(R.id.SignOutBtn);
        changePasswordTxt = getView().findViewById(R.id.ChangePassBtn);
    }

    private void ShowMenu() {
        PopupMenu MenuPopUp = new PopupMenu(getContext(), menu);
        MenuPopUp.getMenuInflater().inflate(R.menu.drawer_menu, MenuPopUp.getMenu());
        MenuPopUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_upload: {
                        Intent intent = new Intent(getContext(), UploadSongActivity.class);
                        startActivity(intent);

                        break;
                    }
                    case R.id.nav_setting: {
                        Intent intent = new Intent(getContext(), ChangePasswordActivity.class);
                        startActivity(intent);
                        break;
                    }

                    case R.id.nav_editProfile: {
                        Intent intent = new Intent(getContext(), EditProfileActivity.class);
                        startActivity(intent);
                        break;
                    }

                    case R.id.nav_logout: {
                        signOut();
                        break;

                    }

                }
                return false;
            }
        });
        MenuPopUp.show();
    }
   /* public void signOut() {
        mAuth.signOut();
    } */

    public void signOut() {
        if (mAuth.getInstance().getCurrentUser().getProviderId() == "google.com") {
            signOutGoogle();
        } else {
            signOutEmailPass();
        }

    }

    private void signOutGoogle() {
        // Firebase sign out
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.getInstance().signOut();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        // Google sign out
        mGoogleSignInClient.signOut();

    }

    public void signOutEmailPass() {
        FirebaseUser user = mAuth.getCurrentUser();
        mAuth.getInstance().signOut();
        if (mAuth.getCurrentUser() == null) {
            Log.d("1abc", "sign_out");
        }
        Log.d("1abc", "Sign_Out" + mAuth.getCurrentUser());
    }
   /* private void setBitMapFit(Bitmap bitmap,int width){

        int currentBitmapWidth = bitmap.getWidth();
        int currentBitmapHeight = bitmap.getHeight();
        int newWidth = width;

        //the image dont need to resize anymore

        int newHeight = (int) Math.floor((double) currentBitmapHeight * ((double) newWidth / (double) currentBitmapWidth));

        Bitmap newBitMap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        backGroundImage.setImageBitmap(newBitMap);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }*/
}
