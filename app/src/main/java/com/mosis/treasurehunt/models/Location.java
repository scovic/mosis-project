package com.mosis.treasurehunt.models;

public class Location {
    private double mLongitude;
    private double mLatitude;

    public Location (double lat, double lon) {
        this.mLatitude = lat;
        this.mLongitude = lon;
    }

    public double getLongitude() { return this.mLongitude; }

    public double getLatitude() { return  this.mLongitude; }

    public void setLongitude(double lon) { this.mLongitude = lon; }

    public void setLatitude(double lat) { this.mLatitude = lat; }
}
