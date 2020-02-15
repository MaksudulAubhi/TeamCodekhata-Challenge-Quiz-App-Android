package com.example.challenge;

public class TotalScoreUpdate {
    int totalScore;
    String userName;
    String institution;

    TotalScoreUpdate(){

    }

    public TotalScoreUpdate(int totalScore, String userName,String institution ) {
        this.totalScore = totalScore;
        this.userName = userName;
        this.institution=institution;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
