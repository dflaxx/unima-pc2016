package de.unima.pc2016.taskloc.database;

import android.location.Location;

/**
 * Created by sven on 14.04.16.
 */
public class LocationDataObject {
    private Location location;

    public void setLocation(double latitude, double longitude){
        this.location = new Location("");
        this.location.setLatitude(latitude);
        this.location.setLongitude(longitude);
    }
    public Location geLocation(){
       return this.location;
    }

}
