package com.example.drswipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagingActivityPD extends AppCompatActivity {

    MessageAdapter messageAdapter;
    RecyclerView exchangebox;
    String twoUsers;
    String user_id;
    String chatmate_id;
    String chatmate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_pd);

        Bundle somebundle = getIntent().getExtras();
        user_id = somebundle.getString("User_ID");
        chatmate_id = somebundle.getString("Chatmate_ID");
        chatmate = somebundle.getString("Chatmate");
        twoUsers = somebundle.getString("Two_Users");

        final Button sendbutton = findViewById(R.id.sendbutt);
        final EditText sendtext = findViewById(R.id.messagebox);
        final TextView chatmatename = findViewById(R.id.chatmatetv);

        final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
        final CollectionReference messref = fStore.collection(twoUsers);

        chatmatename.setText(chatmate);
        exchangebox = findViewById(R.id.messageexchange);


        exchangebox.setHasFixedSize(true);
        exchangebox.setLayoutManager(new LinearLayoutManager(this));

        fetchMessages();

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = sendtext.getText().toString();
                String sender_id = user_id;
                String receiver_id = chatmate_id;

                if(!(TextUtils.isEmpty(message))){
                    MessageModel messageModel = new MessageModel(sender_id, message, receiver_id);
                    sendMessages(messageModel);
                    sendtext.getText().clear();
                    Toast.makeText(MessagingActivityPD.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void fetchMessages(){

        messageAdapter = new MessageAdapter(user_id);
        exchangebox.setAdapter(messageAdapter);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(twoUsers)
                .orderBy("date")
                .addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Log.e("TAG", error.getMessage(), error);
                        }
                        else{
                            List<MessageModel> messages = value.toObjects(MessageModel.class);
                            messageAdapter.setData(messages);
                        }
                    }
                });
    }

    private void sendMessages(MessageModel messageModel){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(twoUsers).add(messageModel).addOnCompleteListener(this, new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MessagingActivityPD.this, "Message Sent!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

