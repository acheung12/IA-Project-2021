package com.example.iasubstituteteacher.Jobs;

/**
 * A child class of the parent class Jobs, containing the extra variable choice and the
 * associated getters and setters
 */

public class RequestedJobs extends Jobs
{
    private boolean choice;

    public RequestedJobs(String jobsId, String subject, String date, String time, String location,
                        boolean active, String lessonPlan, String userId, String usersEmail,
                        boolean choice)
    {
        super(jobsId, subject, date, time, location, active, lessonPlan, userId, usersEmail);
        this.choice = choice;
    }

    public RequestedJobs()
    {

    }

    public boolean isChoice()
    {
        return choice;
    }

    public void setChoice(boolean choice)
    {
        this.choice = choice;
    }
}