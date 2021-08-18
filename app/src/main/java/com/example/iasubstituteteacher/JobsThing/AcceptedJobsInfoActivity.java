package com.example.iasubstituteteacher.JobsThing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iasubstituteteacher.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * AcceptedJobsInfoActivity displays all the necessary jobs information to the current user. Letting
 * the user view their accepted job.
 */

public class AcceptedJobsInfoActivity extends AppCompatActivity
{
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;

    private TextView subject;
    private TextView date;
    private TextView time;
    private TextView location;
    private TextView lessonPlan;

    private String theActive;
    private String theSubject;
    private String theDate;
    private String theTime;
    private String theLocation;
    private String theLessonPlan;
    private String theUserEmail;
    private String theUserUid;
    private String theJobsId;
    private String theAcceptorsEmail;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_jobs_info);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        subject = findViewById(R.id.acceptedJobSubjectText);
        date = findViewById(R.id.acceptedJobDateText);
        time = findViewById(R.id.acceptedJobTimeText);
        location = findViewById(R.id.acceptedJobLocationText);
        lessonPlan = findViewById(R.id.acceptedJobLessonPlanText);

        theActive = getIntent().getStringExtra("acceptActive");
        theSubject = getIntent().getStringExtra("acceptSubject");
        theDate = getIntent().getStringExtra("acceptDate");
        theTime = getIntent().getStringExtra("acceptTime");
        theLocation = getIntent().getStringExtra("acceptLocation");
        theLessonPlan = getIntent().getStringExtra("acceptLessonPlan");
        theUserEmail = getIntent().getStringExtra("acceptUsersEmail");
        theUserUid = getIntent().getStringExtra("acceptUsersID");
        theJobsId = getIntent().getStringExtra("acceptJobsID");
        theAcceptorsEmail = getIntent().getStringExtra("acceptAcceptorsEmail");

        setUpText();
    }

    /**
     * This method sets up the information seen on the screen.
     */

    public void setUpText()
    {
        subject.setText(theSubject);
        date.setText("Date: " + theDate);
        time.setText("Time: " + theTime);
        location.setText("Location: " + theLocation);
        lessonPlan.setText("Lesson Plan: " + theLessonPlan);
    }

    /**
     * This method is an intent that is created to move the user to AcceptedJobsActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "BACK" works in accordance to this method.
     */

    public void backAcceptInfo(View v)
    {
        Intent intent = new Intent(this, AcceptedJobsActivity.class);
        startActivity(intent);
    }
}