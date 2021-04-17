package com.example.iasubstituteteacher.Jobs;

public class AcceptedJobs extends Jobs
{

    public AcceptedJobs(String jobsId, String subject, String date, String time, String location,
                    boolean active, String lessonPlan, String userId, String usersEmail)
    {
        super(jobsId, subject, date, time, location, active, lessonPlan, userId, usersEmail);
    }

    public AcceptedJobs()
    {

    }

}