/**
 * Created by SonPhan on 4/22/2018.
 */
package com.bku.musicandroid.Model;

import android.content.Context;
import android.os.Environment;

import com.bku.musicandroid.Utility.UtilitySongOfflineClass;

import java.io.File;
import java.io.FilenameFilter;

import java.util.ArrayList;

import java.util.Collections;
import java.util.Comparator;


public class OfflineMusicManager {

    Context context;
    public OfflineMusicManager(Context context) {
        this.context=context;
    }

    private ArrayList<SongPlayerOfflineInfo> scanMusic(String path) {
        ArrayList<SongPlayerOfflineInfo> tempList = new ArrayList<>();
        try {
            File home = new File(path);
            File[] listFile = home.listFiles(new FileExtensionFilter());

            //if (home.listFiles(new FileExtensionFilter()).length > 0) {
            if (listFile != null) {

                for (File file : listFile) {
                    if (!file.isDirectory()) {
                        if (file.getName().endsWith(".mp3") || file.getName().endsWith(".MP3")) {
                            SongPlayerOfflineInfo songPlayerOfflineInfo = new SongPlayerOfflineInfo(file, context);
                            // Adding each song to SongList
                            if (!songPlayerOfflineInfo.isBrokenFile())
                                tempList.add(songPlayerOfflineInfo);
                        }
                    } else {
                        tempList.addAll(scanMusic(file.getAbsolutePath()));
                    }
                }
            }
        }
        catch(OutOfMemoryError e){
            //return tempList;
        }
        // return songs list array
        return tempList;
    }

    public ArrayList<SongPlayerOfflineInfo> scanAllOfflineMusic() {
        ArrayList<SongPlayerOfflineInfo> listSong = scanMusic(Environment.getExternalStorageDirectory().getAbsolutePath());
        Collections.sort(listSong, new Comparator<SongPlayerOfflineInfo>() {
            @Override
            public int compare(SongPlayerOfflineInfo t1, SongPlayerOfflineInfo t2) {
                return t1.getSongName().compareTo(t2.getSongName());
            }
        });
        UtilitySongOfflineClass.getInstance().setListAllSongOffline(listSong);
        return listSong;
    }

    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (dir.isDirectory() || name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }



}
