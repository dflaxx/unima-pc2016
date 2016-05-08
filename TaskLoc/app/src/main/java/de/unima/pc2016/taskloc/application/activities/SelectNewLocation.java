package de.unima.pc2016.taskloc.application.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.database.DataSource;
//ToDo: Title muss noch gesetzt werden
public class SelectNewLocation extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private Location newLocation;
    private Context context;
    private AsyncTaskAddLocation addLocation;

    private EditText txtLocationName;
    private String locationName;
    private FloatingActionButton btnAddLocation;

    private Location currLocation;
    private LatLng currPosition;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_new_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        newLocation = new Location("");
        this.context = this.getApplicationContext();
        locationName = "Test_Location";
        this.addLocation = new AsyncTaskAddLocation();
        this.txtLocationName = (EditText) findViewById(R.id.txtLocationName);
        this.txtLocationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationName = txtLocationName.getText().toString();
            }
        });
        this.btnAddLocation = (FloatingActionButton) findViewById(R.id.fabAddLocation);
        this.btnAddLocation.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        locationName = txtLocationName.getText().toString();
                        addLocation.execute(1);
                        finishActivity(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        finish();
                    }
                }
        );

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getAndSetLocation();

        if (context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION")
                        == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true); //Enable the location button of the GMaps API and display the blue location marker.
        }

        // Move camera to current location, if available.
        if (currPosition != null) {
            mMap.addMarker(new MarkerOptions().position(currPosition).title("Aktuelle Position"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(currPosition));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currPosition, 12);

            mMap.animateCamera(cameraUpdate);
        }

        // Setting a click event handler for the map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting the position for the marker
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                // Clears the previously touched position
                mMap.clear();

                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);
                newLocation.setLatitude(latLng.latitude);
                newLocation.setLongitude(latLng.longitude);
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        finish();
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
                        context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION")
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

    public class AsyncTaskAddLocation extends AsyncTask<Integer, Integer, Integer>{

        @Override
        protected Integer doInBackground(Integer... integers) {
            Log.d("SelectNewLocation"," Name of the new location: "+ locationName);
            DataSource.instance(context).createLocation(locationName,
                    String.valueOf(newLocation.getLatitude()),
                    String.valueOf(newLocation.getLongitude()));
            return null;
        }
    }
}
