package com.example.iasubstituteteacher.JobsThing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.iasubstituteteacher.Jobs.OpenJobs;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.RecyclerView.OpenJobsAdapter;
import com.example.iasubstituteteacher.SignInThings.SelectionActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OpenJobsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    private ArrayList<OpenJobs> openJobsList;
    private RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_jobs);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        recView = findViewById(R.id.recView);

        openJobsList = new ArrayList<OpenJobs>();

        getAndPopulateData();
    }

    public void getAndPopulateData()
    {
        firestore.collection("Jobs/Jobs/Open Jobs").get().addOnCompleteListener
                (new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful())
                    {
                        for (DocumentSnapshot document : task.getResult().getDocuments())
                        {
                            OpenJobs theOpenJobs = document.toObject(OpenJobs.class);
                            if (theOpenJobs.isActive())
                            {
                                openJobsList.add(theOpenJobs);
                            }
                        }
                        helperMethod(openJobsList);
                    }
                }
        });
    }

    public void helperMethod(ArrayList<OpenJobs> o)
    {
        openJobsList = o;
        OpenJobsAdapter myAdapter = new OpenJobsAdapter (openJobsList, this);
        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }


    public void backButton(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }




}