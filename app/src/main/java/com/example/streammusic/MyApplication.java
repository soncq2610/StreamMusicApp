package com.example.streammusic;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class MyApplication extends Application {

    public  static final String CHANNEL_ID = "channel_service_example";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();

        createChannelNotification();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannelNotification() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O);
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"channel_Service", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setSound(null,null);
        NotificationManager manager = getSystemService(NotificationManager.class);
        if(manager!=null){
            manager.createNotificationChannel(channel);
        }
    }
}
