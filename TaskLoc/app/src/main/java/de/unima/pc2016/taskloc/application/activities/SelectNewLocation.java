package de.unima.pc2016.taskloc.application.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.database.DataSource;
//ToDo: Title muss noch gesetzt werden
public class SelectNewLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Location newLocation;
    private Context context;
    private AsyncTaskAddLocation addLocation;

    private EditText txtLocationName;
    private String locationName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_new_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        newLocation = new Location("");
        this.context = context;
        locationName = "Test_Location";
        this.addLocation = new AsyncTaskAddLocation();
        this.txtLocationName = (EditText) findViewById(R.id.txtLocationName);
        this.txtLocationName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationName = txtLocationName.getText().toString();
            }
        });
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

        locationName = txtLocationName.getText().toString();
        this.addLocation.execute(1);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        finish();

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
