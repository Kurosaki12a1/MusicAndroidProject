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
    private boolean isUserChangePosition = false;
    private String lastFilePath = "";

    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();
        listSong = new ArrayList<>(UtilitySongOfflineClass.getInstance().getList());
        mp.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle;
        if (intent != null && (bundle = intent.getExtras()) != null
                && (nPosition = bundle.getInt("position", -1)) != -1) {
            // Get the data from activity
            isRepeatOne = bundle.getBoolean("isRepeatOne", false);
            isPause = bundle.getBoolean("isPause", false);
            isRepeatAll = bundle.getBoolean("isRepeatAll", false);
            isShuffle = bundle.getBoolean("isShuffle", false);
            currentPosition = bundle.getInt("currentPosition", 0);
            isUserChangePosition = bundle.getBoolean("isUserChangePosition", false);
            playSong();

            // Thread to send data back to activity to update UI if needed after every 1s
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Thread.sleep(100);
                            currentPosition = mp.getCurrentPosition();
                            sendDataToActivity();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
            thread.start();


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
        currentPosition = 0;
        if (isRepeatOne) {
            lastFilePath = "";
            playSong();
        } else if (isShuffle) {
            //Tron cung mang nghia repeat all
            Random rand = new Random();
            int nTempPosition = rand.nextInt((listSong.size() - 1));
            if (nTempPosition == nPosition) {
                while (nTempPosition == nPosition) {
                    rand = new Random();
                    nTempPosition = rand.nextInt((listSong.size() - 1));
                }
            }
            nPosition = nTempPosition;
            playSong();
        } else {
            //no repeatone or no shuffler->play next song
            if (nPosition < listSong.size() - 1) {

                nPosition++;
                playSong();
            } else if (isRepeatAll) {
                // play first song
                nPosition = 0;
                playSong();
                //     nPosition=0;
            }
        }
    }

    void playSong() {
        if (!lastFilePath.equals(listSong.get(nPosition).getPathFileSong())) {
            lastFilePath = listSong.get(nPosition).getPathFileSong(); // Only need to do these steps if we're going to playing new songs
            mp.reset();
            try {
                mp.setDataSource(listSong.get(nPosition).getPathFileSong());
                mp.prepare();
                totalDuration = mp.getDuration();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        mp.start();
        if (isUserChangePosition) {
            mp.seekTo(currentPosition);
            isUserChangePosition = false; // Return to default
        }
        if (isPause) {
            mp.pause();
        }
    }

    // Function to send data back to activity
    void sendDataToActivity() {
        Intent i = new Intent("MusicPlayerUpdate");
        i.putExtra("position", nPosition);
        i.putExtra("isPause", isPause);
        i.putExtra("isRepeatAll", isRepeatAll);
        i.putExtra("isRepeatOne", isRepeatOne);
        i.putExtra("isShuffle", isShuffle);
        i.putExtra("currentPosition", currentPosition);
        i.putExtra("totalDuration", totalDuration);
        i.putExtra("isUserChangePosition", isUserChangePosition);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

}
