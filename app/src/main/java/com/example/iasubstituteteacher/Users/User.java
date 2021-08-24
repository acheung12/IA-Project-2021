package com.example.iasubstituteteacher.Users;

import java.util.ArrayList;

/**
 * A class containing the getter and setter methods and variables pertaining to the User object.
 */

public class User
{
    String uid;
    String username;
    String email;
    String userType;
    ArrayList<String> acceptedJobs;
    ArrayList<String> declinedJobs;

    public User(String uid, String username, String email, String userType, ArrayList<String>
            acceptedJobs, ArrayList<String> declinedJobs)
    {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.userType = userType;
        this.acceptedJobs = acceptedJobs;
        this.declinedJobs = declinedJobs;
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

    public ArrayList<String> getDeclinedJobs() {
        return declinedJobs;
    }

    public void setDeclinedJobs(ArrayList<String> declinedJobs) {
        this.declinedJobs = declinedJobs;
    }
}
