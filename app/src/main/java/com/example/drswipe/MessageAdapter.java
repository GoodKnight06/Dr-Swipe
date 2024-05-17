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
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {
    static int my_message = 1;
    static int other_message = 2;

    String currentuserID;
    List<MessageModel> messages;
    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss a, MM-dd-yyyy");

    MessageAdapter(String currentuserID){
        this.currentuserID = currentuserID;
        messages = new ArrayList<>();
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View chatBubble;

        if (viewType == my_message){
            chatBubble = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_item, parent, false);
        }
        else  {
            chatBubble = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_message_item, parent, false);
        }

        return new MessageHolder(chatBubble);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.bindView(messages.get(position));
    }

    @Override
    public int getItemViewType(int position){
        if (messages.get(position).getSenderID().equals(currentuserID)){
            return my_message;
        }
        else{
            return other_message;
        }
    }

    @Override
    public int getItemCount(){
        return messages.size();
    }

    public void setData(List<MessageModel> messages){
        this.messages = messages;
        notifyDataSetChanged();
    }

    class MessageHolder extends RecyclerView.ViewHolder {
        TextView messagetv;
        TextView datetv;

        public MessageHolder(@NonNull View view){
            super(view);
            messagetv = view.findViewById(R.id.patient_fullname);
        }

        public void bindView(MessageModel messageModel){
            messagetv.setText(messageModel.getMessageSent());
        }
    }
}

