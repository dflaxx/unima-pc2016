package de.unima.pc2016.taskloc.application.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.unima.pc2016.taskloc.R;

/**
 * Created by sven on 15.04.16.
 */
public class TaskOverviewFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_task, container, false);
        return rootView;
    }
}
