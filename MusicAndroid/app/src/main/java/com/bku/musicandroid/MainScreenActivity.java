package com.bku.musicandroid;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.TextView;

/**
 * Created by SonPhan on 3/24/2018.
 */


public class MainScreenActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, ExploreFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener, LibraryFragment.OnFragmentInteractionListener {
    ViewPager mainViewPager;
    MainFragmentPagerAdapter mainFragmentPagerAdapter;
    TabLayout tabLayout;
    TextView tabHome, tabExplore, tabSearch, tabLibrary;
    TextView txtStatus;
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainscreen);

        //txtStatus = findViewById(R.id.txt);
        txtStatus = findViewById(R.id.txtStatus);
        mainViewPager = findViewById(R.id.viewPager);
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(this, getSupportFragmentManager());

        mainViewPager.setAdapter(mainFragmentPagerAdapter);

        tabLayout = findViewById(R.id.slideTab);
        tabLayout.setupWithViewPager(mainViewPager);
        //Init tabs
        initTabIcons();

        //Handle tab switching
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Selected tab has black icon
                setTabIcon(tab.getPosition(), true);
                //Set status bar with the name of tab
                txtStatus.setText(tab.getText());
            }

            @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                ////Uselected tabs have white icon
                setTabIcon(tab.getPosition(), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    /**
     * Function: initTabIcons
     * Created by: SonPhan 24/3/2018
     * Purpose: Initialize tabs
     * Description: Initialize tabs' icons and title
     */
    void initTabIcons(){
        tabHome = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabExplore = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabSearch = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabLibrary = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);

        setTabIcon(0, true); //Tab "Home" is selected by default
        //The rest is unselected
        setTabIcon(1, false);
        setTabIcon(2, false);

        setTabIcon(3, false);

    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    /**
     * Function: setTabIcon
     * Created by: SonPhan 24/3/2018
     * Purpose: Set icons and titles for tabs
     * Description:
     */
    void setTabIcon(int position, boolean isSelected){
        if (position == 0){ //First tab: Home
            tabHome.setText("Home");
            if (isSelected){
                tabHome.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_home_black_36dp,0, 0);
            } else{
                tabHome.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_home_white_36dp,0, 0);
            }
            tabLayout.getTabAt(position).setCustomView(tabHome);
        } else if (position == 1){ //Second tab: Explore
            tabExplore.setText("Explore");
            if (isSelected){
                tabExplore.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_explore_black_36dp,0, 0);
            } else{
                tabExplore.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_explore_white_36dp,0, 0);
            }
            tabLayout.getTabAt(position).setCustomView(tabExplore);
        } else if (position == 2){ //Third tab: Search
            tabSearch.setText("Search");
            if (isSelected){
                tabSearch.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_search_black_36dp,0, 0);
            } else{
                tabSearch.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_search_white_36dp,0, 0);
            }
            tabLayout.getTabAt(position).setCustomView(tabSearch);
        } else {//position = 3. Last tab: Library
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
}

