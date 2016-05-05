package de.unima.pc2016.taskloc.application.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.unima.pc2016.taskloc.R;
import de.unima.pc2016.taskloc.application.database.DataSource;
import de.unima.pc2016.taskloc.application.database.LocationDataObject;
import de.unima.pc2016.taskloc.application.database.TaskDataObject;


public class AddNewTask extends AppCompatActivity {

    //constant
    private static final String MSG_NO_INPUT = "Please insert Task title.";
    static final int ADD_LOCATION_REQUEST = 1;  // The request code



    //EditText & TextView
    private EditText txtInsertTitle = null;
    private EditText txtDescription = null;
    private EditText taskLocation = null;
    private EditText dateFrom = null;
    private EditText dateTo = null;
    private TextView txtRange = null;
    private TextView txtTitle = null;

    //button
    private Button buttonSave = null;
    private Button buttonCancel = null;
    private Button buttonAddLocation = null;
    
    //SeekBar
    private SeekBar rangeBar = null;
    private int rangeInMeters=0;
    //private int outputRange = rangeBar.getProgress();

    private ArrayList<LocationDataObject> selectedLocations;
    private List<LocationDataObject> locationList;
    private Context context;

    private boolean editMode = false;

    private int taskID= -1;




    protected void onCreate(final Bundle savedInstanceState) {
       //TODO check why if loop doesn't initiate
       if (savedInstanceState != null){
            editMode = true;
            taskID = savedInstanceState.getInt("TaskDbID");
            Log.d("AddNewTask OnCreate", "Handed TaskID " + taskID);
            this.txtTitle = (TextView) findViewById(R.id.txtTitle1);
            txtTitle.setText("Edit Task");

        }



        this.locationList = DataSource.instance(this.getApplicationContext()).getAllLocation();
        this.selectedLocations = new ArrayList<>();
        this.rangeInMeters = 500;
        //Intents
        final Intent main = new Intent(AddNewTask.this, StartActivity.class);


        super.onCreate(savedInstanceState);
        this.context = this.getApplicationContext();
        setContentView(R.layout.activity_add_new_task);

        //EditText & Textview
        this.txtInsertTitle = (EditText) findViewById(R.id.txtInsertTitle);
        this.txtDescription = (EditText) findViewById(R.id.txtDescription);
        this.taskLocation = (EditText) findViewById(R.id.taskLocation);
        this.dateFrom = (EditText) findViewById(R.id.dateFrom);
        this.dateTo = (EditText) findViewById(R.id.dateTo);
        this.txtRange = (TextView) findViewById(R.id.txtRange);

        //Buttons
        this.buttonCancel = (Button) findViewById(R.id.buttonCancel);
        this.buttonSave = (Button) findViewById(R.id.buttonSave);
        this.buttonAddLocation = (Button) findViewById(R.id.insertLocation);

        //View for the Dialog
        View addTaskLayout = (View) findViewById(R.id.addTask);

        //Bar and Init of default value
        this.rangeBar = (SeekBar) findViewById(R.id.rangeBar);


        //ListenerAddLocation
        this.buttonSave.setOnClickListener(new AddLocationListener());
        //ListenerTitle
        txtInsertTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTaskTitle = txtInsertTitle.getText().toString();

            }
        });

        //ListenerDescription
        txtDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTaskDescription = txtDescription.getText().toString();
            }
        });

        //ListenerTaskLocation
        taskLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewTask.this);
                builder.setMessage("Please choose an action.");
                builder.setCancelable(true);

                builder.setPositiveButton("Map", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // startActivity(map);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog mapDialog = builder.create();
                mapDialog.show();


                //setting text to show location names
                //TODO take location names and show in field
                //taskLocation.setText();
            }
        });

        //Initial Range Bar Value
        rangeBar.setProgress(500);
        txtRange.setText("Current reminder range is " + rangeBar.getProgress() + " meters.");


        // BarListener
        rangeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){



            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int range = seekBar.getProgress();
                rangeInMeters = range;
                if (range < 1000) {
                    txtRange.setText("Current reminder range is " + range + " meters.");

                } else{
                    txtRange.setText("Current reminder range is " + range/1000 + " kilometers and " +
                            range%1000 + " meters.");
                }
            }
        });


        //ListenerSave
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if( editMode == false) {

                    Thread tcreateTask = new Thread() {

                        @Override
                        public void run() {
                            int currTaskId = DataSource.instance(context).createNewTask(
                                    txtInsertTitle.getText().toString(),
                                    txtDescription.getText().toString(),
                                    dateFrom.getText().toString(),
                                    dateTo.getText().toString(),
                                    rangeInMeters);
                            if (currTaskId != -1) {
                                Log.d("Add newTask", "Current Task ID: " + currTaskId);
                                DataSource.instance(context).connectLocationWithPlace(currTaskId, selectedLocations);
                            }
                        }
                    };
                    int currTaskId = DataSource.instance(context).createNewTask(
                            txtInsertTitle.getText().toString(),
                            txtDescription.getText().toString(),
                            dateFrom.getText().toString(),
                            dateTo.getText().toString(),
                            rangeInMeters);
                    if (currTaskId != -1) {
                        Log.d("Add newTask", "Current Task ID: " + currTaskId);
                        DataSource.instance(context).connectLocationWithPlace(currTaskId, selectedLocations);
                    }
                    //tcreateTask.start();
                    startActivity(main);
                }else{
                    DataSource.instance(context).updateTask(taskID,
                            txtInsertTitle.getText().toString(),
                            txtDescription.getText().toString(),
                            dateFrom.getText().toString(),
                            dateTo.getText().toString(),
                            rangeInMeters);
                    // TODO implement location change
                    if (taskID != -1){
                      Log.d("Add newTask", "Current Task ID: " + taskID);
                        DataSource.instance(context).connectLocationWithPlace(taskID, selectedLocations);
                    }

                    startActivity(main);




                }

            }
        });

        //ListenerCancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(main);
            }
        });

        setOnclick(this.dateFrom);
        setOnclick(this.dateTo);

        if(editMode){
            TaskDataObject tdo = DataSource.instance(this).getTaskByID(taskID);
            if(tdo != null){
                txtInsertTitle.setText(tdo.getTitle());
                txtDescription.setText(tdo.getDescription());
                locationList = tdo.getLocations();
                dateFrom.setText(tdo.getStartDate().toString());
                dateTo.setText(tdo.getEndDate().toString());
                rangeBar.setProgress(tdo.getRange());
            }

        }
    }

    @Override
    protected void onResume() {
        Log.d("Add.NewTask.onResume()", "Add.NewTask.onResume() called.");
        super.onResume();
    }

    //Date Listener Method
    private void setOnclick(final EditText txt){
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Calendar mCurrentDate = Calendar.getInstance();
                String datum = txt.getText().toString();
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    Date date = sdf.parse(datum);
                    mCurrentDate.setTime(date);
                }catch (java.text.ParseException e) {
                    int mYear = mCurrentDate.get(Calendar.YEAR);
                    int mMonth = mCurrentDate.get(Calendar.MONTH);
                    int mDay = mCurrentDate.get(Calendar.DAY_OF_MONTH);


                    DatePickerDialog datePicker = new DatePickerDialog(AddNewTask.this,
                            new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                            txt.setText(String.format("%02d.%02d.%d",selectedDay,selectedMonth+1,selectedYear));
                        }
                    }, mYear,mMonth,mDay);
                    datePicker.setTitle("Please select a date");
                    datePicker.show();

                }

                }

        });

        this.buttonAddLocation.setOnClickListener(new AddLocationListener());
    }

    @Override
    public void onStart(){
        super.onStart();
        if(selectedLocations != null && selectedLocations.size() > 0){
            selectedLocations.clear();
        }

    }


    //Add Location Listener
    public class AddLocationListener implements View.OnClickListener{
        public void onClick(View view) {
            //Get locations from DB
            locationList = DataSource.instance(getApplicationContext()).getAllLocation();
            Log.d("this.locationList","this.locationList: " + locationList);

            //Show Dialog
            if(selectedLocations.size() > 0)
                selectedLocations.clear();
            final String[] locationStrings = new String[locationList.size()];
            int counter = 0;

            for(LocationDataObject o : locationList){
                locationStrings[counter] = String.valueOf(locationList.get(counter).getName());
                counter++;
            }

            final AlertDialog.Builder builderDialog = new AlertDialog.Builder(AddNewTask.this);
            boolean[] is_checked = new boolean[locationList.size()]; // set is_checked boolean false;
            // Creating multiple selection by using setMutliChoiceItem method
            builderDialog.setMultiChoiceItems(locationStrings, is_checked,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {

                        }
                    });
            builderDialog.setNeutralButton("Add new Location", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(context, SelectNewLocation.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivityForResult(intent, ADD_LOCATION_REQUEST);

                }
            });
            builderDialog.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ListView list = ((AlertDialog) dialog).getListView();
                            // make selected item in the comma seprated string
                            for (int i = 0; i < list.getCount(); i++) {
                                if (list.isItemChecked(i)) {
                                    selectedLocations.add(locationList.get(i));
                                }
                            }


                        }
                    });

            builderDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alert = builderDialog.create();
            alert.show();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == ADD_LOCATION_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            }
        }
    }

}








