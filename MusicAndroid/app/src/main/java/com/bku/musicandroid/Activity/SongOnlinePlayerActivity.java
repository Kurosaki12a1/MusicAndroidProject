package com.bku.musicandroid.Activity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.bku.musicandroid.Utility.TimerOfSong;
import com.bku.musicandroid.Utility.UtilitySongOnlineClass;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by Welcome on 4/30/2018.
 */

public class SongOnlinePlayerActivity extends AppCompatActivity   implements SeekBar.OnSeekBarChangeListener,MediaPlayer.OnCompletionListener {

    public static final String Liked_Path = "All_Liked_Song_Database";
    public static final String Song_Database = "All_Song_Database_Info";
    public static final String Download_Database="All_Download_Song_Database";
    public static final String View_Database="All_View_Song_Database";
    TextView songTitle, songArtist, elapsedTime, durationTime;
    ImageView option, shuffle, repeatAll, repeatOne, previous, play, next, avatarSong, backArrow, download, liked;
    SeekBar progressSong;
    public static MediaPlayer mp;

    //Chinh thanh progress luon cap nhat
    private Handler mHandler;
    private boolean isRepeatOne = false;
    //private boolean isShuffle=false;
    private boolean isPause = false;
    private boolean isRepeatAll = false;
    private boolean isLiked = false;
    private TimerOfSong timerOfSong;

    FirebaseAuth mAuth;
    //Chinh thoi gian display song :^

    String strSongURL = "";
    String strSongName = "";
    String strSongArtist = "";
    String strSongImageURL = "";
    String strSongId = "";

    SongPlayerOnlineInfo songInfo;
    UtilitySongOnlineClass utilitySongOnlineClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_online_song);

        mAuth = FirebaseAuth.getInstance();
        final String userId = mAuth.getCurrentUser().getUid();

        utilitySongOnlineClass = UtilitySongOnlineClass.getInstance();

        songInfo = new SongPlayerOnlineInfo(utilitySongOnlineClass.getItem());

        getValueSongInfo();

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
        download = (ImageView) findViewById(R.id.DownLoad);
        liked = (ImageView) findViewById(R.id.Liked);
        liked.setImageResource(R.drawable.ic_fav_song);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Liked_Path);
        databaseReference.child(strSongId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    if (singleSnapshot.getKey().equals(userId)) {
                        isLiked = true;
                        liked.setImageResource(R.drawable.ic_faved_video);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mp = new MediaPlayer();
        timerOfSong = new TimerOfSong();
        mp.setOnCompletionListener(this);
        progressSong.setOnSeekBarChangeListener(this);

        songTitle.setText(strSongName);
        songArtist.setText(strSongArtist);
        Glide.with(this).load(strSongImageURL).centerCrop().into(avatarSong);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp.isPlaying()) {
                    if (!isPause) {
                        isPause = true;
                        mp.pause();
                        play.setImageResource(R.drawable.btn_playback_play);
                    }
                } else {
                    if (isPause) {
                        isPause = false;
                        mp.start();
                        play.setImageResource(R.drawable.ic_player_pause);
                    }

                }
            }
        });

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

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        repeatAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeatOne = true;
                isRepeatAll = false;
                repeatOne.setVisibility(View.VISIBLE);
                repeatAll.setVisibility(View.GONE);
            }
        });

        repeatOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRepeatOne = false;
                isRepeatAll = true;
                repeatAll.setVisibility(View.VISIBLE);
                repeatOne.setVisibility(View.GONE);
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DownloadTask downloadTask=new DownloadTask(SongOnlinePlayerActivity.this);
                downloadTask.execute(strSongURL);

            }
        });

        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    isLiked = false;
                    liked.setImageResource(R.drawable.ic_fav_song);
                    UnLikedSong(userId);
                } else {
                    isLiked = true;
                    liked.setImageResource(R.drawable.ic_faved_video);
                    LikedSong(userId);
                }
            }
        });

        playSong();

    }

    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mp.getDuration();
            long currentDuration = mp.getCurrentPosition();

            // Displaying Total Duration time
            durationTime.setText("" + timerOfSong.milliSecondsToTimer(totalDuration));
            // Displaying time completed playing
            elapsedTime.setText("" + timerOfSong.milliSecondsToTimer(currentDuration));


            // Updating progress bar
            int progress = (int) (timerOfSong.getProgressPercentage(currentDuration, totalDuration));
            if (!isPause) {
                try {
                    //  matrix.set(avatarSong.getImageMatrix());
                    //   Rotate = Rotate + 1.0f;
                    avatarSong.setRotation(avatarSong.getRotation() + 1.0f);
                   /* matrix.postRotate(Rotate);
                    resizedBitmap = Bitmap.createBitmap(bmpImageSong, 0, 0, bmpImageSong.getWidth(), bmpImageSong.getHeight(), matrix, true);
                    avatarSong.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    avatarSong.setImageBitmap(resizedBitmap);*/
                } catch (Exception e) {
                }

            }

            //Log.d("Progress", ""+progress);
            progressSong.setProgress(progress);


            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };


    @Override
    public void onCompletion(MediaPlayer mp) {

        if (isRepeatAll | isRepeatOne) {
            playSong();
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            if (mp != null) {
                int currentDuration = mp.getDuration() * progress / 100 + 1;
                mp.seekTo(currentDuration);
            }
        }
        if (progress == 100) {
            isPause = false;
            play.setImageResource(R.drawable.btn_playback_play);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = timerOfSong.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }

    @Override
    public void onStop() {
        super.onStop();
        mp.stop();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mp.release();

    }

    private void playSong() {

        avatarSong.setRotation(0.0f);
        mHandler = new Handler();
        try {
            mp.reset();
            mp.setDataSource(strSongURL);
            mp.prepare();
            mp.start();
            play.setImageResource(R.drawable.ic_player_pause);
            upView();
            progressSong.setProgress(0);
            progressSong.setMax(100);
           // upView();
            updateProgressBar();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getValueSongInfo() {
        strSongArtist = songInfo.getSongArtists();
        strSongURL = songInfo.getSongURL();
        strSongImageURL = songInfo.getImageSongURL();
        strSongName = songInfo.getSongName();
        strSongId = songInfo.getSongId();
    }

    private void LikedSong(String userId) {
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Liked_Path).child(strSongId);
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(Song_Database).child(strSongId);
        databaseReference.child(userId).setValue("Liked This Song");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                songInfo.setLiked(String.valueOf(dataSnapshot.getChildrenCount()));
                databaseReference1.setValue(songInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void UnLikedSong(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Liked_Path).child(strSongId);
        final DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference(Song_Database).child(strSongId);
        databaseReference.child(userId).removeValue(); //remove
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                songInfo.setLiked(String.valueOf(dataSnapshot.getChildrenCount()));
                databaseReference1.setValue(songInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private class DownloadTask extends AsyncTask<String, Integer, String> {

        private Context context;

        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();
                output = new FileOutputStream(  Environment.getExternalStorageDirectory()+"/"+strSongName+".mp3");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String params){
            updateDownLoad();
        }
    }

    private void updateDownLoad(){
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(Song_Database).child(strSongId);
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference(Download_Database).child(strSongId);
        databaseReference1.push().setValue("Id of Download");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                songInfo.setDownload(String.valueOf(dataSnapshot.getChildrenCount()));
                databaseReference.setValue(songInfo);
                //ReLoadActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void upView(){

        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(Song_Database).child(strSongId);
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference(View_Database).child(strSongId);
        databaseReference1.push().setValue("Id of View");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
           //   songInfo=dataSnapshot.getValue(SongPlayerOnlineInfo.class);

              songInfo.setView(String.valueOf(dataSnapshot.getChildrenCount()));
              databaseReference.setValue(songInfo);
                //ReLoadActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
