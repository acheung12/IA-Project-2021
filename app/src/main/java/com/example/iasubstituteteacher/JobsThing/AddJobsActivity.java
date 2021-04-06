package com.example.iasubstituteteacher.JobsThing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;

import com.example.iasubstituteteacher.Jobs.OpenJobs;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.SignInThings.ForgotPasswordActivity;
import com.example.iasubstituteteacher.SignInThings.SelectionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddJobsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_jobs);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        selected = "";
    }

    public void checkIfValid()
    {

    }


    public void addJobs(View v)
    {
//        if (selected.equals("Teacher"))
//        {
//            Intent intent = new Intent(this, SelectionActivity.class);
//            startActivity(intent);
//        }
//        else if (selected.equals("Admin"))
//        {
//            Intent intent = new Intent(this, OpenJobsActivity.class);
//            startActivity(intent);
//        }

        Intent intent = new Intent(this, OpenJobsActivity.class);
        startActivity(intent);
    }

    public void backButton(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }


}