package de.unima.pc2016.taskloc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class AddNewTask extends AppCompatActivity {

    //constant
    private static final String MSG_NO_INPUT = "Please insert missing information.";


    //EditText & TextView
    private EditText txtInsertTitle = null;
    private EditText txtDescription = null;
    private EditText taskLocation = null;
    private EditText dateFrom = null;
    private EditText dateTo = null;
    private TextView txtRange = null;

    //button
    private Button buttonSave = null;
    private Button buttonCancel = null;


    //SeekBar
    private SeekBar rangeBar = null;
    //private int outputRange = rangeBar.getProgress();




    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

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

        //Bar and Init of default value
        this.rangeBar = (SeekBar) findViewById(R.id.rangeBar);



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
                //TODO insert Task export and move to main view
                if(txtInsertTitle == null){
                Toast.makeText(v.getContext(), MSG_NO_INPUT, Toast.LENGTH_LONG).show();
                }
                else{
                   // startActivity(main);
                }
            }
        });

        //ListenerCancel
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(main);
            }
        });

        setOnclick(this.dateFrom);
        setOnclick(this.dateTo);



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
    }






}








