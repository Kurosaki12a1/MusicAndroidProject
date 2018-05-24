package com.bku.jobs;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Notify only when app start
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("PROFILE", Context.MODE_PRIVATE);
        String position = sharedPreferences.getString("position", "");
        String location = sharedPreferences.getString("location", "");
        int schedule = sharedPreferences.getInt("schedule", 0);
        String strSchedule = (schedule == 0) ? "fulltime" : "parttime";
        String url = "https://jobs.search.gov/jobs/search.json?query="
                + strSchedule + "+" + position + "+jobs+at+" + location;

        GetRelatedJobs getRelatedJobs = new GetRelatedJobs(getBaseContext());
        getRelatedJobs.execute(url);

    }
}
