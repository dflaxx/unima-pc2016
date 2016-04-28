package de.unima.pc2016.taskloc.application.database;

import android.location.Location;

/**
 * Created by sven on 14.04.16.
 */
public class LocationDataObject {
    private Location location;
    private int id;
    private String name;

    public LocationDataObject() {
        this.location = new Location("");
        id = -1; //Default value
    }

    public LocationDataObject(int id, String name, double latitude, double longitude) {
        this();
        this.setLocation(id, name, latitude, longitude);
    }

    @Override
    public String toString() {
        return "[#" + this.id + "] " + this.name + " " + "(" + this.getLatitude() + ";" + this.getLongitude() + ")";
    }

    public void setLocation(int id, String name, double latitude, double longitude) {
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);
        this.id = id;
        this.name = name;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public Location geLocation(){
       return this.location;
    }
    public Double getLatitude(){
        return location.getLatitude();
    }
    public Double getLongitude(){
        return location.getLongitude();
    }
    public String getName(){
        return this.name;
    }
    public int getId(){
        return id;
    }
    public LocationDataObject getElement(){
        return this;
    }

}
