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
    private Date startDate;
    private Date endDate;
    private int range;
    private List<Location> locations;
    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");

    private String TAG = "TaskDataObject";

    public TaskDataObject(){

    }
    public TaskDataObject(int id,String title, String description, String startDate, String endDate, int range) {
        this.setId(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setRange(range);
    }
    public TaskDataObject(int id,String title, String description, String startDate, String endDate, int range, Location location){
        this.setId(id);
        this.setTitle(title);
        this.setDescription(description);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setRange(range);
    }

    public TaskDataObject(String[] attributes){
        this.setId(Integer.parseInt(attributes[0]));
        this.setTitle(attributes[1]);
        this.setDescription(attributes[2]);
        this.setStartDate(attributes[3]);
        this.setEndDate(attributes[4]);
        this.setRange(Integer.parseInt(attributes[5]));
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
    public String toString(){
        return this.getTitle()+" "+this.getDescription()+" ";
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setStartDate(String startDate) {
        Calendar cal = Calendar.getInstance();
        //cal.setTime(sdf.parse(startDate));
        this.startDate = cal.getTime();
        Log.d(TAG, startDate.toString());
        Log.d(TAG, sdf.toPattern());
    }

    public void setEndDate(String endDate) {
        Calendar cal = Calendar.getInstance();
        //cal.setTime(sdf.parse(endDate));
        this.endDate = cal.getTime();
    }
}
