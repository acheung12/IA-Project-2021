package com.example.iasubstituteteacher.JobsThing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iasubstituteteacher.Jobs.RequestedJobs;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.RecyclerView.RequestedJobsAdapter;
import com.example.iasubstituteteacher.SignInThings.SelectionActivity;
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
 *
 */

public class RequestedJobsActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;


    private ArrayList<RequestedJobs> requestedJobsList;
    private RecyclerView recView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_jobs);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        recView = findViewById(R.id.requestedJobRecView);

        requestedJobsList = new ArrayList<RequestedJobs>();

        getAndPopulateData();
    }

    /**
     *
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
                                if (!theRequestedJobs.isActive() &&
                                        !theRequestedJobs.isChoice())
                                {
                                    requestedJobsList.add(theRequestedJobs);
                                }

                                theDate[0] = theRequestedJobs.getDate();
                                String[] splittingDay = theDate[0].split("/");

                                String openDay = splittingDay[0];
                                String openMonth = splittingDay[1];
                                String openYear = splittingDay[2];

                                theTime[0] = theRequestedJobs.getTime();

                                String[] split = theTime[0].split(" - ");
                                String[] timeSplit = split[0].split(":");
                                String[] afterSplit = timeSplit[1].split(" ");

                                String openHour = timeSplit[0];
                                String openMinute = afterSplit[0];
                                String openAMPM = afterSplit[1];

                                if (Integer.parseInt(openYear) == Integer.parseInt(currentYear))
                                {
                                    if (Integer.parseInt(openMonth) == Integer.parseInt
                                        (currentMonth))
                                    {
                                        if (Integer.parseInt(openDay) == Integer.parseInt
                                            (currentDay))
                                        {
                                            if (openAMPM.equals(currentAmPm))
                                            {
                                                if (Integer.parseInt(openHour) == Integer.parseInt
                                                    (currentHour))
                                                {
                                                    if (Integer.parseInt(openMinute) <= Integer
                                                        .parseInt(currentMinute))
                                                    {
                                                        requestedJobsList.remove(theRequestedJobs);
                                                        firestore.collection("Jobs/" +
                                                            "Jobs/Requested Jobs").document
                                                            (theRequestedJobs.getJobsId())
                                                            .delete();
                                                    }
                                                }
                                                else if (Integer.parseInt(openHour) < Integer
                                                    .parseInt(currentHour))
                                                {
                                                    requestedJobsList.remove(theRequestedJobs);
                                                    firestore.collection("Jobs/Jobs/" +
                                                        "Requested Jobs").document(theRequestedJobs
                                                        .getJobsId()).delete();
                                                }
                                            }
                                            else if (openAMPM.equals("AM") && currentAmPm.equals
                                                ("PM"))
                                            {
                                                requestedJobsList.remove(theRequestedJobs);
                                                firestore.collection("Jobs/Jobs/" +
                                                "Requested Jobs").document(theRequestedJobs
                                                .getJobsId()).delete();
                                            }
                                        }
                                        else if (Integer.parseInt(openDay) < Integer.parseInt
                                            (currentDay))
                                        {
                                            requestedJobsList.remove(theRequestedJobs);
                                            firestore.collection("Jobs/Jobs/" +
                                                "Requested Jobs").document(theRequestedJobs
                                                .getJobsId()).delete();
                                        }
                                    }
                                    else if (Integer.parseInt(openMonth) < Integer.parseInt
                                        (currentMonth))
                                    {
                                        requestedJobsList.remove(theRequestedJobs);
                                        firestore.collection("Jobs/Jobs/Requested Jobs"
                                            ).document(theRequestedJobs.getJobsId()).delete();
                                    }
                                }
                                else if (Integer.parseInt(openYear) < Integer.parseInt(currentYear))
                                {
                                    requestedJobsList.remove(theRequestedJobs);
                                    firestore.collection("Jobs/Jobs/Requested Jobs")
                                        .document(theRequestedJobs.getJobsId()).delete();
                                }
                            }
                            dateAscend();
                            helperMethod(requestedJobsList);
                        }
                    }
                });
    }

    /**
     *
     */

    public void dateAscend()
    {
        String theDate = "";
        String theTime = "";
        String theDate2 = "";
        String theTime2 = "";

        for (int i = 0; i < requestedJobsList.size(); i++)
        {
            theDate = requestedJobsList.get(i).getDate();
            String[] splittingDay = theDate.split("/");

            String openDay = splittingDay[0];
            String openMonth = splittingDay[1];
            String openYear = splittingDay[2];

            theTime = requestedJobsList.get(i).getTime();

            String[] split = theTime.split(" - ");
            String[] timeSplit = split[0].split(":");
            String[] afterSplit = timeSplit[1].split(" ");

            String openHour = timeSplit[0];
            String openMinute = afterSplit[0];
            String openAMPM = afterSplit[1];

            for (int j = 0; j < requestedJobsList.size(); j++)
            {
                theDate2 = requestedJobsList.get(j).getDate();
                String[] splittingDay2 = theDate2.split("/");

                String openDay2 = splittingDay2[0];
                String openMonth2 = splittingDay2[1];
                String openYear2 = splittingDay2[2];

                theTime2 = requestedJobsList.get(j).getTime();

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
                                        Collections.swap(requestedJobsList, i, j);
                                    }
                                }
                                else if (Integer.parseInt(openHour2) > Integer.parseInt(openHour))
                                {
                                    Collections.swap(requestedJobsList, i, j);
                                }
                            }
                            else if (openAMPM2.equals("PM") && openAMPM.equals("AM"))
                            {
                                Collections.swap(requestedJobsList, i, j);
                            }
                        }
                        else if (Integer.parseInt(openDay2) > Integer.parseInt(openDay))
                        {
                            Collections.swap(requestedJobsList, i, j);
                        }
                    }
                    else if (Integer.parseInt(openMonth2) > Integer.parseInt(openMonth))
                    {
                        Collections.swap(requestedJobsList, i, j);
                    }
                }
                else if (Integer.parseInt(openYear2) > Integer.parseInt(openYear))
                {
                    Collections.swap(requestedJobsList, i, j);
                }
            }
        }
    }

    /**
     *
     * @param r
     */

    public void helperMethod(ArrayList<RequestedJobs> r)
    {
        requestedJobsList = r;
        RequestedJobsAdapter myAdapter = new RequestedJobsAdapter (requestedJobsList, this);
        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     *
     * @param v
     */

    public void refreshRequestedActivity(View v)
    {
        requestedJobsList.clear();
        getAndPopulateData();

        RequestedJobsAdapter myAdapter = new RequestedJobsAdapter (requestedJobsList, this);
        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     *
     * @param v
     */

    public void backRequestActivity(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }
}