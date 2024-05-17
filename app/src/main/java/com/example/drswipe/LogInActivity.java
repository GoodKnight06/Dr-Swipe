package com.example.drswipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText emailloginvar = findViewById(R.id.emailloginedittext);
        final EditText passwordloginvar = findViewById(R.id.passwordloginedittext);
        final Button loginbutton = findViewById(R.id.loginbutton);
        final ProgressBar loadbar = findViewById(R.id.loading);

        final FirebaseAuth testAuth = FirebaseAuth.getInstance();
        final FirebaseFirestore fbase = FirebaseFirestore.getInstance();

        String emptyfield = "Field must not be empty!";
        String passfield = "Must be at least 6 characters long.";

        loginbutton.setEnabled(false);
        emailloginvar.setError(emptyfield);
        passwordloginvar.setError(passfield);

        emailloginvar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordloginvar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailcred = emailloginvar.getText().toString();
                final String passwordcred = passwordloginvar.getText().toString();
                loadbar.setVisibility(View.VISIBLE);

                testAuth.signInWithEmailAndPassword(emailcred, passwordcred).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            final String userID = testAuth.getCurrentUser().getUid();
                            final DocumentReference userchecker = fbase.collection("patients").document(userID);
                            userchecker.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        DocumentSnapshot checker = task.getResult();
                                        if(checker.exists()){
                                            Toast.makeText(LogInActivity.this, "Welcome, Patient!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LogInActivity.this, PatientMainUI.class);
                                            final String hometownExtra = checker.getString("hometown");
                                            intent.putExtra("Hometown", hometownExtra);
                                            LogInActivity.this.startActivity(intent);
                                            loadbar.setVisibility(View.INVISIBLE);
                                        }
                                        else {
                                            Toast.makeText(LogInActivity.this, "Welcome, Doctor!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(LogInActivity.this, DoctorMainUI.class);
                                            final String clinicExtra = checker.getString("clinic");
                                            intent.putExtra("Clinic", clinicExtra);
                                            LogInActivity.this.startActivity(intent);
                                            loadbar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                }
                            });

                        }
                        else{
                            Toast.makeText(LogInActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            loadbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });
    }

    public void checkFields() {

        final EditText emailloginvar = findViewById(R.id.emailloginedittext);
        final EditText passwordloginvar = findViewById(R.id.passwordloginedittext);

        final Button loginbutton = findViewById(R.id.loginbutton);

        String emptyfield = "Field must not be empty!";
        String passfield = "Must be at least 6 characters long.";

        String emailloginholder = emailloginvar.getText().toString();
        String passwordloginholder = passwordloginvar.getText().toString();

        if(emailloginholder.isEmpty() || passwordloginholder.isEmpty()) {
            loginbutton.setEnabled(false);
        }
        else {
            loginbutton.setEnabled(true);
        }

        if(emailloginholder.isEmpty()) {
            emailloginvar.setError(emptyfield);
        }
        else {
            emailloginvar.setError(null);
        }

        if(passwordloginholder.isEmpty()) {
            passwordloginvar.setError(passfield);
        }
        else {
            passwordloginvar.setError(null);
        }

    }

}
