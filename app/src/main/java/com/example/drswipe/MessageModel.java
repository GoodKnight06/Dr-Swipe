package com.example.drswipe;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class MessageModel {
    private String senderID;
    private String messageSent;
    private String receiverID;

    @ServerTimestamp
    private Date date;

    public MessageModel(){

    }

    public MessageModel(String senderID, String messageSent, String receiverID){
        this.senderID = senderID;
        this.messageSent = messageSent;
        this.receiverID = receiverID;
    }

    public String getSenderID(){
        return senderID;
    }

    public String getMessageSent(){
        return messageSent;
    }

    public String getReceiverID(){
        return receiverID;
    }

    public Date getDate(){
        return date;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setMessageSent(String messageSent) {
        this.messageSent = messageSent;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public void setDate(Date date){
        this.date = date;
    }

}
