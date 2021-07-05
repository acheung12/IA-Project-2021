package com.example.iasubstituteteacher.SignInThings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * SignUpActivity is for the authentication of the user to allow them to create an account for
 * themselves to allow easy access to their information in the future.
 */

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private String selected;
    private Spinner sUserType;
    private EditText emailField;
    private EditText passwordField;
    private EditText username;
    private EditText adminCode;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailField = findViewById(R.id.editTextEmail);
        passwordField = findViewById(R.id.editTextPassword);
        username = findViewById(R.id.editTextUsername);
        adminCode = findViewById(R.id.adminCode);

        Spinner spinner = findViewById(R.id.userSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.userType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    protected void onStart()
    {
        super.onStart();
    }

    /**
     * This method is used to create an account for the current user when using the app, where once
     * the sign up button is clicked it retrieves their email and password with it, but also their
     * username so it can be used later on. Also checking to see if the right email address is used,
     * as only CIS community members may use this part of the app. Also if all necessary information
     * is there. In addition, this method also checks to see which user is selected, and if an
     * "Admin" is selected whether the correct Admin code is inputted.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the button that works in accordance to this method is "CONTINUE".
     */

    public void signUp(View v)
    {
        final String usernameString = username.getText().toString();
        final String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();
        String adminCodeString = adminCode.getText().toString();

        String[] email = emailString.split("@");
        String rightEmail = email[1];

        if (rightEmail.equals("student.cis.edu.hk") || rightEmail.equals("cis.edu.hk") ||
                rightEmail.equals("gmail.com") || rightEmail.equals("hotmail.com") ||
                rightEmail.equals("alumni.cis.edu.hk"))
        {
            if (emailString.contains("@") && !selected.equals("Admin")) {
                mAuth.createUserWithEmailAndPassword(emailString, passwordString).
                        addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful() && !usernameString.equals("")) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);

                                String userUID = user.getUid();
                                ArrayList<String> acceptedJobs = new ArrayList<>();
                                ArrayList<String> declinedJobs = new ArrayList<>();

                                User currentUser = new User(userUID, usernameString, emailString,
                                        selected, acceptedJobs, declinedJobs);
                                firestore.collection("Users").document(userUID).
                                        set(currentUser);
                                Toast.makeText(SignUpActivity.this, "Successfully " +
                                        "signed up", Toast.LENGTH_SHORT).show();
                            }
                            else if (task.isSuccessful() && usernameString.equals(""))
                            {
                                Toast.makeText(SignUpActivity.this, "Please enter your"
                                        + " username", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                                task.isCanceled();
                            }
                            else
                            {
                                Toast.makeText(SignUpActivity.this, "Authentication" +
                                                " failed", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
            }
            else if (emailString.contains("@") && selected.equals("Admin"))
            {
                if (adminCodeString.equals("CIS2021"))
                {
                    mAuth.createUserWithEmailAndPassword(emailString, passwordString).
                            addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful() && !usernameString.equals("")) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        updateUI(user);

                                        String userUID = user.getUid();
                                        ArrayList<String> acceptedJobs = new ArrayList<>();
                                        ArrayList<String> declinedJobs = new ArrayList<>();

                                        User currentUser = new User(userUID, usernameString,
                                                emailString, selected, acceptedJobs, declinedJobs);

                                        firestore.collection("Users").document(userUID)
                                                .set(currentUser);

                                        Toast.makeText(SignUpActivity.this,
                                                "Successfully signed up",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else if (task.isSuccessful() && usernameString.equals(""))
                                    {
                                        Toast.makeText(SignUpActivity.this, "Please" +
                                                " enter your username", Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                        task.isCanceled();
                                    }
                                    else
                                    {
                                        Toast.makeText(SignUpActivity.this, "" +
                                                "Authentication failed", Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(SignUpActivity.this, "Please input an" +
                                    " appropriate code", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        }
        else
        {
            Toast.makeText(SignUpActivity.this, "Please check that your email is " +
                            "right", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * This method is an intent that is created to move the user to MainActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "Sign In" works in accordance to this method.
     */

    public void backToSignIn(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * This method is used to check whether the current users information is valid or not (null),
     * and it creates an intent to move to SelectionActivity if the information is valid.
     * @param currentUser this param is used to get the current user that is using the app.
     */

    public void updateUI(FirebaseUser currentUser)
    {
        if (currentUser != null)
        {
            Intent intent = new Intent(this, SelectionActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long l)
    {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), text,Toast.LENGTH_LONG).show();
        selected = text;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {
        selected = null;
    }
}