package de.unima.pc2016.taskloc.application.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by sven on 14.04.16.
 */
public class DBHelper  extends SQLiteOpenHelper{

    public static final String LOG_TAG=DBHelper.class.getSimpleName();

    public static final String DATABASE_NAME = "LocationTaskDB.db";
    public static final int DATABASE_VERSION = 1;

    public static final String USER_TABLE_NAME = "user";
    public static final String USER_COLUMN_USER_ID = "userID";
    public static final String USER_COLUMN_USER_NAME = "userName";
    public static final String USER_COLUMN_USER_SURNAME = "userSurname";
    public static final String USER_COLUMN_USER_AGE = "userAge";
    public static final String USER_COLUMN_LIVING_PLACE="livingPlace"; //Ort
    public static final String USER_COLUMN_STREET="street";


    public static final String TASK_TABLE_NAME = "task";
    public static final String TASK_COLUMN_TASK_ID = "taskID";
    public static final String TASK_COLUMN_TITLE = "title";
    public static final String TASK_COLUMN_DESCRIPTION = "description";
    public static final String TASK_COLUMN_START_TIME = "startTime";
    public static final String TASK_COLUMN_END_TIME = "endTime";
    public static final String TASK_COLUMN_START_DATE = "startDate";
    public static final String TASK_COLUMN_END_DATE = "endDate";
    public static final String TASK_COLUMN_RANGE="range";
    public static final String TASK_COLUMN_USER_ID="userID";

    public static final String LOCATION_TABLE_NAME = "location";
    public static final String LOCATION_COLUMN_ID = "locationID";
    public static final String LOCATION_COLUMN_NAME = "locationName";
    public static final String LOCATION_COLUMN_LATITUDE = "latitude";
    public static final String LOCATION_COLUMN_LONGITUDE = "longitude";


    public static final String HASPLACE_TABEL_NAME="hasplace";
    public static final String HASPLACE_COLUMN_TASK_ID="taskID";
    public static final String HASPLACE_COLUMN_LOCATION_ID="locationID";

    private SQLiteDatabase db;


    //SQL Create Table User
    public static final String SQL_CREATE_USER = "CREATE TABLE " + USER_TABLE_NAME +
            "(" + USER_COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_COLUMN_USER_NAME + " TEXT NOT NULL, " +
            USER_COLUMN_USER_SURNAME + " TEXT NOT NULL, " +
            USER_COLUMN_USER_AGE + " INTEGER NOT NULL, " +
            USER_COLUMN_STREET + " TEXT NOT NULL, " +
            USER_COLUMN_LIVING_PLACE + " TEXT NOT NULL);";

    //SQL Create Table Task
    public static final String SQL_CREATE_TASK = "CREATE TABLE " + TASK_TABLE_NAME +
            "(" + TASK_COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TASK_COLUMN_TITLE+" TEXT NOT NULL, "+
            TASK_COLUMN_DESCRIPTION+ " TEXT NOT NULL, " +
            TASK_COLUMN_START_DATE + " TEXT NOT NULL, " +
            TASK_COLUMN_END_DATE + " TEXT NOT NULL, " +
            TASK_COLUMN_RANGE + " INTEGER NOT NULL);";


    //Foreign Key Beziehung zu User ID
    //TASK_COLUMN_USER_ID + " TEXT NOT NULL, "+
    //"FOREIGN KEY("+TASK_COLUMN_USER_ID+") REFERENCES "+USER_TABLE_NAME +"("+USER_COLUMN_USER_ID+") );"

    public static final String SQL_CREATE_LOCATION = "CREATE TABLE " + LOCATION_TABLE_NAME +
            "(" + LOCATION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LOCATION_COLUMN_NAME + " TEXT NOT NULL, "+
            LOCATION_COLUMN_LATITUDE + " TEXT NOT NULL, " +
            LOCATION_COLUMN_LONGITUDE + " TEXT NOT NULL);";

    public static final String SQL_CREATE_HASPLACE = "CREATE TABLE " + HASPLACE_TABEL_NAME +
            "(" +
            HASPLACE_COLUMN_TASK_ID + " INTEGER NOT NULL, " +
            HASPLACE_COLUMN_LOCATION_ID + " INTEGER NOT NULL, " +
            "PRIMARY KEY("+HASPLACE_COLUMN_TASK_ID+","+HASPLACE_COLUMN_LOCATION_ID+"), "+
            "FOREIGN KEY("+HASPLACE_COLUMN_LOCATION_ID+") REFERENCES "+LOCATION_TABLE_NAME+"("+ LOCATION_COLUMN_ID+") ON DELETE CASCADE, "+
            "FOREIGN KEY("+HASPLACE_COLUMN_TASK_ID+") REFERENCES "+TASK_TABLE_NAME+"("+ TASK_COLUMN_TASK_ID+") ON DELETE CASCADE);";


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, 1);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
        this.db = this.getWritableDatabase();
        if (!db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
        this.onCreate(db);
    }


    @Override
    /**
     * this method is called if no database exists
     */
    public void onCreate(SQLiteDatabase dbTest) {
        try {
            if(db.isOpen()){
                Log.d(LOG_TAG, "Database is open");
            }
            if(!this.isTableExists(USER_TABLE_NAME,true)){
                Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_USER + " angelegt.");
                db.execSQL(SQL_CREATE_USER);
            }
            if(!this.isTableExists(TASK_TABLE_NAME,true)){
                Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TASK + " angelegt.");
                db.execSQL(SQL_CREATE_TASK);
            }
            if(!this.isTableExists(LOCATION_TABLE_NAME,true)){
                Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_LOCATION + " angelegt.");
                db.execSQL(SQL_CREATE_LOCATION);
            }
            if(!this.isTableExists(HASPLACE_TABEL_NAME,db.isOpen())){
                Log.d(LOG_TAG, "Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_LOCATION + " angelegt.");
                db.execSQL(SQL_CREATE_HASPLACE);
            }
        }
        catch (Exception ex) {
            Log.e(LOG_TAG, "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Die Datenbank wird versucht upzudaten. ");
    }



    public boolean isTableExists(String tableName, boolean openDb) {
        if(openDb == false) {
            if(db == null || !db.isOpen()) {
                db = getReadableDatabase();
            }

            if(!db.isReadOnly()) {
                db.close();
                db = getWritableDatabase();
                db.execSQL("PRAGMA foreign_keys=ON;");
                Log.d(LOG_TAG, "Database Object newly created");
            }
        }

        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

}
