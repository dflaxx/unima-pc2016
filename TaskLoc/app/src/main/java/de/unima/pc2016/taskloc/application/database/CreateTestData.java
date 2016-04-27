package de.unima.pc2016.taskloc.application.database;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
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
        this.ds = DataSource.instance(context);
        //this.createTaskData();
        //this.creatLocationData();
        //this.createConnection();
        ds.getAllTaskWithLocation();

    }

    public boolean createTaskData(){
        ds.clearAllTasks();
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date d1 =cal.getTime();
        Date d2 = cal.getTime();

        ds.createNewTask("Grocery Shopping", "Description: 1", d1, d2, 200);
        ds.createNewTask("Coffee with Daisy", "Description: 2", d1, d2, 200);
        ds.createNewTask("Group Meeting for PC", "Description: 3", d1, d2, 200);
        return true;

    }

    public boolean creatLocationData(){
        ds.clearAllLocations();
        Location tmpLoc1 = new Location("");
        tmpLoc1.setLatitude(35);
        tmpLoc1.setLongitude(139);

        Location tmpLoc2 = new Location("");
        tmpLoc2.setLatitude(22);
        tmpLoc2.setLongitude(43);

        Location tmpLoc3 = new Location("");
        tmpLoc3.setLatitude(49.473538);
        tmpLoc3.setLongitude(8.474838);

        //ds.createLocation(tmpLoc1);
        //ds.createLocation(tmpLoc2);
        //ds.createLocation(tmpLoc3);
        return true;
    }

    public boolean createConnection(){
        List<TaskDataObject> taskList = ds.getAllTask();
        List<LocationDataObject> locationList = ds.getAllLocation();
        List<LocationDataObject> tmp = new ArrayList<LocationDataObject>();

        ds.connectLocationWithPlace(taskList.get(0).getId(), locationList);
       /*int counter = 0;
        for(TaskDataObject currTask : taskList){
            tmp.add(locationList.get(0));
            ds.connectLocationWithPlace(tmp, currTask.getId());
            counter++;
        }*/
        return true;
    }

}
