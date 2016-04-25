package de.unima.pc2016.taskloc.application.Geofences;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sven on 22.04.16.
 */
public class GeofenceTransitionsIntentService extends IntentService{

    private final String TAG ="GTIntentService";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public GeofenceTransitionsIntentService(String name) {
        super(name);
    }

    public GeofenceTransitionsIntentService(){
        super("GeofenceTransitionsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        int i= 5;
        Log.d(TAG, "onHandlIntent called");

        //ToDo: Start Notification
    }
}
