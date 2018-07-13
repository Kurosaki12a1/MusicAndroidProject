package com.bku.jobs.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.jobs.Activity.JobDetailActivity;
import com.bku.jobs.ModelData.JobData;
import com.bku.jobs.Models.JobInfo;
import com.bku.jobs.R;
import com.bku.jobs.Util.UtilityJob;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Welcome on 5/22/2018.
 */

public class SearchJobAdapter extends RecyclerView.Adapter<SearchJobAdapter.ViewHolder> {

    private static Context ctx;
    ArrayList<JobData> lstJobInfo;


    public SearchJobAdapter(Context context, ArrayList<JobData> lst){
        this.ctx=context;
        this.lstJobInfo=new ArrayList<>(lst);
        this.lstJobInfo=lst;
}

    @NonNull
    @Override
    public SearchJobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_job_item, parent, false);
        ViewHolder viewHolder = new SearchJobAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchJobAdapter.ViewHolder holder, int position) {

        final JobData jobInfo=lstJobInfo.get(position);
        Glide.with(ctx).load(jobInfo.getCompanyLogo()).fitCenter().into(holder.companyLogo);
        holder.titleJob.setText(jobInfo.getTitle());
        holder.typeJob.setText("/ " +jobInfo.getType());
        holder.location.setText(jobInfo.getLocation());
        holder.companyName.setText("Company Name : " +jobInfo.getCompany());
        holder.createdAt.setText(jobInfo.getCreatedAt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilityJob utilityJob=UtilityJob.getInstance();
                utilityJob.setJobInfo(jobInfo);
                ctx.startActivity(new Intent(ctx, JobDetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstJobInfo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.companyLogo) ImageView companyLogo;
        @BindView(R.id.titleJob) TextView titleJob;
        @BindView(R.id.companyName) TextView companyName;
        @BindView(R.id.typeJob) TextView typeJob;
        @BindView(R.id.location) TextView location;
        @BindView(R.id.createdAt) TextView createdAt;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
