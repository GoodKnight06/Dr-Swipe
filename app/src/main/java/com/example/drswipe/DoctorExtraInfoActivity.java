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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DoctorExtraInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_extra_info);

        final ProgressBar loadingbar = findViewById(R.id.bastaprog);

        final EditText clinicaddvar = findViewById(R.id.clinicaddedittext);
        final EditText licenseaddvar = findViewById(R.id.licenseedittext);
        final Spinner studylistvar = findViewById(R.id.studydropdown);

        final Button doctorfinishbutton = findViewById(R.id.doctorsignupbutton);

        String emptyfield = "Field must not be empty!";
        String[] studies = new String[]{"Allergy and Immunology", "Anesthesiology", "Dermatology", "Diagnostic Radiology", "Emergency Medicine", "Family Medicine", "Internal Medicine", "Medical Genetics", "Neurology", "Nuclear Medicine", "Obstetrics and Gynecology", "Ophthalmology", "Pathology", "Pediatrics", "Physical Medicine and Rehabilitation", "Preventive Medicine", "Psychiatry", "Radiation Oncology", "Surgery", "Urology"};

        ArrayAdapter<String> studyadapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, studies);
        studylistvar.setAdapter(studyadapter);
        Bundle signupbundle = getIntent().getExtras();
        final String firstnameval = signupbundle.getString("First_Name");
        final String midnameval = signupbundle.getString("Middle_Name");
        final String lastnameval = signupbundle.getString("Last_Name");
        final String hometownval = signupbundle.getString("Hometown");
        final String ageval = signupbundle.getString("Age");
        final String emailval = signupbundle.getString("Email");
        final String passwordval = signupbundle.getString("Password");

        final FirebaseAuth testAuth = FirebaseAuth.getInstance();


        doctorfinishbutton.setEnabled(false);
        clinicaddvar.setError(emptyfield);
        licenseaddvar.setError(emptyfield);

        clinicaddvar.addTextChangedListener(new TextWatcher() {
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

        licenseaddvar.addTextChangedListener(new TextWatcher() {
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
        studylistvar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkFields();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        doctorfinishbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingbar.setVisibility(View.VISIBLE);
                final String clinicaddholder = clinicaddvar.getText().toString();
                final String licenseholder = licenseaddvar.getText().toString();
                final String professionholder = studylistvar.getSelectedItem().toString();
                final String fullnameholder = firstnameval + " " + midnameval + ". " + lastnameval;

                final FirebaseFirestore fstore = FirebaseFirestore.getInstance();

                testAuth.createUserWithEmailAndPassword(emailval, passwordval).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(DoctorExtraInfoActivity.this, "Signing up as Doctor, Successful", Toast.LENGTH_SHORT).show();

                            final String userID = testAuth.getCurrentUser().getUid();
                            DocumentReference doctorref = fstore.collection("doctors").document(userID);
                            Map<String, Object> doctor = new HashMap<>();
                            doctor.put("firstname", firstnameval);
                            doctor.put("midname", midnameval);
                            doctor.put("lastname", lastnameval);
                            doctor.put("hometown", hometownval);
                            doctor.put("age", ageval);
                            doctor.put("email", emailval);
                            doctor.put("password", passwordval);
                            doctor.put("clinic", clinicaddholder);
                            doctor.put("license", licenseholder);
                            doctor.put("profession", professionholder);
                            doctor.put("fullname", fullnameholder);
                            doctorref.set(doctor).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG", "Doctor Profile created.");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loadingbar.setVisibility(View.INVISIBLE);
                                }
                            });

                            Intent intent = new Intent(DoctorExtraInfoActivity.this, MainActivity.class);
                            DoctorExtraInfoActivity.this.startActivity(intent);
                        }
                        else {
                            Toast.makeText(DoctorExtraInfoActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            loadingbar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });
    }

    public void checkFields() {

        final EditText clinicaddvar = findViewById(R.id.clinicaddedittext);
        final EditText licensevar = findViewById(R.id.licenseedittext);

        final Button doctorfinishbutton = findViewById(R.id.doctorsignupbutton);

        String emptyfield = "Field must not be empty!";

        String clinicholder = clinicaddvar.getText().toString();
        String licenseholder = licensevar.getText().toString();

        if(clinicholder.isEmpty() || licenseholder.isEmpty()) {
            doctorfinishbutton.setEnabled(false);
        }
        else {
            doctorfinishbutton.setEnabled(true);
        }

        if(clinicholder.isEmpty()) {
            clinicaddvar.setError(emptyfield);
        }
        else {
            clinicaddvar.setError(null);
        }

        if(licenseholder.isEmpty()) {
            licensevar.setError(emptyfield);
        }
        else {
            licensevar.setError(null);
        }

    }
}
