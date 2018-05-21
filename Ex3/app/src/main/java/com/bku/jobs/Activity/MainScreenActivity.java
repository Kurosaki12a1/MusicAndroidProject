package com.bku.jobs.Activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bku.jobs.Adapter.MainFragmentPagerAdapter;
import com.bku.jobs.R;

public class MainScreenActivity extends AppCompatActivity {

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
    }
}
