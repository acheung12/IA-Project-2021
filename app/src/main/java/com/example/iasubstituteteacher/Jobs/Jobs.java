package com.example.iasubstituteteacher.Jobs;

import java.util.ArrayList;

public class Jobs {
    String jobsId;
    String subject;
    String date;
    String time;
    String location;
    boolean active;
    String lessonPlan;
    String userId;
    String usersEmail;
    ArrayList<String> declineList;


    public Jobs(String jobsId, String subject, String date, String time, String location,
                boolean active, String lessonPlan, String userId, String usersEmail) {
        this.jobsId = jobsId;
        this.subject = subject;
        this.date = date;
        this.time = time;
        this.location = location;
        this.active = active;
        this.lessonPlan = lessonPlan;
        this.userId = userId;
        this.usersEmail = usersEmail;
    }

    public Jobs() {

    }

    public String getJobsId() {
        return jobsId;
    }

    public void setJobsId(String jobsId) {
        this.jobsId = jobsId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLessonPlan() {
        return lessonPlan;
    }

    public void setLessonPlan(String lessonPlan) {
        this.lessonPlan = lessonPlan;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsersEmail() {
        return usersEmail;
    }

    public void setUsersEmail(String usersEmail) {
        this.usersEmail = usersEmail;
    }

}
