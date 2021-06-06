package com.example.iasubstituteteacher.SignInThings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iasubstituteteacher.JobsThing.AcceptedJobsActivity;
import com.example.iasubstituteteacher.JobsThing.AddJobsActivity;
import com.example.iasubstituteteacher.JobsThing.OpenJobsActivity;
import com.example.iasubstituteteacher.JobsThing.RequestedJobsActivity;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SelectionActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        selected = "";
    }

    public void RequestedJobs()
    {
        Intent intent = new Intent(this, RequestedJobsActivity.class);
        startActivity(intent);
    }

    public void toRequestedJobs(View v)
    {
        FirebaseUser user = mAuth.getCurrentUser();

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

                            if (selected.equals("Admin"))
                            {
                                RequestedJobs();
                            }
                            else
                            {
                                Toast.makeText(SelectionActivity.this, "You need to" +
                                        " be an Admin to access this", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void AddJobs()
    {
        Intent intent = new Intent(this, AddJobsActivity.class);
        startActivity(intent);
    }


    public void toAddJobs(View v)
    {
        FirebaseUser user = mAuth.getCurrentUser();

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

                            if ((selected.equals("Admin")) || (selected.equals("Teacher")))
                            {
                                AddJobs();
                            }
                            else
                            {
                                Toast.makeText(SelectionActivity.this, "You need " +
                                        "to be an Admin or a Teacher to access this",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public void toOpenJobs(View v)
    {
        Intent intent = new Intent(this, OpenJobsActivity.class);
        startActivity(intent);
    }

    public void toAcceptedJobs(View v)
    {
        Intent intent = new Intent(this, AcceptedJobsActivity.class);
        startActivity(intent);
    }

    public void signOut(View v)
    {
        try
        {
            mAuth.signOut();
            Toast.makeText(SelectionActivity.this, "Successfully signed out",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        catch (Exception err)
        {
            Toast messageToUser = Toast.makeText(this, err.toString(), Toast.LENGTH_LONG);
            messageToUser.show();
        }
    }

}