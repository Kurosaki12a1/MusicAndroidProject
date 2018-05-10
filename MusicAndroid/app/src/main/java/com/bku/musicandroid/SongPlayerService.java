package com.bku.musicandroid;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class SongPlayerService extends Service implements MediaPlayer.OnCompletionListener {
    private int nPosition;
    private MediaPlayer mp;
    private ArrayList<SongPlayerOfflineInfo> listSong;
    private boolean isRepeatOne;
    private boolean isShuffle;
    private boolean isPause;
    private boolean isRepeatAll;
    private int currentPosition;
    private int totalDuration;
    private boolean isPrepared = false;
    private String lastFilePath = "";
    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();
        listSong = new ArrayList<>(UtilitySongOfflineClass.getInstance().getList());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle;
        if (intent != null && (bundle = intent.getExtras()) != null
                && (nPosition = bundle.getInt("position", -1)) != -1) {
            isRepeatOne = bundle.getBoolean("isRepeatOne", false);
            isPause = bundle.getBoolean("isPause", false);
            isRepeatAll = bundle.getBoolean("isRepeatAll", false);
            isShuffle = bundle.getBoolean("isShuffle", false);
            currentPosition = bundle.getInt("currentPosition", 0);
            try {
                if (!lastFilePath.equals(listSong.get(nPosition).getPathFileSong())) {
                    lastFilePath = listSong.get(nPosition).getPathFileSong();
                    mp.reset();
                    mp.setDataSource(listSong.get(nPosition).getPathFileSong());
                    mp.prepare();
                    mp.start();
                }
                mp.seekTo(currentPosition);
                totalDuration = mp.getDuration();
                if (isPause) {
                    mp.pause();
                }

//                while (true) {
//                    if (mp.getCurrentPosition() != currentPosition) {
//                        currentPosition = mp.getCurrentPosition();
//                    }
//                    sendDataToActivity();
//
//                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            if (mp.getCurrentPosition() != currentPosition) {
                                currentPosition = mp.getCurrentPosition();
                            }
                            sendDataToActivity();
                        }

                    }
                });
                thread.start();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {


    }

    void sendDataToActivity(){
        Intent i = new Intent("MusicPlayerUpdate");
        i.putExtra("position", nPosition);
        i.putExtra("isPause", isPause);
        i.putExtra("isRepeatAll", isRepeatAll);
        i.putExtra("isRepeatOne", isRepeatOne);
        i.putExtra("isShuffle", isShuffle);
        i.putExtra("currentPosition", currentPosition);
//        i.putExtra("currentDuration", currentDuration);
        i.putExtra("totalDuration", totalDuration);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }
}
