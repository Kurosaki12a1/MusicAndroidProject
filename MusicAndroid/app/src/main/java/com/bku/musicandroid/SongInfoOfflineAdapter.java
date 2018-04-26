/**
 * Created by SonPhan on 4/22/2018.
 */
package com.bku.musicandroid;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SongInfoOfflineAdapter extends ArrayAdapter<SongPlayerOfflineInfo> {
    private ArrayList<SongPlayerOfflineInfo> listSongInfo;
    private final LayoutInflater inflater;
    private final int resource;

    public SongInfoOfflineAdapter(@NonNull Activity context, int resource, @NonNull List<SongPlayerOfflineInfo> objects) {
        super(context, resource, objects);
        inflater = context.getLayoutInflater();
        this.listSongInfo = new ArrayList<>(objects);
        this.resource = resource;
    }

    public void setDataSet(ArrayList<SongPlayerOfflineInfo> objects){
        this.listSongInfo = objects;
    }

    @Override
    public int getCount() {
        return listSongInfo.size();
    }

    @Override
    public SongPlayerOfflineInfo getItem(int i) {
        return listSongInfo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(this.resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtSongName = convertView.findViewById(R.id.txtSongName);
            viewHolder.txtArtistName = convertView.findViewById(R.id.txtArtistName);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SongPlayerInfo songPlayerInfo = getItem(position);
        viewHolder.txtSongName.setText(songPlayerInfo.getSongName());
        viewHolder.txtArtistName.setText(songPlayerInfo.getSongArtists());

        return convertView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    public class ViewHolder{
        TextView txtSongName, txtArtistName;
    }

}
