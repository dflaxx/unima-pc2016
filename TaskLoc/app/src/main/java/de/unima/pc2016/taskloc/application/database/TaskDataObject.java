package de.unima.pc2016.taskloc.application.database;

import android.location.Location;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by sven on 14.04.16.
 */
public class TaskDataObject {
    private int id;
    private String title;
    private String description;
    private String startDate;
    private String endDate;
    private int range;
    private List<LocationDataObject> locations;
    private Double distance;

    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

    private String TAG = "TaskDataObject";


    public TaskDataObject(String[] attributes, List<LocationDataObject> locations){
        this.setId(Integer.parseInt(attributes[0]));
        this.setTitle(attributes[1]);
        this.setDescription(attributes[2]);
        this.setStartDate(attributes[3]);
        this.setEndDate(attributes[4]);
        this.setRange(Integer.parseInt(attributes[5]));
        this.locations = locations;
        this.distance = 9999.0; //Distanz für Sortierung
    }

    public TaskDataObject(String[] attributes){
        this.setId(Integer.parseInt(attributes[0]));
        this.setTitle(attributes[1]);
        this.setDescription(attributes[2]);
        this.setStartDate(attributes[3]);
        this.setEndDate(attributes[4]);
        this.setRange(Integer.parseInt(attributes[5]));
        this.distance = 0.0; //Distanz für Sortierung

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


    public int getRange() {
        return range;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    } //Distanz für Sortierung

    public Double getDistance() {
        return distance;
    } //Distanz für Sortierung




    public List<LocationDataObject> getLocations(){
        return this.locations;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.getTitle());
        sb.append(" (");
        sb.append("#" + this.getId());
        sb.append(")");
        return sb.toString();
    }
}
