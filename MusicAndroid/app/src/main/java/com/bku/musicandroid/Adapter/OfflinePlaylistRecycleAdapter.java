package com.bku.musicandroid.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.musicandroid.Model.OfflinePlaylist;
import com.bku.musicandroid.R;

import java.util.ArrayList;

public class OfflinePlaylistRecycleAdapter extends RecyclerView.Adapter<OfflinePlaylistRecycleAdapter.ViewHolder> {
    private Context ctx;
    private ArrayList<OfflinePlaylist> listPlaylist;

    public OfflinePlaylistRecycleAdapter(Context ctx) {
        this.ctx = ctx;
        this.listPlaylist = new ArrayList<>();
    }

    @Override

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.offline_playlist_item, parent, false);
        ViewHolder viewHolder = new OfflinePlaylistRecycleAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        OfflinePlaylist aPlaylist = listPlaylist.get(position);
        holder.txtPlaylistName.setText(aPlaylist.getName());
        holder.txtSongCount.setText(String.valueOf(aPlaylist.getSongCount()));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPlaylist.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }

    public void add(OfflinePlaylist o) {
        listPlaylist.add(o);
        notifyDataSetChanged();
    }

    public ArrayList<OfflinePlaylist> getItemList() {
        return this.listPlaylist;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtPlaylistName;
        private TextView txtSongCount;
        private ImageView imgDelete;

        ViewHolder(View v) {
            super(v);
            txtPlaylistName = v.findViewById(R.id.txtPlaylistName);
            txtSongCount = v.findViewById(R.id.txtSongCount);
            imgDelete = v.findViewById(R.id.imgDelete);
        }
    }
}
