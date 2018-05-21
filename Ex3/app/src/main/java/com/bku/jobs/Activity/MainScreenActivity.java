package com.bku.jobs.Activity;

import android.graphics.Color;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bku.jobs.Adapter.MainFragmentPagerAdapter;
import com.bku.jobs.Fragment.FavoriteFragment;
import com.bku.jobs.Fragment.HomeFragment;
import com.bku.jobs.R;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class MainScreenActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener,
        FavoriteFragment.OnFragmentInteractionListener {

    ViewPager mainViewPager;
    MainFragmentPagerAdapter mainFragmentPagerAdapter;
    TextView txtStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewPager = findViewById(R.id.vp_horizontal_ntb);
        txtStatus=(TextView)findViewById(R.id.txtTab);
        mainFragmentPagerAdapter = new MainFragmentPagerAdapter(this, getSupportFragmentManager());
        mainViewPager.setAdapter(mainFragmentPagerAdapter);
        initUI();
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
                        getResources().getDrawable(R.drawable.ic_heart),
                        Color.parseColor(colors[1]))
                        .title("Your Favorite Job")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(mainViewPager,0);
        //set Default Item



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
                    txtStatus.setText("Home");
                }
                else {
                    txtStatus.setText("Your Favorite Job");
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });


    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
