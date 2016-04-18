package de.unima.pc2016.taskloc.application.activities;

import android.content.Context;
import android.content.res.Resources;
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
import de.unima.pc2016.taskloc.application.database.TaskDataObject;

/**
 * Created by sven on 18.04.16.
 */
public class TaskListAdapter extends BaseAdapter {
    private final String TAG="TaskListAdapter";
    private List<TaskDataObject> taskList;
    private LayoutInflater mInflater;
    private Context context;



    public TaskListAdapter(Context context, ArrayList d, Resources res){
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
            holder.taskDescription.setOnClickListener(new OnItemClickListener(position));
            Log.d(TAG,taskList.get(position).getTitle()+ " was added to List view");
        //}
        return vi;
    }


    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;

        }

        @Override
        public void onClick(View arg0) {
            Log.d(TAG, taskList.get(mPosition)+ " was selected");
        }
    }

    public static class TaskViewHolder {

        public TextView taskDescription;
        public Button btnTaskDone;
    }
}
