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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * AcceptedJobsActivity is used to display all the AcceptedJobs from the user to the recycler view
 * as long as the date has not passed.
 */

public class AcceptedJobsActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;


    private ArrayList<AcceptedJobs> acceptedJobsList;
    private RecyclerView recView;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accepted_jobs);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        recView = findViewById(R.id.acceptedJobRecView);

        acceptedJobsList = new ArrayList<AcceptedJobs>();

        getAndPopulateData();
    }

    /**
     * This method retrieves all the AcceptedJobs in the data and turns it to a recycler view with
     * the help of the helperMethod below. Only showing the AcceptedJobs that have not passed the
     * current date and time.
     */

    public void getAndPopulateData()
    {
        DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        String dateString = dateFormat.format(new Date());
        String[] calendarDateString = dateString.split("/");
        String currentDay = calendarDateString[0];
        String currentMonth = calendarDateString[1];
        String currentYear = calendarDateString[2];

        DateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
        String timeString = timeFormat.format(new Date());
        String[] calendarTimeString = timeString.split(":");
        String currentHour = calendarTimeString[0];
        String[] afterHour = calendarTimeString[1].split(" ");
        String currentMinute = afterHour[0];
        String currentAmPm = afterHour[1];

        final String[] theDate = {""};
        final String[] theTime = {""};

        firestore.collection("Users").document(user.getUid()).get().
            addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        DocumentSnapshot ds = task.getResult();
                        User theUser = ds.toObject(User.class);

                        firestore.collection("Jobs/Jobs/Accepted Jobs").get().
                            addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task)
                                {
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
                                                theDate[0] = theAcceptedJobs.getDate();
                                                String[] splittingDay = theDate[0].
                                                        split("/");

                                                String openDay = splittingDay[0];
                                                String openMonth = splittingDay[1];
                                                String openYear = splittingDay[2];

                                                theTime[0] = theAcceptedJobs.getTime();

                                                String[] split = theTime[0].split(" - ");
                                                String[] timeSplit = split[0].split(":");
                                                String[] afterSplit = timeSplit[1].
                                                        split(" ");

                                                String openHour = timeSplit[0];
                                                String openMinute = afterSplit[0];
                                                String openAMPM = afterSplit[1];

                                                if (Integer.parseInt(openYear) == Integer.
                                                    parseInt(currentYear))
                                                {
                                                    if (Integer.parseInt(openMonth) == Integer.
                                                        parseInt(currentMonth))
                                                    {
                                                        if (Integer.parseInt(openDay) == Integer.
                                                            parseInt(currentDay))
                                                        {
                                                            if (openAMPM.equals(currentAmPm))
                                                            {
                                                                if (Integer.parseInt(openHour) ==
                                                                    Integer.parseInt(currentHour))
                                                                {
                                                                    if (Integer.parseInt(openMinute)
                                                                        <= Integer.parseInt
                                                                            (currentMinute))
                                                                    {
                                                                        acceptedJobsList.remove
                                                                            (theAcceptedJobs);
                                                                            firestore.collection(
                                                                            "Jobs/" +
                                                                            "Jobs/Accepted Jobs").
                                                                            document(theAcceptedJobs
                                                                            .getJobsId()).delete();
                                                                    }
                                                                }
                                                                else if (Integer.parseInt(openHour)
                                                                    < Integer.parseInt(currentHour))
                                                                {
                                                                    acceptedJobsList.remove
                                                                        (theAcceptedJobs);
                                                                    firestore.collection(
                                                                        "Jobs/Jobs/" +
                                                                        "Accepted Jobs").document(
                                                                        theAcceptedJobs.getJobsId())
                                                                        .delete();
                                                                }
                                                            }
                                                            else if (openAMPM.equals("AM") &&
                                                                currentAmPm.equals("PM"))
                                                            {
                                                                acceptedJobsList.remove
                                                                    (theAcceptedJobs);
                                                                firestore.collection(
                                                                    "Jobs/Jobs/" +
                                                                    "Accepted Jobs").document
                                                                    (theAcceptedJobs.getJobsId()).
                                                                    delete();
                                                            }
                                                        }
                                                        else if (Integer.parseInt(openDay) <
                                                            Integer.parseInt(currentDay))
                                                        {
                                                            acceptedJobsList.remove
                                                                (theAcceptedJobs);
                                                            firestore.collection(
                                                                "Jobs/Jobs/Accepted" +
                                                                " Jobs").document(theAcceptedJobs.
                                                                getJobsId()).delete();
                                                        }
                                                    }
                                                    else if (Integer.parseInt(openMonth) <
                                                        Integer.parseInt(currentMonth))
                                                    {
                                                        acceptedJobsList.remove(theAcceptedJobs);
                                                        firestore.collection("Jobs/" +
                                                            "Jobs/Accepted Jobs").document(
                                                            theAcceptedJobs.getJobsId()).delete();
                                                    }
                                                }
                                                else if (Integer.parseInt(openYear) <
                                                    Integer.parseInt(currentYear))
                                                {
                                                    acceptedJobsList.remove(theAcceptedJobs);
                                                    firestore.collection("Jobs/Jobs/" +
                                                        "Accepted Jobs").document(theAcceptedJobs.
                                                        getJobsId()).delete();
                                                }
                                            }
                                        }
                                        noRepeats(acceptedJobsList);
                                        dateAscend();
                                        helperMethod(acceptedJobsList);
                                    }
                                }
                            });
                    }
                }
            });
    }

    /**
     * This method accepts an ArrayList of any type to make sure that there is no repeated ArrayList
     * presented in the recycler view.
     * @param myList this represents an ArrayList of any type, where an ArrayList will be put here
     *               when calling it in other methods to change the inputted ArrayList.
     * @param <AcceptedJobs> is a variable that accepts any type of ArrayList.
     * @return This executes the code till it reaches the point return where once it is reached in
     *         a method, the program returns to the code that invoked it. Meaning return ends the
     *         execution of a function.
     */

    public static <AcceptedJobs> ArrayList<AcceptedJobs> noRepeats(ArrayList<AcceptedJobs> myList)
    {
        ArrayList <AcceptedJobs> noRepeatsArrayList = new ArrayList<AcceptedJobs>();
        for (int i = 0; i < myList.size(); i++)
        {
            if (!noRepeatsArrayList.contains(myList.get(i)))
            {
                noRepeatsArrayList.add(myList.get(i));
            }
        }
        return noRepeatsArrayList;
    }

    /**
     * This method sorts the recycler view from the most recent job to the furthest.
     */

    public void dateAscend()
    {
        String theDate = "";
        String theTime = "";
        String theDate2 = "";
        String theTime2 = "";

        for (int i = 0; i < acceptedJobsList.size(); i++)
        {
            theDate = acceptedJobsList.get(i).getDate();
            String[] splittingDay = theDate.split("/");

            String openDay = splittingDay[0];
            String openMonth = splittingDay[1];
            String openYear = splittingDay[2];

            theTime = acceptedJobsList.get(i).getTime();

            String[] split = theTime.split(" - ");
            String[] timeSplit = split[0].split(":");
            String[] afterSplit = timeSplit[1].split(" ");

            String openHour = timeSplit[0];
            String openMinute = afterSplit[0];
            String openAMPM = afterSplit[1];

            for (int j = 0; j < acceptedJobsList.size(); j++)
            {
                theDate2 = acceptedJobsList.get(j).getDate();
                String[] splittingDay2 = theDate2.split("/");

                String openDay2 = splittingDay2[0];
                String openMonth2 = splittingDay2[1];
                String openYear2 = splittingDay2[2];

                theTime2 = acceptedJobsList.get(j).getTime();

                String[] split2 = theTime2.split(" - ");
                String[] timeSplit2 = split2[0].split(":");
                String[] afterSplit2 = timeSplit2[1].split(" ");

                String openHour2 = timeSplit2[0];
                String openMinute2 = afterSplit2[0];
                String openAMPM2 = afterSplit2[1];

                if (Integer.parseInt(openYear2) == Integer.parseInt(openYear))
                {
                    if (Integer.parseInt(openMonth2) == Integer.parseInt(openMonth))
                    {
                        if (Integer.parseInt(openDay2) == Integer.parseInt(openDay))
                        {
                            if (openAMPM2.equals(openAMPM))
                            {
                                if (Integer.parseInt(openHour2) == Integer.parseInt(openHour))
                                {
                                    if (Integer.parseInt(openMinute2) > Integer.parseInt
                                        (openMinute))
                                    {
                                        Collections.swap(acceptedJobsList, i, j);
                                    }
                                }
                                else if (Integer.parseInt(openHour2) > Integer.parseInt(openHour))
                                {
                                    Collections.swap(acceptedJobsList, i, j);
                                }
                            }
                            else if (openAMPM2.equals("PM") && openAMPM.equals("AM"))
                            {
                                Collections.swap(acceptedJobsList, i, j);
                            }
                        }
                        else if (Integer.parseInt(openDay2) > Integer.parseInt(openDay))
                        {
                            Collections.swap(acceptedJobsList, i, j);
                        }
                    }
                    else if (Integer.parseInt(openMonth2) > Integer.parseInt(openMonth))
                    {
                        Collections.swap(acceptedJobsList, i, j);
                    }
                }
                else if (Integer.parseInt(openYear2) > Integer.parseInt(openYear))
                {
                    Collections.swap(acceptedJobsList, i, j);
                }
            }
        }
    }

    /**
     * This method creates the recycler view, with the help of the AcceptedJobsAdapter.
     * @param a this represents an ArrayList of AcceptedJobs, where an ArrayList of AcceptedJobs
     *          will be put here when calling it in other methods to set up the recycler view.
     */

    public void helperMethod(ArrayList<AcceptedJobs> a)
    {
        acceptedJobsList = a;
        AcceptedJobsAdapter myAdapter = new AcceptedJobsAdapter (acceptedJobsList, this);
        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * This method is an intent that is created to move the user to SelectionActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "BACK" works in accordance to this method.
     */

    public void backAcceptActivity(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }
}