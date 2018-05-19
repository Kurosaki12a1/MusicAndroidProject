package com.bku.musicandroid.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bku.musicandroid.Database.OfflineDatabaseHelper;
import com.bku.musicandroid.Model.OfflinePlaylist;
import com.bku.musicandroid.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class OfflinePlaylistRecycleAdapter extends RecyclerView.Adapter<OfflinePlaylistRecycleAdapter.ViewHolder> {
    private Context ctx;
    private ArrayList<OfflinePlaylist> listPlaylist;
    private OfflineDatabaseHelper db;

    /**
     * Created by Son on 5/19/2018.
     */
    public OfflinePlaylistRecycleAdapter(Context ctx) {
        this.ctx = ctx;
        this.listPlaylist = new ArrayList<>();
    }

    /**
     * Created by Son on 5/19/2018.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.offline_playlist_item, parent, false);
        ViewHolder viewHolder = new OfflinePlaylistRecycleAdapter.ViewHolder(view);
        db = new OfflineDatabaseHelper(ctx);
        return viewHolder;
    }

    /**
     * Created by Son on 5/19/2018.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final OfflinePlaylist aPlaylist = listPlaylist.get(position);
        holder.txtPlaylistName.setText(aPlaylist.getName());
        holder.txtSongCount.setText(String.valueOf(aPlaylist.getSongCount()));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Delete from db
                db.deletePlaylist(aPlaylist.getId());

                listPlaylist.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    /**
     * Created by Son on 5/19/2018.
     */
    @Override
    public int getItemCount() {
        return listPlaylist.size();
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public void add(OfflinePlaylist o) {
        listPlaylist.add(o);
        Collections.sort(listPlaylist, new Comparator<OfflinePlaylist>() {
            @Override
            public int compare(OfflinePlaylist playlist1, OfflinePlaylist playlist2) {
                return playlist1.getName().compareTo(playlist2.getName());
            }
        });
        notifyDataSetChanged();
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public void setListPlaylist(ArrayList<OfflinePlaylist> listPlaylist){
        this.listPlaylist = listPlaylist;
        notifyDataSetChanged();
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public ArrayList<OfflinePlaylist> getItemList() {
        return this.listPlaylist;
    }

    /**
     * Created by Son on 5/19/2018.
     */
    class ViewHolder extends RecyclerView.ViewHolder {
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
