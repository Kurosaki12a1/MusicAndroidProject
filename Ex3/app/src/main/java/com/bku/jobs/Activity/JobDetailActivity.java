package com.bku.jobs.Activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.jobs.Models.JobInfo;
import com.bku.jobs.R;

public class JobDetailActivity extends AppCompatActivity {

   // JobInfo job;
    ImageView backBtn;

    TextView jobTitle, company, jobType, location, jobCreated, jobDescription, howToApply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        bindView();
        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                Toast.makeText(this,"ERROR", Toast.LENGTH_LONG).show();
            }
            else {
                //company, jobType, location, jobCreated, jobDescription, howToApply;
                jobTitle.setText(extras.getString("jobTitle"));
                company.setText(extras.getString("company"));
                jobType.setText(extras.getString("jobType"));
                location.setText(extras.getString("location"));
                jobCreated.setText(extras.getString("jobCreated"));
                jobDescription.setText(fromHtml(extras.getString("jobDescription")));
                howToApply.setText(extras.getString("howToApply"));

            }
        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }
    private void bindView(){
        backBtn = (ImageView) findViewById(R.id.backBtn);
        jobTitle = (TextView) findViewById(R.id.jobTitle);
        company = (TextView) findViewById(R.id.companyName);
        jobType = (TextView) findViewById(R.id.jobType);
        location = (TextView) findViewById(R.id.location);
        jobCreated = (TextView) findViewById(R.id.jobCreated);
        jobDescription = (TextView) findViewById(R.id.jobDescription);
        howToApply = (TextView) findViewById(R.id.how_to_apply);
    }
}
