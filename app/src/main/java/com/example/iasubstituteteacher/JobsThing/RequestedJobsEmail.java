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

public class RequestedJobsEmail extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        editTextTo = findViewById(R.id.editTextTo);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);

        settingInformation();
    }

    public void sendingAnEmail(View v)
    {
        String mailingList = editTextTo.getText().toString();
        String[] people = mailingList.split(",");

        String subject = editTextSubject.getText().toString();
        String message = editTextMessage.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, people);
        intent.putExtra(Intent.EXTRA_SUBJECT, people);
        intent.putExtra(Intent.EXTRA_TEXT, people);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Please choose an email client"));
    }

    public void settingInformation()
    {
        String to = theUserEmail;
        String subject = "Requested Job";
        String message = "";

        if (theActive.equals("true"))
        {
            message = "Your following requested job has been accepted: \n" +
                    "Subject: " + theSubject + "\n" +
                    "Date: " + theDate + "\n" +
                    "Time: " + theTime + "\n" +
                    "Location: " + theLocation + "\n" +
                    "Lesson Plan: " + theLessonPlan;
        }
        else if (theActive.equals("false"))
        {
            message = "Your following requested job has been declined: \n" +
                    "Subject: " + theSubject + "\n" +
                    "Date: " + theDate + "\n" +
                    "Time: " + theTime + "\n" +
                    "Location: " + theLocation + "\n" +
                    "Lesson Plan: " + theLessonPlan;
        }

        editTextTo.setText(to);
        editTextSubject.setText(subject);
        editTextMessage.setText(message);

        firestore.collection("Jobs/Jobs/Requested Jobs").
                document(theJobsId).delete();
    }
}
