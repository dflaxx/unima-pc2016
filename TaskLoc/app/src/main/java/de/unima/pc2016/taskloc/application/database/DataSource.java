package de.unima.pc2016.taskloc.application.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.gcm.Task;

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

    private static DataSource ds;
    public DataSource(Context context) {
        this.context = context;
        this.dbHelper = new DBHelper(this.context);
    }

    public static DataSource instance(Context context){
        if(DataSource.ds == null){
            DataSource.ds = new DataSource(context);
        }
        return DataSource.ds;
    }

    private SQLiteDatabase getWritableDB(){
        return dbHelper.getWritableDatabase();
    }

    private SQLiteDatabase getReadableDB(){
        return dbHelper.getReadableDatabase();
    }


    /*
     * Task-Queries
     */


    public int createNewTask(String title, String description, String startDate, String endDate, int range){
        String createNewTask = "Insert INTO " +DBHelper.TASK_TABLE_NAME+" ("+

                DBHelper.TASK_COLUMN_TITLE+", "+
                DBHelper.TASK_COLUMN_DESCRIPTION+", "+
                DBHelper.TASK_COLUMN_START_DATE+", "+
                DBHelper.TASK_COLUMN_END_DATE+", "+

                DBHelper.TASK_COLUMN_RANGE+") VALUES(?,?,?,?,?)";
        SQLiteStatement stmt = this.getWritableDB().compileStatement(createNewTask);
        stmt.bindString(1, ""+title); //Double quote to cater for nulls
        stmt.bindString(2, ""+description);
        stmt.bindString(3, ""+startDate);
        stmt.bindString(4, ""+endDate);
        stmt.bindLong(5, range);
        stmt.execute();

        Cursor c = this.getWritableDB().rawQuery("select last_insert_rowid()", null);
        return Integer.parseInt(c.getString(0));


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
    }




    public void deleteTask(int id){
        String deleteTask = "DELETE FROM "+DBHelper.TASK_TABLE_NAME+" WHERE "+
                DBHelper.TASK_COLUMN_TASK_ID+"="+Integer.toString(id)+";";
        this.getWritableDB().execSQL(deleteTask);
        Log.d(TAG, "Task was deleted");
    }

    public List<TaskDataObject> getTaskByID(int id){
        String selectTask = "Select * from "+ DBHelper.TASK_TABLE_NAME+" WHERE"+
                DBHelper.TASK_COLUMN_TASK_ID+"="+id+";";
        return new ArrayList<TaskDataObject>();
    }

    public List<TaskDataObject> getAllTask(){
        String selectAll = "Select * from "+ DBHelper.TASK_TABLE_NAME+";";

        List<TaskDataObject> currentTaskList = null;
        Cursor cursor = this.getReadableDB().rawQuery(selectAll, null);
        if(cursor != null && cursor.getCount() > 0){
            currentTaskList = this.createTaskOjbect(cursor);
            return currentTaskList;
        }
        cursor.close();
        return currentTaskList;
    }

    public List<TaskDataObject> getAllTaskWithLocation(){
        String getTaskWithLocation = "SELECT * FROM (("+DBHelper.TASK_TABLE_NAME+
                " AS A JOIN "+ DBHelper.HASPLACE_TABEL_NAME+
                " AS B ON A."+ DBHelper.TASK_COLUMN_TASK_ID +" = B."+DBHelper.HASPLACE_COLUMN_TASK_ID+") AS TMP JOIN "+
                DBHelper.LOCATION_TABLE_NAME+ " AS C ON C."+ DBHelper.HASPLACE_COLUMN_LOCATION_ID+" = TMP."+DBHelper.LOCATION_COLUMN_ID
                +") ORDER BY TMP."+DBHelper.TASK_COLUMN_TASK_ID+" ASC;";
        List<TaskDataObject> currentTaskList = null;
        Cursor cursor = this.getReadableDB().rawQuery(getTaskWithLocation, null);
        if(cursor != null && cursor.getCount() > 0){
            this.printJoin(cursor);
            currentTaskList = this.createTaskWithLocation(cursor);
            return currentTaskList;
        }

        cursor.close();
        return currentTaskList;
    }

    public List<TaskDataObject> getTaskByName(String name){
        return new ArrayList<TaskDataObject>();
    }


    public void clearAllTasks(){
        String deleteAll = "DELETE FROM "+DBHelper.TASK_TABLE_NAME+";";
        this.getWritableDB().execSQL(deleteAll);

    }
    public void deleteTaskByID(int id){
        String deleteByID = "DELETE FROM "+ DBHelper.TASK_TABLE_NAME+
                "WHERE "+DBHelper.TASK_COLUMN_TASK_ID+ " = "+id+";";
        this.getWritableDB().execSQL(deleteByID);
    }


    /*
     *
     *Location-Queries
     *
     */
    public List<LocationDataObject> getAllLocation(){
        String getLoc = "Select *  from "+DBHelper.LOCATION_TABLE_NAME+";";

        Cursor cursor = this.getReadableDB().rawQuery(getLoc,null);

        if(cursor != null && cursor.getCount()>0){
            return this.creatLocationObjects(cursor);
        }else{
            return new ArrayList<LocationDataObject>(); //return empty list
        }

    }
    public void createLocation(String locationName, String latitude, String longitude){
        String createNewTask = "INSERT INTO "+DBHelper.LOCATION_TABLE_NAME+" ("+
                DBHelper.LOCATION_COLUMN_ID+", "+
                DBHelper.LOCATION_COLUMN_NAME+", "+
                DBHelper.LOCATION_COLUMN_LATITUDE+", "+
                DBHelper.LOCATION_COLUMN_LONGITUDE+") VALUES ("+
                "NULL, '"+
                locationName +"', '"+
                latitude+"', '"+
                longitude+"');";
        this.getWritableDB().execSQL(createNewTask);

    }
    public void createLocation(String locationName, Location location){
        String createNewTask = "INSERT INTO "+DBHelper.LOCATION_TABLE_NAME+" ("+
                DBHelper.LOCATION_COLUMN_ID+", "+
                DBHelper.LOCATION_COLUMN_NAME+", "+
                DBHelper.LOCATION_COLUMN_LATITUDE+", "+
                DBHelper.LOCATION_COLUMN_LONGITUDE+") VALUES ("+
                "NULL, '"+
                locationName +"', '"+
                location.getLatitude()+"', '"+
                location.getLongitude()+"');";
        this.getWritableDB().execSQL(createNewTask);

    }
    public void deleteLocation(int locationID){
        String deleteLoc = "DELETE FROM "+DBHelper.LOCATION_TABLE_NAME+
                " WHERE "+DBHelper.LOCATION_COLUMN_ID+" = "+locationID+";";
        this.getWritableDB().execSQL(deleteLoc);

    }
    public void clearAllLocations(){
        String deleteAllLocation = "DELETE FROM "+ DBHelper.LOCATION_TABLE_NAME+";";
        this.getWritableDB().execSQL(deleteAllLocation);

    }

    /*
        HAS_PLACE TABLE
     */
    public void connectLocationWithPlace(int taskID, List<LocationDataObject> locations){
        for(LocationDataObject tmpLocation : locations){
            String con = "INSERT INTO "+DBHelper.HASPLACE_TABEL_NAME+" ("+
                    DBHelper.HASPLACE_COLUMN_TASK_ID+", "+
                    DBHelper.HASPLACE_COLUMN_LOCATION_ID+") VALUES ("+
                    taskID+", "+
                    tmpLocation.getId()+");";
            this.getWritableDB().execSQL(con);
            //TODO: Das Muss später auskommentiert werden
            this.printHasPlace();

        }

    }

    /*
    Create Task Objects
     */
    private List<TaskDataObject> createTaskOjbect(Cursor c){
        List<TaskDataObject> taskList = new ArrayList<TaskDataObject>();
        //Log.d(TAG, "Start creating Task Objects");
        if(c!=null){
            c.moveToFirst();
            do{
                String[] attributes = new String[c.getColumnCount()];
                String test = "";
                for(int i=0; i<c.getColumnCount(); i++){
                    attributes[i] = c.getString(i);
                    test = test + " "+ attributes[i] + " ";
                }
                //Log.d(TAG, "Join: "+ test);
                test = "";
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
    private List<LocationDataObject> creatLocationObjects(Cursor c){
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
                        attributes[1],
                        Double.parseDouble(attributes[2]),
                        Double.parseDouble(attributes[3]));
                locationList.add(tmp);
                tmp = null;
            }while(c.moveToNext());

        }
        c.close();
        return locationList;
    }

    private List<TaskDataObject> createTaskWithLocation(Cursor c){
        List<TaskDataObject> taskList = new ArrayList<TaskDataObject>();
        if(c!=null){
            c.moveToFirst();
            do{
                String[] attributes = new String[c.getColumnCount()];
                String test = "";
                for(int i=0; i<c.getColumnCount(); i++){
                    attributes[i] = c.getString(i);
                    test = test + " "+ attributes[i] + " ";
                }
                String taskId = attributes[0]; //At this position the id is saved
                List<LocationDataObject> locPerTask = new ArrayList<LocationDataObject>();

                LocationDataObject ldo = new LocationDataObject();
                ldo.setLocation(Integer.parseInt(c.getString(8)),c.getString(9),
                        Double.parseDouble(c.getString(10)), Double.parseDouble(c.getString(11)));
                locPerTask.add(ldo);

                while(c.moveToNext()){
                    if(!c.getString(0).equals(taskId)){ //At this position the id is saved
                        break;
                    }else{
                        LocationDataObject locationDataObject = new LocationDataObject();
                        locationDataObject.setLocation(Integer.parseInt(c.getString(8)),c.getString(9),
                                Double.parseDouble(c.getString(10)),Double.parseDouble(c.getString(11)));
                        locPerTask.add(locationDataObject);
                    }
                }
                c.moveToPrevious();

                //Log.d(TAG, "Output: "+ test);
                test = "";
                TaskDataObject tmp = new TaskDataObject(attributes, locPerTask);
                taskList.add(tmp);
                tmp = null;
            }while(c.moveToNext());
        }
        c.close();
        return taskList;

    }

    private void printHasPlace(){
        String sql = "SELECT * FROM "+DBHelper.HASPLACE_TABEL_NAME+";";
        Cursor c = this.getReadableDB().rawQuery(sql,null);
        if(c != null){
            c.moveToFirst();
            do{
                String[] attributes = new String[c.getColumnCount()];
                String test = "";
                for(int i=0; i<c.getColumnCount(); i++){
                    attributes[i] = c.getString(i);
                    test = test + " "+ attributes[i] + " ";
                }
                //Log.d(TAG, "Print: "+ test);
                test = "";
            }while(c.moveToNext());
        }
    }

    private void printJoin(Cursor c){
        //Cursor c = this.getReadableDB().rawQuery(sql,null);
        if(c != null){
            c.moveToFirst();
            do{
                String[] attributes = new String[c.getColumnCount()];
                String test = "";
                for(int i=0; i<c.getColumnCount(); i++){
                    attributes[i] = c.getString(i);
                    test = test + " "+ attributes[i] + " ";
                }
                //Log.d(TAG, "Tasktable: "+ test);
                test = "";
            }while(c.moveToNext());
        }
    }


}
