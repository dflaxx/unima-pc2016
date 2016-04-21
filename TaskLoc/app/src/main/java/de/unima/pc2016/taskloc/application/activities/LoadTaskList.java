package de.unima.pc2016.taskloc.application.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.util.List;

import de.unima.pc2016.taskloc.application.database.DataSource;
import de.unima.pc2016.taskloc.application.database.TaskDataObject;

/**
 * Created by sven on 16.04.16.
 */
public class LoadTaskList extends AsyncTask<DataSource, List<TaskDataObject>, List<TaskDataObject>> {
    @Override
    protected List<TaskDataObject> doInBackground(DataSource... dataSources) {
        //SQLiteDatabase database = dataSources[0].getReadableDB();
        List<TaskDataObject> currentTaskList =  dataSources[0].getAllTaskWithLocation();
        return currentTaskList;
    }

    @Override
    protected void onPostExecute(List<TaskDataObject> currentTaskList){

    }
}
