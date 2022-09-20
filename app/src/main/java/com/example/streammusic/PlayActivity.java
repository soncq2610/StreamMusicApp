package com.example.streammusic;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Song.Song;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayActivity extends AppCompatActivity {
    private TextView nameSong;
    private String songUrl,imageUrl;
    private int songId;
    private TextView txtStart;
    private TextView txtEnd;
    private SeekBar seekBar;
    private ImageView btnNext,btnPre;
    ImageView btnPlay;
    CircleImageView imageSong;
    BarVisualizer visualizer;
    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean state =true;
    private MusicService musicService;
    private List<Song> mListSong;
    private Animation animation;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Bundle bundle = getIntent().getExtras();
        if(bundle==null){
            return;
        }
        musicService = new MusicService();

         imageSong = findViewById(R.id.image_song2);
        Song song =(Song) bundle.get("object_song");
        int position = bundle.getInt("position");

        Log.e("vi tri",position+"");
         nameSong = findViewById(R.id.songPlay_name2);
         imageUrl = song.getImg()+"";
         songId = Integer.parseInt(song.getSongId());

         Glide.with(this).load(imageUrl+"").into(imageSong);
        nameSong.setText(song.getTitle()+"");
        songUrl = song.getLink()+"";
        btnPlay = findViewById(R.id.playPausebtn);
        btnNext = findViewById(R.id.nextbtn2);
        btnPre = findViewById(R.id.prebtn2);
        txtStart = findViewById(R.id.txt_start2);
        txtEnd = findViewById(R.id.txt_end2);
        seekBar = findViewById(R.id.seekbar_duration2);
        animation = AnimationUtils.loadAnimation(this,R.anim.rotation);

       mediaPlayer = new MediaPlayer();

        seekBar.setMax(100);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    handler.removeCallbacks(updater);
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    getSongById((songId +1)+"");
                    startAnimation(imageSong);
                }
            }
        });
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    handler.removeCallbacks(updater);
                    mediaPlayer.pause();
                    btnPlay.setImageResource(R.drawable.ic_play_myicon);
                    stopAnimation(imageSong);
                }else{
                    mediaPlayer.start();
                    btnPlay.setImageResource(R.drawable.ic_pause_myicon);
                    updateSeekBar();
                    startAnimation(imageSong);
                }
            }
        });

        prepareMediaPlayer();

         seekBar.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View view, MotionEvent motionEvent) {
                 SeekBar seekBar1 =(SeekBar) view;
                 int playPosition = (mediaPlayer.getDuration()/100)* seekBar1.getProgress();
                 mediaPlayer.seekTo(playPosition);
                 txtStart.setText(miliSeconsToTimer(mediaPlayer.getCurrentPosition()));
                 return false;
             }
         });

         mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
             @Override
             public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                 seekBar.setSecondaryProgress(i);
             }
         });

         mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
             @Override
             public void onCompletion(MediaPlayer mediaPlayer) {
                 seekBar.setProgress(0);
                 btnPlay.setImageResource(R.drawable.ic_play_myicon);
                 txtStart.setText("0:00");
                 mediaPlayer.reset();
                 prepareMediaPlayer();
                 stopAnimation(imageSong);
             }
         });



     }




    private void prepareMediaPlayer(){
        try {
            mediaPlayer.setDataSource(String.valueOf(songUrl));
            mediaPlayer.prepare();
            txtEnd.setText(miliSeconsToTimer(mediaPlayer.getDuration()));

        }catch (Exception exception){

        }
    }

    private Runnable updater = new Runnable() {
        @Override
        public void run() {
            updateSeekBar();
            long currentDuration = mediaPlayer.getCurrentPosition();
            txtStart.setText(miliSeconsToTimer(currentDuration));

        }
    };

    private void updateSeekBar(){
        if(mediaPlayer.isPlaying()){
            seekBar.setProgress((int)(((float) mediaPlayer.getCurrentPosition()/mediaPlayer.getDuration())* 100));
            handler.postDelayed(updater,1000);
        }
    }

    private  String miliSeconsToTimer(long miliSeconds){
        String timerString="";
        String secondsString;

        int hours = (int)(miliSeconds/(1000*60*60));
        int minutes = (int)(miliSeconds %(1000*60*60))/(1000*60);
        int seconds =(int)((miliSeconds%(1000*60*60))%(1000*60)/1000);

        if(hours>0){
            timerString = hours+":";
        }
        if(seconds<10){
            secondsString = "0"+seconds;
        }else {
            secondsString = ""+seconds;
        }
        timerString = timerString + minutes +":"+secondsString;
        return timerString;
    }


    public void startAnimation(View view){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                view.animate().rotationBy(360).withEndAction(this).setDuration(10000).setInterpolator(new LinearInterpolator()).start();
            }
        };
        view.animate().rotationBy(360).withEndAction(runnable).setDuration(10000).setInterpolator(new LinearInterpolator()).start();
    }


    public void stopAnimation(View view){
        view.animate().cancel();
        view.clearAnimation();

    }
//    private void getListSong(){
//        FirebaseDatabase database  = FirebaseDatabase.getInstance();
//        DatabaseReference rfrSong = database.getReference("Song");
//        Query query = rfrSong.child("songId");
//        rfrSong.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!task.isSuccessful()) {
//                    Log.e("firebase", "Error getting data", task.getException());
//                }
//                else {
//
//                    Log.d("firebase111", String.valueOf(task.getResult().getValue()));
//
//                }
//            }
//        });
//
//
//    }

    private void getSongById(String Id){

        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference rfrSong = database.getReference("Song");

        rfrSong.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Song song;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("songId").getValue().toString().endsWith(Id+""));
                    Log.e("aaa",dataSnapshot.getValue().toString());
                     song = new Song(dataSnapshot.child("albumId").getValue().toString(),dataSnapshot.child("artist").getValue().toString(),dataSnapshot.child("image").getValue().toString(),dataSnapshot.child("link").getValue().toString(),dataSnapshot.child("name").getValue().toString(),dataSnapshot.child("songId").getValue().toString());
                     songUrl = song.getLink()+"";
                }

                try {

                    mediaPlayer.setDataSource(songUrl);
                    mediaPlayer.prepare();
                    txtEnd.setText(miliSeconsToTimer(mediaPlayer.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                btnPlay.setImageResource(R.drawable.ic_pause_myicon);
                updateSeekBar();
                startAnimation(imageSong);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
        @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updater);
        mediaPlayer.stop();
        mediaPlayer.release();

    }
}