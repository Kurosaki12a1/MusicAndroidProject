package com.bku.jobs.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bku.jobs.Models.JobInfo;

import java.util.ArrayList;

/**
 * Created by Welcome on 5/21/2018.
 */

public class OfflineDatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "favorite_db";

    private Context context;

    public OfflineDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    public OfflineDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JobInfo.CREATE_TABLE);
      /*  sqLiteDatabase.execSQL(OfflinePlaylist.CREATE_TABLE);
        sqLiteDatabase.execSQL(SongPlayerOfflineInfo.CREATE_TABLE);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + JobInfo.TABLE_NAME);
        onCreate(db);
    }

    public String insertJob(JobInfo job){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put(JobInfo.COLUMN_ID,job.getJobId());
        value.put(JobInfo.COLUMN_CreatedAt,job.getJobCreatedAt());
        value.put(JobInfo.COLUMN_Title,job.getJobTitle());
        value.put(JobInfo.COLUMN_Location,job.getLocation());
        value.put(JobInfo.COLUMN_Type,job.getType());
        value.put(JobInfo.COLUMN_Description,job.getDescription());
        value.put(JobInfo.COLUMN_howToApply,job.getHowToApply());
        value.put(JobInfo.COLUMN_Company,job.getCompany());
        value.put(JobInfo.COLUMN_CompanyURL,job.getCompanyURL());
        value.put(JobInfo.COLUMN_CompanyLogo,job.getCompanyLogo());
        value.put(JobInfo.COLUMN_URL,job.getURL());
        db.insert(JobInfo.TABLE_NAME, null, value);
        db.close();
        return  job.getJobId();
    }

    public void updateJob(String id, String columnName, int value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columnName, String.valueOf(value));
        db.update(JobInfo.TABLE_NAME, values, " Id = ? ", new String[]{(id)});
    }

    public void deleteJob(String id) {
        SQLiteDatabase db = getWritableDatabase();
        //  delete the job id in table Jon
        db.delete(JobInfo.TABLE_NAME, "Id = ?", new String[]{(id)});
        db.close();
    }

    public ArrayList<JobInfo> getAllJob() {
        ArrayList<JobInfo> listJob = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + JobInfo.TABLE_NAME ;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                JobInfo lst = new JobInfo();
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_ID)));
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_CreatedAt)));
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_Title)));
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_Location)));
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_Type)));
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_Description)));
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_howToApply)));
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_Company)));
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_CompanyURL)));
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_CompanyLogo)));
                lst.setJobId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_URL)));
                listJob.add(lst);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listJob;
    }



/*
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

    public void deleteSongFromPlaylist(int id, int playlistId) {
        SQLiteDatabase db = getWritableDatabase();

        OfflinePlaylist playlist = getPlaylist(playlistId);
        if (playlist != null) {
            updatePlaylist(playlistId, OfflinePlaylist.COLUMN_SONG_COUNT, playlist.getSongCount() - 1);
        }

        db.delete(SongPlayerOfflineInfo.TABLE_NAME,  SongPlayerOfflineInfo.COLUMN_ID
                        + " = ? and " + SongPlayerOfflineInfo.COLUMN_PLAYLIST_ID + " = ? "
                , new String[]{String.valueOf(id), String.valueOf(playlistId)});

    }*/
}
