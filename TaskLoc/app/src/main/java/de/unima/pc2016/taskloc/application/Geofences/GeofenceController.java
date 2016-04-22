package de.unima.pc2016.taskloc.application.Geofences;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import de.unima.pc2016.taskloc.application.database.LocationDataObject;
import de.unima.pc2016.taskloc.application.database.TaskDataObject;

/**
 * Created by sven on 22.04.16.
 */
public class GeofenceController implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final String TAG="GeofenceController";
    private GoogleApiClient mGoogleApiClient;
    private List<Geofence> mGeofenceList;
    private Context context;
    private PendingIntent mGeofencePendingIntent;
    private Boolean googleAPIConnected = false;

    private static GeofenceController geofenceController;

    public static synchronized GeofenceController instance(Context context){
        if(geofenceController == null){
            GeofenceController.geofenceController = new GeofenceController(context);
        }
        return GeofenceController.geofenceController;
    }

    public GeofenceController(Context context) {
       this.context = context;
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApiClient.connect();
        }

        mGeofenceList = new ArrayList<>();

    }


    public void startGeofencing(){
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.googleAPIConnected == false) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Log.d(TAG, "Permission is missing or GoogleApiClient is not connected yet");
            return;
        }

        LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, getGeofencingRequest(), getGeofencePendingIntent());
    }

    public void addTaskToGeofenceList(TaskDataObject tdo){
        for(LocationDataObject locationDataObject: tdo.getLocations()){
            mGeofenceList.add(new Geofence.Builder()
                    .setRequestId(String.valueOf(tdo.getId()))
                    .setCircularRegion(
                            locationDataObject.getLatitude(),
                            locationDataObject.getLongitude(),
                            tdo.getRange()
                    )
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
                    .build());
        }

    }

    public void addGeofencesToList(List<TaskDataObject> taskDataObjectList){
        for(TaskDataObject tdo : taskDataObjectList){
            this.addTaskToGeofenceList(tdo);
        }

        if(this.googleAPIConnected == true){
            this.startGeofencing();
        }

    }

    /**
     * This method is responsible for creating a Geofencing Request
     * @return
     */
    private GeofencingRequest getGeofencingRequest() {
        if(this.mGeofenceList.size() == 0){
            return null;
        }
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);
        return builder.build();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG,"GoogleClientApi has connected ");
        this.googleAPIConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG,"GoogleClientApi has disconnected ");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG,"GoogleClientApi connection failed");
    }

    /*
            Pending Intent
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(context, GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        return PendingIntent.getService(context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
    }


}
