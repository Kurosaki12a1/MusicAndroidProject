package com.bku.musicandroid.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bku.musicandroid.Database.OfflineDatabaseHelper;
import com.bku.musicandroid.Fragments.OfflineSongInPlaylistFragment;
import com.bku.musicandroid.Model.OfflinePlaylist;
import com.bku.musicandroid.Model.SongPlayerOfflineInfo;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Utility.UtilitySongOfflineClass;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OfflinePlaylistRecycleAdapter extends RecyclerView.Adapter<OfflinePlaylistRecycleAdapter.ViewHolder> {
    private Context ctx;
    private ArrayList<OfflinePlaylist> listPlaylist;
    private OfflineDatabaseHelper db;
    private boolean isAddAction;
    private int _position;

    /**
     * Created by Son on 5/19/2018.
     */
    public OfflinePlaylistRecycleAdapter(Context ctx, int position, boolean isAddAction) {
        this.ctx = ctx;
        this.isAddAction = isAddAction;
        this._position = position;
        this.listPlaylist = new ArrayList<>();
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public OfflinePlaylistRecycleAdapter(Context ctx, boolean isAddAction) {
        this.ctx = ctx;
        this.isAddAction = isAddAction;
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAddAction) {
                    SongPlayerOfflineInfo song = UtilitySongOfflineClass.getInstance().getListAllSongOffline().get(_position);
                    db.insertSongIntoPlaylist(song, aPlaylist.getId());
                    TastyToast.makeText(ctx, "Added to playlist " + aPlaylist.getName(), Toast.LENGTH_LONG, TastyToast.SUCCESS).show();
                    ((Activity) ctx).finish();
                } else {
                    goToSongsPlaylist(aPlaylist.getId(), aPlaylist.getName());
                }
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
    public void setListPlaylist(ArrayList<OfflinePlaylist> listPlaylist) {
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

    /**
     * Function: goToPlaylist
     * Created by: SonPhan 19/05/2018
     * Purpose: Go to playlist
     * Description:
     */
    private void goToSongsPlaylist(int playlistId, String playlistName) {

        android.support.v4.app.FragmentManager fragmentManager = ((FragmentActivity)ctx).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        OfflineSongInPlaylistFragment fragment = new OfflineSongInPlaylistFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("playlistId", playlistId);
        bundle.putString("playlistName", playlistName);
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.fragmentPlaylist, fragment).commit();
        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commitAllowingStateLoss();
//        fragmentManager.executePendingTransactions();
    }

}
