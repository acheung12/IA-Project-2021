package com.example.iasubstituteteacher.Jobs;

/**
 * A child class of the parent class Jobs in case anything specific objects to OpenJobs will be used
 * in future app development
 */

public class OpenJobs extends Jobs
{

    public OpenJobs(String jobsId, String subject, String date, String time, String location,
                    boolean active, String lessonPlan, String userId, String usersEmail)
    {
        super(jobsId, subject, date, time, location, active, lessonPlan, userId, usersEmail);
    }

    public OpenJobs()
    {

    }
}
