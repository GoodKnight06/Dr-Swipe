package com.example.drswipe;

public class PSHPatient {
    private String fullname;
    private String hometown;
    private String patient_id;

    public PSHPatient(){

    }

    public PSHPatient(String fullname, String hometown){
        this.fullname = fullname;
        this.hometown = hometown;
    }

    public String getFullname() {
        return fullname;
    }

    public String getHometown() {
        return hometown;
    }
}
