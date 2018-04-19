package com.bku.musicandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainScreenActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, ExploreFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener, LibraryFragment.OnFragmentInteractionListener {
    private static final String TAG = "MainScreenActivity";
    private Context mContext = MainScreenActivity.this;
    ViewPager mainViewPager;
    MainFragmentPagerAdapter mainFragmentPagerAdapter;
    TabLayout tabLayout;
    TextView tabHome, tabExplore, tabSearch, tabLibrary;
    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //Navigation Drawer
    private DrawerLayout drawer;
    NavigationView navigationView;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtEmail;
    private static final String urlNavHeaderBg = "https://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://images.idgesg.net/images/article/2017/08/android_robot_logo_by_ornecolorada_cc0_via_pixabay1904852_wide-100732483-large.jpg";

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);
        setupFirebaseAuth();
        mainViewPager = findViewById(R.id.viewPager);
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(this, getSupportFragmentManager());

        initDrawer();
        mainViewPager.setAdapter(mainFragmentPagerAdapter);

        tabLayout = findViewById(R.id.slideTab);
        tabLayout.setupWithViewPager(mainViewPager);

        initTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabIcon(tab.getPosition(), true);
            }

            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabIcon(tab.getPosition(), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    void initTabIcons(){
        tabHome = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabExplore = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabSearch = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabLibrary = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        setTabIcon(0, true);

        setTabIcon(1, false);
        setTabIcon(2, false);

        setTabIcon(3, false);

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    void setTabIcon(int position, boolean isSelected){
        if (position == 0){
            tabHome.setText("Home");
            if (isSelected){
                tabHome.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_home_black_36dp,0, 0);
            } else{
                tabHome.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_home_white_36dp,0, 0);
            }
            tabLayout.getTabAt(position).setCustomView(tabHome);
        } else if (position == 1){
            tabExplore.setText("Explore");
            if (isSelected){
                tabExplore.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_explore_black_36dp,0, 0);
            } else{
                tabExplore.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_explore_white_36dp,0, 0);
            }
            tabLayout.getTabAt(position).setCustomView(tabExplore);
        } else if (position == 2){
            tabSearch.setText("Search");
            if (isSelected){
                tabSearch.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_search_black_36dp,0, 0);
            } else{
                tabSearch.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_search_white_36dp,0, 0);
            }
            tabLayout.getTabAt(position).setCustomView(tabSearch);
        } else {
            tabLibrary.setText("Library");
            if (isSelected){
                tabLibrary.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_library_music_black_36dp,0, 0);
            } else{
                tabLibrary.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_library_music_white_36dp,0, 0);
            }
            tabLayout.getTabAt(position).setCustomView(tabLibrary);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }

    /*
    ** init Navagation Drawer
     */
    private void initDrawer(){
        drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtEmail = (TextView) navHeader.findViewById(R.id.email);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

//                 if (id == R.id.nav_upload) {
//
//                } else if (id == R.id.nav_setting) {
//
//                }
//                else
                    if (id == R.id.nav_logout) {
                    mAuth.signOut();
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        loadNavHeader();
    }


    @Override
    public void onBackPressed() {
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void loadNavHeader() {
        // name, website
        txtName.setText("HyHy");
        txtEmail.setText("huy@gmail.com");

        // loading header background image
        Glide.with(this).load(urlNavHeaderBg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

    }
    /*
    ---------------------------------------- Firebase --------------------------------
     */
    private void checkCurrentUser(FirebaseUser user){
        Log.d(TAG, "checkCurrentUser: checking if user is logged in");
        if(user == null){
            Intent intent = new Intent(mContext,Login.class);
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
}

