package com.bku.musicandroid;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Welcome on 3/8/2018.
 */

public class SongPlayerOnlineInfo extends SongPlayerInfo {

    private final String DATABASE_PATH="All_Users_Info_Database";
    private String songURL;

    private String userId;

    private String imageSongURL;

    private String liked;

    private String songId;

    private String songGenre;

    private String userName;

    private String download;

    private String view;
    public SongPlayerOnlineInfo(String songId,String songName,String songArtists,
                                String songURL,String imageSongURL,String liked,String userId,
                                String songGenre,String userName,String Download,String View){
        this.songId=songId;
        this.songName=songName;
        this.songArtists=songArtists;
        this.songURL=songURL;
        this.imageSongURL=imageSongURL;
        this.liked=liked;
        this.userId=userId;
        this.songGenre=songGenre;
        this.userName=userName;
        this.download=Download;
        this.view=View;
    }
    public SongPlayerOnlineInfo(){

    }

    public SongPlayerOnlineInfo(SongPlayerOnlineInfo obj){
        this.songId=obj.getSongId();
        this.songName=obj.getSongName();
        this.songArtists=obj.getSongArtists();
        this.songURL=obj.getSongURL();
        this.imageSongURL=obj.getImageSongURL();
        this.liked=obj.getLiked();
        this.userId=obj.getUserId();
        this.songGenre=obj.getSongGenre();
        this.userName=obj.getUserName();
        this.download=obj.getDownload();
        this.view=obj.getView();
    }
    public String getSongURL(){return songURL;}
    public String getUserId(){return userId;}
    public String getImageSongURL(){return imageSongURL;}
    public String getSongGenre(){return songGenre;}
    public String getLiked(){return liked;}
    public String getSongId(){return songId;}
    public String getUserName(){return userName;}
    public String getDownload(){return download;}
    public String getView(){return view;}

    public void setLiked(String liked){this.liked=liked;}
    public void setDownload(String download){this.download=download;}
    public void setView(String view){this.view=view;}
   // public String getMusicId(){return musicId;}
}
