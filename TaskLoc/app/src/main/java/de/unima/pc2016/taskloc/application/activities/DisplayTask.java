package de.unima.pc2016.taskloc.application.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;
import java.util.List;

import de.unima.pc2016.taskloc.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Load google maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Dummy Stuff
        tv_taskTitle = (TextView) findViewById(R.id.displayTaskView_taskTitle);
        tv_description_header = (TextView) findViewById((R.id.displayTaskView_description_header));
        tv_description_text = (TextView) findViewById((R.id.displayTaskView_description_text));
        tv_location_header = (TextView) findViewById((R.id.displayTaskView_location_header));
        tv_location_text = (TextView) findViewById((R.id.displayTaskView_location_text));

        Intent intent = getIntent();

        Intent test = new Intent();
        test.putExtra("taskID", 0);

        setupView(test);

    }

    private void setupView(Intent intent) {
        //Intent contains the task's ID as a numeric string (integer)
        if (intent != null){
            int taskId = intent.getIntExtra("taskID", -1);
            if (taskId != -1) {
                //TaskDataObject task = DataSource.instance(null).getTaskByID(taskId);
                // Dev/Debug only
                TaskDataObject task = createTestData();

                Log.d("DisplayTask.setupView", "intent.getIntExtra(\"taskID\", -1): " + taskId);

                // Get data from TaskDataObject
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

    }
}
