package com.example.iasubstituteteacher.JobsThing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.iasubstituteteacher.Jobs.AcceptedJobs;
import com.example.iasubstituteteacher.Jobs.OpenJobs;
import com.example.iasubstituteteacher.Jobs.RequestedJobs;
import com.example.iasubstituteteacher.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class RequestedJobsInfoActivity extends AppCompatActivity {

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
    private String theChoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_jobs_info);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        subject = findViewById(R.id.requestedJobsSubjectText);
        date = findViewById(R.id.requestedJobDateText);
        time = findViewById(R.id.requestedJobTimeText);
        location = findViewById(R.id.requestedJobLocationText);
        lessonPlan = findViewById(R.id.requestedJobLessonPlanText);

        theActive = getIntent().getStringExtra("requestActive");
        theSubject = getIntent().getStringExtra("requestSubject");
        theDate = getIntent().getStringExtra("requestDate");
        theTime = getIntent().getStringExtra("requestTime");
        theLocation = getIntent().getStringExtra("requestLocation");
        theLessonPlan = getIntent().getStringExtra("requestLessonPlan");
        theUserEmail = getIntent().getStringExtra("requestUsersEmail");
        theUserUid = getIntent().getStringExtra("requestUsersID");
        theJobsId = getIntent().getStringExtra("requestJobsID");

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

    public void acceptRequest(View v)
    {
        firestore.collection("Jobs/Jobs/Requested Jobs").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot document : task.getResult().getDocuments())
                            {
                                RequestedJobs theRequestedJobs = document.toObject
                                        (RequestedJobs.class);
                                theRequestedJobs.setChoice(true);
                                theRequestedJobs.setActive(true);
                                firestore.collection("Jobs/Jobs/Requested Jobs").
                                        document(theJobsId).set(theRequestedJobs);
                            }
                        }
                    }
                });
        OpenJobs openJob = new OpenJobs(theJobsId, theSubject, theDate, theTime,
                theLocation, true, theLessonPlan, theUserUid, theUserEmail);

        firestore.collection("Jobs").document("Jobs").collection(
                "Open Jobs").document(theJobsId).set(openJob);

        Intent intent = new Intent(this, RequestedJobsActivity.class);
        startActivity(intent);
    }

    public void declineRequest(View v)
    {
        firestore.collection("Jobs/Jobs/Requested Jobs").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot document : task.getResult().getDocuments())
                            {
                                RequestedJobs theRequestedJobs = document.toObject
                                        (RequestedJobs.class);
                                theRequestedJobs.setChoice(true);
                                firestore.collection("Jobs/Jobs/Requested Jobs").
                                        document(theJobsId).set(theRequestedJobs);
                            }
                        }
                    }
                });
        Intent intent = new Intent(this, RequestedJobsActivity.class);
        startActivity(intent);
    }

    public void backRequestInfo(View v)
    {
        Intent intent = new Intent(this, RequestedJobsActivity.class);
        startActivity(intent);
    }
}