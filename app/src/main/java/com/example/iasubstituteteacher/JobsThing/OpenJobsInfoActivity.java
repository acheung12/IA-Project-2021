package com.example.iasubstituteteacher.JobsThing;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class OpenJobsInfoActivity extends AppCompatActivity {

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

    public void sendEmailNotification()
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
                            }
                        }
                    }
                });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username = "substituteteacherapp@gmail.com";
                final String password = "CISsubstitute123";
                String emailSubject = "Accepted Open Job";
                String messageToSend = "Your following submitted job has been accepted: \n" +
                        "Subject: " + theSubject + "\n" +
                        "Date: " + theDate + "\n" +
                        "Time: " + theTime + "\n" +
                        "Location: " + theLocation + "\n" +
                        "Lesson Plan: " + theLessonPlan + "\n";

                Properties props = new Properties();
                props.put("mail.smtp.auth","true");
                props.put("mail.smtp.starttls.enable","true");
                props.put("mail.smtp.host","smtp@gmail.com");
                props.put("mail.smtp.port","465");

                Session session = Session.getInstance(props, new javax.mail.Authenticator(){
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

                try{
                    String OpenJobsEmail = "substituteteacherapp@gmail.com";

                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(OpenJobsEmail));
                    message.setSubject(emailSubject);
                    message.setText(messageToSend);

                    Transport.send(message);
                }

                catch(MessagingException e){
                    throw new RuntimeException(e);
                }
            }
        });

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

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

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("d/M/yyyy");
        String date = dateFormatter.format(c.getTime());
        SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm a");
        String time = timeFormatter.format(c.getTime());

        String ampm = time.substring(Math.max(time.length() - 2, 0));

        if (ampm.equals("am"))
        {
            ampm = "AM";
        }
        else if (ampm.equals("pm"))
        {
            ampm = "PM";
        }
        time = time.substring(0, time.length() - 2);
        time += ampm;

        String[] startTime = theTime.split(" - ");
        if (theDate.equals(date))
        {
            if (startTime[0].equals(time))
            {
                notificationManager.notify(id, notification);
            }
        }

    }

    private void startAlarm(Calendar c)
    {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, OpenJobsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
    }


    public void backOpenInfo(View v)
    {
        Intent intent = new Intent(this, OpenJobsActivity.class);
        startActivity(intent);
    }
}