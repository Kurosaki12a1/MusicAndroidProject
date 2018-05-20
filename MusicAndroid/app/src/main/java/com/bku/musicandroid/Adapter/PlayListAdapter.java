package com.bku.musicandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.musicandroid.Activity.AddSongToPlayListPopUp;
import com.bku.musicandroid.Activity.SongOnlinePlayerActivity;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Utility.UtilitySongOnlineClass;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 5/14/2018.
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder>  {

    static Context context;
    ArrayList<SongPlayerOnlineInfo> ListSong;
    private String playListId;
    public static final String Song_List="All_Song_Of_PlayList_Database";
    public PlayListAdapter(Context context,ArrayList<SongPlayerOnlineInfo> lst,String playListId)
    {
        this.context=context;
        this.ListSong=new ArrayList<>(lst);
        this.ListSong=lst;
        this.playListId=playListId;
        UtilitySongOnlineClass utilitySongOnlineClass=UtilitySongOnlineClass.getInstance();
        utilitySongOnlineClass.setItemOfList(lst);
    }
    @Override
    public PlayListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_song_online_list, parent, false);

        PlayListAdapter.ViewHolder viewHolder = new PlayListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PlayListAdapter.ViewHolder holder, int position) {
        final int nTempPosition=position;
        final SongPlayerOnlineInfo songPlayerOnlineInfo=ListSong.get(position);
        holder.nameArtist.setText(songPlayerOnlineInfo.getSongArtists());
        Glide.with(context).load(songPlayerOnlineInfo.getImageSongURL()).into(holder.songImage);
        holder.nameSong.setText(songPlayerOnlineInfo.getSongName());
        holder.userUpload.setText(songPlayerOnlineInfo.getUserName());
        String strTemp="Listen : "+songPlayerOnlineInfo.getView();
        holder.ViewListen.setText(strTemp);
        strTemp="DownLoad : "+songPlayerOnlineInfo.getDownload();
        holder.DownLoad.setText(strTemp);
        strTemp="Liked : "+songPlayerOnlineInfo.getLiked();
        holder.Liked.setText(strTemp);

        holder.addPlayList.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_trash));
        holder.addPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListSong.remove(nTempPosition);
                notifyItemRemoved(nTempPosition);
                notifyItemRangeChanged(nTempPosition,ListSong.size());
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(Song_List);
                databaseReference.child(playListId).child(songPlayerOnlineInfo.getSongId()).removeValue();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilitySongOnlineClass utilitySongOnlineClass=UtilitySongOnlineClass.getInstance();
                utilitySongOnlineClass.setItemOfList(ListSong);
                Intent intent=new Intent(context,SongOnlinePlayerActivity.class);
                intent.putExtra("currentPosition",nTempPosition);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ListSong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView songImage,addPlayList;
        public TextView nameSong,userUpload,nameArtist,DownLoad,Liked,ViewListen;
        public ViewHolder(View itemView) {
            super(itemView);

            songImage=(ImageView)itemView.findViewById(R.id.songImage);
            nameArtist=(TextView)itemView.findViewById(R.id.nameArtist);
            nameSong=(TextView)itemView.findViewById(R.id.nameSong);
            userUpload=(TextView)itemView.findViewById(R.id.userUpload);
            DownLoad=(TextView)itemView.findViewById(R.id.DownLoad);
            Liked=(TextView)itemView.findViewById(R.id.Liked);
            ViewListen=(TextView)itemView.findViewById(R.id.ViewListen);
            addPlayList=(ImageView)itemView.findViewById(R.id.addPlayList);
        }
    }
}
