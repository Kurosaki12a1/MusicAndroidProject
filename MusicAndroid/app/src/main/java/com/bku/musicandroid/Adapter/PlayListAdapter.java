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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Welcome on 5/14/2018.
 */

public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.ViewHolder>  {

    static Context context;
    ArrayList<SongPlayerOnlineInfo> ListSong;

    public PlayListAdapter(Context context,ArrayList<SongPlayerOnlineInfo> lst)
    {
        this.context=context;
        this.ListSong=new ArrayList<>(lst);
        this.ListSong=lst;
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

        holder.addPlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddSongToPlayListPopUp.class);

                intent.putExtra("nameSong",songPlayerOnlineInfo.getSongName());
                intent.putExtra("nameArtist",songPlayerOnlineInfo.getSongArtists());
                intent.putExtra("userUpload",songPlayerOnlineInfo.getUserName());
                intent.putExtra("viewListen",songPlayerOnlineInfo.getView());
                intent.putExtra("Download",songPlayerOnlineInfo.getDownload());
                intent.putExtra("Liked",songPlayerOnlineInfo.getLiked());
                intent.putExtra("songId",songPlayerOnlineInfo.getSongId());
                intent.putExtra("songGenre",songPlayerOnlineInfo.getSongGenre());
                intent.putExtra("ImageSongURL",songPlayerOnlineInfo.getImageSongURL());
                intent.putExtra("songURL",songPlayerOnlineInfo.getSongURL());
                intent.putExtra("userId",songPlayerOnlineInfo.getUserId());
                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo phan son se lam
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
