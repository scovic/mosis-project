package com.mosis.treasurehunt.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.activities.HomeActivity;
import com.mosis.treasurehunt.models.Clue;
import com.mosis.treasurehunt.models.Hunt;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

public class LocationTrackerService extends Service {
    public static final String CHANNEL_ID = "LocationTrackerService";
    private User mUser;
    private UserRepository mUserRepo;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;

    public LocationTrackerService() {}

    @Override
    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesWrapper sharedWrapper = SharedPreferencesWrapper.getInstance();
        mUserRepo = UserRepository.getInstance();
        mUser =  mUserRepo.getUserByUsername(sharedWrapper.getUsername());
        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i("Location changedetected", "Detected");
                mUser.setCurrentLocation(new com.mosis.treasurehunt.models.Location(location.getLatitude(), location.getLongitude()));
                mUserRepo.updateUser(mUser);
                scanForNearbyObjects(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        };

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        //noinspection MissingPermission
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000 , 0, mLocationListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();
        Intent notifIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notifIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Location Tracker")
                .setSmallIcon(R.drawable.icon_location_tracker)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mLocationManager != null) {
            mLocationManager.removeUpdates(mLocationListener);
        }
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

    private void scanForNearbyObjects(Location loc) {
        final double lat = loc.getLatitude();
        final double lon = loc.getLongitude();
        final double dist = 200; // 100 meters
        for (User u : mUser.getFriendList()) {
            User user = mUserRepo.getUserByUsername(u.getUsername());
            com.mosis.treasurehunt.models.Location currLoc = user.getCurrentLocation();
            Log.i("User", user.getUsername());
            Log.i("Location", Double.toString(measure(lat, currLoc.getLatitude(), lon, currLoc.getLongitude())));
            if (measure(lat, currLoc.getLatitude(), lon, currLoc.getLongitude()) < dist) {
                createFoundObjectNotification();
            }
        }

        for (Hunt hunt : mUser.getJoinedHunts()) {
            Clue clue = hunt.getUnansweredClue();
            if(measure(lat, clue.getLatitude(), lon, clue.getLongitude()) < dist) {
                createFoundObjectNotification();
            }
        }

    }

    private void createFoundObjectNotification() {
        NotificationManager manager = getSystemService(NotificationManager.class);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Object found")
                .setContentText("Friend or clue is nearby")
                .setSmallIcon(R.drawable.show_friends_icon)
                .build();

        manager.notify(2, notification);
    }

    private double measure(double lat1, double lat2, double lon1, double lon2) {
        final double R = 6378.137;
        double dLat = Math.abs(lat1 * Math.PI / 180 - lat2 * Math.PI / 180);
        double dLon = Math.abs(lon1 * Math.PI / 180 - lon2 * Math.PI / 180);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        return d * 1000;
    }

}
