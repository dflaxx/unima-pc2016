package de.unima.pc2016.taskloc.application.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.Geofences.GeofenceController;
import de.unima.pc2016.taskloc.application.database.DataSource;
import de.unima.pc2016.taskloc.application.database.LocationDataObject;
import de.unima.pc2016.taskloc.application.database.TaskDataObject;

/**
 * Created by sven on 15.04.16.
 */
public class MapsOverviewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Context context;
    private final String TAG = "MapsOverviewFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_map, container, false);

       this.context = rootView.getContext();
        SupportMapFragment mp = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_overview));
        if(mp != null){
            mp.getMapAsync(this);
        }
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //TODO: Show a different marker for the current location
        LatLng sydney = new LatLng(34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        LoadTaskInBackground loadTaskList = new LoadTaskInBackground();
        loadTaskList.execute();

    }

    @Override
    public void onStart(){
        super.onStart();
        LoadTaskInBackground loadTaskList = new LoadTaskInBackground();
        loadTaskList.execute();
    }

    @Override
    public void onResume(){
        super.onResume();
        LoadTaskInBackground loadTaskList = new LoadTaskInBackground();
        loadTaskList.execute();

    }

    @Override
    public void onPause(){
        super.onPause();

    }

    public class LoadTaskInBackground extends AsyncTask<String, String, List<TaskDataObject>> {
        @Override
        protected List<TaskDataObject> doInBackground(String... params) {
            return DataSource.instance(context).getAllTaskWithLocation();
        }

        @Override
        protected void onPostExecute(List<TaskDataObject> currentTaskList){
            //Log.d(TAG, "Try to add new marker to the map");
            mMap.clear();
            if (currentTaskList != null) {
               for(TaskDataObject taskDataObject: currentTaskList){
                   if(taskDataObject.getLocations().size() > 0){
                       for(LocationDataObject location: taskDataObject.getLocations()){
                           LatLng currLatIng = new LatLng(location.getLatitude(), location.getLongitude());
                           mMap.addMarker(new MarkerOptions().position(currLatIng).title(taskDataObject.getTitle()));
                       }
                   }else{
                       Log.d(TAG, "No location was assigend to the task");
                   }
               }
                GeofenceController.instance(context).addGeofencesToList(currentTaskList);
            }//End if

        }

    }
}
