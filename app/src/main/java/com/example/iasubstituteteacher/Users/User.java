package com.example.iasubstituteteacher.Users;

import java.util.ArrayList;

public class User
{
    String uid;
    String username;
    String email;
    String userType;
    ArrayList<String> acceptedJobs;

    public User(String uid, String username, String email, String userType, ArrayList<String> acceptedJobs) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.acceptedJobs = acceptedJobs;
    }

    public User()
    {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public ArrayList<String> getAcceptedJobs() {
        return acceptedJobs;
    }

    public void setAcceptedJobs(ArrayList<String> acceptedJobs) {
        this.acceptedJobs = acceptedJobs;
    }
}
