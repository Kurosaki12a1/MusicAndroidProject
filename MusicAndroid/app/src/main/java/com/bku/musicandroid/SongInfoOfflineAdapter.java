/**
 * Created by SonPhan on 4/22/2018.
 */
package com.bku.musicandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SongInfoOfflineAdapter extends ArrayAdapter<SongPlayerOfflineInfo> {
    private ArrayList<SongPlayerOfflineInfo> listSongInfo;
    private final LayoutInflater inflater;
    private final int resource;
    byte[] data1;
    Context context;
    UtilitySongOfflineClass utilitySongOfflineClass;

    public SongInfoOfflineAdapter(@NonNull Activity context, int resource, @NonNull List<SongPlayerOfflineInfo> objects) {
        super(context, resource, objects);
        this.context=context;
        inflater = context.getLayoutInflater();
        this.listSongInfo = new ArrayList<SongPlayerOfflineInfo>(objects);
        this.resource = resource;

    }

    public void setDataSet(ArrayList<SongPlayerOfflineInfo> objects){
            this.listSongInfo = objects;
            utilitySongOfflineClass = UtilitySongOfflineClass.getInstance();
            utilitySongOfflineClass.setList(objects);
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
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = inflater.inflate(this.resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtSongName = convertView.findViewById(R.id.nameSong);
            viewHolder.txtArtistName = convertView.findViewById(R.id.nameArtist);
            viewHolder.songImage=convertView.findViewById(R.id.songImage);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final SongPlayerInfo songPlayerInfo = getItem(position);
        viewHolder.txtSongName.setText(songPlayerInfo.getSongName());
        viewHolder.txtArtistName.setText(songPlayerInfo.getSongArtists());
        data1=songPlayerInfo.getSongImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
        viewHolder.songImage.setImageBitmap(bitmap);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,SongOfflinePlayerActivity.class);
                intent.putExtra("currentPosition",position);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                context.startActivity(intent);
            }
        });

        return convertView;
    }

    @Nullable
    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    public class ViewHolder{
        TextView txtSongName, txtArtistName;
        ImageView songImage;

    }

}
