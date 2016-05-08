package de.unima.pc2016.taskloc.application.Geofences;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.activities.DisplayTask;
import de.unima.pc2016.taskloc.application.activities.StartActivity;
import de.unima.pc2016.taskloc.application.database.DataSource;
import de.unima.pc2016.taskloc.application.database.TaskDataObject;

/**
 * Created by sven on 22.04.16.
 */
public class GeofenceTransitionsIntentService extends IntentService{

    private final String TAG ="GTIntentService";
    public static Context notificationContext;
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
        notificationContext = this.getApplicationContext();
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER){

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            List triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            List<Geofence> triggerList = geofencingEvent.getTriggeringGeofences();
            // Get the transition details as a String.
            Log.d(TAG, "GeofencList Trigger "+ triggeringGeofences.size()+ " "+ triggerList.size());
            //String geofenceTransitionDetails = getGeofenceTransitionDetails(this, geofenceTransition, triggeringGeofences);
            for(int i=0; i<triggerList.size(); i++){
                createNotification(Integer.parseInt(triggerList.get(i).getRequestId()));
            }


        } else {
            // Log the error.

        }






    }

    public void createNotification(int taskID) {
        //Placed here for demo usage
        String title = String.valueOf(R.string.notification_title);

        TaskDataObject taskList = DataSource.instance(this.getApplicationContext()).getTaskByID(taskID);

        if(taskList == null){
            return;
        }
        Log.d(TAG, "Notification of following task "+ taskList.getTitle());
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_taskloc_launcher) //Icon shown in notification bar
                        .setContentTitle("TaskLoc") // Notification title
                        .setContentText("Task: "+ taskList.getTitle()); // Message shown in notification bar
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, DisplayTask.class);
        resultIntent.putExtra("taskID", taskID);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(DisplayTask.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        // (int) System.currentTimeMillis() used to create unique ID
        mNotificationManager.notify(taskID, mBuilder.build());

    }

}
