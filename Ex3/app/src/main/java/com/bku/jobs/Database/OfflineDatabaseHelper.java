package com.bku.jobs.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bku.jobs.ModelData.JobData;
import com.bku.jobs.Models.JobInfo;

import java.util.ArrayList;

/**
 * Created by Welcome on 5/21/2018.
 */

public class OfflineDatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "favorite_job_db";

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

    public int insertJob(JobData job){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues value=new ContentValues();
        value.put(JobInfo.COLUMN_ID,job.getId());
        value.put(JobInfo.COLUMN_CreatedAt,job.getCreatedAt());
        value.put(JobInfo.COLUMN_Title,job.getTitle());
        value.put(JobInfo.COLUMN_Location,job.getLocation());
        value.put(JobInfo.COLUMN_Type,job.getType());
        value.put(JobInfo.COLUMN_Description,job.getDescription());
        value.put(JobInfo.COLUMN_howToApply,job.getHowToApply());
        value.put(JobInfo.COLUMN_Company,job.getCompany());
        value.put(JobInfo.COLUMN_CompanyURL,job.getCompanyUrl());
        value.put(JobInfo.COLUMN_CompanyLogo,job.getCompanyLogo());
        value.put(JobInfo.COLUMN_URL,job.getUrl());
        long id = db.insert(JobInfo.TABLE_NAME, null, value);
        db.close();
        return  (int) id;
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

    public boolean checkExist(String id){
        SQLiteDatabase db = getReadableDatabase();
        String selectQuery = "SELECT * FROM "+ JobInfo.TABLE_NAME + " WHERE "+JobInfo.COLUMN_ID + " ='"+id+"' ";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor!=null){
            if(cursor.getCount()>0){
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public ArrayList<JobData> getAllJob() {
        ArrayList<JobData> listJob = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + JobInfo.TABLE_NAME ;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                JobData lst = new JobData();
                lst.setId(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_ID)));
                lst.setCreatedAt(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_CreatedAt)));
                lst.setTitle(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_Title)));
                lst.setLocation(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_Location)));
                lst.setType(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_Type)));
                lst.setDescription(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_Description)));
                lst.setHowToApply(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_howToApply)));
                lst.setCompany(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_Company)));
                lst.setCompanyUrl(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_CompanyURL)));
                lst.setCompanyLogo(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_CompanyLogo)));
                lst.setUrl(cursor.getString(cursor.getColumnIndex(JobInfo.COLUMN_URL)));
                listJob.add(lst);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listJob;
    }


}
