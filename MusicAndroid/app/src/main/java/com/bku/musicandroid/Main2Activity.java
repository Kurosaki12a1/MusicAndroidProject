package com.bku.musicandroid;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.zip.Inflater;


public class Main2Activity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, ExploreFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener, LibraryFragment.OnFragmentInteractionListener {
    ViewPager mainViewPager;
    MainFragmentPagerAdapter mainFragmentPagerAdapter;
    TabLayout tabLayout;
    TextView tabHome, tabExplore, tabSearch, tabLibrary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mainViewPager = findViewById(R.id.viewPager);
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(this, getSupportFragmentManager());

        mainViewPager.setAdapter(mainFragmentPagerAdapter);

        tabLayout = findViewById(R.id.slideTab);
        tabLayout.setupWithViewPager(mainViewPager);

        initTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setTabIcon(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                setTabIcon(tab.getPosition(), false);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

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
}
