package de.unima.pc2016.taskloc.application.Geofences;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.activities.DisplayTask;
import de.unima.pc2016.taskloc.application.activities.StartActivity;

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

        createNotification("TestTask1");
    }

    public void createNotification(String task) {
        //Placed here for demo usage
        String title = "TaskLoc";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_taskloc_launcher) //Icon shown in notification bar
                        .setContentTitle(title) // Notification title
                        .setContentText(task); // Message shown in notification bar
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, DisplayTask.class);

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
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        // (int) System.currentTimeMillis() used to create unique ID
        mNotificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());

    }

}
