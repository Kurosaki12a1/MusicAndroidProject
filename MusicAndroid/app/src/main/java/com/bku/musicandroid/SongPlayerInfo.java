package com.bku.musicandroid;

public class SongPlayerInfo {

    protected String songArtists;

    protected String songName;

    public SongPlayerInfo() {
    }

    public SongPlayerInfo(String songArtists, String songName) {
        this.songArtists = songArtists;
        this.songName = songName;
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

    public void setSongName(String songName) {
        this.songName = songName;
    }
}

