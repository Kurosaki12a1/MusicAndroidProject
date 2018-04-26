/**
 * Created by SonPhan on 4/22/2018.
 */
package com.bku.musicandroid;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class OfflineMusicManager {

    public OfflineMusicManager() {

    }

    private ArrayList<SongPlayerOfflineInfo> scanMusic(String path) {
        ArrayList<SongPlayerOfflineInfo> tempList = new ArrayList<>();
        File home = new File(path);
        File[] listFile = home.listFiles(new FileExtensionFilter());

        //if (home.listFiles(new FileExtensionFilter()).length > 0) {
        if (listFile != null) {

            for (File file : listFile) {
                if (!file.isDirectory()) {
                    if (file.getName().endsWith(".mp3") || file.getName().endsWith(".MP3")) {
                        SongPlayerOfflineInfo songPlayerOfflineInfo = new SongPlayerOfflineInfo(file);
                        // Adding each song to SongList
                        if (!songPlayerOfflineInfo.isBrokenFile())
                            tempList.add(songPlayerOfflineInfo);
                    }
                } else {
                    tempList.addAll(scanMusic(file.getAbsolutePath()));
                }
            }
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
        return listSong;
    }

    class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {
            return (dir.isDirectory() || name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }



}
