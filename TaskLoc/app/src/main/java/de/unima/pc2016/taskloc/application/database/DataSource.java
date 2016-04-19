package de.unima.pc2016.taskloc.application.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by sven on 14.04.16.
 */
public class DataSource {
    private final String TAG ="DataSource";
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private Context context;

    public DataSource(Context context) {
        Log.d(TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
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
    public void getUserByID(int id){
        String getByID = "Select from "+DBHelper.USER_TABLE_NAME+
                " WHERE "+DBHelper.USER_COLUMN_USER_ID+" = "+id+";";
    }
    public void createNewUser(String name, String surname, int age, String streetName, String livingPlace){
        String createUser = "Insert INTO ("+DBHelper.TASK_TABLE_NAME+", "+
                DBHelper.USER_COLUMN_USER_ID+", "+
                DBHelper.USER_COLUMN_USER_NAME+", "+
                DBHelper.USER_COLUMN_USER_SURNAME+", "+
                DBHelper.USER_COLUMN_USER_AGE+", "+
                DBHelper.USER_COLUMN_STREET+", "+
                DBHelper.USER_COLUMN_LIVING_PLACE+") VALUES("+
                "NULL, '"+
                name+", '"+
                surname+"', "+
                age+"', "+
                streetName+", '"+
                livingPlace+"');";
        this.getWritableDB().execSQL(createUser);
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
        String createNewTask = "INSERT INTO "+DBHelper.TASK_TABLE_NAME+" ("+
                DBHelper.TASK_COLUMN_TASK_ID+", "+
                DBHelper.TASK_COLUMN_TITLE+", "+
                DBHelper.TASK_COLUMN_DESCRIPTION+", "+
                DBHelper.TASK_COLUMN_START_DATE+", "+
                DBHelper.TASK_COLUMN_END_DATE+", "+
                DBHelper.TASK_COLUMN_RANGE+") VALUES ("+
                "NULL, '"+
                title+"', '"+
                description+"', '"+
                startTime.toString()+"', '"+
                endTime.toString()+"', "+
                range+");";
         this.getWritableDB().execSQL(createNewTask);
        Log.d(TAG, "New Task: " + title + " was created");
    }


    public void deleteTask(int id){
        String deleteTask = "DELETE FROM "+DBHelper.TASK_TABLE_NAME+" WHERE "+
                DBHelper.TASK_COLUMN_TASK_ID+"="+Integer.toString(id)+";";
        this.getWritableDB().execSQL(deleteTask);
    }

    public List<TaskDataObject> getTaskByID(int id){
        String selectTask = "Select * from "+ DBHelper.TASK_TABLE_NAME+" WHERE"+
                DBHelper.TASK_COLUMN_TASK_ID+"="+id+";";
        return null;
    }

    public List<TaskDataObject> getAllTask(){
        String selectAll = "Select * from "+ DBHelper.TASK_TABLE_NAME+";";
        List<TaskDataObject> currentTaskList = null;
        Cursor cursor = this.getReadableDB().rawQuery(selectAll, null);
        if(cursor != null && cursor.getCount() > 0){
            Log.d(TAG, cursor.getCount()+ " Tasks have been found.");
            currentTaskList = this.createTaskOjbect(cursor);
            return currentTaskList;
        }
        Log.d(TAG, cursor.getCount()+ " Tasks have been found.");
        cursor.close();
        return currentTaskList;
    }
    public List<TaskDataObject> getTaskByName(String name){
        return null;
    }


    public void clearAllTasks(){
        String deleteAll = "DELETE FROM "+DBHelper.TASK_TABLE_NAME+";";
        this.getWritableDB().execSQL(deleteAll);
        Log.d(TAG, "Deleted all old Tasks");
    }
    public void deleteTaskByID(int id){
        String deleteByID = "DELETE FROM "+ DBHelper.TASK_TABLE_NAME+
                "WHERE "+DBHelper.TASK_COLUMN_TASK_ID+ " = "+id+";";
        this.getWritableDB().execSQL(deleteByID);
        Log.d(TAG, "Deleted all old Tasks");
    }


    /*
     *
     *
     *
     *
     *Location-Queries
     *
     *
     *
     *
     */
    public List<LocationDataObject> getAllLocation(){
        String getLoc = "Select *  from "+DBHelper.LOCATION_TABLE_NAME+";";
        Cursor cursor = this.getReadableDB().rawQuery(getLoc,null);

        if(cursor != null && cursor.getCount()>0){
            return this.creatLocationObjects(cursor);
        }else{
            return null;
        }

    }
    public TaskDataObject getLocationByName(String name){
        return null;
    }
    public void createLocation(String name, String latitude, String longitude){
        String createNewTask = "INSERT INTO "+DBHelper.LOCATION_TABLE_NAME+" ("+
                DBHelper.LOCATION_COLUMN_ID+", "+
                DBHelper.LOCATION_COLUMN_LATITUDE+", "+
                DBHelper.LOCATION_COLUMN_LONGITUDE+") VALUES ("+
                "NULL, '"+
                latitude+"', '"+
                longitude+");";
        this.getWritableDB().execSQL(createNewTask);
        Log.d(TAG, "New Location was added to the database");
    }
    public void createLocation(Location location){
        String createNewTask = "INSERT INTO "+DBHelper.LOCATION_TABLE_NAME+" ("+
                DBHelper.LOCATION_COLUMN_ID+", "+
                DBHelper.LOCATION_COLUMN_LATITUDE+", "+
                DBHelper.LOCATION_COLUMN_LONGITUDE+") VALUES ("+
                "NULL, '"+
                location.getLatitude()+"', '"+
                location.getLongitude()+"');";
        this.getWritableDB().execSQL(createNewTask);
        Log.d(TAG, "New Location was added to the database");
    }
    public void deleteLocation(int locationID){
        String deleteLoc = "DELETE FROM "+DBHelper.LOCATION_TABLE_NAME+
                " WHERE "+DBHelper.LOCATION_COLUMN_ID+" = "+locationID+";";
        this.getWritableDB().execSQL(deleteLoc);
        Log.d(TAG, "Deleted location "+ locationID);
    }
    public void clearAllLocations(){
        String deleteAllLocation = "DELETE FROM "+ DBHelper.LOCATION_TABLE_NAME+";";
        this.getWritableDB().execSQL(deleteAllLocation);
        Log.d(TAG, "Deleted all old Locations");
    }

    /*
        HAS_PLACE TABLE
     */
    public void connectLocationWithPlace(int taskID,List<Integer> locations){

        for(Integer tmpLocation : locations){
            String con = "INSERT INTO "+DBHelper.HASPLACE_TABEL_NAME+" ("+
                    DBHelper.HASPLACE_COLUMN_TASK_ID+", "+
                    DBHelper.TASK_COLUMN_RANGE+") VALUES ("+
                    tmpLocation+", "+
                    taskID+");";
            this.getWritableDB().execSQL(con);

        }
        Log.d(TAG, "Task was connected to "+locations.size());
    }

    public void connectLocationWithPlace(List<LocationDataObject> locations, int taskID){

        for(LocationDataObject tmpLocation : locations){
            String con = "INSERT INTO "+DBHelper.HASPLACE_TABEL_NAME+" ("+
                    DBHelper.HASPLACE_COLUMN_TASK_ID+", "+
                    DBHelper.HASPLACE_COLUMN_LOCATION_ID+") VALUES ("+
                    tmpLocation.getId()+", "+
                    taskID+");";
            this.getWritableDB().execSQL(con);

        }
        Log.d(TAG, "Task was connected to "+locations.size());
    }

    /*
    Create Task Objects
     */
    public List<TaskDataObject> createTaskOjbect(Cursor c){
        List<TaskDataObject> taskList = new ArrayList<TaskDataObject>();
        if(c!=null){
            c.moveToFirst();
            do{
                String[] attributes = new String[c.getColumnCount()];
                for(int i=0; i<c.getColumnCount(); i++){
                    attributes[i] = c.getString(i);
                }
                TaskDataObject tmp = new TaskDataObject(attributes);
                taskList.add(tmp);
                tmp = null;
            }while(c.moveToNext());

        }
        c.close();
        return taskList;
    }
    /*
       Create Location Object
     */
    public List<LocationDataObject> creatLocationObjects(Cursor c){
        List<LocationDataObject> locationList = new ArrayList<LocationDataObject>();
        if(c!=null){
            c.moveToFirst();
            do{
                String[] attributes = new String[c.getColumnCount()];
                for(int i=0; i<c.getColumnCount(); i++){ //ID does not need to be saved in Location Object --> Start from one
                    attributes[i] = c.getString(i);
                }
                LocationDataObject tmp = new LocationDataObject();
                tmp.setLocation(
                        Integer.parseInt(attributes[0]),
                        Double.parseDouble(attributes[1]),
                        Double.parseDouble(attributes[2]));
                locationList.add(tmp);
                tmp = null;
            }while(c.moveToNext());

        }
        c.close();
        return locationList;
    }
}
