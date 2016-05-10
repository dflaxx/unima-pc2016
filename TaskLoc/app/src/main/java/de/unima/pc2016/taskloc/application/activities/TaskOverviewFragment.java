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

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.database.CreateTestData;
import de.unima.pc2016.taskloc.application.database.DataSource;
import de.unima.pc2016.taskloc.application.database.LocationDataObject;
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


    private double longitude;
    private double latitude;

    private double toLongitude;
    private double toLatitude;

    private double shortestDistanceToTask;

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

            List<TaskDataObject> currentTaskList = DataSource.instance(context).getAllTaskWithLocation();

            //HashMap<String, Double> distanceToCurrentPosition = new HashMap<>();


            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Log.d("TaskLocDebug", "Permissioncheck steht an");

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Log.d("TaskLocDebug", " Task werden nicht geladen, da Location nicht Activiert");
                return null;
            }



            Location currLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (currLocation != null) { // Prüfen, ob letzte Location vorhanden
                Log.i("TaskLocDebug", "LastLocation erhalten");

                //Getting longitude and latitude
                longitude = currLocation.getLongitude();
                latitude = currLocation.getLatitude();

                Log.i("TaskLocDebug", "LastLocation Longitude:  "+ longitude);
                Log.i("TaskLocDebug", "LastLocation Latitude:   " +  latitude);
            }


            if (currentTaskList != null) {

                Log.d("TaskLocDebug", "Innerhalb if-Block");

                for(TaskDataObject currObj : currentTaskList){
                    Log.d("TaskLocDebug", "Innerhalb 1. for-Schleife");
                    Log.d("TaskLocDebug", "TaskID: " + currObj.getId());




                    List<LocationDataObject> locationlist = currObj.getLocations();


                    if (locationlist != null) {
                        shortestDistanceToTask = 50000; // initialization of variable for later usage.
                        for (LocationDataObject tempLocation : locationlist) { // Alle Locations zu einer Task aufrufen

                            Log.d("TaskLocDebug", "Innerhalb 2. for-Schleife");

                            // Latitude & Longitude berechnen

                            // Koordianten der jeweiligen Task
                            toLatitude = tempLocation.getLatitude();
                            toLongitude =  tempLocation.getLongitude();

                            LatLng from = new LatLng(latitude,longitude); // Letzte bekannte Koordinaten des Geräts
                            LatLng to = new LatLng(toLatitude,toLongitude);
                            //distanzberechnung anhand Library (build.gradle beachten)
                            Double distance = SphericalUtil.computeDistanceBetween(from, to);
                            if(distance <= shortestDistanceToTask ){shortestDistanceToTask=distance;}



                        }

                        currObj.setDistance(shortestDistanceToTask);

                    }


                }

                Log.d("TaskLocDebug", "Sortieren der Liste");

                // Sortierung der Liste der Größe nach
                Collections.sort(currentTaskList, new Comparator<TaskDataObject>() {
                    public int compare(TaskDataObject task1, TaskDataObject task2) {
                        return task1.getDistance().compareTo(task2.getDistance());
                    }
                });


                return currentTaskList;
            }


            return null;
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
