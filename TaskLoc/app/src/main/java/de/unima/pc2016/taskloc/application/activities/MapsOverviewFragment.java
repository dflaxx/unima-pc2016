package de.unima.pc2016.taskloc.application.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
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
public class MapsOverviewFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Context context;
    private final String TAG = "MapsOverviewFragment";
    private Location currLocation;
    private LatLng currPosition;
    private LocationManager locationManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_map, container, false);

        this.context = rootView.getContext();

        SupportMapFragment mp = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_overview));
        if (mp != null) {
            mp.getMapAsync(this);
        }
        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true); //Enable the location button of the GMaps API and display the blue location marker.

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        currLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        Log.d("MapsOverviewFragment", "currLocation = " + currLocation);
//
//        if (currLocation == null) {
//            currLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//            Log.d("MapsOverviewFragment", "currLocation = " + currLocation);
//        }
//
//        if (currLocation != null) {
//            currPosition = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
//            Log.d("MapsOverviewFragment", "currPosition = " + currPosition + ", "
//                    + "currLocation = (" + currLocation.getLatitude() + ", " + currLocation.getLongitude() + ")");
//        }

        getAndSetLocation();

        LoadTaskInBackground loadTaskList = new LoadTaskInBackground();
        loadTaskList.execute();

    }

    @Override
    public void onStart() {
        super.onStart();
        LoadTaskInBackground loadTaskList = new LoadTaskInBackground();
        loadTaskList.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        getAndSetLocation();
        LoadTaskInBackground loadTaskList = new LoadTaskInBackground();
        loadTaskList.execute();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    private void getAndSetLocation() {
        Log.d("MOF - getAndSetLocation",
                "method called");

        try {
            locationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Log.d("MOF - getAndSetLocation",
                    "isGPSEnabled = " + isGPSEnabled + ", "
                    + "isNetworkEnabled = " + isNetworkEnabled);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("MOF - getAndSetLocation",
                        "No active provider found.");
            } else {
                //this.canGetLocation = true;
                if (isNetworkEnabled &&
                        getContext().checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION")
                                == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            1 * 60 * 1000, // 1 minute
                            10,
                            this);
                    Log.d("Network", "Network Enabled");
                    if (locationManager != null) {
                        currLocation = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (currLocation != null) {
                            currPosition = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
                            Log.d("MOF - getAndSetLocation",
                                    "Network PosFix: "
                                            + "(" + currPosition.latitude + "," + currPosition.longitude + ")");
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (currLocation == null) {
                        Log.d("MOF - getAndSetLocation",
                                "Overriding Network PosFix by GPS...");

                    }
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                1 * 60 * 1000, // 1 min
                                10,
                                this);
                        Log.d("GPS", "GPS Enabled");
                        if (locationManager != null) {
                            currLocation = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (currLocation != null) {
                                currPosition = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());                            Log.d("MOF - getAndSetLocation",
                                        "GPS PosFix: "
                                                + "(" + currPosition.latitude + "," + currPosition.longitude + ")");
                            }
                        }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class LoadTaskInBackground extends AsyncTask<String, String, List<TaskDataObject>> {
        @Override
        protected List<TaskDataObject> doInBackground(String... params) {
            return DataSource.instance(context).getAllTaskWithLocation();
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
                for (TaskDataObject taskDataObject : currentTaskList) {
                    if (taskDataObject.getLocations().size() > 0) {
                        for (LocationDataObject location : taskDataObject.getLocations()) {
                            LatLng currLatIng = new LatLng(location.getLatitude(), location.getLongitude());
                            mMap.addMarker(new MarkerOptions().position(currLatIng).title(taskDataObject.getTitle()));
                        }
                    } else {
                        Log.d(TAG, "No location was assigend to the task");
                    }
                }


                GeofenceController.getInstance(context).addGeofencesToList(currentTaskList);
            }//End if

        }

    }
}
