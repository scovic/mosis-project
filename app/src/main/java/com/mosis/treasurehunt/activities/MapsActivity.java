package com.mosis.treasurehunt.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import com.google.common.collect.Maps;
import com.mosis.treasurehunt.R;
import com.mosis.treasurehunt.models.Clue;
import com.mosis.treasurehunt.models.User;
import com.mosis.treasurehunt.repositories.UserRepository;
import com.mosis.treasurehunt.wrappers.SharedPreferencesWrapper;

import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MapsActivity extends AppCompatActivity {
    MapView map = null;
    Clue clue = null;
//    UserRepository userrepo;
    IMapController mapController = null;
    MyLocationNewOverlay myLocationOverlay;
    FusedLocationProviderClient fusedLocationClient;

    static final int PERMISSION_ACCESS_FINE_LOCATION = 1;

    public static final int SHOW_MAP = 0;
    public static final int CENTER_PLACE_ON_MAP = 1;
    public static final int SELECT_COORDINATES = 2;
    public static final int SELECT_CURRENT_COORDS = 4;
    public static final int SHOW_FRIENDS = 5;

    private int state = 0;
    private boolean selCoordsEnabled = false;
    private GeoPoint clueLoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Intent mapIntent = getIntent();
            Bundle mapBundle = mapIntent.getExtras();
            if (mapBundle != null) {
                state = mapBundle.getInt("state");
                if (state == CENTER_PLACE_ON_MAP) {
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
        switch(state) {
            case SHOW_MAP: {
                setMyLocationOverlay();
//                showMyPlaces();
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
            case CENTER_PLACE_ON_MAP: {
//                showPlace();
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
            } else if (id == android.R.id.home) {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

//    private void showMyClues() {
//        if (myLocationOverlay != null) {
//            this.map.getOverlays().remove(myLocationOverlay);
//        }
//    }

}


