package com.example.iasubstituteteacher.JobsThing;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.iasubstituteteacher.Jobs.OpenJobs;
import com.example.iasubstituteteacher.Jobs.RequestedJobs;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.SignInThings.SelectionActivity;
import com.example.iasubstituteteacher.TimePickerFragment;
import com.example.iasubstituteteacher.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.UUID;

/**
 * AddJobsActivity adds a new Job to the recycler view based on the information that the user types
 * in. Adding the information to the firebase database in order for future use.
 */

public class AddJobsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener
        , DatePickerDialog.OnDateSetListener
{

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    private String selected;

    private EditText subject;
    private EditText date;
    private EditText startTime;
    private EditText endTime;
    private EditText location;
    private EditText lessonPlan;

    public final String[] tag = {""};

    public static final String TIME_PICKER = "initial time picker";
    public static final String TIME_PICKER_2 = "end time picker";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jobs);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        selected = "";

        subject = findViewById(R.id.addJobsSubject);
        date = findViewById(R.id.addJobsDate);
        startTime = findViewById(R.id.addJobsTime);
        endTime = findViewById(R.id.addJobsTime2);
        location = findViewById(R.id.addJobsLocation);
        lessonPlan = findViewById(R.id.addJobsLessonPlan);


        date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment initialTimePicker = TimePickerFragment.instance(TIME_PICKER);
                initialTimePicker.show(getSupportFragmentManager(), "initial time picker");
                tag[0] = initialTimePicker.getTag();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                DialogFragment endTimePicker = TimePickerFragment.instance(TIME_PICKER_2);
                endTimePicker.show(getSupportFragmentManager(), "end time picker");
                tag[0] = endTimePicker.getTag();
            }
        });
    }

    /**
     * This method checks to see if the information the user inputted is valid or not and has the
     * right data type.
     * @return This executes the code till it reaches the point return where once it is reached in
     *         a method, the program returns to the code that invoked it. Meaning return ends the
     *        execution of a function
     */

    public boolean validInfo()
    {
        String subjectString = subject.getText().toString();
        String dateString = date.getText().toString();
        String locationString = location.getText().toString();
        String lessonPlanString = lessonPlan.getText().toString();

        String startTimeString = startTime.getText().toString();
        String[] splitStartTime = startTimeString.split(":");
        String[] afterHourStart = splitStartTime[1].split(" ");

        String endTimeString = endTime.getText().toString();
        String[] splitEndTime = endTimeString.split(":");
        String[] afterHourEnd = splitEndTime[1].split(" ");

        int totalEndTime = Integer.parseInt(splitEndTime[0]) * 60 +
                Integer.parseInt(afterHourEnd[0]);
        int totalStartTime = Integer.parseInt(splitStartTime[0]) * 60 +
                Integer.parseInt(afterHourStart[0]);
        if (Integer.parseInt(splitStartTime[0]) == 12)
        {
            totalStartTime -= 60 * 12;
        }
        if (Integer.parseInt(splitEndTime[0]) == 12)
        {
            totalEndTime -= 60 * 12;
        }

        if (afterHourStart[1].equals("PM"))
        {
            totalStartTime += 60 * 12;
        }
        if (afterHourEnd[1].equals("PM"))
        {
            totalEndTime += 60 * 12;
        }

        if (!subjectString.equals("") && !dateString.equals("") && !startTimeString.equals("") &&
            !endTimeString.equals("") &&!locationString.equals("") && !lessonPlanString.equals(""))
        {
            if (totalStartTime < totalEndTime)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * This method creates a new DatePickerDialog for the user allowing the user to pick the year,
     * month, day.
     */

    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    /**
     * This method adds the information inputted from the user to Jobs/Jobs/Requested Jobs in the
     * firestore database if the method validInfo() returns true.Whilst also creating an intent to
     * move the user to SelectionActivity.
     */

    public void teacher()
    {
        if (validInfo())
        {
            String subjectString = subject.getText().toString();
            String dateString = date.getText().toString();
            String startTimeString = startTime.getText().toString();
            String endTimeString = endTime.getText().toString();
            String locationString = location.getText().toString();
            String lessonPlanString = lessonPlan.getText().toString();

            String timeString = startTimeString + " - " + endTimeString;

            String JobsId = UUID.randomUUID().toString();
            String usersUID = user.getUid();
            String usersEmail = user.getEmail();

            RequestedJobs addedJob = new RequestedJobs(JobsId, subjectString, dateString,
                    timeString, locationString, false, lessonPlanString, usersUID,
                    usersEmail, false);

            firestore.collection("Jobs").document("Jobs").collection
                    ("Requested Jobs").document(JobsId).set(addedJob);

            Toast.makeText(AddJobsActivity.this, "Job successfully requested",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, SelectionActivity.class);
            startActivity(intent);
        }
        else if (!validInfo())
        {
            Toast.makeText(AddJobsActivity.this, "Make sure all information is filled"
                            + "in correctly", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method adds the information inputted from the user to Jobs/Jobs/Open Jobs in the
     * firestore database if the method validInfo() returns true. Whilst also creating an intent to
     * move the user to OpenJobsActivity.
     */

    public void admin()
    {
        if (validInfo())
        {
            String subjectString = subject.getText().toString();
            String dateString = date.getText().toString();
            String startTimeString = startTime.getText().toString();
            String endTimeString = endTime.getText().toString();
            String locationString = location.getText().toString();
            String lessonPlanString = lessonPlan.getText().toString();

            String timeString = startTimeString + " - " + endTimeString;
            String JobsId = UUID.randomUUID().toString();
            String usersUID = user.getUid();
            String usersEmail = user.getEmail();

            OpenJobs addedJob = new OpenJobs(JobsId, subjectString, dateString, timeString,
                    locationString, true, lessonPlanString, usersUID, usersEmail);

            firestore.collection("Jobs").document("Jobs").collection(
                    "Open Jobs").document(JobsId).set(addedJob);

            Toast.makeText(AddJobsActivity.this, "Job successfully added",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, OpenJobsActivity.class);
            startActivity(intent);
        }
        else if (!validInfo())
        {
            Toast.makeText(AddJobsActivity.this, "Make sure all information is" +
                            " filled in correctly", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method calls either the teacher() or admin() method depending on whether the current
     * user is an "Admin" or a "Teacher".
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "ADD JOB" works in accordance to this method.
     */

    public void addJobs(View v)
    {
        firestore.collection("Users").document(user.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot ds = task.getResult();
                            User theUser = ds.toObject(User.class);

                            String userType = theUser.getUserType();
                            selected = userType;

                            if (selected.equals("Teacher"))
                            {
                                teacher();
                            }
                            if(selected.equals("Admin"))
                            {
                                admin();
                            }
                        }
                    }
                });
    }

    /**
     * This method is an intent that is created to move the user to SelectionActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "BACK" works in accordance to this method.
     */

    public void backAddJob(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }

    /**
     * This method retrieves the selected year, month, and day of month selected to set the text on
     * screen once the user finishes selecting the information.
     * @param datePicker this is a date picker allowing the user to select a date from a calendar.
     * @param year this is of type int used to receive the year
     * @param month this is of type int used to receive the month
     * @param dayOfMonth this is of type int used to receive the day of the month
     */

    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth)
    {
        month++;
        String dateSet = dayOfMonth + "/" + month + "/" + year;
        date.setText(dateSet);
    }

    /**
     * This method retrieves the selected hour of the day and minute the user has selected whilst
     * making it a 12 hour clock instead of 24 to set the text on screen once the user finishes
     * selecting the information.
     * @param timePicker this is a time picker allowing the user to select a certain time
     * @param hourOfDay this is of type int used to receive the hour of the day
     * @param minute this is of type int used to receive the minute
     */

    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute)
    {
        String timeSet = "";
        String dayOrNight = "";

        if (hourOfDay > 12)
        {
            hourOfDay = hourOfDay - 12;
            dayOrNight = "PM";
        }
        else if (hourOfDay == 0)
        {
            hourOfDay = hourOfDay + 12;
            dayOrNight = "AM";
        }
        else if (hourOfDay == 12)
        {
            dayOrNight = "PM";
        }
        else
        {
            dayOrNight = "AM";
        }

        String minuteString = String.valueOf(minute);
        String hourString = String.valueOf(hourOfDay);

        if (minute / 10 < 1)
        {
           minuteString = "0" + minute;
        }
        if (hourOfDay / 10 < 1)
        {
            hourString = "0" + hourOfDay;
        }

        timeSet = hourString + ":" + minuteString + " " + dayOrNight;

        if (tag[0].equals("initial time picker"))
        {
            startTime.setText(timeSet);
        }
        else
        {
            endTime.setText(timeSet);
        }
    }
}