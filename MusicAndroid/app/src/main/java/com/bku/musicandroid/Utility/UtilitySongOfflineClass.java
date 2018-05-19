package com.bku.musicandroid.Utility;

import com.bku.musicandroid.Model.SongPlayerOfflineInfo;

import java.util.ArrayList;

/**
 * Created by Welcome on 4/28/2018.
 */

public class UtilitySongOfflineClass {
    private static UtilitySongOfflineClass instance = null;

    private ArrayList<SongPlayerOfflineInfo> currentList;

    private ArrayList<SongPlayerOfflineInfo> listAllSongOffline;

    public ArrayList<SongPlayerOfflineInfo> getList() {
        return this.currentList;
    }

    public ArrayList<SongPlayerOfflineInfo> getListAllSongOffline() {
        return this.listAllSongOffline;
    }

    public void setList(ArrayList<SongPlayerOfflineInfo> list) {
        this.currentList = new ArrayList<>(list);
//        this.list = list;
    }

    public void setListAllSongOffline(ArrayList<SongPlayerOfflineInfo> list) {
        this.listAllSongOffline = new ArrayList<>(list);
    }

    private UtilitySongOfflineClass() {
    }

    public static UtilitySongOfflineClass getInstance() {
        if (instance == null) {
            instance = new UtilitySongOfflineClass();
        }
        return instance;
    }
}
