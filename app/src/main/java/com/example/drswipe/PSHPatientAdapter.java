package com.example.drswipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class PSHPatientAdapter extends FirestoreRecyclerAdapter<PSHPatient, PSHPatientAdapter.PSHPatientHolder> {

    private OnItemClickListener2 listener2;

    public PSHPatientAdapter(@NonNull FirestoreRecyclerOptions<PSHPatient> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull PSHPatientHolder holder, int position, @NonNull PSHPatient model) {
        holder.PSHfullname.setText(model.getFullname());
        holder.PSHhometown.setText(model.getHometown());
    }

    @NonNull
    @Override
    public PSHPatientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_item, parent, false);
        return new PSHPatientHolder(v);
    }

    class PSHPatientHolder extends RecyclerView.ViewHolder {

        TextView PSHfullname;
        TextView PSHhometown;
        ToggleButton PSHstatus;

        public PSHPatientHolder(@NonNull View itemView) {
            super(itemView);
            PSHfullname = itemView.findViewById(R.id.patient_fullname);
            PSHhometown = itemView.findViewById(R.id.patient_details);
            PSHstatus = itemView.findViewById(R.id.patientMarker);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener2 != null){
                        listener2.onItemClick2(getSnapshots().getSnapshot(position), position);
                    }
                }
            });

            PSHstatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean on = PSHstatus.isChecked();
                    if(on){
                        PSHstatus.setTextOff("PENDING");
                    }
                    else{
                        PSHstatus.setTextOn("TREATED");
                    }
                }
            });
        }
    }

    public interface OnItemClickListener2 {
        void onItemClick2(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener2(OnItemClickListener2 listener2){
        this.listener2 = listener2;
    }
}
