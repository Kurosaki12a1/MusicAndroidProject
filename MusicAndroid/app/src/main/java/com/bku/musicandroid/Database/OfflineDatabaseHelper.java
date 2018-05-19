package com.bku.musicandroid.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bku.musicandroid.Model.OfflinePlaylist;

import java.util.ArrayList;

/**
 * Created by Son on 5/19/2018.
 */
public class OfflineDatabaseHelper extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "playlist_db";

    /**
     * Created by Son on 5/19/2018.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(OfflinePlaylist.CREATE_TABLE);
    }

    public OfflineDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Created by Son on 5/19/2018.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OfflinePlaylist.TABLE_NAME);

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

        return (int)id;
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public void deletePlaylist(int id) {
        SQLiteDatabase db = getWritableDatabase();

        db.delete(OfflinePlaylist.TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
    }

    /**
     * Created by Son on 5/19/2018.
     */
    public ArrayList<OfflinePlaylist> getAllPlaylist() {
        ArrayList<OfflinePlaylist> listPlaylist = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + OfflinePlaylist.TABLE_NAME + " ORDER BY " + OfflinePlaylist.COLUMN_NAME;

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
}
