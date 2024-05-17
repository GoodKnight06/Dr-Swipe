package com.example.drswipe;

public class DNYDoctor {
    private String fullname;
    private String profession;

    public DNYDoctor(){

    }

    public DNYDoctor(String fullname, String profession) {
        this.fullname = fullname;
        this.profession = profession;
    }

    public String getFullname() {
        return fullname;
    }

    public String getProfession() {
        return profession;
    }
}
