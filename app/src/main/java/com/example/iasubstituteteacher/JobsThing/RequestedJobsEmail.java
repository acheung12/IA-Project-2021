package com.example.iasubstituteteacher.JobsThing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iasubstituteteacher.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * RequestedJobsEmail is a screen to let the Admin send an Email to the user who created the job
 * letting the user know whether their RequestedJob has been accepted or rejected.
 */

public class RequestedJobsEmail extends AppCompatActivity
{
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore firestore;

    private EditText editTextTo;
    private EditText editTextSubject;
    private EditText editTextMessage;

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
    private String theUserUsername;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_jobs_email);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        theActive = getIntent().getStringExtra("requestActive");
        theSubject = getIntent().getStringExtra("requestSubject");
        theDate = getIntent().getStringExtra("requestDate");
        theTime = getIntent().getStringExtra("requestTime");
        theLocation = getIntent().getStringExtra("requestLocation");
        theLessonPlan = getIntent().getStringExtra("requestLessonPlan");
        theUserEmail = getIntent().getStringExtra("requestUsersEmail");
        theUserUid = getIntent().getStringExtra("requestUsersID");
        theJobsId = getIntent().getStringExtra("requestJobsID");
        theChoice = getIntent().getStringExtra("requestChoice");
        theUserUsername = getIntent().getStringExtra("requestUsersUsername");

        editTextTo = findViewById(R.id.editTextTo);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);

        settingInformation();
    }

    /**
     * This method creates an intent to allow the user to choose an email client they would like,
     * whilst decoding the information written on the RequestedJobsEmail activity.
     * @param v this is what the user sees when the android studio is run and the app appears.
     */

    public void sendingAnEmail(View v)
    {
        String mailingList = editTextTo.getText().toString();
        String[] people = mailingList.split(",");

        String subject = editTextSubject.getText().toString();
        String message = editTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, people);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Please choose an email client"));
    }

    /**
     * This method is an intent that is created to move the user to RequestedJobsActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "BACK" works in accordance to this method.
     */

    public void backToRequest(View v)
    {
        Intent goingBack = new Intent(this, RequestedJobsActivity.class);
        startActivity(goingBack);
    }

    /**
     * This method automatically sets the information of the email with the Requested Job chosen
     * automatically.
     */

    public void settingInformation()
    {
        String to = theUserEmail;
        String subject = "Requested Job";
        String message = "";
        if (theActive.equals("true"))
        {
            message = "Hey User,\n\n" +
                    "Great news your following requested job has been accepted. Below is the" +
                    " substitute job you requested: \n" +
                    "   Subject: " + theSubject + "\n" +
                    "   Date: " + theDate + "\n" +
                    "   Time: " + theTime + "\n" +
                    "   Location: " + theLocation + "\n" +
                    "   Lesson Plan: " + theLessonPlan + "\n\n" +
                    "Kind regards, \n" +
                    "The Admin Team";
        }
        else if (theActive.equals("false"))
        {
            message = "Hey User,\n" +
                    "Unfortunately your following requested job has been declined. Below is the" +
                    " substitute job you requested: \n" +
                    "   Subject: " + theSubject + "\n" +
                    "   Date: " + theDate + "\n" +
                    "   Time: " + theTime + "\n" +
                    "   Location: " + theLocation + "\n" +
                    "   Lesson Plan: " + theLessonPlan + "\n\n" +
                    "Kind regards, \n" +
                    "The Admin Team";
        }

        editTextTo.setText(to);
        editTextSubject.setText(subject);
        editTextMessage.setText(message);

        firestore.collection("Jobs/Jobs/Requested Jobs").document(theJobsId).delete();
    }
}
