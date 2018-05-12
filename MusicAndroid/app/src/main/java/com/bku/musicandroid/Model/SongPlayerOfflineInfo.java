package com.bku.musicandroid.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import com.bku.musicandroid.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;

public class SongPlayerOfflineInfo extends SongPlayerInfo implements Serializable {
    private File fileSong;
    private Context context;
    private boolean isBrokenFile;

    public SongPlayerOfflineInfo() {
    }

    public SongPlayerOfflineInfo(File fileSong,Context context) {
        this.fileSong = fileSong;
        this.context=context;
        try {
            String songName;
            String songArtist;
            MediaMetadataRetriever m = new MediaMetadataRetriever();
            m.setDataSource(fileSong.getAbsolutePath());
            songName = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            songArtist = m.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            byte [] bytePicSong = null;
            if (songName == null) {
                songName = fileSong.getName();
            }
            if (songArtist == null) {
                songArtist = "Unknown";
            }

            if(m.getEmbeddedPicture()!=null){
                byte [] data1=m.getEmbeddedPicture();
                Bitmap bitmap = BitmapFactory.decodeByteArray(data1, 0, data1.length);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                bytePicSong=new byte[byteArrayOutputStream.size()];
                bytePicSong = byteArrayOutputStream.toByteArray();
            }
            else{
                Bitmap bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_audiotrack_dark);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                bytePicSong=new byte[byteArrayOutputStream.size()];
                bytePicSong = byteArrayOutputStream.toByteArray();
            }
            this.setSongName(songName);
            this.setSongArtists(songArtist);
            this.setSongImage(bytePicSong);
            isBrokenFile = false;
        } catch (Exception ex){
            isBrokenFile = true;
            return;

        }
    }

    public File getFileSong() {
        return fileSong;
    }

    public boolean isBrokenFile() {
        return isBrokenFile;
    }

    public void setFileSong(File fileSong) {
        this.fileSong = fileSong;
    }

    public String getPathFileSong(){return fileSong.getAbsolutePath();}
}
