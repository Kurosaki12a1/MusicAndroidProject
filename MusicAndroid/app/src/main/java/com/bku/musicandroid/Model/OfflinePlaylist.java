package com.bku.musicandroid.Model;

/**
 * Created by Son on 5/19/2018.
 */

public class OfflinePlaylist {
    private int id;
    private String name;
    private int songCount;

    public OfflinePlaylist(String name, int songCount) {
        this.name = name;
        this.songCount = songCount;
    }

    public OfflinePlaylist(String name) {
//        new OfflinePlaylist(name, 0);
        this.name = name;
        this.songCount = 0;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;

    }

    public int getSongCount() {
        return songCount;
    }

    public void setSongCount(int songCount) {
        this.songCount = songCount;
    }
}
