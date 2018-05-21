package com.bku.musicandroid.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bku.musicandroid.Fragments.ProfileFragment;
import com.bku.musicandroid.Model.SongPlayerInfo;
import com.bku.musicandroid.Utility.CircleTransform;

import com.bku.musicandroid.Fragments.ExploreFragment;
import com.bku.musicandroid.Fragments.HomeFragment;
import com.bku.musicandroid.Fragments.LibraryFragment;
import com.bku.musicandroid.Fragments.SearchFragment;
import com.bku.musicandroid.Fragments.SongGenreFragment;
import com.bku.musicandroid.Adapter.MainFragmentPagerAdapter;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Fragments.SongsFragment;
import com.bku.musicandroid.Utility.Constants;
import com.bku.musicandroid.Utility.UtilitySongOfflineClass;
import com.bku.musicandroid.Utility.UtilitySongOnlineClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

/**
 * Created by SonPhan on 3/24/2018.
 */


public class MainScreenActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        ExploreFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        LibraryFragment.OnFragmentInteractionListener, SongsFragment.OnFragmentInteractionListener, SongGenreFragment.OnFragmentInteractionListener ,
        ProfileFragment.OnFragmentInteractionListener{
    private static final String TAG = "MainScreenActivity";

    private Context mContext = MainScreenActivity.this;
    ViewPager mainViewPager;
    MainFragmentPagerAdapter mainFragmentPagerAdapter;
    TextView txtStatus;

    ImageView imgPlay;
    LinearLayout layoutMusicControl;
    BroadcastReceiver receiver;
    ArrayList<? extends SongPlayerInfo> listSong;
    TextView txtSongName;
    String lastSongPath = "";
    int nPosition;
    boolean isOnline;
    boolean isPause;
    boolean isRepeatAll;
    boolean isRepeatOne;
    boolean isShuffle;
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private int currentItem=0;
    //Navigation Drawer


    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        setupFirebaseAuth();
        mainViewPager = findViewById(R.id.vp_horizontal_ntb);
        txtStatus=(TextView)findViewById(R.id.txtTab);
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(this, getSupportFragmentManager());
        mainViewPager.setAdapter(mainFragmentPagerAdapter);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            currentItem=extras.getInt("currentItem");
            mainViewPager.setCurrentItem(currentItem);
        }
        //set Default Item 
        currentItem=0;

        imgPlay = findViewById(R.id.imgPlay);
        layoutMusicControl = findViewById(R.id.layoutMusicControl);
        txtSongName = findViewById(R.id.txtSongName);
        txtSongName.setSelected(true);

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                nPosition = intent.getIntExtra("position", 0);
                isOnline = intent.getBooleanExtra("isOnline", false);
                isPause = intent.getBooleanExtra("isPause", false);
                isRepeatAll = intent.getBooleanExtra("isRepeatAll", isRepeatAll);
                isRepeatOne = intent.getBooleanExtra("isRepeatOne", isRepeatOne);
                isShuffle = intent.getBooleanExtra("isShuffle", isShuffle);

                if (isOnline) {
                    listSong = UtilitySongOnlineClass.getInstance().getItemOfList();
                } else {
                    listSong = UtilitySongOfflineClass.getInstance().getList();
                }

                if (!isPause) {
                    imgPlay.setImageResource(R.drawable.ic_player_pause);
                } else {
                    imgPlay.setImageResource(R.drawable.btn_playback_play);
                }

                // Update new song info UI when auto move to another song
                try {
                    if (!lastSongPath.equals(listSong.get(nPosition).getPath())) {
                        layoutMusicControl.setVisibility(View.VISIBLE);
                        lastSongPath = listSong.get(nPosition).getPath();
                        txtSongName.setText(listSong.get(nPosition).getSongName());
                    }
                } catch (Exception e) {
                    // WELL, LET'S TRY AGAIN NEXT TIME :V
                    lastSongPath = "";
                }
            }
        };

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Constants.ACTION.PLAY_OR_PAUSE_ACTION);
                sendBroadcast(i);
            }
        });

        layoutMusicControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if (isOnline) {
                    i = new Intent(MainScreenActivity.this, SongOnlinePlayerActivity.class);
                } else {
                    i = new Intent(MainScreenActivity.this, SongOfflinePlayerActivity.class);
                }
                i.putExtra("currentPosition", nPosition);
                i.putExtra("isPause", isPause);
                i.putExtra("isRepeatAll", isRepeatAll);
                i.putExtra("isRepeatOne", isRepeatOne);
                i.putExtra("isShuffle", isShuffle);
                startActivity(i);
            }
        });

        initUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("MusicPlayerUpdate"));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }

    private void initUI(){
        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_transparent_home),
                        Color.parseColor(colors[0]))
                        .title("Home")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_transparent_compass),
                        Color.parseColor(colors[1]))
                        .title("Explore")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_transparent_search),
                        Color.parseColor(colors[2]))
                        .title("Search")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_transparent_music),
                        Color.parseColor(colors[3]))
                        .title("Library")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_profile_transparent),
                        Color.parseColor(colors[4]))
                        .title("Profile")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(mainViewPager,currentItem);

        //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
        navigationTabBar.setBehaviorEnabled(true);



        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(final NavigationTabBar.Model model, final int index) {

            }

            @Override
            public void onEndTabSelected(final NavigationTabBar.Model model, final int index) {
                model.hideBadge();
            }
        });
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                if(position==0){
                    txtStatus.setText(R.string.home);
                }
                else if(position==1){
                    txtStatus.setText(R.string.explore);
                }
                else if(position==2){
                    txtStatus.setText(R.string.search);
                }
                else if(position==3){
                    txtStatus.setText(R.string.library);
                }
                else{
                    txtStatus.setText(R.string.profle);
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });


    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in");
        if(user == null){
            Intent intent = new Intent(mContext,LoginActivity.class);
            startActivity(intent);
        }
    }
    /*
    Setup Firebase Auth
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = mAuth.getCurrentUser();
                checkCurrentUser(user);
                if(user!=null) {
                    //User signed in
                    Log.d(TAG, "onAuthStateChanged: signed_in" + user.getUid());
                } else {
                    // User signed out
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}

