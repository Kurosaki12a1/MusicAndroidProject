package com.bku.musicandroid.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bku.musicandroid.R;
import com.bku.musicandroid.Model.SongPlayerOnlineInfo;
import com.bku.musicandroid.Service.SongPlayerService;
import com.bku.musicandroid.Utility.TimerOfSong;
import com.bku.musicandroid.Utility.UtilitySongOfflineClass;
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
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Welcome on 4/30/2018.
 */

public class SongOnlinePlayerActivity extends AppCompatActivity   implements SeekBar.OnSeekBarChangeListener{

    public static final String Liked_Path = "All_Liked_Song_Database";
    public static final String Song_Database = "All_Song_Database_Info";
    public static final String Download_Database="All_Download_Song_Database";
    public static final String View_Database="All_View_Song_Database";
    TextView songTitle, songArtist, elapsedTime, durationTime;
    ImageView option, shuffle, repeatAll, repeatOne, previous, play, next, avatarSong, backArrow, download, liked;
    SeekBar progressSong;
    public static MediaPlayer mp;

    //Chinh thanh progress luon cap nhat
//    private Handler mHandler;
    private boolean isRepeatOne = false;
    private boolean isShuffle=false;
    private boolean isPause = false;
    private int totalDuration = 100;
    private int currentPosition = 0;
    private boolean isRepeatAll = false;
    private boolean isLiked = false;
    private TimerOfSong timerOfSong;
    private boolean isUserChangePosition = false;

    private int nPosition = 0;
    private BroadcastReceiver receiver;

    FirebaseAuth mAuth;
    //Chinh thoi gian display song :^

    String strSongURL = "";
    String strLastSongURL = "";
    String strSongName = "";
    String strSongArtist = "";
    String strSongImageURL = "";
    String strSongId = "";

//    SongPlayerOnlineInfo songInfo;
    ArrayList<SongPlayerOnlineInfo> listSong;
    UtilitySongOnlineClass utilitySongOnlineClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_online_song);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nPosition = extras.getInt("currentPosition");
            isPause = extras.getBoolean("isPause", false);
            isRepeatAll = extras.getBoolean("isRepeatAll", false);
            isRepeatOne = extras.getBoolean("isRepeatOne", false);
            isShuffle = extras.getBoolean("isShuffle", isShuffle);
        }

        /**
         * Created by Son on 5/15/2018.
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
                    repeatOne.setImageResource(R.drawable.exo_controls_repeat_one);
                } else {
                    repeatOne.setImageResource(R.drawable.exo_controls_repeat_all);
                }

                // Update new song info UI when auto move to another song
                if (!strLastSongURL.equals(listSong.get(nPosition).getPath())) {
                    strLastSongURL = listSong.get(nPosition).getPath();
                    getCurrentInfoSong(nPosition);
                    updateSongInfoUI();
                    avatarSong.setRotation(0.0f);
                }

                // Update progress bar and rotate the bitmap every 100ms
                updateSongProgressUI();
                rotateBitmap();

            }
        };

        mAuth = FirebaseAuth.getInstance();
        final String userId = mAuth.getCurrentUser().getUid();

        utilitySongOnlineClass = UtilitySongOnlineClass.getInstance();

//        songInfo = new SongPlayerOnlineInfo(utilitySongOnlineClass.getItem());
        listSong = UtilitySongOnlineClass.getInstance().getItemOfList();

        getCurrentInfoSong(nPosition);

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
        timerOfSong = new TimerOfSong();
        progressSong.setOnSeekBarChangeListener(this);


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
                    if(isShuffle)
                    {
                        //Tron cung mang nghia repeat all
                        Random rand=new Random();
                        int nTempPosition;
                        do {
                            nTempPosition = rand.nextInt((listSong.size()-nPosition-1));
                        } while (nTempPosition == nPosition);
                        nPosition=nTempPosition;
                        playSong();
                    }
                    else {
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

                if(nPosition==listSong.size()-1){
                    nPosition = 0;
                    playSong();
                }
                else {
                    if(isShuffle)
                    {
                        //Tron cung mang nghia repeat all
                        Random rand=new Random();
                        int nTempPosition;
                        do {
                            nTempPosition = rand.nextInt((listSong.size()-nPosition-1));
                        } while (nTempPosition == nPosition);
                        nPosition=nTempPosition;
                        playSong();
                    }
                    else {
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
                isRepeatOne = false;
                startMusicService();
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

    /**
     * Created by Son on 5/15/2018.
     */
    private void rotateBitmap() {
        if (!isPause) {
            try {
                avatarSong.setRotation(avatarSong.getRotation() + 1.0f);

            } catch (Exception e) { }

        }
    }

    /**
     * Created by Son on 5/15/2018.
     */
    private void updateSongProgressUI() {
        // Displaying Total Duration time
        durationTime.setText("" + timerOfSong.milliSecondsToTimer(totalDuration));
        // Displaying time completed playing
        elapsedTime.setText("" + timerOfSong.milliSecondsToTimer(currentPosition));


        // Updating progress bar
        int progress = (int) (timerOfSong.getProgressPercentage(currentPosition, totalDuration));

        progressSong.setProgress(progress);
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
        currentPosition = timerOfSong.progressToTimer(seekBar.getProgress(), totalDuration);
        isUserChangePosition = true;
        startMusicService();
        isUserChangePosition = false; // return to default
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void playSong() {
        avatarSong.setRotation(0.0f);
        //Play new song so we need to update new UI
        getCurrentInfoSong(nPosition);
        updateSongInfoUI();
        try {
            startMusicService();
            play.setImageResource(R.drawable.ic_player_pause);
            upView();
            progressSong.setProgress(0);
            progressSong.setMax(100);
            isUserChangePosition = false; // Return to default
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * Created by Son on 5/15/2018.
     */
    private void getCurrentInfoSong(int nPosition) {
        SongPlayerOnlineInfo songInfo = listSong.get(nPosition);
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
                listSong.get(nPosition).setLiked(String.valueOf(dataSnapshot.getChildrenCount()));
                databaseReference1.setValue(listSong.get(nPosition));
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
                listSong.get(nPosition).setLiked(String.valueOf(dataSnapshot.getChildrenCount()));
                databaseReference1.setValue(listSong.get(nPosition));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("MusicPlayerUpdate"));
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

                byte data[] = new byte[1024];
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

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    private void updateDownLoad(){
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference(Song_Database).child(strSongId);
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference(Download_Database).child(strSongId);
        databaseReference1.push().setValue("Id of Download");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listSong.get(nPosition).setDownload(String.valueOf(dataSnapshot.getChildrenCount()));
                databaseReference.setValue(listSong.get(nPosition));
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

              listSong.get(nPosition).setView(String.valueOf(dataSnapshot.getChildrenCount()));
              databaseReference.setValue(listSong.get(nPosition));
                //ReLoadActivity();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Created by Son on 5/15/2018.
     */
    private void startMusicService() {
        Intent i = new Intent(SongOnlinePlayerActivity.this, SongPlayerService.class);
        i.putExtra("position", nPosition);
        i.putExtra("isRepeatOne", isRepeatOne);
        i.putExtra("isPause", isPause);
        i.putExtra("isRepeatAll", isRepeatAll);
        i.putExtra("currentPosition", currentPosition);
        i.putExtra("isUserChangePosition", isUserChangePosition);
        i.putExtra("isOnline", true);
        startService(i);
    }

    /**
     * Created by Son on 5/15/2018.
     */
    private void updateSongInfoUI() {
        songTitle.setText(strSongName);
        songArtist.setText(strSongArtist);
        Glide.with(this).load(strSongImageURL).centerCrop().into(avatarSong);
    }
}
