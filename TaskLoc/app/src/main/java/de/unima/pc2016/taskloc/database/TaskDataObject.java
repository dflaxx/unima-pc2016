package de.unima.pc2016.taskloc.database;

import android.location.Location;

import java.util.Calendar;
import java.util.List;

/**
 * Created by sven on 14.04.16.
 */
public class TaskDataObject {
    private String id;
    private String title;
    private String description;
    private Calendar calendar;
    private int range;
    private List<Location> locations;

    public TaskDataObject(){

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Calendar getCalendar() {
        return calendar;
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

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }
}
