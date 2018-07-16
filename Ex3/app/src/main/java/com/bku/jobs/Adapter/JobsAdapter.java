package com.bku.jobs.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.jobs.ModelData.JobData;
import com.bku.jobs.Models.JobInfo;
import com.bku.jobs.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Huy on 05/23/18.
 */

public class JobsAdapter extends ArrayAdapter<JobData> {
    private Context mContext;
    private List<JobData> jobList = new ArrayList<>();

    @BindView(R.id.companyLogo) ImageView companyLogo;
    @BindView(R.id.titleJob) TextView titleJob;
    @BindView(R.id.companyName) TextView companyName;
    @BindView(R.id.typeJob) TextView typeJob;
    @BindView(R.id.location) TextView location;
    @BindView(R.id.createdAt) TextView createdAt;

    public JobsAdapter(@NonNull Context context, ArrayList<JobData> list){
        super(context,0,list);
        this.mContext = context;
        this.jobList = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.favorite_job_item,parent,false);
        JobData jobInfo=jobList.get(position);
        ButterKnife.bind(this,listItem);
        Glide.with(mContext).load(jobInfo.getCompanyLogo()).into(companyLogo);
        titleJob.setText(jobInfo.getTitle());
        typeJob.setText("/ " +jobInfo.getType());
        companyName.setText("Company Name : " +jobInfo.getCompany());
        createdAt.setText(jobInfo.getCreatedAt());
        location.setText("Location : "+jobInfo.getLocation());
        return listItem;
    }

}
