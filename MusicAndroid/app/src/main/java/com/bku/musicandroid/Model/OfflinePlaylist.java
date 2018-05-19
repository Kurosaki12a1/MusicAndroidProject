package com.bku.musicandroid.Model;

/**
 * Created by Son on 5/19/2018.
 */

public class OfflinePlaylist {
    public static final String TABLE_NAME = "playlist";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SONG_COUNT = "song_count";


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_SONG_COUNT + " INTEGER "
                    + ")";
    private int id;
    private String name;
    private int songCount;

    public OfflinePlaylist() {
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public OfflinePlaylist(String name, int songCount) {
        this.name = name;
        this.songCount = songCount;
    }

    /**
     * Created by Son on 5/19/2018.
     */
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
