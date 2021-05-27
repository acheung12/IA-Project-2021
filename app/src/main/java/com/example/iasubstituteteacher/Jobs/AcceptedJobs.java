package com.example.iasubstituteteacher.Jobs;

import java.util.ArrayList;

public class AcceptedJobs extends Jobs
{

    String acceptorsEmail;

    public AcceptedJobs(String jobsId, String subject, String date, String time, String location,
                    boolean active, String lessonPlan, String userId, String usersEmail,
                        String acceptorsEmail)
    {
        super(jobsId, subject, date, time, location, active, lessonPlan, userId, usersEmail);
        this.acceptorsEmail = acceptorsEmail;
    }

    public AcceptedJobs()
    {

    }

    public String getAcceptorsEmail() {
        return acceptorsEmail;
    }

    public void setAcceptorsEmail(String acceptorsEmail) {
        this.acceptorsEmail = acceptorsEmail;
    }


}