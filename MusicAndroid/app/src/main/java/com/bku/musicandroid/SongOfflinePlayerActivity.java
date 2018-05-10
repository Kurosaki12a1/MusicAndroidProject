package com.bku.musicandroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Thinh on 4/24/2018.
 */

public class SongOfflinePlayerActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener/*,MediaPlayer.OnCompletionListener*/{



    TextView songTitle,songArtist,elapsedTime,durationTime;
    ImageView option,shuffle,repeatAll,repeatOne,previous,play,next,avatarSong,backArrow;
    SeekBar progressSong;
//    public static MediaPlayer mp;

    //Chinh thanh progress luon cap nhat
    private Handler mHandler ;
    private boolean isRepeatOne=false;
    private boolean isShuffle=false;
    private boolean isPause=false;
    private boolean isRepeatAll=false;
    private boolean isUserChangePosition = false;
//    private int currentDuration = 0;
    private int totalDuration = 0;
    private int currentPosition = 0;
    private TimerOfSong timerOfSong;
    private BroadcastReceiver receiver;
    //Chinh thoi gian display song :^

    int nPosition=0;
    final int DEFAULT_WIDTH_OF_AVATAR=180;
    String strSongName="";
    String strSongArtist="";
    byte [] byteOfImageSong=null;
    Bitmap bmpImageSong;


    ArrayList<SongPlayerOfflineInfo> listSong;
    UtilitySongOfflineClass utilitySongOfflineClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_offline_song);

        Bundle extras=getIntent().getExtras();
        if(extras!=null){
            nPosition=extras.getInt("currentPosition");
            utilitySongOfflineClass = UtilitySongOfflineClass.getInstance();
            listSong=new ArrayList<>(utilitySongOfflineClass.getList());
            listSong= utilitySongOfflineClass.getList();
        }

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                nPosition = intent.getIntExtra("position", 0);
                isRepeatOne = intent.getBooleanExtra("isRepeatOne", false);
                isRepeatAll = intent.getBooleanExtra("isRepeatAll", false);
                isShuffle = intent.getBooleanExtra("isShuffle", false);
                isPause = intent.getBooleanExtra("isPause", false);
                currentPosition = intent.getIntExtra("currentPosition", 0);
//                currentDuration = intent.getIntExtra("currentDuration", 0);
                totalDuration = intent.getIntExtra("totalDuration", 0);

                if (!isPause) {
                    play.setImageResource(R.drawable.ic_player_pause);
                }

            }
        };


        avatarSong=(ImageView)findViewById(R.id.albumImage);
        songTitle=(TextView)findViewById(R.id.song_title);
        songArtist=(TextView) findViewById(R.id.song_artist);
        elapsedTime=(TextView)findViewById(R.id.song_elapsed_time);
        durationTime=(TextView)findViewById(R.id.song_duration);
        option=(ImageView)findViewById(R.id.option);
        backArrow=(ImageView)findViewById(R.id.backArrow);
        shuffle=(ImageView)findViewById(R.id.shuffle);
        repeatAll=(ImageView)findViewById(R.id.repeatAll);
        repeatOne=(ImageView)findViewById(R.id.repeatOne);
        previous=(ImageView)findViewById(R.id.previous);
        play=(ImageView)findViewById(R.id.play);
        next=(ImageView)findViewById(R.id.next);
        progressSong=(SeekBar)findViewById(R.id.song_progress);

//        mp=new MediaPlayer();
        timerOfSong=new TimerOfSong();


        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isPause){
                    isPause=true;
//                    mp.pause();
                    startMusicService();
                    play.setImageResource(R.drawable.btn_playback_play);
                }
                else
                {
                    isPause=false;
//                    mp.start();
                    startMusicService();
                    play.setImageResource(R.drawable.ic_player_pause);
                }


            }
        });
//        mp.setOnCompletionListener(this);
        progressSong.setOnSeekBarChangeListener(this);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                finish();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUserChangePosition = true;
                if(nPosition==0){
                    playSong(0);
                }
                else {
                    nPosition--;
                    playSong(nPosition);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUserChangePosition = true;

                if(nPosition==listSong.size()-1){
                    playSong(nPosition);
                }
                else {
                    nPosition++;
                    playSong(nPosition);
                }
            }
        });

        repeatAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeatOne=true;
                isRepeatAll=false;
                startMusicService();
                repeatOne.setVisibility(View.VISIBLE);
                repeatAll.setVisibility(View.GONE);
            }
        });

        repeatOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeatOne=false;
                isRepeatAll=true;
                startMusicService();
                repeatAll.setVisibility(View.VISIBLE);
                repeatOne.setVisibility(View.GONE);
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShuffle=!isShuffle;
                isRepeatOne=false;
                startMusicService();
            }
        });

        playSong(nPosition);

    }
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {

            // Displaying Total Duration time
            durationTime.setText(""+timerOfSong.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            elapsedTime.setText(""+timerOfSong.milliSecondsToTimer(currentPosition));


            // Updating progress bar
            int progress = (int)(timerOfSong.getProgressPercentage(currentPosition, totalDuration));
            if(!isPause){
                try {
                  //  matrix.set(avatarSong.getImageMatrix());
                 //   Rotate = Rotate + 1.0f;
                    avatarSong.setRotation(avatarSong.getRotation()+1.0f);
                   /* matrix.postRotate(Rotate);
                    resizedBitmap = Bitmap.createBitmap(bmpImageSong, 0, 0, bmpImageSong.getWidth(), bmpImageSong.getHeight(), matrix, true);
                    avatarSong.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    avatarSong.setImageBitmap(resizedBitmap);*/
                }
                catch(Exception e) { }

            }

            //Log.d("Progress", ""+progress);
            progressSong.setProgress(progress);


            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    public void setBitMapFit(){
        int currentBitmapWidth = bmpImageSong.getWidth();
        int currentBitmapHeight = bmpImageSong.getHeight();
        int newWidth = DEFAULT_WIDTH_OF_AVATAR;

        //the image dont need to resize anymore

        int newHeight = (int) Math.floor((double) currentBitmapHeight *( (double) newWidth / (double) currentBitmapWidth));

        bmpImageSong = Bitmap.createScaledBitmap(bmpImageSong, newWidth, newHeight, true);
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser){
            currentPosition=totalDuration*progress/100+1;
            startMusicService();
        }
        if(progress==100){
            isPause=false;
            startMusicService();
            play.setImageResource(R.drawable.btn_playback_play);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);

        // forward or backward to certain seconds
        currentPosition = timerOfSong.progressToTimer(seekBar.getProgress(), totalDuration);
        isUserChangePosition = true;
        startMusicService();

        // update timer progress again
        updateProgressBar();
    }

    public void getCurrentInfoSong(int nPosition){

        strSongArtist=listSong.get(nPosition).getSongArtists();
        strSongName=listSong.get(nPosition).getSongName();
        byteOfImageSong=listSong.get(nPosition).getSongImage();

        songTitle.setText(strSongName);
        if(songArtist!=null) songArtist.setText(strSongArtist);

        if(byteOfImageSong!=null) {
            bmpImageSong = BitmapFactory.decodeByteArray(byteOfImageSong, 0, byteOfImageSong.length);
        }
        else
        {
            bmpImageSong=BitmapFactory.decodeResource(this.getResources(),R.drawable.ic_audiotrack_dark);
        }
        setBitMapFit();
        avatarSong.setImageBitmap(bmpImageSong);
    }

    public void playSong(int nPosition){
      //  numOfSong++;
        avatarSong.setRotation(0.0f);
        mHandler = new Handler();
        getCurrentInfoSong(nPosition);
        try {
            startMusicService();
            play.setImageResource(R.drawable.ic_player_pause);
            progressSong.setProgress(0);
            progressSong.setMax(100);
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        mp.stop();
        mHandler.removeCallbacks(mUpdateTimeTask);
//        mp.release();
        /*   mp.stop();*/

    }

    @Override
    public void onPause(){
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
      /*  mp.pause();*/
    }

    @Override
    public void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("MusicPlayerUpdate"));
      /*  if(mp.isPlaying()){
            mp.start();
        }*/
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

    }

    // Function to start music service in background
    // Beside the first call, we will call it everytime we need to change something in playing song
    void startMusicService(){
        Intent i = new Intent(SongOfflinePlayerActivity.this, SongPlayerService.class);
        i.putExtra("position", nPosition);
        i.putExtra("isRepeatOne", isRepeatOne);
        i.putExtra("isPause", isPause);
        i.putExtra("isRepeatAll", isRepeatAll);
        i.putExtra("currentPosition", currentPosition);
        i.putExtra("isUserChangePosition", isUserChangePosition);
        startService(i);
    }
}
