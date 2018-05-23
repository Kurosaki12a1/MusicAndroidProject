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

import com.bku.jobs.Models.JobInfo;
import com.bku.jobs.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Huy on 05/23/18.
 */

public class JobsAdapter extends ArrayAdapter<JobInfo> {
    private Context mContext;
    private List<JobInfo> jobList = new ArrayList<>();

    public JobsAdapter(@NonNull Context context, ArrayList<JobInfo> list){
        super(context,0,list);
        this.mContext = context;
        this.jobList = list;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
        JobInfo currentJob = jobList.get(position);
        ImageView logoCompany = (ImageView) listItem.findViewById(R.id.companyLogo);
        Glide.with(mContext).load(currentJob.getCompanyLogo()).into(logoCompany);
        TextView jobTitle = (TextView) listItem.findViewById(R.id.jobTitle);
        jobTitle.setText(currentJob.getJobTitle());
        TextView company = (TextView) listItem.findViewById(R.id.companyName);
        company.setText(currentJob.getCompany());
        TextView jobType = (TextView) listItem.findViewById(R.id.jobType);
        jobType.setText(currentJob.getType());
        TextView location = (TextView) listItem.findViewById(R.id.location);
        location.setText(currentJob.getLocation());
        TextView jobCreated = (TextView) listItem.findViewById(R.id.jobCreated);
        jobCreated.setText(currentJob.getJobCreatedAt());
        return listItem;
    }

}
