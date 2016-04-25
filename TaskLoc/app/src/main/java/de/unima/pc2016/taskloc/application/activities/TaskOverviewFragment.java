package de.unima.pc2016.taskloc.application.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.gcm.Task;

import java.util.ArrayList;
import java.util.List;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.database.CreateTestData;
import de.unima.pc2016.taskloc.application.database.DataSource;
import de.unima.pc2016.taskloc.application.database.TaskDataObject;

/**
 * Created by sven on 15.04.16.
 */
public class TaskOverviewFragment extends Fragment{
    private final String TAG="TaskOverviewFragment";
   // protected ArrayAdapter<TaskDataObject> taskListAdapter;
    protected List<TaskDataObject> list;
    protected TaskListAdapter taskListAdapter;
    protected DataSource dataSource;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_task, container, false);


        CreateTestData ts = new CreateTestData(container.getContext());

        dataSource = new DataSource(rootView.getContext());


        list = new ArrayList<TaskDataObject>();
        //taskListAdapter = new ArrayAdapter<TaskDataObject>(rootView.getContext(), R.layout.listview_tasklist, list);
        taskListAdapter = new TaskListAdapter(rootView.getContext(),dataSource);

        ListView listView = (ListView) rootView.findViewById(R.id.task_list);
        listView.setAdapter(taskListAdapter);

        LoadTaskList loadTaskList = new LoadTaskList();
        loadTaskList.execute(dataSource);

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Wir prüfen, ob Menü-Element mit der ID "action_daten_aktualisieren"
        // ausgewählt wurde und geben eine Meldung aus
        int id = item.getItemId();
        if (id == R.id.task_list) {
            Log.d(TAG, "Task was selected");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /*

     */
    private class LoadTaskList extends AsyncTask<DataSource, Integer, List<TaskDataObject>> {
        @Override
        protected List<TaskDataObject> doInBackground(DataSource... dataSource) {
            //SQLiteDatabase database = dataSources[0].getReadableDB();
            List<TaskDataObject> currentTaskList =  dataSource[0].getAllTask();
            return currentTaskList;
        }

        @Override
        protected void onPostExecute(List<TaskDataObject> currentTaskList){
            if (currentTaskList != null) {
                taskListAdapter.clear();
                for (TaskDataObject aktienString : currentTaskList) {
                    taskListAdapter.addTask(aktienString);
                }
            }

        }
    }
}
