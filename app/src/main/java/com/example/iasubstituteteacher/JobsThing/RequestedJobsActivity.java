package com.example.iasubstituteteacher.JobsThing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.iasubstituteteacher.Jobs.RequestedJobs;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.RecyclerView.RequestedJobsAdapter;
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

public class RequestedJobsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;


    private ArrayList<RequestedJobs> requestedJobsList;
    private RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_jobs);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        recView = findViewById(R.id.requestedJobRecView);

        requestedJobsList = new ArrayList<RequestedJobs>();

        getAndPopulateData();
    }

    public void getAndPopulateData()
    {
        firestore.collection("Jobs/Jobs/Requested Jobs").get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (DocumentSnapshot document : task.getResult().getDocuments())
                            {
                                RequestedJobs theRequestedJobs = document.
                                        toObject(RequestedJobs.class);
                                if (theRequestedJobs.isActive() == false &&
                                        theRequestedJobs.isChoice() == false)
                                {
                                    requestedJobsList.add(theRequestedJobs);
                                }
                            }
                            helperMethod(requestedJobsList);
                        }
                    }
                });
    }

    public void helperMethod(ArrayList<RequestedJobs> r)
    {
        requestedJobsList = r;
        RequestedJobsAdapter myAdapter = new RequestedJobsAdapter (requestedJobsList, this);
        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void backRequestActivity(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }
}