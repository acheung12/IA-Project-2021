package com.example.iasubstituteteacher.JobsThing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iasubstituteteacher.Jobs.Jobs;
import com.example.iasubstituteteacher.Jobs.OpenJobs;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.SignInThings.ForgotPasswordActivity;
import com.example.iasubstituteteacher.SignInThings.SelectionActivity;
import com.example.iasubstituteteacher.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class AddJobsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    private String selected;

    private EditText subject;
    private EditText date;
    private EditText time;
    private EditText location;
    private EditText lessonPlan;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jobs);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        selected = "";

        subject = findViewById(R.id.addJobsSubject);
        date = findViewById(R.id.addJobsDate);
        time = findViewById(R.id.addJobsTime);
        location = findViewById(R.id.addJobsLocation);
        lessonPlan = findViewById(R.id.addJobsLessonPlan);
    }

    public boolean validInfo()
    {
        String subjectString = subject.getText().toString();
        String dateString = date.getText().toString();
        String timeString = time.getText().toString();
        String locationString = location.getText().toString();
        String lessonPlanString = lessonPlan.getText().toString();

        if (!subjectString.equals("") && !dateString.equals("") && !timeString.equals("") &&
                !locationString.equals("") && !lessonPlanString.equals(""))
        {
            return true;
        }
        return false;
    }

    public void teacher()
    {
        if (validInfo())
        {

            String subjectString = subject.getText().toString();
            String dateString = date.getText().toString();
            String timeString = time.getText().toString();
            String locationString = location.getText().toString();
            String lessonPlanString = lessonPlan.getText().toString();

            String JobsId = UUID.randomUUID().toString();
            String usersUID = user.getUid();
            String usersEmail = user.getEmail();

            Jobs addedJob = new Jobs(JobsId, subjectString, dateString, timeString, locationString,
                    true, lessonPlanString, usersUID, usersEmail);

            firestore.collection("Jobs").document("Jobs").collection(
                    "Requested Jobs").document(dateString).set(addedJob);

            Toast.makeText(AddJobsActivity.this, "Job successfully requested",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, SelectionActivity.class);
            startActivity(intent);
        }
        else if (!validInfo())
        {
            Toast.makeText(AddJobsActivity.this, "Make sure all information is filled",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void admin()
    {
        if (validInfo()) {
            String subjectString = subject.getText().toString();
            String dateString = date.getText().toString();
            String timeString = time.getText().toString();
            String locationString = location.getText().toString();
            String lessonPlanString = lessonPlan.getText().toString();

            String JobsId = UUID.randomUUID().toString();
            String usersUID = user.getUid();
            String usersEmail = user.getEmail();

            Jobs addedJob = new Jobs(JobsId, subjectString, dateString, timeString, locationString,
                    true, lessonPlanString, usersUID, usersEmail);

            firestore.collection("Jobs").document("Jobs").collection(
                    "Open Jobs").document(dateString).set(addedJob);

            Toast.makeText(AddJobsActivity.this, "Job successfully added",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, OpenJobsActivity.class);
            startActivity(intent);
        }
        else if (!validInfo())
        {
            Toast.makeText(AddJobsActivity.this, "Make sure all information is" +
                            " filled in", Toast.LENGTH_SHORT).show();
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

    public void backButton(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }


}