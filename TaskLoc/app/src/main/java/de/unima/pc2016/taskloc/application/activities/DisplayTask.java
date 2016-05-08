package de.unima.pc2016.taskloc.application.activities;


import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.Geofences.GeofenceController;
import de.unima.pc2016.taskloc.application.database.DataSource;
import de.unima.pc2016.taskloc.application.database.LocationDataObject;
import de.unima.pc2016.taskloc.application.database.TaskDataObject;

public class DisplayTask extends AppCompatActivity implements OnMapReadyCallback {

    //Attributes
    private TextView tv_taskTitle;
    private TextView tv_description_header;
    private TextView tv_description_text;
    private TextView tv_location_header;
    private TextView tv_location_text;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private GoogleMap mMap;

    private Context context;
    private int taskID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_selected_task);
        //Load google maps
        //(SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.context = getApplicationContext();
        //Dummy Stuff
        tv_taskTitle = (TextView) findViewById(R.id.displayTaskView_taskTitle);
        tv_description_header = (TextView) findViewById((R.id.displayTaskView_description_header));
        tv_description_text = (TextView) findViewById((R.id.displayTaskView_description_text));
        tv_location_header = (TextView) findViewById((R.id.displayTaskView_location_header));
        tv_location_text = (TextView) findViewById((R.id.displayTaskView_location_text));

        Intent intent = getIntent();
        taskID = intent.getIntExtra("taskID", -1);
        /*Intent test = new Intent();
        test.putExtra("taskID", 0);*/

        setupView(intent);

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setupView(Intent intent) {
        //Intent contains the task's ID as a numeric string (integer)
        if (intent != null) {
            int taskId = intent.getIntExtra("taskID", -1);
            if (taskId != -1) {
                //TaskDataObject task = DataSource.instance(null).getTaskByID(taskId);
                // Dev/Debug only

                LoadTaskInBackground loadTaskList = new LoadTaskInBackground();
                loadTaskList.execute();

                //TaskDataObject task = createTestData();

                Log.d("DisplayTask.setupView", "intent.getIntExtra(\"taskID\", -1): " + taskId);

                // Get data from TaskDataObject


            }

        }

    }

    private TaskDataObject createTestData() {
        String[] attr = {
                "100", //ID
                "Test Task", //Title
                "This task was created to test the abilities of this awesome app.", //Desc
                "2016-01-01", //StartDate
                "", //EndDate
                "300", //Range
        };

        List<LocationDataObject> locations = new ArrayList<>();
        locations.add(new LocationDataObject(900, "Somewhere", 50, 0));

        TaskDataObject tdo = new TaskDataObject(attr, locations);

        Log.d("createTestData()", "Method called. Test Task created: " + tdo.toString());

        return tdo;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DisplayTask Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://de.unima.pc2016.taskloc.application.activities/http/host/path")
        );

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "DisplayTask Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://de.unima.pc2016.taskloc.application.activities/http/host/path")
        );
        client.disconnect();
    }


    public class LoadTaskInBackground extends AsyncTask<String, String, List<TaskDataObject>> {
        @Override
        protected List<TaskDataObject> doInBackground(String... params) {
            return DataSource.instance(context).getTaskByIDWithLocations(taskID);
        }

        @Override
        protected void onPostExecute(List<TaskDataObject> currentTaskList) {
            //Log.d(TAG, "Try to add new marker to the map");
            if (mMap == null) {
                return;
            }
            mMap.clear();

//            if (currPosition != null) {
//                mMap.addMarker(new MarkerOptions().position(currPosition).title("Aktuelle Position"));
//                //mMap.moveCamera(CameraUpdateFactory.newLatLng(currPosition));
//                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currPosition, 15);
//
//                mMap.animateCamera(cameraUpdate);
//            }
            if (currentTaskList != null) {
                for (TaskDataObject task : currentTaskList) {
                    if (task.getLocations().size() > 0) {
                        for (LocationDataObject location : task.getLocations()) {
                            LatLng currLatIng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currLatIng).title(task.getTitle()));
                        }
                    }
                    int taskID = task.getId();
                    String taskTitle = task.getTitle();
                    String taskDescription = task.getDescription();
                    List<LocationDataObject> taskLocationsList = task.getLocations();

                    // Set TextViews
                    tv_taskTitle.setText("#" + taskID + " " + taskTitle);
                    tv_description_text.setText(taskDescription);
                    tv_location_text.setText("");

                    if (taskLocationsList.size() > 1) {
                        tv_location_header.setText("Locations"); //Plural if more than one location.
                    } else {
                        tv_location_header.setText("Location"); //Singular if only one location.
                    }

                    StringBuilder locationsSB = new StringBuilder();
                    for (LocationDataObject ldo : taskLocationsList) {
                        locationsSB.append(ldo.toString());
                        locationsSB.append("\n");
                    }

                    tv_location_text.setText(locationsSB.toString());
                }
            }//End if

        }

    }
}
