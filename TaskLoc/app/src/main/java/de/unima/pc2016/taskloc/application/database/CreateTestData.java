package de.unima.pc2016.taskloc.application.database;

import android.content.Context;
import android.location.Location;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by sven on 14.04.16.
 */
public class CreateTestData {
    private DataSource ds;
    private final String Tag = "TaskDataObject";

    public CreateTestData(Context context){
        this.ds = new DataSource(context);
        this.createTaskData();
        this.creatLocationData();
        this.createConnection();
    }

    public boolean createTaskData(){
        ds.clearAllTasks();
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date d1 =cal.getTime();
        Date d2 = cal.getTime();

        ds.createNewTask("Grocery Shopping", "Description: 1", d1, d2, 10);
        ds.createNewTask("Coffee with Daisy", "Description: 2", d1, d2, 10);
        ds.createNewTask("Group Meeting for PC", "Description: 3", d1, d2, 10);
        return true;

    }

    public boolean creatLocationData(){
        ds.clearAllLocations();
        Location tmpLoc1 = new Location("");
        tmpLoc1.setLatitude(32.122098);
        tmpLoc1.setLongitude(34.795655);

        Location tmpLoc2 = new Location("");
        tmpLoc2.setLatitude(49.487442);
        tmpLoc2.setLongitude(8.466043);

        Location tmpLoc3 = new Location("");
        tmpLoc3.setLatitude(49.473976);
        tmpLoc3.setLongitude(8.607746);
        ds.createLocation(tmpLoc1);
        ds.createLocation(tmpLoc2);
        ds.createLocation(tmpLoc3);
        return true;
    }

    public boolean createConnection(){
        List<TaskDataObject> taskList = ds.getAllTask();
        List<LocationDataObject> locationList = ds.getAllLocation();
        for(TaskDataObject currTask : taskList){
           ds.connectLocationWithPlace(locationList, currTask.getId());
        }
        return true;
    }

}
