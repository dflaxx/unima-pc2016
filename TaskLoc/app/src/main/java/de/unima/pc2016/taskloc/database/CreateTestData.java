package de.unima.pc2016.taskloc.database;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by sven on 14.04.16.
 */
public class CreateTestData {
    private DataSource ds;
    public CreateTestData(Context context){
        this.ds = new DataSource(context);
        Date d1 = Calendar.getInstance().getTime();
        Date d2 = Calendar.getInstance().getTime();
        ds.createNewTask("Shopping", "Go to SM", d1, d2, 10);
        //ds.createNewTask("Coffee with Daisy", "I like Coffee so much, best drug ever", d1, d2, 10);
        //ds.createNewTask("Group Meeting", "Go and meet my group", d1, d2, 10);
    }
}
