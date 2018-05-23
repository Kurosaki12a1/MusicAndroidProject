package com.bku.jobs.Activity;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.jobs.Models.JobInfo;
import com.bku.jobs.R;
import com.bku.jobs.Util.UlTagHandler;

public class JobDetailActivity extends AppCompatActivity {


    ImageView backBtn;
    TextView jobTitle, company, jobType, location, jobCreated, jobDescription;
    Button applyBtn;
    String howToApply;

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

                jobDescription.setMovementMethod(LinkMovementMethod.getInstance());
                howToApply = extras.getString("howToApply");
            }
        }
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                final TextView message = new TextView(JobDetailActivity.this);
                message.setPadding(10,5,5,0);
                message.setText(fromHtml(howToApply));
                message.setMovementMethod(LinkMovementMethod.getInstance());
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    builder = new AlertDialog.Builder(JobDetailActivity.this, android.R.style.Theme_Material_Dialog_Alert);
//                } else {
                    builder = new AlertDialog.Builder(JobDetailActivity.this);
              //  }
                builder.setTitle(fromHtml("<b>How to apply:</b>"))
                        .setView(message)
                        .setPositiveButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .show();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY,null, new UlTagHandler());
        } else {
            return Html.fromHtml(html,null,new UlTagHandler());
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
        applyBtn = (Button)findViewById(R.id.applyBtn);
    }

}
