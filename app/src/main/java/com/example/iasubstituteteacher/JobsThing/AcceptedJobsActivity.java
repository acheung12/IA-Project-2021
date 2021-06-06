package com.example.iasubstituteteacher.JobsThing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iasubstituteteacher.Jobs.AcceptedJobs;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.RecyclerView.AcceptedJobsAdapter;
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
        firestore.collection("Users").document(user.getUid()).get().
            addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful())
                    {
                        DocumentSnapshot ds = task.getResult();
                        User theUser = ds.toObject(User.class);

                        firestore.collection("Jobs/Jobs/Accepted Jobs").get().
                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful())
                                    {
                                        for (int i = 0; i < theUser.getAcceptedJobs().size();
                                             i++)
                                        {
                                            String acceptedJob = theUser.getAcceptedJobs().
                                                    get(i);
                                            for (DocumentSnapshot document : task.getResult().
                                                getDocuments())
                                            {
                                                AcceptedJobs theAcceptedJobs = document.toObject
                                                    (AcceptedJobs.class);

                                                if (theAcceptedJobs.getJobsId().equals(acceptedJob))
                                                {
                                                    acceptedJobsList.add(theAcceptedJobs);
                                                }
                                            }
                                        }
                                        uniquefy(acceptedJobsList);
                                        helperMethod(acceptedJobsList);
                                    }
                                }
                            });
                    }
                }
            });
    }



    public static <T> ArrayList<T> uniquefy(ArrayList<T> myList) {

        ArrayList <T> uniqueArrayList = new ArrayList<T>();
        for (int i = 0; i < myList.size(); i++)
        {
            if (!uniqueArrayList.contains(myList.get(i)))
            {
                uniqueArrayList.add(myList.get(i));
            }
        }

        return uniqueArrayList;
    }

    public void helperMethod(ArrayList<AcceptedJobs> a)
    {
        acceptedJobsList = a;
        AcceptedJobsAdapter myAdapter = new AcceptedJobsAdapter (acceptedJobsList, this);
        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void backAcceptActivity(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }

}