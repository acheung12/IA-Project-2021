package com.example.iasubstituteteacher.JobsThing;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iasubstituteteacher.Jobs.OpenJobs;
import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.RecyclerView.OpenJobsAdapter;
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
 * OpenJobsActivity is used to display all the OpenJobs from the user to the recycler view
 * as long as the date has not passed.
 */

public class OpenJobsActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private FirebaseUser user;

    private ArrayList<OpenJobs> openJobsList;
    private RecyclerView recView;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_jobs);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        recView = findViewById(R.id.requestedJobRecView);

        openJobsList = new ArrayList<OpenJobs>();

        getAndPopulateData();
    }

    /**
     * This method retrieves all the OpenJobs in the data and turns it to a recycler view with
     * the help of the helperMethod below. Only showing the OpenJobs that have not passed the
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
                    public void onComplete(Task<DocumentSnapshot> task)
                    {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot ds = task.getResult();
                            User theUser = ds.toObject(User.class);

                            firestore.collection("Jobs/Jobs/Open Jobs").get().
                                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
                                {
                                public void onComplete(Task<QuerySnapshot> task)
                                {
                                    if (task.isSuccessful())
                                    {
                                        for (DocumentSnapshot document : task.getResult().
                                                getDocuments())
                                        {
                                            OpenJobs theOpenJobs = document.toObject
                                                    (OpenJobs.class);

                                            if (theOpenJobs.isActive())
                                            {
                                                openJobsList.add(theOpenJobs);
                                            }
                                            for (int i = 0; i < theUser.getDeclinedJobs().size();
                                                 i++)
                                            {
                                                String declinedJob = theUser.getDeclinedJobs().
                                                        get(i);
                                                if (theOpenJobs.getJobsId().equals(declinedJob))
                                                {
                                                    openJobsList.remove(theOpenJobs);
                                                }
                                            }

                                            theDate[0] = theOpenJobs.getDate();
                                            String[] splittingDay = theDate[0].split("/");

                                            String openDay = splittingDay[0];
                                            String openMonth = splittingDay[1];
                                            String openYear = splittingDay[2];

                                            theTime[0] = theOpenJobs.getTime();

                                            String[] split = theTime[0].split(" - ");
                                            String[] timeSplit = split[0].split(":");
                                            String[] afterSplit = timeSplit[1].split(" ");

                                            String openHour = timeSplit[0];
                                            String openMinute = afterSplit[0];
                                            String openAMPM = afterSplit[1];

                                            if (Integer.parseInt(openYear) == Integer.parseInt
                                                (currentYear))
                                            {
                                                if (Integer.parseInt(openMonth) == Integer.parseInt
                                                    (currentMonth))
                                                {
                                                    if (Integer.parseInt(openDay) == Integer.
                                                        parseInt(currentDay))
                                                    {
                                                        if (openAMPM.equals(currentAmPm))
                                                        {
                                                            if (Integer.parseInt(openHour) ==
                                                                Integer.parseInt(currentHour))
                                                            {
                                                                if (Integer.parseInt(openMinute) <=
                                                                    Integer.parseInt(currentMinute))
                                                                {
                                                                    openJobsList.remove(
                                                                        theOpenJobs);
                                                                    firestore.collection(
                                                                        "Jobs/Jobs/" +
                                                                        "Open Jobs").document(
                                                                        theOpenJobs.getJobsId())
                                                                        .delete();
                                                                }
                                                            }
                                                            else if (Integer.parseInt(openHour) <
                                                                Integer.parseInt(currentHour))
                                                            {
                                                                openJobsList.remove(theOpenJobs);
                                                                firestore.collection(
                                                                    "Jobs/Jobs/" +
                                                                    "Open Jobs").document(
                                                                    theOpenJobs.getJobsId())
                                                                    .delete();
                                                            }
                                                        }
                                                        else if (openAMPM.equals("AM") &&
                                                            currentAmPm.equals("PM"))
                                                        {
                                                            openJobsList.remove(theOpenJobs);
                                                            firestore.collection(
                                                                "Jobs/Jobs/Open Jobs")
                                                                .document(theOpenJobs.getJobsId())
                                                                .delete();
                                                        }
                                                    }
                                                    else if (Integer.parseInt(openDay) < Integer
                                                        .parseInt(currentDay))
                                                    {
                                                        openJobsList.remove(theOpenJobs);
                                                        firestore.collection("Jobs/" +
                                                            "Jobs/Open Jobs").document(theOpenJobs
                                                            .getJobsId()).delete();
                                                    }
                                                }
                                                else if (Integer.parseInt(openMonth) < Integer
                                                    .parseInt(currentMonth))
                                                {
                                                    openJobsList.remove(theOpenJobs);
                                                    firestore.collection("Jobs/Jobs/" +
                                                        "Open Jobs").document(theOpenJobs
                                                        .getJobsId()).delete();
                                                }
                                            }
                                            else if (Integer.parseInt(openYear) < Integer.parseInt
                                                (currentYear))
                                            {
                                                openJobsList.remove(theOpenJobs);
                                                firestore.collection("Jobs/Jobs/" +
                                                "Open Jobs").document(theOpenJobs.getJobsId())
                                                .delete();
                                            }
                                        }
                                        dateAscend();
                                        helperMethod(openJobsList);
                                    }
                                }
                            });
                        }
                    }
        });
    }

    /**
     * This method creates the recycler view, with the help of the OpenJobsAdapter.
     * @param o This represents an ArrayList OpenJobs, where an ArrayList of OpenJobs
     *          will be put here when calling it in other methods to set up the recycler view.
     */

    public void helperMethod(ArrayList<OpenJobs> o)
    {
        openJobsList = o;
        OpenJobsAdapter myAdapter = new OpenJobsAdapter (openJobsList, this);
        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
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

        for (int i = 0; i < openJobsList.size(); i++)
        {
            theDate = openJobsList.get(i).getDate();
            String[] splittingDay = theDate.split("/");

            String openDay = splittingDay[0];
            String openMonth = splittingDay[1];
            String openYear = splittingDay[2];

            theTime = openJobsList.get(i).getTime();

            String[] split = theTime.split(" - ");
            String[] timeSplit = split[0].split(":");
            String[] afterSplit = timeSplit[1].split(" ");

            String openHour = timeSplit[0];
            String openMinute = afterSplit[0];
            String openAMPM = afterSplit[1];

            for (int j = 0; j < openJobsList.size(); j++)
            {
                theDate2 = openJobsList.get(j).getDate();
                String[] splittingDay2 = theDate2.split("/");

                String openDay2 = splittingDay2[0];
                String openMonth2 = splittingDay2[1];
                String openYear2 = splittingDay2[2];

                theTime2 = openJobsList.get(j).getTime();

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
                                    if (Integer.parseInt(openMinute2) >
                                            Integer.parseInt(openMinute))
                                    {
                                        Collections.swap(openJobsList, i, j);
                                    }
                                }
                                else if (Integer.parseInt(openHour2) > Integer.parseInt(openHour))
                                {
                                    Collections.swap(openJobsList, i, j);
                                }
                            }
                            else if (openAMPM2.equals("PM") && openAMPM.equals("AM"))
                            {
                                Collections.swap(openJobsList, i, j);
                            }
                        }
                        else if (Integer.parseInt(openDay2) > Integer.parseInt(openDay))
                        {
                            Collections.swap(openJobsList, i, j);
                        }
                    }
                    else if (Integer.parseInt(openMonth2) > Integer.parseInt(openMonth))
                    {
                        Collections.swap(openJobsList, i, j);
                    }
                }
                else if (Integer.parseInt(openYear2) > Integer.parseInt(openYear))
                {
                    Collections.swap(openJobsList, i, j);
                }
            }
        }
    }

    /**
     * This method calls the dateAscend() method.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "ASCEND" works in accordance to this method.
     */

    public void dateArrangementAscending(View v)
    {
        dateAscend();
        helperMethod(openJobsList);
    }

    /**
     * This method sorts the recycler view from the furthest job to the most recent.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "DESCEND" works in accordance to this method.
     */

    public void dateArrangementDescending(View v)
    {
        String theDate = "";
        String theTime = "";
        String theDate2 = "";
        String theTime2 = "";

        for (int i = 0; i < openJobsList.size(); i++)
        {
            theDate = openJobsList.get(i).getDate();
            String[] splittingDay = theDate.split("/");

            String openDay = splittingDay[0];
            String openMonth = splittingDay[1];
            String openYear = splittingDay[2];

            theTime = openJobsList.get(i).getTime();

            String[] split = theTime.split(" - ");
            String[] timeSplit = split[0].split(":");
            String[] afterSplit = timeSplit[1].split(" ");

            String openHour = timeSplit[0];
            String openMinute = afterSplit[0];
            String openAMPM = afterSplit[1];

            for (int j = 0; j < openJobsList.size(); j++)
            {
                theDate2 = openJobsList.get(j).getDate();
                String[] splittingDay2 = theDate2.split("/");

                String openDay2 = splittingDay2[0];
                String openMonth2 = splittingDay2[1];
                String openYear2 = splittingDay2[2];

                theTime2 = openJobsList.get(j).getTime();

                String[] split2 = theTime2.split(" - ");
                String[] timeSplit2 = split2[0].split(":");
                String[] afterSplit2 = timeSplit2[1].split(" ");

                String openHour2 = timeSplit2[0];
                String openMinute2 = afterSplit2[0];
                String openAMPM2 = afterSplit2[1];

                if (Integer.parseInt(openYear) == Integer.parseInt(openYear2))
                {
                    if (Integer.parseInt(openMonth) == Integer.parseInt(openMonth2))
                    {
                        if (Integer.parseInt(openDay) == Integer.parseInt(openDay2))
                        {
                            if (openAMPM.equals(openAMPM2))
                            {
                                if (Integer.parseInt(openHour) == Integer.parseInt(openHour2))
                                {
                                    if (Integer.parseInt(openMinute) > Integer.parseInt
                                        (openMinute2))
                                    {
                                        Collections.swap(openJobsList, i, j);
                                    }
                                }
                                else if (Integer.parseInt(openHour) > Integer.parseInt(openHour2))
                                {
                                    Collections.swap(openJobsList, i, j);
                                }
                            }
                            else if (openAMPM.equals("PM") && openAMPM2.equals("AM"))
                            {
                                Collections.swap(openJobsList, i, j);
                            }
                        }
                        else if (Integer.parseInt(openDay) > Integer.parseInt(openDay2))
                        {
                            Collections.swap(openJobsList, i, j);
                        }
                    }
                    else if (Integer.parseInt(openMonth) > Integer.parseInt(openMonth2))
                    {
                        Collections.swap(openJobsList, i, j);
                    }
                }
                else if (Integer.parseInt(openYear) > Integer.parseInt(openYear2))
                {
                    Collections.swap(openJobsList, i, j);
                }
            }
        }
        helperMethod(openJobsList);
    }

    /**
     * This method refreshes the UI to show the latest information on the server.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "REFRESH" works in accordance to this method.
     */

    public void refreshOpenActivity(View v)
    {
        openJobsList.clear();
        getAndPopulateData();
        OpenJobsAdapter myAdapter = new OpenJobsAdapter (openJobsList, this);
        recView.setAdapter(myAdapter);
        recView.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * This method is an intent that is created to move the user to SelectionActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "BACK" works in accordance to this method.
     */

    public void backOpenActivity(View v)
    {
        Intent intent = new Intent(this, SelectionActivity.class);
        startActivity(intent);
    }
}