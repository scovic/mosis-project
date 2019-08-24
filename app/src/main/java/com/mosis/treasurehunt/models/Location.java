package com.mosis.treasurehunt.models;

public class Location {
    private double longitude;
    private double latitude;

    public Location() {}

    public Location(double lat, double lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    public double getLongitude() { return this.longitude; }

    public double getLatitude() { return  this.latitude; }


    public void setLongitude(double lon) { this.longitude = lon; }

    public void setLatitude(double lat) { this.latitude = lat; }
}
