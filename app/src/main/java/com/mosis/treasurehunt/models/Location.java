package com.mosis.treasurehunt.models;

public class Location {
    private double mLongitude;
    private double mLatitude;

    public Location() { }

    public Location (double lat, double lon) {
        this.mLatitude = lat;
        this.mLongitude = lon;
    }

    public double getLongitude() { return this.mLongitude; }

    public double getLatitude() { return  this.mLatitude; }

    public void setLongitude(double lon) { this.mLongitude = lon; }

    public void setLatitude(double lat) { this.mLatitude = lat; }
}
