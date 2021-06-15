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

public class AddJobsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener
        , DatePickerDialog.OnDateSetListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    private String selected;
    private String usersUsername;

    private EditText subject;
    private EditText date;
    private EditText startTime;
    private EditText endTime;
    private EditText location;
    private EditText lessonPlan;

    public final String[] tag = {""};

    public static final String TIME_PICKER = "initial time picker";
    public static final String TIME_PICKER_2 = "end time picker";


    @Override
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


        date.setOnClickListener(new View.OnClickListener() {
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

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment endTimePicker = TimePickerFragment.instance(TIME_PICKER_2);
                endTimePicker.show(getSupportFragmentManager(), "end time picker");
                tag[0] = endTimePicker.getTag();
            }
        });
    }

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

    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

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

    public void admin()
    {
        if (validInfo()) {
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

    public void backAddJob(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        month++;
        String dateSet = dayOfMonth + "/" + month + "/" + year;
        date.setText(dateSet);
    }

    @Override
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