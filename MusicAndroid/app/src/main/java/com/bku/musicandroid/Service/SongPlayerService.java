package com.bku.musicandroid.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.bku.musicandroid.Activity.MainScreenActivity;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.SongPlayerOfflineInfo;
import com.bku.musicandroid.Utility.Constants;
import com.bku.musicandroid.Utility.UtilitySongOfflineClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Son on 5/11/2018.
 */

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
    private boolean isChangeSongFromService = false;
    private String lastFilePath = "";

    private Notification notification;
    private NotificationCompat.Builder builder;
    private RemoteViews remoteViews;
    private NotificationManager notificationManager;
    private BroadcastReceiver musicPlayerBroadcastReceiver;

    public static final int NOTIF_ID = 1209;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();
        listSong = new ArrayList<>(UtilitySongOfflineClass.getInstance().getList());
        mp.setOnCompletionListener(this);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.layout_song_player_notification);

        buildNotification();

        setNotificationListeners();

        musicPlayerBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action != null) {
                    if (action.equalsIgnoreCase(Constants.ACTION.PLAY_OR_PAUSE_ACTION)) {
                        if (isPause) {
                            remoteViews.setImageViewResource(R.id.imgPlay, R.drawable.btn_playback_play);
                            isPause = false;
                            mp.start();
                        } else {
                            remoteViews.setImageViewResource(R.id.imgPlay, R.drawable.ic_player_pause);
                            isPause = true;
                            mp.pause();
                        }
                    } else if (action.equalsIgnoreCase(Constants.ACTION.PREV_ACTION)) {
                        isChangeSongFromService = true;
                        isUserChangePosition = true;
                        currentPosition = 0;
                        if (isShuffle) {
                            //Tron cung mang nghia repeat all
                            Random rand = new Random();
                            int nTempPosition;
                            do {
                                nTempPosition = rand.nextInt((listSong.size() - 1));
                            } while (nTempPosition == nPosition);
                            nPosition = nTempPosition;
                            playSong();
                        } else if (nPosition == 0) {
                            nPosition = listSong.size() - 1;
                            playSong();
                        } else {
                            nPosition--;
                            playSong();
                        }

                    } else if (action.equalsIgnoreCase(Constants.ACTION.NEXT_ACTION)) {
                        isChangeSongFromService = true;
                        isUserChangePosition = true;
                        currentPosition = 0;
                        if (isShuffle) {
                            //Tron cung mang nghia repeat all
                            Random rand = new Random();
                            int nTempPosition;
                            do {
                                nTempPosition = rand.nextInt((listSong.size() - 1));
                            } while (nTempPosition == nPosition);
                            nPosition = nTempPosition;
                            playSong();
                        } else if (nPosition == listSong.size() - 1) {
                            nPosition = 0;
                            playSong();
                        } else {
                            nPosition++;
                            playSong();
                        }
                    } else if (action.equalsIgnoreCase(Constants.ACTION.CLOSE_ACTION)) {
//                        serviceIsRunning = false;
                        isPause = true;
                        mp.pause();
                        return;
                    }

                    setNotificationInfo();
                    notificationManager.notify(NOTIF_ID, notification);
                }
            }
        };


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION.PLAY_OR_PAUSE_ACTION);
        intentFilter.addAction(Constants.ACTION.PREV_ACTION);
        intentFilter.addAction(Constants.ACTION.NEXT_ACTION);
        intentFilter.addAction(Constants.ACTION.CLOSE_ACTION);

        registerReceiver(musicPlayerBroadcastReceiver, intentFilter);

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
                    while (MainScreenActivity.isRunning ) {
                        try {
                            Thread.sleep(100);
                            currentPosition = mp.getCurrentPosition();
                            sendDataToActivity();
                            // Return default
                            isChangeSongFromService = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    try {
                        unregisterReceiver(musicPlayerBroadcastReceiver);
                    } catch (Exception e) {
                        Log.d("exception", e.toString());
                    } finally {
                        notificationManager.cancel(NOTIF_ID);
                        stopSelf();
                    }

                }
            });
            thread.start();

            setNotificationInfo();
            notificationManager.notify(NOTIF_ID, notification);


        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean stopService(Intent name) {
        mp.stop();
        mp.release();
        return super.stopService(name);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    /**
     * Created by Son on 5/11/2018.
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() <= 1000) { // End song, we accept 1s of delay
            isChangeSongFromService = true;
            currentPosition = 0;
            if (isRepeatOne) {
                lastFilePath = "";
                playSong();
            } else if (isShuffle) {
                //Tron cung mang nghia repeat all
                Random rand = new Random();
                int nTempPosition;
                do {
                    nTempPosition = rand.nextInt((listSong.size() - 1));
                } while (nTempPosition == nPosition);
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
    }

    /**
     * Created by Son on 5/11/2018.
     */

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
        {

        }
    }

    /**
     * Created by Son on 5/12/2018.
     */

    private void setNotificationInfo() {
        SongPlayerOfflineInfo song = listSong.get(nPosition);

        String songName = song.getSongName();
        // Name too long so we will epitomize it
        if (songName.length() >= 30) {
            songName = songName.substring(0, 26) + "...";
        }
        remoteViews.setTextViewText(R.id.txtSongName, songName);

        String songArtists = song.getSongArtists();
        // Name too long so we will epitomize it
        if (songArtists.length() >= 30) {
            songArtists = songName.substring(0, 26) + "...";
        }
        remoteViews.setTextViewText(R.id.txtArtistName, songArtists);

        Bitmap imgSong = BitmapFactory.decodeByteArray(song.getSongImage(), 0, song.getSongImage().length);
        remoteViews.setImageViewBitmap(R.id.imgSongs, imgSong);

        if (isPause) {
            remoteViews.setImageViewResource(R.id.imgPlay, R.drawable.btn_playback_play);
        } else {
            remoteViews.setImageViewResource(R.id.imgPlay, R.drawable.ic_player_pause);
        }
    }

    /**
     * Created by Son on 5/11/2018.
     */
    // Function to broadcast data all over the application
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

        i.putExtra("isChangeSongFromService", isChangeSongFromService);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    /**
     * Created by Son on 5/13/2018.
     */
    private void setNotificationListeners() {
        Intent playOrPauseIntent = new Intent(Constants.ACTION.PLAY_OR_PAUSE_ACTION);
        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, playOrPauseIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.imgPlay, pPlay);


        Intent prevIntent = new Intent(Constants.ACTION.PREV_ACTION);
        PendingIntent pPrev = PendingIntent.getBroadcast(getApplicationContext(), 0, prevIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.imgPrev, pPrev);

        Intent nextIntent = new Intent(Constants.ACTION.NEXT_ACTION);
        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, nextIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.imgNext, pNext);


    }

    /**
     * Created by Son on 5/13/2018.
     */
    private void buildNotification() {
        Intent closeIntent = new Intent(Constants.ACTION.CLOSE_ACTION);
        PendingIntent pClose = PendingIntent.getBroadcast(getApplicationContext(), 0, closeIntent, 0);

        builder = new NotificationCompat.Builder(getApplicationContext(), "MY_ID")
                .setOngoing(true)
                .setSmallIcon(R.drawable.ic_audiotrack_dark)
                .setCustomBigContentView(remoteViews)
                .setDeleteIntent(pClose);
        notification = builder.build();
    }


}
