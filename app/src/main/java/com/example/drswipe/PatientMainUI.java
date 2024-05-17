package com.example.drswipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class PatientMainUI extends AppCompatActivity {

    final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    final CollectionReference DNYReference = fStore.collection("doctors");

    private RecyclerView DNYlist;

    DNYDoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_main_ui);

        final TextView completename = findViewById(R.id.patientcompletenameview);
        final TextView addresstext = findViewById(R.id.patientaddressview);
        final ProgressBar patientinfobar = findViewById(R.id.patientbar);
        final Button patientlogoutbutton = findViewById(R.id.logoutbutt_d);

        final FirebaseAuth fAuth = FirebaseAuth.getInstance();

        Bundle hometownbundle = getIntent().getExtras();
        final String querymatch = hometownbundle.getString("Hometown");

        final String userID = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("patients").document(userID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        final String firstname = documentSnapshot.getString("firstname");
                        final String midname = documentSnapshot.getString("midname");
                        final String lastname = documentSnapshot.getString("lastname");
                        final String fullname = lastname + ", " + firstname + " " + midname + ".";
                        addresstext.setText(documentSnapshot.getString("hometown"));
                        completename.setText(fullname);
                        patientinfobar.setVisibility(View.INVISIBLE);
                        patientlogoutbutton.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(PatientMainUI.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Query query = DNYReference.whereEqualTo("clinic", querymatch).orderBy("profession", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<DNYDoctor> options = new FirestoreRecyclerOptions.Builder<DNYDoctor>()
                .setQuery(query, DNYDoctor.class)
                .build();

        adapter = new DNYDoctorAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.DNYview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);

        patientlogoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PatientMainUI.this, LogInActivity.class);
                PatientMainUI.this.startActivity(intent);
            }
        });
        adapter.setOnItemClickListener(new DNYDoctorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String doctor_id = documentSnapshot.getId();
                String doctor_chatmate = documentSnapshot.getString("fullname");
                Intent intent = new Intent(PatientMainUI.this, MessagingActivityPD.class);
                intent.putExtra("Chatmate", doctor_chatmate);
                intent.putExtra("Chatmate_ID", doctor_id);
                intent.putExtra("User_ID", userID);
                String two_users = userID + "___" + doctor_id;
                intent.putExtra("Two_Users", two_users);
                PatientMainUI.this.startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }
}
