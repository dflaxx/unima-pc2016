package de.unima.pc2016.taskloc.application.activities;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.Geofences.GeofenceController;

public class StartActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private FloatingActionButton fbAddAction;
    private Context context;
    public MapsOverviewFragment mapsFragment;
    private GeofenceController geofenceController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this.getApplicationContext();
        setContentView(R.layout.activity_start);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        fbAddAction = (FloatingActionButton) findViewById(R.id.fabAddTask);
        fbAddAction.setOnClickListener(new AddNewTaskListener());

        viewPager.setOnPageChangeListener(new FragmentChanged()); //Needed so the onStop and onResume methods are called
         this.geofenceController = GeofenceController.getInstance(this.getApplicationContext());

    }



    private void setupViewPager(ViewPager viewPager) {

        this.viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mapsFragment = new MapsOverviewFragment();

        viewPagerAdapter.addFragment(new TaskOverviewFragment(), "Task Overview");
        viewPagerAdapter.addFragment(mapsFragment, "Map View");
        viewPager.setAdapter(viewPagerAdapter);
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public class AddNewTaskListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, AddNewTask.class);
            startActivity(intent);
        }
    }

    public class FragmentChanged implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            //viewPagerAdapter.getItem(position).onPause();
            viewPagerAdapter.getItem(position).onResume();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }



}
