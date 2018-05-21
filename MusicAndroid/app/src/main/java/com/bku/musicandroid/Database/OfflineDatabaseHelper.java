package com.bku.musicandroid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bku.musicandroid.Model.OfflinePlaylist;
import com.bku.musicandroid.Model.SongPlayerOfflineInfo;

import java.util.ArrayList;

/**
 * Created by Son on 5/19/2018.
 */
public class OfflineDatabaseHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "playlist_db";

    private Context ctx;

    /**
     * Created by Son on 5/19/2018.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(OfflinePlaylist.CREATE_TABLE);
        sqLiteDatabase.execSQL(SongPlayerOfflineInfo.CREATE_TABLE);
    }

    public OfflineDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;
    }

    /**
     * Created by Son on 5/19/2018.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OfflinePlaylist.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SongPlayerOfflineInfo.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

    /**
     * Created by Son on 5/19/2018.
     */
    public int insertPlaylist(OfflinePlaylist playlist) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(OfflinePlaylist.COLUMN_NAME, playlist.getName());
        value.put(OfflinePlaylist.COLUMN_SONG_COUNT, playlist.getSongCount());
        long id = db.insert(OfflinePlaylist.TABLE_NAME, null, value);
        db.close();

        return (int) id;
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public void updatePlaylist(int id, String columnName, int value) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(columnName, String.valueOf(value));
        db.update(OfflinePlaylist.TABLE_NAME, values, " id = ? ", new String[]{String.valueOf(id)});
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public void deletePlaylist(int id) {
        SQLiteDatabase db = getWritableDatabase();
        // First delete the playlist in table playlist
        db.delete(OfflinePlaylist.TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        // Then delete all song belong to this playlist in the table
        db.delete(SongPlayerOfflineInfo.TABLE_NAME, SongPlayerOfflineInfo.COLUMN_PLAYLIST_ID + " = ? ", new String[]{String.valueOf(id)});
        db.close();
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public OfflinePlaylist getPlaylist(int id) {
        SQLiteDatabase db = getReadableDatabase();
        // Select All Query
        String selectQuery = "SELECT * FROM " + OfflinePlaylist.TABLE_NAME + " WHERE id = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            OfflinePlaylist playlist = new OfflinePlaylist();
            playlist.setId(cursor.getInt(cursor.getColumnIndex(OfflinePlaylist.COLUMN_ID)));
            playlist.setName(cursor.getString(cursor.getColumnIndex(OfflinePlaylist.COLUMN_NAME)));
            playlist.setSongCount(cursor.getInt(cursor.getColumnIndex(OfflinePlaylist.COLUMN_SONG_COUNT)));

            cursor.close();
//            db.close();
            return playlist;

        } else {
            cursor.close();
//            db.close();
            return null;
        }
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public ArrayList<OfflinePlaylist> getAllPlaylist() {
        ArrayList<OfflinePlaylist> listPlaylist = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + OfflinePlaylist.TABLE_NAME + " ORDER BY " + OfflinePlaylist.COLUMN_NAME;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                OfflinePlaylist playlist = new OfflinePlaylist();
                playlist.setId(cursor.getInt(cursor.getColumnIndex(OfflinePlaylist.COLUMN_ID)));
                playlist.setName(cursor.getString(cursor.getColumnIndex(OfflinePlaylist.COLUMN_NAME)));
                playlist.setSongCount(cursor.getInt(cursor.getColumnIndex(OfflinePlaylist.COLUMN_SONG_COUNT)));

                listPlaylist.add(playlist);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listPlaylist;
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public int insertSongIntoPlaylist(SongPlayerOfflineInfo song, int idPlaylist) {
        SQLiteDatabase db = getWritableDatabase();

        OfflinePlaylist playlist = getPlaylist(idPlaylist);
        if (playlist != null) {
            updatePlaylist(idPlaylist, OfflinePlaylist.COLUMN_SONG_COUNT, playlist.getSongCount() + 1);
        }

        ContentValues value = new ContentValues();
        value.put(SongPlayerOfflineInfo.COLUMN_PLAYLIST_ID, idPlaylist);
        value.put(SongPlayerOfflineInfo.COLUMN_FILE_PATH, song.getPathFileSong());
        long id = db.insert(SongPlayerOfflineInfo.TABLE_NAME, null, value);
        db.close();

        return (int) id;
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public ArrayList<SongPlayerOfflineInfo> getAllSongFromPlaylist(int id) {
        SQLiteDatabase db = getReadableDatabase();
//        SongPlayerOfflineInfo song = new SongPlayerOfflineInfo();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SongPlayerOfflineInfo.TABLE_NAME + " WHERE " + SongPlayerOfflineInfo.COLUMN_PLAYLIST_ID + " = " + id;

        ArrayList<SongPlayerOfflineInfo> songList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int vId = cursor.getInt(cursor.getColumnIndex(SongPlayerOfflineInfo.COLUMN_ID));
                int playlistId = cursor.getInt(cursor.getColumnIndex(SongPlayerOfflineInfo.COLUMN_PLAYLIST_ID));
                String filePath = cursor.getString(cursor.getColumnIndex(SongPlayerOfflineInfo.COLUMN_FILE_PATH));

                SongPlayerOfflineInfo song = new SongPlayerOfflineInfo(filePath, this.ctx);
                song.setId(vId);
                song.setPlaylistId(playlistId);

                songList.add(song);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return songList;
    }
    /**
     * Created by Son on 5/19/2018.
     */
    public void deleteSongFromPlaylist(int id, int playlistId) {
        SQLiteDatabase db = getWritableDatabase();

        OfflinePlaylist playlist = getPlaylist(playlistId);
        if (playlist != null) {
            updatePlaylist(playlistId, OfflinePlaylist.COLUMN_SONG_COUNT, playlist.getSongCount() - 1);
        }

        db.delete(SongPlayerOfflineInfo.TABLE_NAME,  SongPlayerOfflineInfo.COLUMN_ID
                + " = ? and " + SongPlayerOfflineInfo.COLUMN_PLAYLIST_ID + " = ? "
                , new String[]{String.valueOf(id), String.valueOf(playlistId)});

    }
}
