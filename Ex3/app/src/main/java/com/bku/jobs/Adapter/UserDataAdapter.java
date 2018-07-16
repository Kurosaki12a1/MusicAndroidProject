package com.bku.jobs.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.jobs.ModelData.UserData.ResultsItem;
import com.bku.jobs.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import javax.xml.transform.Result;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Welcome on 7/15/2018.
 */

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {

    static Context context;
    ArrayList<ResultsItem> lstResult;

    public UserDataAdapter(Context context,ArrayList<ResultsItem> lst){
        this.context=context;
        this.lstResult=lst;
    }

    @NonNull
    @Override
    public UserDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDataAdapter.ViewHolder holder, int position) {
        ResultsItem data=lstResult.get(position);
        Glide.with(context).load(data.getPicture().getThumbnail()).into(holder.avatarUser);
        holder.nameUser.setText(data.getName().getFirst() + " " +data.getName().getLast());
        holder.genderUser.setText(data.getGender());
        holder.emailUser.setText(data.getEmail());
        holder.phoneUser.setText(data.getPhone());
        holder.cellUser.setText(data.getCell());
        holder.natUser.setText(data.getNat());
    }

    @Override
    public int getItemCount() {
        return lstResult.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.avatarUser) ImageView avatarUser;
        @BindView(R.id.nameUser) TextView nameUser;
        @BindView(R.id.genderUser) TextView genderUser;
        @BindView(R.id.emailUser) TextView emailUser;
        @BindView(R.id.phoneUser) TextView phoneUser;
        @BindView(R.id.cellUser) TextView cellUser;
        @BindView(R.id.natUser) TextView natUser;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
