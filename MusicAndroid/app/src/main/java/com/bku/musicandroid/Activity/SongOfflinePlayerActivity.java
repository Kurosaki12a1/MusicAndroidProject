package com.bku.musicandroid.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.SongPlayerOfflineInfo;
import com.bku.musicandroid.Service.SongPlayerService;
import com.bku.musicandroid.Utility.TimerOfSong;
import com.bku.musicandroid.Utility.UtilitySongOfflineClass;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Thinh on 4/24/2018.
 */

public class SongOfflinePlayerActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {


    TextView songTitle, songArtist, elapsedTime, durationTime;
    ImageView option, shuffle, repeatAll, repeatOne, previous, play, next, avatarSong, backArrow;
    SeekBar progressSong;

    private boolean isRepeatOne = false;
    private boolean isShuffle = false;
    private boolean isPause = false;
    private boolean isRepeatAll = false;
    private boolean isUserChangePosition = false;
    private int totalDuration = 100;
    private int currentPosition = 0;
    private TimerOfSong timerOfSong;
    private BroadcastReceiver receiver;
    private String lastSongPath = "";
    //Chinh thoi gian display song :^

    int nPosition = 0;
    final int DEFAULT_WIDTH_OF_AVATAR = 180;
    String strSongName = "";
    String strSongArtist = "";
    byte[] byteOfImageSong = null;
    Bitmap bmpImageSong;


    ArrayList<SongPlayerOfflineInfo> listSong;
    UtilitySongOfflineClass utilitySongOfflineClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_offline_song);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nPosition = extras.getInt("currentPosition");
            utilitySongOfflineClass = UtilitySongOfflineClass.getInstance();
            listSong = new ArrayList<>(utilitySongOfflineClass.getList());
            isPause = extras.getBoolean("isPause", false);
            isRepeatAll = extras.getBoolean("isRepeatAll", false);
            isRepeatOne = extras.getBoolean("isRepeatOne", false);
            isShuffle = extras.getBoolean("isShuffle", false);
//            listSong = utilitySongOfflineClass.getList();
        }


        /**
         * Created by Son on 5/11/2018.
         */

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                nPosition = intent.getIntExtra("position", nPosition);
                isRepeatOne = intent.getBooleanExtra("isRepeatOne", isRepeatOne);
                isRepeatAll = intent.getBooleanExtra("isRepeatAll", isRepeatAll);
                isShuffle = intent.getBooleanExtra("isShuffle", isShuffle);
                isPause = intent.getBooleanExtra("isPause", isPause);
                currentPosition = intent.getIntExtra("currentPosition", currentPosition);
                totalDuration = intent.getIntExtra("totalDuration", totalDuration);

                if (!isPause) {
                    play.setImageResource(R.drawable.ic_player_pause);
                } else {
                    play.setImageResource(R.drawable.btn_playback_play);
                }

                if (isRepeatOne) {
                    repeatOne.setVisibility(View.VISIBLE);
                    repeatAll.setVisibility(View.INVISIBLE);
                } else {
                    repeatOne.setVisibility(View.INVISIBLE);
                    repeatAll.setVisibility(View.VISIBLE);
                }

                // Update new song info UI when auto move to another song
                try {
                    if (!lastSongPath.equals(listSong.get(nPosition).getPathFileSong())) {
                        lastSongPath = listSong.get(nPosition).getPathFileSong();
                        getCurrentInfoSong(nPosition);
                        updateSongInfoUI();
                        avatarSong.setRotation(0.0f);
                    }
                } catch (Exception e) {
                    // WELL, LET'S TRY AGAIN NEXT TIME :V
                    lastSongPath = "";
                }

                // Update progress bar and rotate the bitmap every 100ms
                updateSongProgressUI();
                rotateBitmap();

            }
        };


        avatarSong = (ImageView) findViewById(R.id.albumImage);
        songTitle = (TextView) findViewById(R.id.song_title);
        songArtist = (TextView) findViewById(R.id.song_artist);
        elapsedTime = (TextView) findViewById(R.id.song_elapsed_time);
        durationTime = (TextView) findViewById(R.id.song_duration);
        option = (ImageView) findViewById(R.id.option);
        backArrow = (ImageView) findViewById(R.id.backArrow);
        shuffle = (ImageView) findViewById(R.id.shuffle);
        repeatAll = (ImageView) findViewById(R.id.repeatAll);
        repeatOne = (ImageView) findViewById(R.id.repeatOne);
        previous = (ImageView) findViewById(R.id.previous);
        play = (ImageView) findViewById(R.id.play);
        next = (ImageView) findViewById(R.id.next);
        progressSong = (SeekBar) findViewById(R.id.song_progress);

        timerOfSong = new TimerOfSong();


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPause) {
                    isPause = true;
                    startMusicService();
                    play.setImageResource(R.drawable.btn_playback_play);
                } else {
                    isPause = false;
                    startMusicService();
                    play.setImageResource(R.drawable.ic_player_pause);
                }


            }
        });
        progressSong.setOnSeekBarChangeListener(this);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUserChangePosition = true;
                isPause = false;
                if (nPosition == 0) {
                    nPosition = listSong.size() - 1;
                    playSong();
                } else {
                    if (isShuffle) {
                        //Tron cung mang nghia repeat all
                        Random rand = new Random();
                        int nTempPosition;
                        do {
                            nTempPosition = rand.nextInt((listSong.size() - nPosition - 1));
                        } while (nTempPosition == nPosition);
                        nPosition = nTempPosition;
                        playSong();
                    } else {
                        nPosition--;
                        playSong();
                    }
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUserChangePosition = true;
                isPause = false;

                if (nPosition == listSong.size() - 1) {
                    nPosition = 0;
                    playSong();
                } else {
                    if (isShuffle) {
                        //Tron cung mang nghia repeat all
                        Random rand = new Random();
                        int nTempPosition;
                        do {
                            nTempPosition = rand.nextInt((listSong.size() - nPosition - 1));
                        } while (nTempPosition == nPosition);
                        nPosition = nTempPosition;
                        playSong();
                    } else {
                        nPosition++;
                        playSong();
                    }
                }
            }
        });

        repeatAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeatOne = true;
                isRepeatAll = false;
                startMusicService();
                repeatOne.setVisibility(View.VISIBLE);
                repeatAll.setVisibility(View.INVISIBLE);
            }
        });

        repeatOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeatOne = false;
                isRepeatAll = true;
                startMusicService();
                repeatAll.setVisibility(View.VISIBLE);
                repeatOne.setVisibility(View.INVISIBLE);
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShuffle = !isShuffle;
                if (isShuffle) {
                    TastyToast.makeText(SongOfflinePlayerActivity.this, "Shuffle mode : ON", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT).show();

                } else {
                    TastyToast.makeText(SongOfflinePlayerActivity.this, "Shuffle mode : OFF", TastyToast.LENGTH_SHORT, TastyToast.DEFAULT).show();
                }
                isRepeatOne = false;
                startMusicService();
            }
        });

        playSong();

    }

    public void setBitMapFit() {
        int currentBitmapWidth = bmpImageSong.getWidth();
        int currentBitmapHeight = bmpImageSong.getHeight();
        int newWidth = DEFAULT_WIDTH_OF_AVATAR;

        //the image dont need to resize anymore

        int newHeight = (int) Math.floor((double) currentBitmapHeight * ((double) newWidth / (double) currentBitmapWidth));

        bmpImageSong = Bitmap.createScaledBitmap(bmpImageSong, newWidth, newHeight, true);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            currentPosition = totalDuration * progress / 100 + 1;
            startMusicService();
        }
        if (progress == 100) {
            isPause = false;
            startMusicService();
            play.setImageResource(R.drawable.btn_playback_play);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        // forward or backward to certain seconds
        currentPosition = timerOfSong.progressToTimer(seekBar.getProgress(), totalDuration);
        isUserChangePosition = true;
        startMusicService();
        isUserChangePosition = false; // return to default
    }

    public void getCurrentInfoSong(int nPosition) {

        strSongArtist = listSong.get(nPosition).getSongArtists();
        strSongName = listSong.get(nPosition).getSongName();
        byteOfImageSong = listSong.get(nPosition).getSongImage();

    }

    public void playSong() {
        avatarSong.setRotation(0.0f);
        //Play new song so we need to update new UI
        getCurrentInfoSong(nPosition);
        updateSongInfoUI();
        try {
            startMusicService();
            play.setImageResource(R.drawable.ic_player_pause);
            progressSong.setProgress(0);
            progressSong.setMax(100);
            isUserChangePosition = false; // Return to default
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("MusicPlayerUpdate"));
    }


    /**
     * Created by Son on 5/11/2018.
     * <p>
     * Function to start music service in background
     * Beside the first call, we will call it each time user changes something in playing song
     */
    void startMusicService() {
        Intent i = new Intent(SongOfflinePlayerActivity.this, SongPlayerService.class);
        i.putExtra("position", nPosition);
        i.putExtra("isRepeatOne", isRepeatOne);
        i.putExtra("isPause", isPause);
        i.putExtra("isRepeatAll", isRepeatAll);
        i.putExtra("currentPosition", currentPosition);
        i.putExtra("isUserChangePosition", isUserChangePosition);
        i.putExtra("isShuffle", isShuffle);
        startService(i);
    }

    /**
     * Created by Son on 5/12/2018.
     */
    private void updateSongInfoUI() {
        songTitle.setText(strSongName);
        if (songArtist != null) songArtist.setText(strSongArtist);

        if (byteOfImageSong != null) {
            bmpImageSong = BitmapFactory.decodeByteArray(byteOfImageSong, 0, byteOfImageSong.length);
        } else {
            bmpImageSong = BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_audiotrack_dark);
        }
        setBitMapFit();
        avatarSong.setImageBitmap(bmpImageSong);
    }

    /**
     * Created by Son on 5/12/2018.
     */
    private void updateSongProgressUI() {
        // Displaying Total Duration time
        durationTime.setText("" + timerOfSong.milliSecondsToTimer(totalDuration));
        // Displaying time completed playing
        elapsedTime.setText("" + timerOfSong.milliSecondsToTimer(currentPosition));


        // Updating progress bar
        int progress = (int) (timerOfSong.getProgressPercentage(currentPosition, totalDuration));

        //Log.d("Progress", ""+progress);
        progressSong.setProgress(progress);

    }

    /**
     * Created by Son on 5/12/2018.
     */
    private void rotateBitmap() {
        if (!isPause) {
            try {
                avatarSong.setRotation(avatarSong.getRotation() + 1.0f);

            } catch (Exception e) {
            }

        }
    }
}
