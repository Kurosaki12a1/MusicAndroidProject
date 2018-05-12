package com.bku.musicandroid.Utility;

import com.bku.musicandroid.Model.SongPlayerOfflineInfo;

import java.util.ArrayList;

/**
 * Created by Welcome on 4/28/2018.
 */

public class UtilitySongOfflineClass {
    private static UtilitySongOfflineClass instance=null;

    private ArrayList<SongPlayerOfflineInfo> list;

    public ArrayList<SongPlayerOfflineInfo> getList() {
        return this.list;
    }

    public void setList(ArrayList<SongPlayerOfflineInfo> list) {
        this.list=new ArrayList<>(list);
        this.list = list;
    }

    private UtilitySongOfflineClass(){}

    public static UtilitySongOfflineClass getInstance(){
        if(instance == null){
            instance = new UtilitySongOfflineClass();
        }
        return instance;
    }
}
