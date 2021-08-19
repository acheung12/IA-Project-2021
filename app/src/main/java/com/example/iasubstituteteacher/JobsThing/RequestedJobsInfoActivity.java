package com.example.iasubstituteteacher.JobsThing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

/**
 * RequestedJobsInfoActivity displays all the necessary jobs information to the current user.
 * Letting the user view their requested job.
 */

public class RequestedJobsInfoActivity extends AppCompatActivity
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

    protected void onCreate(Bundle savedInstanceState)
    {
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
     * This method creates a new OpenJob when pressed whilst creating an intent to
     * RequestedJobsEmail along with the current information in this activity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "ACCEPT" works in accordance to this method.
     */

    public void acceptRequest(View v)
    {
        firestore.collection("Jobs/Jobs/Requested Jobs").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
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

        Intent intent = new Intent(this, RequestedJobsEmail.class);
        intent.putExtra("requestSubject", theSubject);
        intent.putExtra("requestJobsID", theJobsId);
        intent.putExtra("requestDate", theDate);
        intent.putExtra("requestTime", theTime);
        intent.putExtra("requestLocation", theLocation);
        intent.putExtra("requestActive", theActive);
        intent.putExtra("requestLessonPlan", theLessonPlan);
        intent.putExtra("requestUsersEmail", theUserEmail);
        intent.putExtra("requestUsersID", theUserUid);
        startActivity(intent);
    }

    /**
     * This method sets the current RequestedJob's Choice to true whilst creating an intent to
     * RequestedJobsEmail along with the current information in this activity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "DECLINE" works in accordance to this method.
     */

    public void declineRequest(View v)
    {
        firestore.collection("Jobs/Jobs/Requested Jobs").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>()
                {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
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

        Intent intent = new Intent(this, RequestedJobsEmail.class);
        intent.putExtra("requestSubject", theSubject);
        intent.putExtra("requestJobsID", theJobsId);
        intent.putExtra("requestDate", theDate);
        intent.putExtra("requestTime", theTime);
        intent.putExtra("requestLocation", theLocation);
        intent.putExtra("requestActive", theActive);
        intent.putExtra("requestLessonPlan", theLessonPlan);
        intent.putExtra("requestUsersEmail", theUserEmail);
        intent.putExtra("requestUsersID", theUserUid);
        startActivity(intent);
    }

    /**
     * This method is an intent that is created to move the user to RequestedJobsActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "BACK" works in accordance to this method.
     */

    public void backRequestInfo(View v)
    {
        Intent intent = new Intent(this, RequestedJobsActivity.class);
        startActivity(intent);
    }
}