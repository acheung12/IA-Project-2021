package com.example.iasubstituteteacher.Jobs;

/**
 * A child class of the parent class Jobs, containing the extra variable acceptorsEmail and the
 * associated getters and setters
 */

public class AcceptedJobs extends Jobs
{
    private String acceptorsEmail;

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

    public String getAcceptorsEmail()
    {
        return acceptorsEmail;
    }

    public void setAcceptorsEmail(String acceptorsEmail)
    {
        this.acceptorsEmail = acceptorsEmail;
    }
}