package com.bku.musicandroid.Model;

import java.io.Serializable;

public abstract class SongPlayerInfo implements Serializable {

    protected String songArtists;

    protected String songName;

    protected byte [] songImage=null;

    public SongPlayerInfo() {
    }

    public SongPlayerInfo(String songArtists, String songName,byte[] songImage) {
        this.songArtists = songArtists;
        this.songName = songName;
        this.songImage=songImage;
    }

    public String getSongArtists() {
        return songArtists;
    }

    public void setSongArtists(String songArtists) {
        this.songArtists = songArtists;
    }

    public String getSongName() {
        return songName;
    }

    public byte [] getSongImage(){return songImage;}
    public void setSongImage(byte[] data){
        this.songImage=data;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public abstract String getPath();




}