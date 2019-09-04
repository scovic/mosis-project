package com.mosis.treasurehunt.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.models.Clue;
import com.mosis.treasurehunt.models.Hunt;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends AppCompatActivity {
    MapView map = null;
    Clue clue = null;
    IMapController mapController = null;
    MyLocationNewOverlay myLocationOverlay;
    ItemizedIconOverlay itemizedIconOverlay;
    FusedLocationProviderClient fusedLocationClient;
    User user;
    Timer timer;

    static final int PERMISSION_ACCESS_FINE_LOCATION = 1;

    public static final int SHOW_MAP = 0;
    public static final int CENTER_CLUE_ON_MAP = 1;
    public static final int SELECT_COORDINATES = 2;
    public static final int SELECT_CURRENT_COORDS = 4;
    public static final int SHOW_FRIENDS = 5;
    public static final int SHOW_CLUES = 6;

    public static final String CLUE_TITLE = "hunt_title";

    private int state = 0;
    private boolean selCoordsEnabled = false;
    private GeoPoint clueLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = UserRepository.getInstance().getUserByUsername(SharedPreferencesWrapper.getInstance().getUsername());
        timer = new Timer();
        try {
            Intent mapIntent = getIntent();
            Bundle mapBundle = mapIntent.getExtras();
            if (mapBundle != null) {
                state = mapBundle.getInt("state");
                if (state == CENTER_CLUE_ON_MAP) {
                    String clueLat = mapBundle.getString("lat");
                    String clueLong = mapBundle.getString("lon");
                    String desc = mapBundle.getString("desc");
                    String name = mapBundle.getString("name");
                    this.clue = new Clue();
                    this.clue.setLatitude(Double.parseDouble(clueLat));
                    this.clue.setLongitude(Double.parseDouble(clueLong));
//                    this.place = new MyPlace(name, desc);
//                    this.place.setLatitude(placeLat);
//                    this.place.setLongitude(placeLong);
                    clueLoc = new GeoPoint(Double.parseDouble(clueLat), Double.parseDouble(clueLong));
                } else if (state == SELECT_CURRENT_COORDS) {
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        double lat = location.getLatitude();
                                        double lon = location.getLongitude();
                                        Intent myLocationIntent = new Intent();
                                        myLocationIntent.putExtra("lat", Double.toString(lat));
                                        myLocationIntent.putExtra("lon", Double.toString(lon));
                                        setResult(Activity.RESULT_OK, myLocationIntent);

                                    }
                                    finish();
                                }
                            });
                }
            }
        } catch(Exception e) {
            Log.d("Error", "Error reading state");
        }

        setContentView(R.layout.activity_maps);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Map");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        map = findViewById(R.id.map);

        map.setMultiTouchControls(true);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // if no permissions, request them
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_ACCESS_FINE_LOCATION);
        } else {
            setupMap();
            setMyLocationOverlay();
            setOnMapClickOverlay();

        }

        mapController = map.getController();
        if (mapController != null) {
            mapController.setZoom(15.0);
            GeoPoint startPoint = new GeoPoint(43.3209, 21.8958);
            mapController.setCenter(startPoint);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.purge();
            timer.cancel();
        }

    }

    private void setMyLocationOverlay() {
        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(MapsActivity.this), map);
        myLocationOverlay.enableMyLocation();
        map.getOverlays().add(myLocationOverlay);
        mapController = map.getController();

        if (mapController != null) {
            mapController.setZoom(15.0);
            myLocationOverlay.enableFollowLocation();
        }
    }

    private void setOnMapClickOverlay() {
        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                if (state == SELECT_COORDINATES && selCoordsEnabled) {
                    String lat = Double.toString(p.getLatitude());
                    String lon = Double.toString(p.getLongitude());

                    Intent locationIntent = new Intent();
                    locationIntent.putExtra("lat", lat);
                    locationIntent.putExtra("lon", lon);
                    setResult(Activity.RESULT_OK, locationIntent);
                    finish();
                }
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        MapEventsOverlay OverlayEvents = new MapEventsOverlay(mReceive);
        map.getOverlays().add(OverlayEvents);
    }

    private void setupMap() {
        if (state != SHOW_FRIENDS && timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }

        switch(state) {
            case SHOW_MAP: {
                setMyLocationOverlay();
                break;
            }
            case SELECT_COORDINATES: {
                mapController = map.getController();
                if (mapController != null) {
                    mapController.setZoom(15.0);
                    mapController.setCenter(new GeoPoint(43.3209, 21.8958));
                }
                setMyLocationOverlay();
                break;
            }
//            case CENTER_CLUE_ON_MAP: {
////                showPlace();
//                break;
//            }
            case SHOW_FRIENDS: {
                timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        showFriends();
                    }
                }, 0, 5000);

                break;
            }
            case SHOW_CLUES: {
//                setMyLocationOverlay();
                showClues();
                break;
            }
            default: {
//                setCenterPlaceOnMap();
                break;
            }
        }
    }

    private void showClueLocation() {
        if (myLocationOverlay != null) {
            this.map.getOverlays().remove(myLocationOverlay);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (state == SELECT_COORDINATES && !selCoordsEnabled) {
            menu.add(0,1,1,"Select Coordinates");
            menu.add(0,2,2, "Cancel");

            return super.onCreateOptionsMenu(menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_my_maps, menu);
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (state == SELECT_COORDINATES && !selCoordsEnabled) {
            if (id == 1) {
                selCoordsEnabled = true;
                Toast.makeText(this, "Select coordinates", Toast.LENGTH_SHORT).show();
            } else { // cancel
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        } else {
            if (id == R.id.new_hunt_item) {
                Intent i = new Intent(this, NewHuntActivity.class);
                startActivity(i);
            } else if (id == R.id.show_friends_on_map) {
                this.state = SHOW_FRIENDS;
                this.setupMap();
            } else if (id == R.id.show_clues_on_map) {
                this.state = SHOW_CLUES;
                setupMap();
            } else if (id == android.R.id.home) {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    public void showFriends() {
        if (myLocationOverlay != null) {
            this.map.getOverlays().remove(myLocationOverlay);
        }

        UserRepository.getInstance().update(user);
        user = UserRepository.getInstance().getUserByUsername(SharedPreferencesWrapper.getInstance().getUsername());

        final List<OverlayItem> items = new ArrayList<>();
        for (User u : user.getFriendList()) {
            User us = UserRepository.getInstance().getUserByUsername(u.getUsername());
            com.mosis.treasurehunt.models.Location location = us.getCurrentLocation();
            OverlayItem item  = new OverlayItem(us.getUsername(), us.getFullName(), new GeoPoint(location.getLatitude(), location.getLongitude()));
            item.setMarker(this.getResources().getDrawable(R.drawable.icon_user_map));
            items.add(item);
        }

        if (itemizedIconOverlay != null) {
            this.map.getOverlays().remove(itemizedIconOverlay);
        }

        itemizedIconOverlay = new ItemizedIconOverlay(items,
                new ItemizedIconOverlay.OnItemGestureListener() {
                    @Override
                    public boolean onItemSingleTapUp(int index, Object item) {
                        OverlayItem overlayItem = (OverlayItem) item;
                        String username = overlayItem.getTitle();
                        Intent i = new Intent(MapsActivity.this, UserProfileActivity.class);
                        i.putExtra("state", username);
                        startActivity(i);
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, Object item) {
                        return false;
                    }
                }, getApplicationContext());
        this.map.getOverlays().add(itemizedIconOverlay);
    }

    public void showClues() {
        if (myLocationOverlay != null) {
            this.map.getOverlays().remove(myLocationOverlay);
        }


        final List<OverlayItem> items = new ArrayList<>();
        for (Hunt hunt : user.getJoinedHunts()) {
            User huntOwner = UserRepository.getInstance().getUserByUsername(hunt.getOwner());
            boolean isCompleted = hunt.isCompleted() || huntOwner.isHuntCompleted(hunt.getTitle());
            if (!isCompleted) {
                Clue clue = hunt.findFirstUnansweredClue();
                OverlayItem item = new OverlayItem(hunt.getTitle(), hunt.getOwner(), new GeoPoint(clue.getLatitude(), clue.getLongitude()));
                item.setMarker(this.getResources().getDrawable(R.drawable.icon_clue));
                items.add(item);
            }
        }

        if (itemizedIconOverlay != null) {
            this.map.getOverlays().remove(itemizedIconOverlay);
        }

        itemizedIconOverlay = new ItemizedIconOverlay(items,
                new ItemizedIconOverlay.OnItemGestureListener() {
                    @Override
                    public boolean onItemSingleTapUp(int index, Object item) {
                        OverlayItem o = (OverlayItem) item;
                        GeoPoint gp = (GeoPoint) o.getPoint();
                        GeoPoint lastKnownLocation = myLocationOverlay.getMyLocation();
                        if (lastKnownLocation != null) {
                            double distance = measure(gp.getLatitude(), lastKnownLocation.getLatitude(), gp.getLongitude(), lastKnownLocation.getLongitude());
                            if (distance > 100) {
                                Toast.makeText(MapsActivity.this, "You are too far away from clue", Toast.LENGTH_SHORT).show();
                            } else {
                               String huntTitle = o.getTitle();
                               String huntOwnerUsername = o.getSnippet();
                               if (!UserRepository.getInstance().getUserByUsername(huntOwnerUsername).isHuntCompleted(huntTitle)) {
                                   Intent i = new Intent(MapsActivity.this, AnswerClueActivity.class);
                                   i.putExtra(CLUE_TITLE, huntTitle);
                                   startActivityForResult(i, 1);
                               } else {
                                   Toast.makeText(MapsActivity.this, "Sorry, someone has already completed that hunt", Toast.LENGTH_SHORT).show();
                               }
                           }
                        } else {
                            Toast.makeText(MapsActivity.this, "No last know location", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }

                    @Override
                    public boolean onItemLongPress(int index, Object item) {
                        return false;
                    }
                }, getApplicationContext());
        this.map.getOverlays().add(itemizedIconOverlay);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            UserRepository.getInstance().update();
            showClues();
        }
    }
}


