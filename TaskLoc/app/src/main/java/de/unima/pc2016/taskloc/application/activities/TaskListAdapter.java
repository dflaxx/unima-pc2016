package de.unima.pc2016.taskloc.application.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.Geofences.GeofenceController;
import de.unima.pc2016.taskloc.application.Geofences.GeofenceTransitionsIntentService;
import de.unima.pc2016.taskloc.application.database.DataSource;
import de.unima.pc2016.taskloc.application.database.TaskDataObject;

/**
 * Created by sven on 18.04.16.
 */
public class TaskListAdapter extends BaseAdapter {
    private final String TAG="TaskListAdapter";
    private List<TaskDataObject> taskList;
    private LayoutInflater mInflater;
    private Context context;



    public TaskListAdapter(Context context){
        super();
        this.context = context;
        taskList = new ArrayList<TaskDataObject>();
        mInflater = ( LayoutInflater ) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addTask(TaskDataObject newTask){
        taskList.add(newTask);
        Log.d(TAG,"Update Adapter was called for "+ newTask.getTitle());
        notifyDataSetChanged();
    }

    public void updateList(List<TaskDataObject> newList){
        if(taskList != null && taskList.size() > 0){
            taskList.clear();
            for(TaskDataObject tmp: newList){
                taskList.add(tmp);
            }
        }
        GeofenceController.getInstance(context).addGeofencesToList(taskList);
        notifyDataSetChanged();
    }

    public void removeTaskByID(int id){
        int counter = 0;
        Thread t1 = new DeleteTaskFromDatabaseThread(id);
        t1.start();
        for(TaskDataObject tmp : taskList){
            if(tmp.getId() == id){
                taskList.remove(counter);
                break;
            }
            counter++;
        }
        notifyDataSetChanged();

    }

    private class DeleteTaskFromDatabaseThread extends Thread{
        private int taskID;
        public DeleteTaskFromDatabaseThread(int taskID){
            this.taskID = taskID;
        }
        public void run(){
            DataSource.instance(context).deleteTask(taskID);
        }
    }
    public void clear(){
        taskList.clear();
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskViewHolder holder = null;
        View vi = convertView;
        if(convertView == null){
            vi = mInflater.inflate(R.layout.tasklist_item, null);
            holder = new TaskViewHolder();
            holder.taskDescription = (TextView)vi.findViewById(R.id.taskTitle);
            holder.btnTaskDone = (Button) vi.findViewById(R.id.taskDoneSelector);

            vi.setTag(holder);
        }
        //else{
            TaskDataObject currTaskObject = taskList.get(position);
            holder = (TaskViewHolder) vi.getTag();
            holder.taskDescription.setText(currTaskObject.getTitle());
            holder.taskDescription.setOnClickListener(new OnItemClickListener(position, currTaskObject.getId()));
            holder.btnTaskDone.setOnClickListener(new TaskDoneListener(position, currTaskObject.getId()));
        //}
        return vi;
    }


    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;
        private int id;
        OnItemClickListener(int position, int id){
            mPosition = position;
            this.id = id;
        }

        @Override
        public void onClick(View arg0) {
           Log.d(TAG, taskList.get(mPosition)+ " was selected" + " "+ id);

            Intent editTaskIntent = new Intent (context, AddNewTask.class);
            Bundle taskData = new Bundle();
            taskData.putInt("id", id);

            editTaskIntent.putExtras(taskData);

            context.startActivity(editTaskIntent);
            //arg0.getContext().startActivity(editTaskIntent);

        }
    }

    /**
     * Click Listener if task should be deleted from the list
     */
    private class TaskDoneListener implements View.OnClickListener{
        private int position;
        private int id;

        TaskDoneListener(int position, int id){
            this.position = position;
            this.id = id;
        }


        public void onClick(View view){
            removeTaskByID(id);
            Thread deleteFromDatabase = new Thread(){
                public void run(){
                    DataSource.instance(context).deleteTask(id);
                }
            };
            deleteFromDatabase.start();

            Thread deleteFromGeofence = new Thread(){
                public void run(){
                    GeofenceController.getInstance(context).removeTaskFromGeofenceList(id);
                }
            };

            if(GeofenceTransitionsIntentService.notificationContext != null){
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(GeofenceTransitionsIntentService.notificationContext.NOTIFICATION_SERVICE);
                notificationManager.cancel(id);
            }



        }
    }

    public static class TaskViewHolder {

        public TextView taskDescription;
        public Button btnTaskDone;
    }
}
