package com.mosis.treasurehunt.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.mosis.treasurehunt.repositories.UserRepository;

import java.util.Timer;
import java.util.TimerTask;


public class LocationTrackerService2 extends IntentService {

    private UserRepository mUserRepo;
    private Timer mTimer;

    public static final String ACTION_TRACK_USER_LOCATION = "track-user-location";

    public LocationTrackerService2() {
        super("LocationTrackerService2");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_TRACK_USER_LOCATION.equals(action)) {
                handleActionTrackUserLocation();
            }
        }
    }


    private void handleActionTrackUserLocation() {
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Log.i("Interval", "Well this is working fine!");
            }
        }, 0, 3000);

    }

}
