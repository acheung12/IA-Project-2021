package com.example.iasubstituteteacher.SignInThings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iasubstituteteacher.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ForgotPasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;

    private EditText emailField;
    private EditText newPassword;
    private EditText reenterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        emailField = findViewById(R.id.editTextEmail3);
        newPassword = findViewById(R.id.editTextNewPassword);
        reenterPassword = findViewById(R.id.editTextReEnterPassword);
    }

    public void changePassword(View v)
    {
        String newPasswordString = newPassword.getText().toString();
        String reenterPasswordString = reenterPassword.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser();

        if (newPasswordString.equals(reenterPasswordString))
        {
            user.updatePassword(newPasswordString).addOnCompleteListener(this, new
                    OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                updateUI(user);
                                Toast.makeText(ForgotPasswordActivity.this,
                                "Password succesfully changed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(ForgotPasswordActivity.this, "Please check all fields",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void updateUI(FirebaseUser currentUser)
    {
        if (currentUser != null)
        {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}