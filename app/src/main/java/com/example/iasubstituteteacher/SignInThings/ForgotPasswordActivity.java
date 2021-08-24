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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * ForgotPasswordActivity is a screen to let the user send a reset password link to themselves if
 * they forgot their password.
 */

public class ForgotPasswordActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private EditText emailField;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailField = findViewById(R.id.editTextEmail3);
    }

    /**
     * This method obtains the written text checking whether it is an email or not and sending
     * the specified email a reset password link.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the button that works in accordance to this method is "RESET PASSWORD".
     */

    public void changePassword(View v)
    {
        String emailString = emailField.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();

        try
        {
            mAuth.sendPasswordResetEmail(emailString).addOnCompleteListener(this, new
                    OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                updateUI(user);
                                Toast.makeText(ForgotPasswordActivity.this,
                                        "Email successfully sent", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(ForgotPasswordActivity.this, "Password "
                                        + "reset email is unsuccessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        catch (Exception err)
        {
            Toast messageToUser = Toast.makeText(this, err.toString(), Toast.LENGTH_LONG);
            messageToUser.show();
        }
    }

    /**
     * This method is used to check whether the current user's information is valid or not (null),
     * and it creates an intent to move to MainActivity if the information is valid.
     * @param currentUser this param is used to get the current user that is using the app.
     */

    public void updateUI(FirebaseUser currentUser)
    {
        if (currentUser != null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    /**
     * This method is an intent that is created to move the user to MainActivity.
     * @param v this is what the user sees when the android studio is run and the app appears. In
     *          this case the button that works in accordance to this method is "BACK TO SIGN IN".
     */

    public void toSignIn(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}