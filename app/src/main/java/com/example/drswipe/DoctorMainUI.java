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

public class DoctorMainUI extends AppCompatActivity {

    private FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference PSHReference = fstore.collection("patients");

    private RecyclerView PSHlist;

    PSHPatientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_main_ui);

        final TextView completenameview = findViewById(R.id.doctorcompletenameView);
        final TextView clinicview = findViewById(R.id.doctorclinicView);
        final ProgressBar doctorinfobar = findViewById(R.id.doctorbar);
        final Button doctorlogoutbutton = findViewById(R.id.logoutbutt_d);

        Bundle clinicbundle = getIntent().getExtras();
        final String clinicmatch = clinicbundle.getString("Clinic");

        final String userID = firebaseAuth.getUid();

        final DocumentReference documentReference = fstore.collection("doctors").document(userID);
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
                        clinicview.setText(documentSnapshot.getString("clinic"));
                        completenameview.setText(fullname);
                        doctorinfobar.setVisibility(View.INVISIBLE);
                        doctorlogoutbutton.setVisibility(View.VISIBLE);
                    }
                    else {
                        Toast.makeText(DoctorMainUI.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        Query query = PSHReference.orderBy("hometown", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<PSHPatient> options = new FirestoreRecyclerOptions.Builder<PSHPatient>()
                .setQuery(query, PSHPatient.class)
                .build();

        adapter = new PSHPatientAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.PSHView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);

        doctorlogoutbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DoctorMainUI.this, LogInActivity.class);
                DoctorMainUI.this.startActivity(intent);
            }
        });
        adapter.setOnItemClickListener2(new PSHPatientAdapter.OnItemClickListener2() {
            @Override
            public void onItemClick2(DocumentSnapshot documentSnapshot, int position) {
                String patient_id = documentSnapshot.getId();
                String patient_chatmate = documentSnapshot.getString("fullname");
                Intent intent = new Intent(DoctorMainUI.this, MessagingActivityPD.class);
                intent.putExtra("Chatmate", patient_chatmate);
                intent.putExtra("Chatmate_ID", patient_id);
                intent.putExtra("User_ID", userID);
                intent.putExtra("User_ID", userID);
                String two_users = patient_id + "___" + userID;
                intent.putExtra("Two_Users", two_users);
                DoctorMainUI.this.startActivity(intent);
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
