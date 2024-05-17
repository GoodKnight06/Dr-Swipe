package com.example.drswipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class DNYDoctorAdapter extends FirestoreRecyclerAdapter<DNYDoctor, DNYDoctorAdapter.DNYDoctorHolder> {

    private OnItemClickListener listener;

    public DNYDoctorAdapter(@NonNull FirestoreRecyclerOptions<DNYDoctor> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull DNYDoctorHolder holder, int position, @NonNull DNYDoctor model) {
        holder.DNYfullname.setText(model.getFullname());
        holder.DNYprofession.setText(model.getProfession());
    }

    @NonNull
    @Override
    public DNYDoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item, parent, false);
        return new DNYDoctorHolder(v);
    }

    class DNYDoctorHolder extends RecyclerView.ViewHolder {

        TextView DNYfullname;
        TextView DNYprofession;

        public DNYDoctorHolder(@NonNull View itemView) {
            super(itemView);
            DNYfullname = itemView.findViewById(R.id.doctor_fullname);
            DNYprofession = itemView.findViewById(R.id.doctor_details);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }

    }

    public interface OnItemClickListener{
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

}
