package com.example.iasubstituteteacher.SignInThings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iasubstituteteacher.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * MainActivity is for the authentication of the user to allow them to access the substitute teacher
 * app. This is to allow existing users to sign into their account.
 */

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailField = findViewById(R.id.editTextEmail2);
        passwordField = findViewById(R.id.editTextPassword2);
    }

    /**
     * The method occurs on the click of "SIGN IN" on the app, where this method allows previously
     * signed up users to sign in, with there password and email. Checking to see if the right
     * information is typed in.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the button that works in accordance to this method is "SIGN IN".
     */

    public void signIn(View v)
    {
        final String emailString = emailField.getText().toString();
        String passwordString = passwordField.getText().toString();

        mAuth.signInWithEmailAndPassword(emailString, passwordString)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(MainActivity.this, "Successfully logged in",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
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

    /**
     * This method is an intent that is created to move the user to SignUpActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "Sign Up" works in accordance to this method.
     */

    public void toSignUp(View v)
    {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    /**
     * This method is an intent that is created to move the user to ForgotPasswordActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the text "Forgot Password?" works in accordance to this method.
     */

    public void forgotPassword(View v)
    {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}