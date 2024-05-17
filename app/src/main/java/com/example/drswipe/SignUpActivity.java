package com.example.drswipe;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class SignUpActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        final ProgressBar loadingbar = findViewById(R.id.progressBar);
        final EditText firstnamevar = findViewById(R.id.firstnameedittext);
        final EditText midnamevar = findViewById(R.id.middlenameedittext);
        final EditText lastnamevar = findViewById(R.id.lastnamedittext);
        final EditText hometownvar = findViewById(R.id.hometownedittext);
        final EditText agevar = findViewById(R.id.ageedittext);
        final EditText emailvar = findViewById(R.id.emailedittext);
        final EditText passwordvar = findViewById(R.id.passwordedittext);

        final Button imadoctorbutton = findViewById(R.id.doctorbutton);
        final Button imapatientbutton = findViewById(R.id.patientbutton);

        imadoctorbutton.setEnabled(false);
        imapatientbutton.setEnabled(false);

        final FirebaseAuth testAuth = FirebaseAuth.getInstance();

        final Intent docextrainfointent = new Intent(this, DoctorExtraInfoActivity.class);

        String emptyfield = "Field must not be empty!";
        String passfield = "Must be at least 6 characters long.";

        firstnamevar.setError(emptyfield);
        midnamevar.setError(emptyfield);
        lastnamevar.setError(emptyfield);
        hometownvar.setError(emptyfield);
        agevar.setError(emptyfield);
        emailvar.setError(emptyfield);
        passwordvar.setError(passfield);

        firstnamevar.addTextChangedListener(new TextWatcher() {
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

        midnamevar.addTextChangedListener(new TextWatcher() {
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

        lastnamevar.addTextChangedListener(new TextWatcher() {
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

        hometownvar.addTextChangedListener(new TextWatcher() {
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

        agevar.addTextChangedListener(new TextWatcher() {
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

        emailvar.addTextChangedListener(new TextWatcher() {
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

        passwordvar.addTextChangedListener(new TextWatcher() {
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

        imadoctorbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstnameval = firstnamevar.getText().toString();
                docextrainfointent.putExtra("First_Name", firstnameval);
                String midnameval = midnamevar.getText().toString();
                docextrainfointent.putExtra("Middle_Name", midnameval);
                String lastnameval = lastnamevar.getText().toString();
                docextrainfointent.putExtra("Last_Name", lastnameval);
                String hometownval = hometownvar.getText().toString();
                docextrainfointent.putExtra("Hometown", hometownval);
                String ageval = agevar.getText().toString();
                docextrainfointent.putExtra("Age", ageval);
                String emailval = emailvar.getText().toString();
                docextrainfointent.putExtra("Email", emailval);
                String passwordval = passwordvar.getText().toString();
                docextrainfointent.putExtra("Password", passwordval);
                startActivity(docextrainfointent);
            }
        });

        imapatientbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                loadingbar.setVisibility(View.VISIBLE);
                final String firstnameholder = firstnamevar.getText().toString();
                final String midnameholder = midnamevar.getText().toString();
                final String lastnameholder = lastnamevar.getText().toString();
                final String hometownholder = hometownvar.getText().toString();
                final String ageholder = agevar.getText().toString();
                final String emailholder = emailvar.getText().toString();
                final String passwordholder = passwordvar.getText().toString();
                final String fullnameholder = firstnameholder + " " + midnameholder + ". " + lastnameholder;
                final FirebaseFirestore fstore = FirebaseFirestore.getInstance();


                testAuth.createUserWithEmailAndPassword(emailholder, passwordholder).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignUpActivity.this, "Signing up as Patient Successful", Toast.LENGTH_SHORT).show();

                            final String userID = testAuth.getCurrentUser().getUid();
                            DocumentReference patientref = fstore.collection("patients").document(userID);
                            Map<String, Object> patient = new HashMap<>();
                            patient.put("firstname", firstnameholder);
                            patient.put("midname", midnameholder);
                            patient.put("lastname", lastnameholder);
                            patient.put("hometown", hometownholder);
                            patient.put("age", ageholder);
                            patient.put("email", emailholder);
                            patient.put("password", passwordholder);
                            patient.put("fullname", fullnameholder);
                            patientref.set(patient).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "Patient Profile created.");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loadingbar.setVisibility(View.INVISIBLE);
                                }
                            });

                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            SignUpActivity.this.startActivity(intent);
                        }
                        else {
                            Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            loadingbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });

    }

    public void checkFields() {

        final EditText firstnamevar = findViewById(R.id.firstnameedittext);
        final EditText midnamevar = findViewById(R.id.middlenameedittext);
        final EditText lastnamevar = findViewById(R.id.lastnamedittext);
        final EditText hometownvar = findViewById(R.id.hometownedittext);
        final EditText agevar = findViewById(R.id.ageedittext);
        final EditText emailvar = findViewById(R.id.emailedittext);
        final EditText passwordvar = findViewById(R.id.passwordedittext);

        final Button imadoctorbutton = findViewById(R.id.doctorbutton);
        final Button imapatientbutton = findViewById(R.id.patientbutton);

        String emptyfield = "Field must not be empty!";
        String passfield = "Must be at least 6 characters long.";

        String firstnameholder = firstnamevar.getText().toString();
        String midnameholder = midnamevar.getText().toString();
        String lastnameholder = lastnamevar.getText().toString();
        String hometownholder = hometownvar.getText().toString();
        String ageholder = agevar.getText().toString();
        String emailholder = emailvar.getText().toString();
        String passholder = passwordvar.getText().toString();
        String fullnameholder = firstnameholder + midnameholder + lastnameholder;

        if(firstnameholder.isEmpty() || midnameholder.isEmpty() || lastnameholder.isEmpty() || hometownholder.isEmpty() || ageholder.isEmpty() || emailholder.isEmpty() || passholder.isEmpty()) {
            imadoctorbutton.setEnabled(false);
            imapatientbutton.setEnabled(false);
        }
        else {
            imapatientbutton.setEnabled(true);
            imadoctorbutton.setEnabled(true);
        }

        if(firstnameholder.isEmpty()){
            firstnamevar.setError(emptyfield);
        }
        else {
            firstnamevar.setError(null);
        }

        if(midnameholder.isEmpty()){
            midnamevar.setError(emptyfield);
        }
        else {
            midnamevar.setError(null);
        }

        if(lastnameholder.isEmpty()) {
            lastnamevar.setError(emptyfield);
        }
        else {
            lastnamevar.setError(null);
        }

        if(hometownholder.isEmpty()){
            hometownvar.setError(emptyfield);
        }
        else {
            hometownvar.setError(null);
        }

        if(ageholder.isEmpty()){
            agevar.setError(emptyfield);
        }
        else {
            agevar.setError(null);
        }

        if(emailholder.isEmpty()) {
            emailvar.setError(emptyfield);
        }
        else {
            emailvar.setError(null);
        }

        if(passholder.length()<6) {
            passwordvar.setError(passfield);
        }
        else {
            passwordvar.setError(null);
        }
    }
}
