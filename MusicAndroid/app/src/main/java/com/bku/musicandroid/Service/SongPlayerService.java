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

import com.bku.musicandroid.Activity.LoginActivity;
import com.bku.musicandroid.Activity.MainScreenActivity;
import com.bku.musicandroid.Model.SongPlayerInfo;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.SongPlayerOfflineInfo;
import com.bku.musicandroid.Utility.Constants;
import com.bku.musicandroid.Utility.UtilitySongOfflineClass;
import com.bku.musicandroid.Utility.UtilitySongOnlineClass;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Son on 5/11/2018.
 */

public class SongPlayerService extends Service implements MediaPlayer.OnCompletionListener {
    private int nPosition;
    private MediaPlayer mp;
    private ArrayList<? extends SongPlayerInfo> listSong;
    private boolean isRepeatOne;
    private boolean isShuffle;
    private boolean isPause;
    private boolean isRepeatAll;
    private boolean isOnline;
    private int currentPosition;
    private int totalDuration;
    private boolean isUserChangePosition = false;
    private String lastFilePath = "";

    private boolean mediaPlayerPrepared = false;
    private boolean serviceIsRunning = true;

    private Notification notification;
    private NotificationCompat.Builder builder;
    private RemoteViews remoteViews;


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        notificationManager.cancel(NOTIF_ID);
        stopThisService();
    }

    private NotificationManager notificationManager;
    private BroadcastReceiver musicPlayerBroadcastReceiver;

    public static final int NOTIF_ID = 1209;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        mp = new MediaPlayer();
//        listSong = new ArrayList<>(UtilitySongOfflineClass.getInstance().getList());
//        listSong = UtilitySongOfflineClass.getInstance().getList();
        mp.setOnCompletionListener(this);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getPackageName(), R.layout.layout_song_player_notification);

        buildNotification();

        setNotificationListeners();

        // Receive from notification interactions
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
                        isUserChangePosition = true;
                        isPause = false;
                        currentPosition = 0;
                        if (isShuffle) {
                            //Tron cung mang nghia repeat all
                            Random rand = new Random();
                            int nTempPosition;
                            do {
                                nTempPosition = rand.nextInt((listSong.size() - 1));
                            } while (nTempPosition == nPosition);
                            nPosition = nTempPosition;
                        } else if (nPosition == 0) {
                            nPosition = listSong.size() - 1;
                        } else {
                            nPosition--;
                        }
                        playSong();

                    } else if (action.equalsIgnoreCase(Constants.ACTION.NEXT_ACTION)) {
                        isUserChangePosition = true;
                        isPause = false;
                        currentPosition = 0;
                        if (isShuffle) {
                            //Tron cung mang nghia repeat all
                            Random rand = new Random();
                            int nTempPosition;
                            do {
                                nTempPosition = rand.nextInt((listSong.size() - 1));
                            } while (nTempPosition == nPosition);
                            nPosition = nTempPosition;
                        } else if (nPosition == listSong.size() - 1) {
                            nPosition = 0;
                        } else {
                            nPosition++;
                        }
                        playSong();
                    } else if (action.equalsIgnoreCase(Constants.ACTION.CLOSE_ACTION)) {
                        isPause = true;
                        mp.pause();
                        return;
                    }
                    setNotificationInfo();
                    notificationManager.notify(NOTIF_ID, notification);
                }
            }
        };

        // Thread to send data back to activity to update UI if needed after every 0.1s
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (serviceIsRunning && !LoginActivity.isAtLogin) {
                    try {
                        Thread.sleep(100);
                        if (mediaPlayerPrepared) {
                            currentPosition = mp.getCurrentPosition();

                            sendDataToActivity();
                        }
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
                    stopThisService();
                }

            }
        });
        thread.start();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION.PLAY_OR_PAUSE_ACTION);
        intentFilter.addAction(Constants.ACTION.PREV_ACTION);
        intentFilter.addAction(Constants.ACTION.NEXT_ACTION);
        intentFilter.addAction(Constants.ACTION.CLOSE_ACTION);

        serviceIsRunning = true;
        registerReceiver(musicPlayerBroadcastReceiver, intentFilter);

        Bundle bundle;
        if (intent != null && (bundle = intent.getExtras()) != null) {
            // Get the data from activity
            nPosition = bundle.getInt("position", 0);
            isRepeatOne = bundle.getBoolean("isRepeatOne", false);
            isPause = bundle.getBoolean("isPause", false);
            isRepeatAll = bundle.getBoolean("isRepeatAll", false);
            isShuffle = bundle.getBoolean("isShuffle", false);
            currentPosition = bundle.getInt("currentPosition", 0);
            isUserChangePosition = bundle.getBoolean("isUserChangePosition", false);
            isOnline = bundle.getBoolean("isOnline", false);

            if (!isOnline) {
                listSong = UtilitySongOfflineClass.getInstance().getList();
            } else {
                listSong = UtilitySongOnlineClass.getInstance().getItemOfList();
            }

            playSong();


            setNotificationInfo();
            notificationManager.notify(NOTIF_ID, notification);

        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceIsRunning = false;

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
        if (mediaPlayer.getDuration() - mediaPlayer.getCurrentPosition() <= 500) { // End song, we accept 0.5s of delay
            currentPosition = 0;
            if (isRepeatOne) {
                lastFilePath = "";
            } else if (isShuffle) {
                //Tron cung mang nghia repeat all
                Random rand = new Random();
                int nTempPosition;
                do {
                    nTempPosition = rand.nextInt((listSong.size() - 1));
                } while (nTempPosition == nPosition);
                nPosition = nTempPosition;
            } else {
                //no repeatone or no shuffler->play next song
                if (nPosition < listSong.size() - 1) {

                    nPosition++;
                } else if (isRepeatAll) {
                    // play first song
                    nPosition = 0;
                }
            }
            playSong();
            setNotificationInfo();
            buildNotification();
            notificationManager.notify(NOTIF_ID, notification);
        }
    }

    /**
     * Created by Son on 5/11/2018.
     */

    void playSong() {
        if (!lastFilePath.equals(listSong.get(nPosition).getPath())) {
            // Only need to do these steps if we're going to playing new songs
            lastFilePath = listSong.get(nPosition).getPath();
            mediaPlayerPrepared = false;
            mp.release();
            mp = new MediaPlayer();
            mp.setOnCompletionListener(this);
            mp.reset();
            try {
                mp.setDataSource(listSong.get(nPosition).getPath());
//                mp.prepare();
                mp.prepareAsync();
                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayerPrepared = true;
                        mp.start();
                        mp.seekTo(0);
                        totalDuration = mp.getDuration();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (isUserChangePosition) {
            mp.seekTo(currentPosition);
            isUserChangePosition = false; // Return to default
        }
        if (isPause) {
            mp.pause();
        } else {
            mp.start();
        }
    }

    /**
     * Created by Son on 5/12/2018.
     */

    private void setNotificationInfo() {
        SongPlayerInfo song = listSong.get(nPosition);

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

        if (!isOnline) {
            Bitmap imgSong = BitmapFactory.decodeByteArray(song.getSongImage(), 0, song.getSongImage().length);
            remoteViews.setImageViewBitmap(R.id.imgSongs, imgSong);

        } else {
            NotificationTarget notificationTarget = new NotificationTarget(getApplicationContext(),
                    remoteViews, R.id.imgSongs, notification, NOTIF_ID);
            Glide.with(getApplicationContext()).load(((SongPlayerOnlineInfo) song).getImageSongURL())
                    .asBitmap().centerCrop().into(notificationTarget);
        }

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
        i.putExtra("isOnline", isOnline);

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

    /**
     * Created by Son on 5/13/2018.
     */
    private void stopThisService() {
        stopSelf();
//        mp.stop();
        mp.release();
    }


}
