package com.mosis.treasurehunt.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.activities.HomeActivity;

import java.util.Timer;
import java.util.TimerTask;

public class LocationTrackerService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private Timer mTimer;
    public LocationTrackerService() {
        mTimer = new Timer();
    }

    @Override
    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       Log.i("Service", "Service started");
        createNotificationChannel();
        Intent notifIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notifIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("Hello friend")
                .setSmallIcon(R.drawable.show_friends_icon)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);


//       mTimer.scheduleAtFixedRate(new TimerTask() {
//           @Override
//           public void run() {
//               Log.i("Service interval", "Working");
//           }
//       }, 0, 3000);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i("Service", "Service stopped");
//        mTimer.cancel();
//        mTimer.purge();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
