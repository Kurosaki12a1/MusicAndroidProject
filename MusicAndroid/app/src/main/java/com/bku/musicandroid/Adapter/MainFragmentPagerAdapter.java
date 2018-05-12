package com.bku.musicandroid.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bku.musicandroid.Fragments.ExploreFragment;
import com.bku.musicandroid.Fragments.HomeFragment;
import com.bku.musicandroid.Fragments.LibraryFragment;
import com.bku.musicandroid.Fragments.SearchFragment;

/**
 * Created by SonPhan on 3/24/2018.
 */

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MainFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new HomeFragment();
        } else if (position == 1){
            return new ExploreFragment();
        } else if (position == 2){
            return new SearchFragment();
        } else {
            return new LibraryFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                //return mContext.getString(R.string.category_usefulinfo);
                return "Home";
            case 1:
                //return mContext.getString(R.string.category_places);
                return "Explore";
            case 2:
                //return mContext.getString(R.string.category_food);
                return "Search";
            case 3:
                //return mContext.getString(R.string.category_nature);
                return "Library";
            default:
                return null;
        }
    }


}