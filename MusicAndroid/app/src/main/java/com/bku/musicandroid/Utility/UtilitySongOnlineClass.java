package com.bku.musicandroid.Utility;

import com.bku.musicandroid.Model.SongPlayerOnlineInfo;

import java.util.ArrayList;

/**
 * Created by Welcome on 4/30/2018.
 */

public class UtilitySongOnlineClass {
    private static UtilitySongOnlineClass instance=null;

    private SongPlayerOnlineInfo item;

    private ArrayList<SongPlayerOnlineInfo> lstSong;

    public SongPlayerOnlineInfo getItem() {
        return this.item;
       // return new SongPlayerOnlineInfo(this.item.getSongId(),this.item.getSongName(),this.item.getSongArtists(),this.item.getSongURL(),this.item.getImageSongURL(),this.item.getLiked(),this.item.getUserId(),this.item.getSongGenre(),this.item.getUserName(),this.item.getDownload(),this.item.getView());
    }

    public ArrayList<SongPlayerOnlineInfo> getItemOfList(){
        return this.lstSong;
    }

    public void setItem(SongPlayerOnlineInfo obj) {
        this.item=new SongPlayerOnlineInfo(obj);
      // this.item=new SongPlayerOnlineInfo(obj.getSongId(),obj.getSongName(),obj.getSongArtists(),obj.getSongURL(),obj.getImageSongURL(),obj.getLiked(),obj.getUserId(),obj.getSongGenre(),obj.getUserName(),obj.getDownload(),obj.getView());
    }

    public void setItemOfList(ArrayList<SongPlayerOnlineInfo> obj){
        this.lstSong=new ArrayList<>(obj);
        this.lstSong=obj;
    }

    private UtilitySongOnlineClass(){}

    public static UtilitySongOnlineClass getInstance(){
        if(instance == null){
            instance = new UtilitySongOnlineClass();
        }
        return instance;
    }
}
