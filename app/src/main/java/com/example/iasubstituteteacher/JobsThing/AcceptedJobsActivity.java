package com.example.iasubstituteteacher.JobsThing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.iasubstituteteacher.Jobs.AcceptedJobs;
import com.example.iasubstituteteacher.Jobs.RequestedJobs;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.RecyclerView.AcceptedJobsAdapter;
import com.example.iasubstituteteacher.RecyclerView.RequestedJobsAdapter;
import com.example.iasubstituteteacher.SignInThings.SelectionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class AcceptedJobsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;


    private ArrayList<AcceptedJobs> acceptedJobsList;
    private RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_jobs);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        recView = findViewById(R.id.acceptedJobRecView);

        acceptedJobsList = new ArrayList<AcceptedJobs>();

        getAndPopulateData();
    }

    public void getAndPopulateData()
    {
        //Do stuff here dont forget
    }


    public void helperMethod(ArrayList<AcceptedJobs> a)
    {
        acceptedJobsList = a;
        AcceptedJobsAdapter myAdapter = new AcceptedJobsAdapter (acceptedJobsList, this);
        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void backButton(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }

}