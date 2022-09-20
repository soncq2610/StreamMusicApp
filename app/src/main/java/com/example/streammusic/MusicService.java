package com.example.streammusic;

import static com.example.streammusic.MyApplication.CHANNEL_ID;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.List;

import Song.Song;

public class MusicService extends Service {
    private static final int ACTION_PAUSE=1;
    private static final int ACTION_RESUME=2;
    private static final int ACTION_CLEAR=3;
    private boolean isPlaying;
    private List<Song> songList;
    private MediaPlayer mediaPlayer;
    private Song mSong;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Service stattus:","Service on create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundle = intent.getExtras();

        if(bundle!=null){
            Song song =(Song) bundle.get("Song");
            mSong=song;
          //  Log.e("title",song.getTitle()+"");
            String url = "https://firebasestorage.googleapis.com/v0/b/musicapp-5e1bc.appspot.com/o/audio%2FOlivia%20Rodrigo%20-%20drivers%20license%20(Official%20Video).mp3?alt=media&token=11c2fe03-b75b-4a74-bfb4-c43182dbfbb9";
            startSong(url);

            senNotification(mSong);

        }
        int actionMusic = intent.getIntExtra("action_music",0);
        handleActionMusic(actionMusic);

        return START_NOT_STICKY;
    }

    private void handleActionMusic(int action){
        switch (action){
            case ACTION_PAUSE:
                pauseMusic();
                break;
            case ACTION_RESUME:
                resumeMusic();
                break;
            case ACTION_CLEAR:
                stopSelf();
                break;
        }
    }

    private void pauseMusic(){

            mediaPlayer.pause();
            isPlaying=false;
            senNotification(mSong);

    }
    private void resumeMusic(){

            mediaPlayer.start();
            isPlaying=true;
            senNotification(mSong);

    }

    private void senNotification(Song song) {
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

//        Glide.with(getApplicationContext().getApplicationContext())
//                .load(song.getImg()+"")
//                .asBitmap()
//                .placeholder(placeholder)
//                .error(placeholder)
//                .into(notificationTarget);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.icon_music);

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.layout_music_notification);
        remoteViews.setTextViewText(R.id.tv_Notifi_title_song,song.getTitle());
        remoteViews.setTextViewText(R.id.tv_Notifi_single_song,song.getArtist());

        //su kien click tren remoteview
        if(isPlaying){
            remoteViews.setOnClickPendingIntent(R.id.notifi_play,getPendingIntent(this,ACTION_PAUSE));
            remoteViews.setImageViewResource(R.id.notifi_play,R.drawable.ic_pause_myicon);
        }else{
            remoteViews.setOnClickPendingIntent(R.id.notifi_play,getPendingIntent(this,ACTION_RESUME));
            remoteViews.setImageViewResource(R.id.notifi_play,R.drawable.ic_play_myicon);
        }
            remoteViews.setOnClickPendingIntent(R.id.notifi_play,getPendingIntent(this,ACTION_CLEAR));


        Notification notification = new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)

                .setSmallIcon(R.drawable.icon_music)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteViews)
                .setSound(null)
                .build();

        startForeground(1,notification);
    }



    private PendingIntent getPendingIntent(Context context,int action){
        Intent intent = new Intent(this,MyReceiver.class);
        intent.putExtra("action_music",action);

        return PendingIntent.getBroadcast(context.getApplicationContext(),action,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void startSong(String url) {
        mediaPlayer = new MediaPlayer();
        if(mediaPlayer!=null){
            try {
                mediaPlayer.setDataSource(url);
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });
                isPlaying=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer =null;
        }
    }
}
