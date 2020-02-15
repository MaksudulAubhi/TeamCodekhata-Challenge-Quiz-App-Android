package com.example.challenge;

public class ProfileUpload {
    private String name;
    private String username;
    private String gender;
    private String institution;
    private String ImageUri;

    public ProfileUpload(){

    }

    public ProfileUpload(String name, String username, String gender, String institution,String imageUri) {
        this.name = name;
        this.username = username;
        this.gender = gender;
        this.institution = institution;
        this.ImageUri=imageUri;
    }


    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public String getInstitution() {
        return institution;
    }

    public String getImageUri(){ return ImageUri;}



    public void setName(String name) { this.name = name; }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public void setImageUri(String imageUri){ this.ImageUri=imageUri;}
}
