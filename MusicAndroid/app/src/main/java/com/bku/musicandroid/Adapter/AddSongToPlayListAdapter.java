package com.bku.musicandroid.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.musicandroid.Activity.AddSongToPlayListPopUp;
import com.bku.musicandroid.Model.PlayListOnlineInfo;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.bku.musicandroid.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Welcome on 5/14/2018.
 */

public class AddSongToPlayListAdapter extends RecyclerView.Adapter<AddSongToPlayListAdapter.ViewHolder> {
    static Context context;
    SongPlayerOnlineInfo song;
    ArrayList<PlayListOnlineInfo> lst;

    public static final String Database_Path="All_PLayList_Info_Database";
    public static final String Song_List="All_Song_Of_PlayList_Database";
    public static final String Liked_Path="All_Liked_PlayList_Database";
    public static final String View_Path="All_View_PlayList_Database";

    public AddSongToPlayListAdapter(Context context,SongPlayerOnlineInfo song,ArrayList<PlayListOnlineInfo>list){
        this.context=context;
        this.song=song;
        this.lst=new ArrayList<>(list);
        this.lst=list;
    }
    @Override
    public AddSongToPlayListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_playlist_online, parent, false);

        AddSongToPlayListAdapter.ViewHolder viewHolder = new AddSongToPlayListAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AddSongToPlayListAdapter.ViewHolder holder, int position) {
        final int nTempPosition=position;
        final PlayListOnlineInfo playListOnlineInfo=lst.get(position);
        holder.namePlayList.setText(playListOnlineInfo.getPlayListName());
        holder.userUpload.setText(playListOnlineInfo.getUserName());
        holder.Liked.setText(playListOnlineInfo.getLiked());
        holder.ViewListen.setText(playListOnlineInfo.getView());
        holder.deletePlayList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lst.remove(nTempPosition);
                notifyItemRemoved(nTempPosition);
                removePlayList(playListOnlineInfo.getPlayListId(),playListOnlineInfo.getUserId());
                notifyItemRangeChanged(nTempPosition,lst.size());
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(Song_List);
                databaseReference.child(playListOnlineInfo.getPlayListId()).child(song.getSongId()).setValue(song);
                ((AddSongToPlayListPopUp)context).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return lst.size();
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
    private void removePlayList(String id,String key){
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference(Database_Path);
        databaseReference.child(key).child(id).removeValue();

        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference(Liked_Path);
        databaseReference1.child(id).removeValue();

        DatabaseReference databaseReference2=FirebaseDatabase.getInstance().getReference(Song_List);
        databaseReference2.child(id).removeValue();

        DatabaseReference databaseReference3=FirebaseDatabase.getInstance().getReference(View_Path);
        databaseReference3.child(id).removeValue();

    }
    private void OnClickListener(){

    }
}
