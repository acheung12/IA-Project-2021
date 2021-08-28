package com.example.iasubstituteteacher.JobsThing;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.iasubstituteteacher.Jobs.AcceptedJobs;
import com.example.iasubstituteteacher.Jobs.OpenJobs;
import com.example.iasubstituteteacher.Notifications;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Random;

/**
 * OpenJobsInfoActivity displays all the necessary jobs information to the current user. Letting
 * the user view their open job.
 */

public class OpenJobsInfoActivity extends AppCompatActivity
{
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;

    private TextView subject;
    private TextView date;
    private TextView time;
    private TextView location;
    private TextView lessonPlan;
    private Button acceptButton;

    private String theActive;
    private String theSubject;
    private String theDate;
    private String theTime;
    private String theLocation;
    private String theLessonPlan;
    private String theUserEmail;
    private String theUserUid;
    private String theJobsId;

    private NotificationManagerCompat notificationManager;

    protected void onCreate(Bundle savedInstanceState)
    {
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

        theActive = getIntent().getStringExtra("openActive");
        theSubject = getIntent().getStringExtra("openSubject");
        theDate = getIntent().getStringExtra("openDate");
        theTime = getIntent().getStringExtra("openTime");
        theLocation = getIntent().getStringExtra("openLocation");
        theLessonPlan = getIntent().getStringExtra("openLessonPlan");
        theUserEmail = getIntent().getStringExtra("openUsersEmail");
        theUserUid = getIntent().getStringExtra("openUsersID");
        theJobsId = getIntent().getStringExtra("openJobsID");

        acceptButton = findViewById(R.id.openJobInfoAcceptButton);
        notificationManager = NotificationManagerCompat.from(this);

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
     * This method transfers the current OpenJob to the current user's AcceptedJobs, sending a
     * notification and toast message that the job has successfully been accepted. Also, adding the
     * current OpenJob to the current user's AcceptedJobs list, whilst creating an intent to
     * OpenJobsActivity.
     * intent to OpenJobsActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "ACCEPT" works in accordance to this method.
     */

    public void acceptOpenJob(View v)
    {
        firestore.collection("Jobs/Jobs/Open Jobs").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>()
                {
                    public void onComplete(@NonNull Task<QuerySnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot document : task.getResult().getDocuments())
                            {
                                OpenJobs theOpenJobs = document.toObject(OpenJobs.class);
                                firestore.collection("Jobs/Jobs/Open Jobs").document(
                                        theJobsId).delete();
                            }
                        }
                    }
                });

        firestore.collection("Users").document(user.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                    public void onComplete(@NonNull Task<DocumentSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot ds = task.getResult();
                            User theUser = ds.toObject(User.class);
                            ArrayList<String> acceptedList = new ArrayList<>();

                            if (!theUser.getAcceptedJobs().isEmpty())
                            {
                                acceptedList = theUser.getAcceptedJobs();
                            }

                            acceptedList.add(theJobsId);
                            theUser.setAcceptedJobs(acceptedList);

                            firestore.collection("Users").document(user.getUid()).
                                    set(theUser);
                        }
                    }
                });

        AcceptedJobs acceptedJob = new AcceptedJobs(theJobsId, theSubject, theDate, theTime,
                theLocation, false, theLessonPlan, theUserUid, theUserEmail,
                user.getEmail());

        firestore.collection("Jobs").document("Jobs").collection(
                "Accepted Jobs").document(theJobsId).set(acceptedJob);

        sendingNotifications();

        Intent intent = new Intent(this, OpenJobsActivity.class);
        startActivity(intent);

        Toast.makeText(OpenJobsInfoActivity.this, "Job has successfully been accepted",
                Toast.LENGTH_SHORT).show();
    }

    /**
     * This method adds the current OpenJob to the users declinedJobs creating an intent to
     * OpenJobsActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "DECLINE" works in accordance to this method.
     */

    public void declineOpenJob(View v)
    {
        firestore.collection("Users").document(user.getUid()).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
                {
                  public void onComplete(Task<DocumentSnapshot> task)
                  {
                      if (task.isSuccessful())
                      {
                          DocumentSnapshot ds = task.getResult();
                          User theUser = ds.toObject(User.class);
                          ArrayList<String> declinedList = new ArrayList<>();

                          if (!theUser.getDeclinedJobs().isEmpty())
                          {
                              declinedList = theUser.getDeclinedJobs();
                          }

                          declinedList.add(theJobsId);
                          theUser.setDeclinedJobs(declinedList);

                          firestore.collection("Users").document(user.getUid()).
                                  set(theUser);
                      }
                  }
              });

        Intent intent = new Intent(this, OpenJobsActivity.class);
        startActivity(intent);
    }

    /**
     * This method sends a notification to the user with the following information regarding the
     * accepted job.
     */

    public void sendingNotifications()
    {
        String title = "Accepted Open Job";
        String text = "Subject: " + theSubject + "\n" +
                "Date: " + theDate + "\n" +
                "Time: " + theTime + "\n" +
                "Location: " + theLocation + "\n" +
                "Lesson Plan: " + theLessonPlan;

        Notification notification =  new NotificationCompat.Builder(this,
                Notifications.CHANNEL_ID)
                .setSmallIcon(R.drawable.cis_logo)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        Random rand = new Random();
        int max = 2147483647;
        int id = rand.nextInt(max);

        notificationManager.notify(id, notification);
    }

    /**
     * This method is an intent that is created to move the user to OpenJobsActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "BACK" works in accordance to this method.
     */

    public void backOpenInfo(View v)
    {
        Intent intent = new Intent(this, OpenJobsActivity.class);
        startActivity(intent);
    }
}