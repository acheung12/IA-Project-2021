package com.example.iasubstituteteacher.JobsThing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.SignInThings.SelectionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AcceptedJobsInfoActivity extends AppCompatActivity {

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


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_jobs_info);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        subject = findViewById(R.id.openJobSubjectText);
        date = findViewById(R.id.openJobDateText);
        time = findViewById(R.id.openJobTimeText);
        location = findViewById(R.id.openJobLocationText);
        lessonPlan = findViewById(R.id.openJobLessonPlanText);

        theActive = getIntent().getStringExtra("active");
        theSubject = getIntent().getStringExtra("subject");
        theDate = getIntent().getStringExtra("date");
        theTime = getIntent().getStringExtra("time");
        theLocation = getIntent().getStringExtra("location");
        theLessonPlan = getIntent().getStringExtra("lessonPlan");
        theUserEmail = getIntent().getStringExtra("usersEmail");
        theUserUid = getIntent().getStringExtra("usersID");
        theJobsId = getIntent().getStringExtra("jobsID");
        theAcceptorsEmail = getIntent().getStringExtra("acceptorsEmail");

        setUpButtons();
    }

    public void setUpButtons()
    {
        subject.setText(theSubject);
        date.setText("Date: " + theDate);
        time.setText("Time: " + theTime);
        location.setText("Location: " + theLocation);
        lessonPlan.setText("Lesson Plan: " + theLessonPlan);
    }

    public void notifications()
    {

    }


    public void backAcceptInfo(View v)
    {
        Intent intent = new Intent(this, AcceptedJobsActivity.class);
        startActivity(intent);
    }
}