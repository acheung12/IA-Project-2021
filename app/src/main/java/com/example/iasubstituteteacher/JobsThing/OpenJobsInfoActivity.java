package com.example.iasubstituteteacher.JobsThing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.iasubstituteteacher.Jobs.AcceptedJobs;
import com.example.iasubstituteteacher.Jobs.OpenJobs;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.SignInThings.SelectionActivity;
import com.example.iasubstituteteacher.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OpenJobsInfoActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_jobs_info);

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

    public void acceptOpenJob(View v)
    {
        firestore.collection("Jobs/Jobs/Open Jobs").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot document : task.getResult().getDocuments())
                            {
                                OpenJobs theOpenJobs = document.toObject(OpenJobs.class);
                                theOpenJobs.setActive(false);
                                firestore.collection("Jobs/Jobs/Open Jobs").document(
                                        theJobsId).set(theOpenJobs);
                            }
                        }
                    }
                });

        firestore.collection("Users").document(user.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot ds = task.getResult();
                            User theUser = ds.toObject(User.class);
                            ArrayList<String> acceptedList = theUser.getAcceptedJobs();
                            acceptedList.add(theJobsId);
                            theUser.setAcceptedJobs(acceptedList);

                            firestore.collection("Users").document(theUserUid).
                                    set(theUser);
                        }
                    }
                });

        AcceptedJobs acceptedJob = new AcceptedJobs(theJobsId, theSubject, theDate, theTime,
                theLocation, false, theLessonPlan, theUserUid, theUserEmail, user.getUid());

        firestore.collection("Jobs").document("Jobs").collection(
                "Accepted Jobs").document(theJobsId).set(acceptedJob);

        Intent intent = new Intent(this, OpenJobsActivity.class);
        startActivity(intent);
    }

    public void declineOpenJob(View v)
    {
        firestore.collection("Users").document(theUserUid).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                      if (task.isSuccessful())
                      {
                          DocumentSnapshot ds = task.getResult();
                          User theUser = ds.toObject(User.class);
                          ArrayList<String> declinedList = theUser.getDeclinedJobs();
                          declinedList.add(theJobsId);
                          theUser.setDeclinedJobs(declinedList);

                          firestore.collection("Users").document(theUserUid).
                                  set(theUser);
                      }
                  }
              });

        Intent intent = new Intent(this, OpenJobsActivity.class);
        startActivity(intent);
    }

    public void backOpenInfo(View v)
    {
        Intent intent = new Intent(this, OpenJobsActivity.class);
        startActivity(intent);
    }
}