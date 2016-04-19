package de.unima.pc2016.taskloc.application.database;

import android.location.Location;

/**
 * Created by sven on 14.04.16.
 */
public class LocationDataObject {
    private Location location;
    private int id;

    public LocationDataObject(){
        this.location = new Location("");
        id = -1; //Default value
    }
    public void setLocation(int id, double latitude, double longitude){
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);
        this.id = id;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public Location geLocation(){
       return this.location;
    }

    public int getId(){
        return id;
    }

}
