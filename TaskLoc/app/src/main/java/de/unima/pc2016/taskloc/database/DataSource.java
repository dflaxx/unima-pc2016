package de.unima.pc2016.taskloc.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.nfc.Tag;
import android.util.Log;

import java.util.Date;
import java.util.List;

/**
 * Created by sven on 14.04.16.
 */
public class DataSource {
    private final String LOG_TAG="DataSource";
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context context;





    public DataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        this.context = context;
        this.dbHelper = new DBHelper(this.context);
    }

    public SQLiteDatabase getWritableDB(){
        return dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getReadableDB(){
        return dbHelper.getReadableDatabase();
    }

    /*
     * USER-Queries
     */
    public List<UserTaskObject> getAllUser(){
        String getAllUser = "Select * from"+DBHelper.USER_TABLE_NAME;
        Cursor cursor =this.getReadableDB().rawQuery(getAllUser, null);
        return null;
    }
    public void getUserByID(int ID){

    }
    public void createNewUser(String name, String surname, int Age){

    }

    /*
     * Task-Queries
     */

    public void createNewTask(String title, String description, String startDate, String endDate, int range){
        String createNewTask = "Insert INTO ("+DBHelper.TASK_TABLE_NAME+", "+
                DBHelper.TASK_COLUMN_TITLE+", "+
                DBHelper.TASK_COLUMN_DESCRIPTION+", "+
                DBHelper.TASK_COLUMN_START_DATE+", "+
                DBHelper.TASK_COLUMN_END_DATE+", "+
                DBHelper.TASK_COLUMN_RANGE+") VALUES("+
                title+","+
                description+","+
                startDate+","+
                endDate+","+
                range+");";
        this.getWritableDB().execSQL(createNewTask);
    }
    public void createNewTask(String title, String description, Date startTime,Date endTime, int range){
        String createNewTask = "Insert INTO "+DBHelper.TASK_TABLE_NAME+" ("+
                DBHelper.TASK_COLUMN_TASK_ID+", "+
                DBHelper.TASK_COLUMN_TITLE+", "+
                DBHelper.TASK_COLUMN_DESCRIPTION+", "+
                DBHelper.TASK_COLUMN_START_DATE+", "+
                DBHelper.TASK_COLUMN_END_DATE+", "+
                DBHelper.TASK_COLUMN_RANGE+") VALUES ("+
                "null, "+
                title+","+
                description+","+
                startTime.toString()+","+
                endTime.toString()+","+
                range+");";
        this.getWritableDB().execSQL("Insert into task (taskID, title, description, startDate, endDate, range)" +
                " VALUES (NULL,'Shopping','Go', 'datum1', 'datum2', 10);");
         //this.getWritableDB().execSQL(createNewTask);
        Log.d(LOG_TAG,"New Task: "+ title+ " was created");
    }
    public void deleteTask(int id){
        String deleteTask = "DELETE * FROM"+DBHelper.TASK_TABLE_NAME+" WHERE"+
                DBHelper.TASK_COLUMN_TASK_ID+"="+Integer.toString(id)+";";
        this.getWritableDB().execSQL(deleteTask);
    }

    public List<TaskDataObject> getTaskByID(int id){
        String selectTask = "Select * from "+ DBHelper.TASK_TABLE_NAME+" WHERE"+
                DBHelper.TASK_COLUMN_TASK_ID+"="+id+";";
        return null;
    }
    public List<TaskDataObject> getTaskByName(String name){
        return null;
    }


    /*
     *Location-Queries
     */
    public void getAllLocation(){

    }
    public void getLocationByName(String name){

    }
    public void createLocation(String name, String latitude, String longitude){

    }
    public void createLocation(Location location){

    }
    public void deleteLocation(int locationID){

    }
}
