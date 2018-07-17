package com.bku.jobs.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.jobs.Database.OfflineDatabaseHelper;
import com.bku.jobs.ModelData.JobData;
import com.bku.jobs.Models.JobInfo;
import com.bku.jobs.R;
import com.bku.jobs.Util.UlTagHandler;
import com.bku.jobs.Util.UtilityJob;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class JobDetailActivity extends AppCompatActivity {

    @BindView(R.id.liked) ImageView liked;
    @BindView(R.id.jobTitle) TextView jobTitle;
    @BindView(R.id.companyName) TextView company;
    @BindView(R.id.jobType) TextView jobType;
    @BindView(R.id.location) TextView location;
    @BindView(R.id.jobCreated) TextView jobCreated;
    @BindView(R.id.jobDescription) TextView jobDescription;
    @BindView(R.id.applyBtn) Button applyBtn;
    @BindView(R.id.backBtn) ImageView backBtn;
    String howToApply;
    OfflineDatabaseHelper db;
    JobData jobInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);
        ButterKnife.bind(this);
        //Declare Database
        db=new OfflineDatabaseHelper(JobDetailActivity.this);
        //Get JobInfo
        jobInfo= UtilityJob.getInstance().getJobInfo();
        checkAlreadyLiked();
        initUI();
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
                builder = new AlertDialog.Builder(JobDetailActivity.this);
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

        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.checkExist(jobInfo.getId())){
                    db.deleteJob(jobInfo.getId());
                    liked.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_video));
                }
                else {
                    db.insertJob(jobInfo);
                    liked.setImageDrawable(getResources().getDrawable(R.drawable.ic_faved_album));
                }
            }
        });
    }

    private void checkAlreadyLiked(){
        if(db.checkExist(jobInfo.getId())){
            liked.setImageDrawable(getResources().getDrawable(R.drawable.ic_faved_album));
        }
        else{
            liked.setImageDrawable(getResources().getDrawable(R.drawable.ic_fav_video));
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY,null, new UlTagHandler());
        } else {
            return Html.fromHtml(html,null,new UlTagHandler());
        }
    }
    private void initUI(){
        jobTitle.setText(jobInfo.getTitle());
        company.setText(jobInfo.getCompany());
        jobType.setText(jobInfo.getType());
        location.setText(jobInfo.getLocation());
        jobCreated.setText(jobInfo.getCreatedAt());
        jobDescription.setText(fromHtml(jobInfo.getDescription()));
        jobDescription.setMovementMethod(LinkMovementMethod.getInstance());
        howToApply =jobInfo.getHowToApply();
        Log.d("__", "initUI: ______" +  jobInfo.getHowToApply()) ;
    }



}
