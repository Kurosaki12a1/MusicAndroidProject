package com.bku.listenmusic;

/**
 * Created by Welcome on 3/8/2018.
 */

public class SongPlayerOnlineInfo {

    private String songURL;

    private String userId;

    private String imageSongURL;

    private String songArtists;

    private String songName;

    private String liked;

    private String songId;

    private String songGenre;

    public SongPlayerOnlineInfo(String songId,String songName,String songArtists,String songURL,String imageSongURL,String liked,String userId,String songGenre){
        this.songId=songId;
        this.songName=songName;
        this.songArtists=songArtists;
        this.songURL=songURL;
        this.imageSongURL=imageSongURL;
        this.liked=liked;
        this.userId=userId;
        this.songGenre=songGenre;
    }
    public SongPlayerOnlineInfo(){

    }
    public String getSongURL(){return songURL;}
    public String getUserId(){return userId;}
    public String getImageSongURL(){return imageSongURL;}
    public String getSongArtists(){return songArtists;}
    public String getSongGenre(){return songGenre;}
    public String getSongName(){return songName;}
    public String getLiked(){return liked;}
    public String getSongId(){return songId;}

   // public String getMusicId(){return musicId;}
}
