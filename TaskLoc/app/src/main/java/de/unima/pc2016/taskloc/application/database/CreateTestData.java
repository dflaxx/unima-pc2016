package de.unima.pc2016.taskloc.application.database;

import android.content.Context;

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
        this.getTaskData();

    }

    public Boolean createTaskData(){
        ds.clearAllTasks();
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date d1 =cal.getTime();
        Date d2 = cal.getTime();

        //ds.createNewTask("Shopping", "Go to SM", d1, d2, 10);
        //ds.createNewTask("Coffee with Daisy", "I like Coffee so much, best drug ever", d1, d2, 10);
        //ds.createNewTask("Group Meeting", "Go and meet my group", d1, d2, 10);
        return true;
    }

    public void getTaskData(){
        List<TaskDataObject> list = ds.getAllTask();
    }
}
