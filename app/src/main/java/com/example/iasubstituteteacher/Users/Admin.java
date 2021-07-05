package com.example.iasubstituteteacher.Users;

import java.util.ArrayList;

/**
 * A child class of the parent class User in case anything specific objects to Admin will be used in
 * future app development
 */

public class Admin extends User
{
    public Admin(String uid, String username, String email, String userType, ArrayList<String>
            acceptedJobs, ArrayList<String> declinedJobs)
    {
        super(uid, username, email, userType, acceptedJobs, declinedJobs);
    }
}
