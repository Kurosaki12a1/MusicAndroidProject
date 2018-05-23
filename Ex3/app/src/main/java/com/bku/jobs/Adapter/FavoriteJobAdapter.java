package com.bku.jobs.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.jobs.Database.OfflineDatabaseHelper;
import com.bku.jobs.Fragment.FavoriteFragment;
import com.bku.jobs.Models.JobInfo;
import com.bku.jobs.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Welcome on 5/22/2018.
 */

public class FavoriteJobAdapter extends RecyclerView.Adapter<FavoriteJobAdapter.ViewHolder> {

    private static Context ctx;
    ArrayList<JobInfo> lstJobInfo;


    public FavoriteJobAdapter(Context context, ArrayList<JobInfo> lst){
        this.ctx=context;
        this.lstJobInfo=new ArrayList<>(lst);
        this.lstJobInfo=lst;
}

    @NonNull
    @Override
    public FavoriteJobAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_job_item, parent, false);
        ViewHolder viewHolder = new FavoriteJobAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteJobAdapter.ViewHolder holder, int position) {

        JobInfo jobInfo=lstJobInfo.get(position);
        Glide.with(ctx).load(jobInfo.getCompanyLogo()).fitCenter().into(holder.companyLogo);
        holder.titleJob.setText(jobInfo.getJobTitle());
        holder.typeJob.setText("/ " +jobInfo.getType());
        holder.companyName.setText("Company Name : " +jobInfo.getCompany());
        holder.createdAt.setText(jobInfo.getJobCreatedAt());
        holder.location.setText("Location : "+jobInfo.getLocation());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
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
