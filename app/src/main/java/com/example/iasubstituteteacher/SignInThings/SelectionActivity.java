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

/**
 * SelectionActivity is a screen that lets the user pick the next screen they would like to go
 * whether it is to see the available jobs, see their accepted jobs. Also, depending on what type of
 * user they are they could add a job or look at the requested jobs out there. This activity
 * functions like a home page to this app.
 */

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

    /**
     * This method is an intent that is created to move the user to RequestedJobsActivity.
     */

    public void RequestedJobs()
    {
        Intent intent = new Intent(this, RequestedJobsActivity.class);
        startActivity(intent);
    }

    /**
     * This method calls the method RequestedJobs() after checking to see if the current user is of
     * user type "Admin" or not.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the button that works in accordance to this method is REQUESTED JOBS".
     */

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

    /**
     * This method is an intent that is created to move the user to AddJobsActivity.
     */

    public void AddJobs()
    {
        Intent intent = new Intent(this, AddJobsActivity.class);
        startActivity(intent);
    }

    /**
     * This method calls the method AddJobs() after checking to see if the current user is of
     * user type "Admin" or either "Teacher".
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the button that works in accordance to this method is "ADD JOBS".
     */

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

    /**
     * This method is an intent that is created to move the user to OpenJobsActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the button that works in accordance to this method is "OPEN JOBS".
     */

    public void toOpenJobs(View v)
    {
        Intent intent = new Intent(this, OpenJobsActivity.class);
        startActivity(intent);
    }

    /**
     * This method is an intent that is created to move the user to AcceptedJobsActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the button that works in accordance to this method is "ACCEPTED JOBS".
     */

    public void toAcceptedJobs(View v)
    {
        Intent intent = new Intent(this, AcceptedJobsActivity.class);
        startActivity(intent);
    }

    /**
     * This method signs the user out of the app, creating an intent to return the user back to
     * MainActivity, sending a Toast message to the user whether the operation is successful or not.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the button that works in accordance to this method is "SIGN OUT".
     */

    public void signOut(View v)
    {
        try
        {
            Toast.makeText(SelectionActivity.this, "Successfully signed out",
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            mAuth.signOut();
            finish();
        }
        catch (Exception err)
        {
            Toast messageToUser = Toast.makeText(this, err.toString(), Toast.LENGTH_LONG);
            messageToUser.show();
        }
    }
}