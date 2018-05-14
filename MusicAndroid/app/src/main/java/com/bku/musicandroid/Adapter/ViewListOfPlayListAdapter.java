package com.bku.musicandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.musicandroid.Activity.PlayListActivity;
import com.bku.musicandroid.Model.PlayListOnlineInfo;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Utility.UtilitySongOnlineClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 5/14/2018.
 */

public class ViewListOfPlayListAdapter extends RecyclerView.Adapter<ViewListOfPlayListAdapter.ViewHolder>  {

    static Context context;
    ArrayList<PlayListOnlineInfo> ListPlayList;

    public static final String Liked_Path="All_Liked_PlayList_Database";
    public static final String View_Path="All_View_PlayList_Database";

    public ViewListOfPlayListAdapter(Context context, ArrayList<PlayListOnlineInfo> lst){
        this.context=context;
        this.ListPlayList=new ArrayList<>(lst);
        this.ListPlayList=lst;
    }

    @Override
    public ViewListOfPlayListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_playlist_online, parent, false);

        ViewListOfPlayListAdapter.ViewHolder viewHolder = new ViewListOfPlayListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewListOfPlayListAdapter.ViewHolder holder,  int position) {
        final int nTempPosition=position;
        final PlayListOnlineInfo playListOnlineInfo=ListPlayList.get(position);
        holder.namePlayList.setText(playListOnlineInfo.getPlayListName());
        holder.userUpload.setText(playListOnlineInfo.getUserName());
        holder.Liked.setText(playListOnlineInfo.getLiked());
        holder.ViewListen.setText(playListOnlineInfo.getView());
        holder.deletePlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListPlayList.remove(nTempPosition);
                notifyItemRemoved(nTempPosition);
                notifyItemRangeChanged(nTempPosition,ListPlayList.size());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PlayListActivity.class);
                intent.putExtra("currentPosition",nTempPosition);
                intent.putExtra("PlayListId",playListOnlineInfo.getPlayListId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ListPlayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView namePlayList,userUpload,ViewListen,Liked;
        private ImageView deletePlayList;

        public ViewHolder(View itemView) {
            super(itemView);
            namePlayList=itemView.findViewById(R.id.namePlayList);
            userUpload=itemView.findViewById(R.id.userUpload);
            ViewListen= itemView.findViewById(R.id.ViewListen);
            Liked=itemView.findViewById(R.id.Liked);
            deletePlayList=itemView.findViewById(R.id.deletePlayList);
        }
    }
}
