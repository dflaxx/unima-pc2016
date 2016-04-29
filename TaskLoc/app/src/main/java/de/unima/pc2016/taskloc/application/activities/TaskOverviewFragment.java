package de.unima.pc2016.taskloc.application.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.gcm.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.database.CreateTestData;
import de.unima.pc2016.taskloc.application.database.DataSource;
import de.unima.pc2016.taskloc.application.database.TaskDataObject;

/**
 * Created by sven on 15.04.16.
 */
public class TaskOverviewFragment extends Fragment {
    private final String TAG = "TaskOverviewFragment";
    // protected ArrayAdapter<TaskDataObject> taskListAdapter;
    protected List<TaskDataObject> list;
    protected TaskListAdapter taskListAdapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_task, container, false);

        context = rootView.getContext();


        list = new ArrayList<TaskDataObject>();
        //taskListAdapter = new ArrayAdapter<TaskDataObject>(rootView.getContext(), R.layout.listview_tasklist, list);
        taskListAdapter = new TaskListAdapter(rootView.getContext());

        ListView listView = (ListView) rootView.findViewById(R.id.task_list);
        listView.setAdapter(taskListAdapter);

        LoadTaskList loadTaskList = new LoadTaskList();
        loadTaskList.execute(1);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    @Override
    public void onResume() {
        super.onResume();
        List<TaskDataObject> list = DataSource.instance(context).getAllTask();

        if (list != null && taskListAdapter != null) {
            int currTaskAmount = list.size();
            if (currTaskAmount != taskListAdapter.getCount()) {
                taskListAdapter.updateList(list);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Wir prüfen, ob Menü-Element mit der ID "action_daten_aktualisieren"
        // ausgewählt wurde und geben eine Meldung aus
        int id = item.getItemId();
        if (id == R.id.task_list) {
            Log.d(TAG, "Task was selected");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*

     */
    private class LoadTaskList extends AsyncTask<Integer, Integer, List<TaskDataObject>> {

        @Override
        protected List<TaskDataObject> doInBackground(Integer... integers) {
            List<TaskDataObject> currentTaskList = DataSource.instance(context).getAllTask();
            HashMap<String, Double> distanceToCurrentPosition = new HashMap<>();
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, " Task werden nicht geladen, da Location nicht Activiert");
                return null;
            }
            Location currLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //ToDo: Get Distance to location

            for(TaskDataObject currObj : currentTaskList){
                /*double dist = 6378.388 * Math.acos(
                        Math.sin(currObj.getLocations().get(0).getLatitude())*
                        Math.sin(currLocation.getLatitude() +
                        Math.cos(currObj.getLocations().get(0).getLatitude()))*
                        Math.cos(currLocation.getLatitude() *
                        Math.cos(currLocation.getLongitude() - currObj.getLocations().get(0).getLongitude())) );*/
            }

            //ToDo: Sort Values after Current Position
            /*
            dist = 6378.388 * acos(sin(lat1) * sin(lat2) + cos(lat1) * cos(lat2) * cos(lon2 - lon1))
            https://www.kompf.de/gps/distcalc.html
             */

            return currentTaskList;
        }

        @Override
        protected void onPostExecute(List<TaskDataObject> currentTaskList){
            if (currentTaskList != null) {
                taskListAdapter.clear();
                for (TaskDataObject aktienString : currentTaskList) {
                    taskListAdapter.addTask(aktienString);
                }
            }

        }
    }
}
