package com.example.iasubstituteteacher.SignInThings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.iasubstituteteacher.R;
import com.example.iasubstituteteacher.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private String selected;
    private Spinner sUserType;
    private EditText emailField;
    private EditText passwordField;
    private EditText username;

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

    public void signUp(View v)
    {
        final String usernameString = username.getText().toString();
        final String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();

        if (emailString.contains("@"))
        {
            mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener
                    (this, new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful() && !usernameString.equals(""))
                    {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);

                        String userUID = user.getUid();
                        ArrayList<String> acceptedJobs = new ArrayList<>();
                        ArrayList<String> declinedJobs = new ArrayList<>();

                        User currentUser = new User(userUID, usernameString, emailString, selected,
                                acceptedJobs, declinedJobs);
                        firestore.collection("Users").document(userUID).
                                set(currentUser);
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
                        Log.w("SIGN UP", "Unable to sign up for the user",
                                task.getException());
                        Toast.makeText(SignUpActivity.this, "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }
        else
        {
            Toast.makeText(SignUpActivity.this, "Please check that your email is " +
                            "right", Toast.LENGTH_SHORT).show();
        }
    }

    public void backToSignIn(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

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