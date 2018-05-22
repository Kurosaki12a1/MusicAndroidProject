package com.bku.jobs.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.bku.jobs.R;

/**
 * Created by Welcome on 5/22/2018.
 */

public class FullDetailPopUp extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_popup);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);


        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .8), (int) (height * .4));
    }
}
