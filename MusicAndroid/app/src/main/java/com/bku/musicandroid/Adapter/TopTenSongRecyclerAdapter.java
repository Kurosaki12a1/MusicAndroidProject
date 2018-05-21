package com.bku.musicandroid.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.musicandroid.R;
import com.bku.musicandroid.Activity.SongOnlinePlayerActivity;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.bku.musicandroid.Utility.UtilitySongOnlineClass;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by Welcome on 4/29/2018.
 */

public class TopTenSongRecyclerAdapter extends RecyclerView.Adapter<TopTenSongRecyclerAdapter.ViewHolder> {

    static Context context;
    ArrayList<SongPlayerOnlineInfo> ListSong;
    public final String USER_DATABASE="All_Users_Info_Database";
    public TopTenSongRecyclerAdapter(Context context, ArrayList<SongPlayerOnlineInfo>listSong){
        this.ListSong=listSong;
        this.context=context;
    }

    @Override
    public TopTenSongRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top10_item, parent, false);
        TopTenSongRecyclerAdapter.ViewHolder viewHolder = new TopTenSongRecyclerAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TopTenSongRecyclerAdapter.ViewHolder holder, int position) {
        final SongPlayerOnlineInfo songPlayerOnlineInfo=ListSong.get(position);
        holder.nameArtist.setText(songPlayerOnlineInfo.getSongArtists());
        Glide.with(context).load(songPlayerOnlineInfo.getImageSongURL()).into(holder.songImage);
        holder.nameSong.setText(songPlayerOnlineInfo.getSongName());
        holder.downloadTxt.setText("Downloaded: "+songPlayerOnlineInfo.getDownload());
        holder.likedTxt.setText("Liked: "+songPlayerOnlineInfo.getLiked());
        holder.viewTxt.setText("Listened: "+songPlayerOnlineInfo.getView());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilitySongOnlineClass utilitySongOnlineClass=UtilitySongOnlineClass.getInstance();
//                utilitySongOnlineClass.setItem(songPlayerOnlineInfo);
                utilitySongOnlineClass.setItemOfList(ListSong);
                Intent intent=new Intent(context,SongOnlinePlayerActivity.class);
                intent.putExtra("currentPosition", ListSong.indexOf(songPlayerOnlineInfo));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return ListSong.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView songImage;
        public TextView nameSong,nameArtist,downloadTxt,likedTxt, viewTxt;
        public ViewHolder(View itemView) {
            super(itemView);
            songImage=(ImageView)itemView.findViewById(R.id.imageView);
            nameArtist=(TextView)itemView.findViewById(R.id.txtName);
            nameSong=(TextView)itemView.findViewById(R.id.txtAuthor);
            downloadTxt =(TextView)itemView.findViewById(R.id.txtDownload);
            likedTxt =(TextView)itemView.findViewById(R.id.txtLike);
            viewTxt = (TextView)itemView.findViewById(R.id.txtView);
        }
    }
}
